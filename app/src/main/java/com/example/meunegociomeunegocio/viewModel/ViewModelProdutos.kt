package com.example.meunegociomeunegocio.viewModel

import androidx.lifecycle.ViewModel
import com.example.meunegociomeunegocio.repositorioRom.ProdutoServico
import com.example.meunegociomeunegocio.repositorioRom.Repositorio
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class ViewModelProdutos @Inject constructor(private val repositorio: Repositorio) : ViewModel() {
    val produtos = repositorio.fluxoProdutoServico()
    val fluxoDePesquisa= MutableStateFlow<PesquiProduto?>(null)
    val mostraProduto = MutableStateFlow(false)
    val idProduto = MutableStateFlow(0)
    suspend fun salvarProduto(produto: ProdutoServico){
        repositorio.inserirProdutoServico(produto)

    }
    suspend fun apagarProduto(produto: ProdutoServico){
        repositorio.apagarProdutoServico(produto)
    }
    suspend fun atualizarProduto(produto: ProdutoServico){
        repositorio.atualizarProdutoServico(produto)
    }

}

data class PesquiProduto(val prod: String)