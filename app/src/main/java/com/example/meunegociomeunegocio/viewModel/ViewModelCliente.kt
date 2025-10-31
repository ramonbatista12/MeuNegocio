package com.example.meunegociomeunegocio.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meunegociomeunegocio.repositorioRom.Cliente
import com.example.meunegociomeunegocio.repositorioRom.DadosDeClientes
import com.example.meunegociomeunegocio.repositorioRom.Repositorio
import com.example.meunegociomeunegocio.utilitario.EstadosDeLoad
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@HiltViewModel
class ViewModelCliente @Inject constructor(private val repositorio: Repositorio) : ViewModel() {
    private val Tag = "ViewModelCliente"
    private val coroutineScope =viewModelScope
    val telaVisualizada= MutableStateFlow<TelasInternasDeClientes>(TelasInternasDeClientes.ListaDeClientes)
    val daosClioente= MutableStateFlow<TelasInternasDadosDeClientes>(TelasInternasDadosDeClientes.Telefone)
    val pesquisa= MutableStateFlow<Pesquisa?>(null)
    val fluxoDeCliente =repositorio.fluxoDeClientes().map {
        delay(4000)
        if(it==null||it.size==0) EstadosDeLoad.Empty
        else EstadosDeLoad.Caregado(it)
    }
    val fluxoDeDadosDeClientesPainelExpandido=telaVisualizada.flatMapLatest{
        when(it){
            is TelasInternasDeClientes.DadosDoCliente -> {
                repositorio.fluxoDadosDoCliente(it.idCliente).map {
                    if(it==null)EstadosDeLoad.Empty
                    else EstadosDeLoad.Caregado<DadosDeClientes>(it)
                }
            }
            else ->{
                flowOf(EstadosDeLoad.Empty)
            }
        }
    }
    val fluxoDePesquisa=pesquisa.flatMapLatest {
        Log.d("ViewModelCliente","pesquisa fluxoDePesquisa ")
         if (it==null) emptyFlow()
         else repositorio.pesquisaClientes(it.pesquisa)
    }
    suspend fun adicionarCliente(cliente: Cliente)=repositorio.inserirCliente(cliente)
    suspend fun apagarCliente(cliente: Cliente)=repositorio.apagarCliente(cliente)
    suspend fun modificarCliente(cliente: Cliente)=repositorio.atuAlizarCliente(cliente)
    suspend fun mudarTelaVisualizada(tela: TelasInternasDeClientes)=telaVisualizada.emit(tela)
    suspend fun mudarDadoDoCliente(tela: TelasInternasDadosDeClientes)=daosClioente.emit(tela)
     fun mudarPesquisa(pesquisa: Pesquisa?){

        Log.d("ViewModelCliente","pesquisa mudarPesquisa ${ if (pesquisa==null) null else pesquisa.pesquisa}")
        this.pesquisa.value=pesquisa
    }
    fun dadosDocliente(id:Int)=repositorio.fluxoDadosDoCliente(id).map {
        if(it==null)EstadosDeLoad.Caregado<DadosDeClientes>(DadosDeClientes(Cliente(0,"","",""), emptyList(),emptyList())) //EstadosDeLoad.Empty
        else EstadosDeLoad.Caregado<DadosDeClientes>(it)
    }
}

sealed class TelasInternasDeClientes(){
    object ListaDeClientes: TelasInternasDeClientes()
    data class DadosDoCliente(val idCliente: Int): TelasInternasDeClientes()
}

sealed class TelasInternasDadosDeClientes(){
    object Telefone: TelasInternasDadosDeClientes()
    object Endereco: TelasInternasDadosDeClientes()

}

data class Pesquisa(val pesquisa: String)