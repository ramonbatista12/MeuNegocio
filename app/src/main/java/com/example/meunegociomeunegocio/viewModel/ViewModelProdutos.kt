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
    val pesquisa= MutableStateFlow<Pesquisa>(Pesquisa(""))
    val mostraProduto = MutableStateFlow(false)
    private val coroutinesScope= viewModelScope
    private val idProduto = MutableStateFlow(0)
    private val _telasDeProdutos=MutableStateFlow<TealasDeProduto>(TealasDeProduto.Lista)
    val telasDeProdutos=_telasDeProdutos
    val produto = idProduto.flatMapLatest {
        repositorio.fluxoPodutoPorID(it).map {
            if(it==null) EstadosDeLoad.Empty
            else EstadosDeLoad.Caregado(it)
        }

    }
    val pesquisaDeProduto = pesquisa.flatMapLatest {
        repositorio.fluxoDePesquisaDeProdutos(it.pesquisa)
    }

    suspend fun mudarPesquisa(pesquisa: Pesquisa){
        this.pesquisa.emit(pesquisa)
    }
    fun ocultar(){
        coroutinesScope.launch {
            mostraProduto.emit(false)

        }


    }
    fun formatarPreco(preco: Double):String{
        return String.format("%.2f",preco).replace(".",",")

    }
    fun mostraDescricao(id: Int){
        coroutinesScope.launch {
            idProduto.emit(id)
            telasDeProdutos.emit(TealasDeProduto.Descricao)

        }
    }
    fun mostraLista(){
        coroutinesScope.launch {
            telasDeProdutos.emit(TealasDeProduto.Lista)
        }
    }
}


sealed class TealasDeProduto(){
    object Lista : TealasDeProduto()
    object Descricao : TealasDeProduto()

}