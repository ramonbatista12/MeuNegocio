package com.example.meunegociomeunegocio.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meunegociomeunegocio.repositorioRom.Repositorio
import com.example.meunegociomeunegocio.utilitario.EstadosDeLoad
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class ViewModelRequisicoes@Inject constructor(repositorio: Repositorio): ViewModel() {
    private val Tag="ViewModelRequisicoes"
    private val coroutineScope=viewModelScope
    val fluxoDeId= MutableStateFlow(0)
    val fluxoTodasAsRequisicoes =repositorio.fluxoRequisicao()
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

    val fluxoProdutosRequisitados= fluxoDeId.flatMapLatest{
        repositorio.produtoRequisitado(it).map {
            if(it==null||it.isEmpty())
                EstadosDeLoad.Empty
            else
                EstadosDeLoad.Caregado(it)
        }
    }

    val valorTotalRequisicao= fluxoDeId.flatMapLatest{
        repositorio.custoTotalPorRequisicao(it).map{
            if(it==null)
               EstadosDeLoad.Empty
            else
                EstadosDeLoad.Caregado(it)


        }}
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
}

sealed class TelasInternasDeRequisicoes {
    object Lista: TelasInternasDeRequisicoes()
    data class Requisicao(val idRequisicao:Int): TelasInternasDeRequisicoes()

}

sealed class ListaHistorico{
    object Lista: ListaHistorico()
    object Historico: ListaHistorico()
}