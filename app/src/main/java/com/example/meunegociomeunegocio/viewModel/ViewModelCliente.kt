package com.example.meunegociomeunegocio.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meunegociomeunegocio.repositorioRom.Cliente
import com.example.meunegociomeunegocio.repositorioRom.DadosDeClientes
import com.example.meunegociomeunegocio.repositorioRom.Repositorio
import com.example.meunegociomeunegocio.utilitario.EstadosDeLoad
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
@HiltViewModel
class ViewModelCliente @Inject constructor(private val repositorio: Repositorio) : ViewModel() {
    private val Tag = "ViewModelCliente"
    private val coroutineScope =viewModelScope
    val telaVisualizada= MutableStateFlow<TelasInternasDeClientes>(TelasInternasDeClientes.ListaDeClientes)

    val fluxoDeCliente =repositorio.fluxoDeClientes()
                                    .shareIn(coroutineScope,SharingStarted.WhileSubscribed(5000))
    suspend fun adicionarCliente(cliente: Cliente)=repositorio.inserirCliente(cliente)
    suspend fun apagarCliente(cliente: Cliente)=repositorio.apagarCliente(cliente)
    suspend fun modificarCliente(cliente: Cliente)=repositorio.atuAlizarCliente(cliente)
    suspend fun mudarTelaVisualizada(tela: TelasInternasDeClientes)=telaVisualizada.emit(tela)
    fun dasDeClientes(id:Int)=repositorio.fluxoDadosDoCliente(id).map {
        if(it==null)EstadosDeLoad.Empty
        else EstadosDeLoad.Caregado<DadosDeClientes>(it)
    }
}

sealed class TelasInternasDeClientes(){
    object ListaDeClientes: TelasInternasDeClientes()
    data class DadosDoCliente(val idCliente: Int): TelasInternasDeClientes()
}