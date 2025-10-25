package com.example.meunegociomeunegocio.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meunegociomeunegocio.repositorioRom.Repositorio
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ViewModelRequisicoes@Inject constructor(repositorio: Repositorio): ViewModel() {
    private val Tag="ViewModelRequisicoes"
    private val coroutineScope=viewModelScope
    val fluxoTodasAsRequisicoes =repositorio.fluxoRequisicao();
    val telaInternasRequisicoes= MutableStateFlow<TelasInternasDeRequisicoes>(TelasInternasDeRequisicoes.Lista)
    val estadoListaHistorico= MutableStateFlow<ListaHistorico>(ListaHistorico.Lista)
    val fluxoDadosDeRequisicao =repositorio.requisicaoPorId(1)
    val fluxoHistoricoDeMudancas=repositorio.fluxoHistoricoDeMudancas(1)
    val fluxoProdutosRequisitados=repositorio.produtoRequisitado(1)
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
}

sealed class TelasInternasDeRequisicoes {
    object Lista: TelasInternasDeRequisicoes()
    data class Requisicao(val idRequisicao:Int): TelasInternasDeRequisicoes()

}

sealed class ListaHistorico{
    object Lista: ListaHistorico()
    object Historico: ListaHistorico()
}