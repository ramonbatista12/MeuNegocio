package com.example.meunegociomeunegocio.viewModel

import androidx.lifecycle.ViewModel
import com.example.meunegociomeunegocio.repositorioRom.ProdutoServico
import com.example.meunegociomeunegocio.repositorioRom.Repositorio
import jakarta.inject.Inject

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