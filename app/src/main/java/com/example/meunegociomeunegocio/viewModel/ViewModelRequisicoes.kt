package com.example.meunegociomeunegocio.viewModel

import android.net.Uri
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meunegociomeunegocio.hillt.ModuloRom_ProviderCriadorPdfFactory
import com.example.meunegociomeunegocio.pdf.CriadorDePfd
import com.example.meunegociomeunegocio.repositorioRom.DadosDaRequisicao
import com.example.meunegociomeunegocio.repositorioRom.ProdutoRequisitado
import com.example.meunegociomeunegocio.repositorioRom.Repositorio
import com.example.meunegociomeunegocio.utilitario.EstadoLoadObterUri
import com.example.meunegociomeunegocio.utilitario.EstadosDeLoad
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.net.URI

@HiltViewModel
class ViewModelRequisicoes@Inject constructor(private val repositorio: Repositorio,private val pdf: CriadorDePfd): ViewModel() {
    private val Tag="ViewModelRequisicoes"
    private val coroutineScope=viewModelScope
    private val fluxoDeFiltros= MutableStateFlow<FiltroDePesquisaRequisicoes?>(null)
    private val _caixaDeDialogoCriarPdf=MutableStateFlow(false)
    private val _estadosDeCriacaoDePdf= MutableStateFlow<EstadoLoadObterUri>(EstadoLoadObterUri.Iniciando)
    private val _envioDeRequisicao = MutableStateFlow<Uri?>(null)
    val fluxoDeId= MutableStateFlow(0)
    val fluxoTodasAsRequisicoes =repositorio.fluxoRequisicao().map {
        if(it==null ||it.isEmpty()) EstadosDeLoad.Empty
        else EstadosDeLoad.Caregado(it)
    }
    val telaInternasRequisicoes= MutableStateFlow<TelasInternasDeRequisicoes>(TelasInternasDeRequisicoes.Lista)
    val estadoListaHistorico= MutableStateFlow<ListaHistorico>(ListaHistorico.Lista)
    val fluxoDadosDeRequisicao = fluxoDeId.flatMapLatest{
        repositorio.requisicaoPorId(it).map {
            if(it==null)
                EstadosDeLoad.Empty
            else
                EstadosDeLoad.Caregado(it)
        }
    }
    val fluxoHistoricoDeMudancas= fluxoDeId.flatMapLatest {

        repositorio.fluxoHistoricoDeMudancas(it).map {
            if(it==null||it.isEmpty())
                EstadosDeLoad.Empty
            else
            EstadosDeLoad.Caregado(it)
        }
    }
    val fluxoDePesquisaRequisicoes=fluxoDeFiltros.flatMapLatest {
        if(it==null) flow { emit(EstadosDeLoad.Empty) }
        else{
            when(it){
                is FiltroDePesquisaRequisicoes.Clientes -> repositorio.fluxoDeRequisicoesPorCliente(it.query).map {
                    if(it==null)
                        EstadosDeLoad.Empty
                    else
                        EstadosDeLoad.Caregado(it)

                }
                is FiltroDePesquisaRequisicoes.Data -> repositorio.fluxoDeRequisicoesPorDataDeMudanca(it.query).map {
                    if(it==null)
                        EstadosDeLoad.Empty
                    else
                        EstadosDeLoad.Caregado(it)

                }
                is FiltroDePesquisaRequisicoes.Estado -> repositorio.fluxoDeRequisicaoPorEstado(it.query).map {
                    if(it==null)
                        EstadosDeLoad.Empty
                    else
                        EstadosDeLoad.Caregado(it)

                }
            }
        }

    }
    val fluxoProdutosRequisitados= fluxoDeId.flatMapLatest{
        repositorio.produtoRequisitado(it).map {
            if(it==null||it.isEmpty())
                EstadosDeLoad.Empty
            else
                EstadosDeLoad.Caregado(it)
        }
    }
    val caixaDeDialogoCriarPdf= _caixaDeDialogoCriarPdf
    val valorTotalRequisicao= fluxoDeId.flatMapLatest{
        repositorio.custoTotalPorRequisicao(it).map{
            if(it==null)
               EstadosDeLoad.Empty
            else
                EstadosDeLoad.Caregado(it)


        }}
    val estadosDeCriacaoDePdf=_estadosDeCriacaoDePdf
    val snackbarHostState = SnackbarHostState()
    val envioDerequisicao =_envioDeRequisicao
    fun mostrarRequisicao(id:Int){

        coroutineScope.launch {
            telaInternasRequisicoes.emit(TelasInternasDeRequisicoes.Requisicao(id))
        }

    }
    fun voutarALista(){
        coroutineScope.launch {
            telaInternasRequisicoes.emit(TelasInternasDeRequisicoes.Lista)
        }
    }
    fun verLista(){
        coroutineScope.launch {
            estadoListaHistorico.emit(ListaHistorico.Lista)

        }
    }
    fun verHistorico(){
        coroutineScope.launch {
            estadoListaHistorico.emit(ListaHistorico.Historico)
        }
    }
    fun mudarId(id:Int){
        coroutineScope.launch {
            fluxoDeId.emit(id)
        }
    }
    fun mudarFiltro(filtro: FiltroDePesquisaRequisicoes){
        coroutineScope.launch {
            fluxoDeFiltros.emit(filtro)
        }

    }
    fun limparFiltro(){
        coroutineScope.launch {
            fluxoDeFiltros.emit(null)
        }
    }
    fun criarPdf(uri: Uri?){
        coroutineScope.launch {
            if(uri==null){
                _estadosDeCriacaoDePdf.emit(EstadoLoadObterUri.Erro)
                return@launch
            }
            val dadosDaRequisicao=repositorio.requisicaoPorId(fluxoDeId.value).first()
            val listaDeProdutos=repositorio.produtoRequisitado(fluxoDeId.value).first()
            _estadosDeCriacaoDePdf.emit(EstadoLoadObterUri.Criando)
            try{
                if(dadosDaRequisicao!=null&&listaDeProdutos!=null)
                pdf.create(uri,dadosDaRequisicao!!,listaDeProdutos)
                _estadosDeCriacaoDePdf.emit(EstadoLoadObterUri.Sucesso)
                _envioDeRequisicao.emit(uri)
            }catch (e: Exception){
                _estadosDeCriacaoDePdf.emit(EstadoLoadObterUri.Erro)
                return@launch
            }


        }

    }
    fun abrirDialogo(){
        coroutineScope.launch {
            _estadosDeCriacaoDePdf.emit(EstadoLoadObterUri.Iniciando)
            _caixaDeDialogoCriarPdf.emit(true)
        }

    }
    fun fecharDialogo(){
        coroutineScope.launch{
            _caixaDeDialogoCriarPdf.emit(false)
        }
    }

    fun anunciarConclusao(){
        coroutineScope.launch {
            snackbarHostState.showSnackbar("Pdf criado com sucesso")
        }
    }
}

sealed class TelasInternasDeRequisicoes {
    object Lista: TelasInternasDeRequisicoes()
    data class Requisicao(val idRequisicao:Int): TelasInternasDeRequisicoes()

}

sealed class ListaHistorico{
    object Lista: ListaHistorico()
    object Historico: ListaHistorico()
}
sealed class SelecaoDeFiltros(){
    object Cliente: SelecaoDeFiltros()
    object Estado: SelecaoDeFiltros()
    object Data: SelecaoDeFiltros()
}
sealed class FiltroDePesquisaRequisicoes(){
    data class Clientes(val query:String) : FiltroDePesquisaRequisicoes()
    data class Estado(val query: String): FiltroDePesquisaRequisicoes()
    data class Data(val query: String): FiltroDePesquisaRequisicoes()

}