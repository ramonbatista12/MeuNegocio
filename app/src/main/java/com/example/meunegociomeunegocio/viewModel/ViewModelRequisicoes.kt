package com.example.meunegociomeunegocio.viewModel

import android.net.Uri
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meunegociomeunegocio.pdf.CriadorDePfd
import com.example.meunegociomeunegocio.repositorioRom.Repositorio
import com.example.meunegociomeunegocio.utilitario.EstadoLoadAcoes
import com.example.meunegociomeunegocio.utilitario.EstadosDeLoadCaregamento
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class ViewModelRequisicoes@Inject constructor(private val repositorio: Repositorio,private val pdf: CriadorDePfd): ViewModel(),
    IDialogoCriacaoPdf {
    private val Tag="ViewModelRequisicoes"
    private val coroutineScope=viewModelScope
    private val fluxoDeFiltros= MutableStateFlow<FiltroDePesquisaRequisicoes?>(null)
    private val _caixaDeDialogoCriarPdf=MutableStateFlow(false)
    private val _estadosDeCriacaoDePdf= MutableStateFlow<EstadoLoadAcoes>(EstadoLoadAcoes.Iniciando)
    private val _envioDeRequisicao = MutableStateFlow<Uri?>(null)
    val fluxoDeId= MutableStateFlow(0)
    val fluxoTodasAsRequisicoes =repositorio.fluxoRequisicao().map {
        if(it==null ||it.isEmpty()) EstadosDeLoadCaregamento.Empty
        else EstadosDeLoadCaregamento.Caregado(it)
    }
    val telaInternasRequisicoes= MutableStateFlow<TelasInternasDeRequisicoes>(TelasInternasDeRequisicoes.Lista)
    val estadoListaHistorico= MutableStateFlow<ListaHistorico>(ListaHistorico.Lista)
    val fluxoDadosDeRequisicao = fluxoDeId.flatMapLatest{
        repositorio.requisicaoPorId(it).map {
            if(it==null)
                EstadosDeLoadCaregamento.Empty
            else
                EstadosDeLoadCaregamento.Caregado(it)
        }
    }
    val fluxoHistoricoDeMudancas= fluxoDeId.flatMapLatest {

        repositorio.fluxoHistoricoDeMudancas(it).map {
            if(it==null||it.isEmpty())
                EstadosDeLoadCaregamento.Empty
            else
            EstadosDeLoadCaregamento.Caregado(it)
        }
    }
    val fluxoDePesquisaRequisicoes=fluxoDeFiltros.flatMapLatest {
        if(it==null) flow { emit(EstadosDeLoadCaregamento.Empty) }
        else{
            when(it){
                is FiltroDePesquisaRequisicoes.Clientes -> repositorio.fluxoDeRequisicoesPorCliente(it.query).map {
                    if(it==null)
                        EstadosDeLoadCaregamento.Empty
                    else
                        EstadosDeLoadCaregamento.Caregado(it)

                }
                is FiltroDePesquisaRequisicoes.Data -> repositorio.fluxoDeRequisicoesPorDataDeMudanca(it.query).map {
                    if(it==null)
                        EstadosDeLoadCaregamento.Empty
                    else
                        EstadosDeLoadCaregamento.Caregado(it)

                }
                is FiltroDePesquisaRequisicoes.Estado -> repositorio.fluxoDeRequisicaoPorEstado(it.query).map {
                    if(it==null)
                        EstadosDeLoadCaregamento.Empty
                    else
                        EstadosDeLoadCaregamento.Caregado(it)

                }
            }
        }

    }
    val fluxoProdutosRequisitados= fluxoDeId.flatMapLatest{
        repositorio.produtoRequisitado(it).map {
            if(it==null||it.isEmpty())
                EstadosDeLoadCaregamento.Empty
            else
                EstadosDeLoadCaregamento.Caregado(it)
        }
    }
    override val caixaDeDialogoCriarPdf= _caixaDeDialogoCriarPdf
    val valorTotalRequisicao= fluxoDeId.flatMapLatest{
        repositorio.custoTotalPorRequisicao(it).map{
            if(it==null)
               EstadosDeLoadCaregamento.Empty
            else
                EstadosDeLoadCaregamento.Caregado(it)


        }}
    override val estadosDeCriacaoDePdf=_estadosDeCriacaoDePdf
    val snackbarHostState = SnackbarHostState()
    override val envioDerequisicao =_envioDeRequisicao
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
    override fun criarPdf(uri: Uri?){
        coroutineScope.launch {
            if(uri==null){
                _estadosDeCriacaoDePdf.emit(EstadoLoadAcoes.Erro)
                return@launch
            }
            val dadosDaRequisicao=repositorio.requisicaoPorId(fluxoDeId.value).first()
            val listaDeProdutos=repositorio.produtoRequisitado(fluxoDeId.value).first()
            _estadosDeCriacaoDePdf.emit(EstadoLoadAcoes.Criando)
            try{
                if(dadosDaRequisicao!=null&&listaDeProdutos!=null)
                pdf.create(uri,dadosDaRequisicao!!,listaDeProdutos)
                _estadosDeCriacaoDePdf.emit(EstadoLoadAcoes.Sucesso)
                _envioDeRequisicao.emit(uri)
            }catch (e: Exception){
                _estadosDeCriacaoDePdf.emit(EstadoLoadAcoes.Erro)
                return@launch
            }


        }

    }
    override fun abrirDialogo(){
        coroutineScope.launch {
            _estadosDeCriacaoDePdf.emit(EstadoLoadAcoes.Iniciando)
            _caixaDeDialogoCriarPdf.emit(true)
        }

    }
    override fun fecharDialogo(){
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