package com.example.meunegociomeunegocio.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meunegociomeunegocio.repositorioRom.ProdutoServico
import com.example.meunegociomeunegocio.repositorioRom.Repositorio
import com.example.meunegociomeunegocio.utilitario.EstadoLoadAcoes
import com.example.meunegociomeunegocio.utilitario.EstadosDeLoadCaregamento
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class ViewModelProdutos @Inject constructor(private val repositorio: Repositorio) : ViewModel(),
    IDialogoDeExclusao {
    val produtos = repositorio.fluxoProdutoServico().map {
        if(it.isEmpty()) EstadosDeLoadCaregamento.Empty
        else EstadosDeLoadCaregamento.Caregado(it)
    }
    val pesquisa= MutableStateFlow<Pesquisa>(Pesquisa(""))
    val mostraProduto = MutableStateFlow(false)
    private val coroutinesScope= viewModelScope
    private val idProduto = MutableStateFlow(0)
    private val _telasDeProdutos=MutableStateFlow<TealasDeProduto>(TealasDeProduto.Lista)
    private val _dialogoDeExclusao=MutableStateFlow(false)
    private val _estadoDeExclusao=MutableStateFlow<EstadoLoadAcoes>(EstadoLoadAcoes.Iniciando)
    val telasDeProdutos=_telasDeProdutos
    val produto = idProduto.flatMapLatest {
        repositorio.fluxoPodutoPorID(it).map {
            if(it==null) EstadosDeLoadCaregamento.Empty
            else EstadosDeLoadCaregamento.Caregado(it)
        }

    }
    val pesquisaDeProduto = pesquisa.flatMapLatest {
        repositorio.fluxoDePesquisaDeProdutos(it.pesquisa)
    }
    override val abrirDialogoDeExclusao: MutableStateFlow<Boolean> = _dialogoDeExclusao
    override val estadoDeExclusao: MutableStateFlow<EstadoLoadAcoes> = _estadoDeExclusao
    override val idExclusao: MutableStateFlow<Int> = idProduto

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

    override fun excluir() {
        viewModelScope.launch {
            _estadoDeExclusao.emit(EstadoLoadAcoes.Criando)
            try {
                repositorio.apagarProdutoServico(produto = ProdutoServico(idProduto.value,false," ","",0.0f,false))
                _estadoDeExclusao.emit(EstadoLoadAcoes.Sucesso)
                idProduto.emit(0)
                return@launch
            }catch (e: Exception){
                _estadoDeExclusao.emit(EstadoLoadAcoes.Erro)
                return@launch

            }
        }
    }

    override fun abrirDialogo(id: Int) {
        viewModelScope.launch {
            idProduto.emit(id)
            _dialogoDeExclusao.emit(true)

        }
    }
    override fun fecharDialogo() {
        viewModelScope.launch {
            idProduto.emit(0)
            _dialogoDeExclusao.emit(false)

        }
    }
}


sealed class TealasDeProduto(){
    object Lista : TealasDeProduto()
    object Descricao : TealasDeProduto()

}