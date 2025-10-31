package com.example.meunegociomeunegocio.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meunegociomeunegocio.repositorioRom.ProdutoServico
import com.example.meunegociomeunegocio.repositorioRom.Repositorio
import com.example.meunegociomeunegocio.utilitario.EstadosDeLoad
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class ViewModelProdutos @Inject constructor(private val repositorio: Repositorio) : ViewModel() {
    val produtos = repositorio.fluxoProdutoServico().map {
        if(it.isEmpty()) EstadosDeLoad.Empty
        else EstadosDeLoad.Caregado(it)
    }
    val fluxoDePesquisa= MutableStateFlow<PesquiProduto?>(null)
    val mostraProduto = MutableStateFlow(false)
    private val coroutinesScope= viewModelScope
    private val idProduto = MutableStateFlow(0)
    val produto = idProduto.flatMapLatest {
        repositorio.fluxoPodutoPorID(it).map {
            if(it==null) EstadosDeLoad.Empty
            else EstadosDeLoad.Caregado(it)
        }

    }
    suspend fun salvarProduto(produto: ProdutoServico){
        repositorio.inserirProdutoServico(produto)

    }
    suspend fun apagarProduto(produto: ProdutoServico){
        repositorio.apagarProdutoServico(produto)
    }
    suspend fun atualizarProduto(produto: ProdutoServico){
        repositorio.atualizarProdutoServico(produto)
    }
    fun mostrarProduto(id:Int){
        coroutinesScope.launch {
            idProduto.emit(id)
            mostraProduto.emit(true)
        }

    }
    fun ocultar(){
        coroutinesScope.launch {
            mostraProduto.emit(false)

        }


    }
    fun formatarPreco(preco: Double):String{
        return String.format("%.2f",preco).replace(".",",")

    }
}

data class PesquiProduto(val prod: String)