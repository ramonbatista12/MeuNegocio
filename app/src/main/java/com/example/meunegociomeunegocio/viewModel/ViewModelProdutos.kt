package com.example.meunegociomeunegocio.viewModel

import androidx.lifecycle.ViewModel
import com.example.meunegociomeunegocio.repositorioRom.ProdutoServico
import com.example.meunegociomeunegocio.repositorioRom.Repositorio
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
@HiltViewModel
class ViewModelProdutos @Inject constructor(private val repositorio: Repositorio) : ViewModel() {
    val produtos = repositorio.fluxoProdutoServico()
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