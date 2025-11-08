package com.example.meunegociomeunegocio.viewModel

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meunegociomeunegocio.repositorioRom.ProdutoServico
import com.example.meunegociomeunegocio.repositorioRom.Repositorio
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ViewModelAdicaoDeProdutos @Inject constructor(val repositorio: Repositorio): ViewModel() {
    private val validador= AuxiliarValidacaoDadosDeProdutos()
    private val corotinesScope=viewModelScope
    val snackbarHostState= SnackbarHostState()
    suspend fun adicionarProduto(produto: ProdutoServico,callback:()-> Unit){
        try {
            repositorio.inserirProdutoServico(validador.validarProduto(produto))
            callback()
        }catch (e: IllegalArgumentException){
            corotinesScope.launch {
                snackbarHostState.showSnackbar(e.message.toString())
            }

        }
    }
}

class AuxiliarValidacaoDadosDeProdutos{
    suspend fun validarProduto(produto: ProdutoServico):ProdutoServico{
        val nome=validarNome(produto.nome)
        val preco=validarPreco(produto.preco.toString())
        val descricao=validarDescricao(produto.descrisao)
        return ProdutoServico(id=produto.id, nome = nome, preco = preco, descrisao = descricao, servico = produto.servico, ativo = produto.ativo)
     }
    private suspend fun validarNome(string: String):String{
        if(string.isBlank()) throw IllegalArgumentException("Nome nao pode estar vasio")
        return string
    }
    private suspend fun validarPreco(string: String): Float{
        if(string.isBlank()) throw IllegalArgumentException("Preco nao pode estar vasio")
        if(string.matches(Regex("0,0|0,00"))) throw IllegalArgumentException("Preco nao pode ter valor 0,0")
        return string.toFloat()

    }
    private suspend fun validarDescricao(string: String):String{
        if(string.isBlank()) throw IllegalArgumentException("Descricao nao pode estar vasio")
        return string

    }
}