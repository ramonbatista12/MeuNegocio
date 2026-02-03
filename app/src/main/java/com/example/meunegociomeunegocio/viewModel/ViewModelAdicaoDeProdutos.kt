package com.example.meunegociomeunegocio.viewModel

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.text.input.setTextAndSelectAll
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meunegociomeunegocio.repositorioRom.ProdutoServico
import com.example.meunegociomeunegocio.repositorioRom.Repositorio
import com.example.meunegociomeunegocio.utilitario.AuxiliarValidacaoDadosDeProdutos
import com.example.meunegociomeunegocio.utilitario.EstadosDeLoadCaregamento
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalFoundationApi::class)
@HiltViewModel(assistedFactory = ViewModelAdicaoDeProdutos.Fabrica::class)
class ViewModelAdicaoDeProdutos @AssistedInject constructor(val repositorio: Repositorio,@Assisted val id:Int?): ViewModel() {
    private val validador= AuxiliarValidacaoDadosDeProdutos()
    private val corotinesScope=viewModelScope
    private val _textFildStateNomeProduto = TextFieldState()
    private val _textFildStatePreco= TextFieldState()
    private val _textFildStateDescricao= TextFieldState()
    private val _servicoProduto = MutableStateFlow(false)
    private val _estadoLoadProduto = MutableStateFlow<EstadosDeLoadCaregamento>(EstadosDeLoadCaregamento.Empty)
    private final val TAG="ViewModelAdicaoDeProdutos"
    val focoNome = FocusRequester()
    val focoPreco= FocusRequester()
    val focoDescricao = FocusRequester()
    val textFildStateNomeProduto =_textFildStateNomeProduto
    val textFildStateDescricao =_textFildStateDescricao
    val textFildPReco =_textFildStatePreco
    val servicoProduto =_servicoProduto
    val loadProduto=_estadoLoadProduto

    val snackbarHostState= SnackbarHostState()
    init {
        if(id!=null)caregarProduto()
    }
    private fun caregarProduto(){
        corotinesScope.launch(Dispatchers.IO) {
            _estadoLoadProduto.emit(EstadosDeLoadCaregamento.load)
            val produto =repositorio.fluxoPodutoPorID(id!!).first()
            if(produto!=null){
                withContext(Dispatchers.Main) {
                    _textFildStateNomeProduto.edit {
                        replace(0,this.length,produto.nome)
                    }
                    _textFildStateNomeProduto.undoState.redo()
                    _textFildStatePreco.edit{
                        replace(0,this.length,produto.preco.toString())
                    }
                    _textFildStateDescricao.edit{
                        replace(0,this.length,produto.descrisao)
                    }
                    _servicoProduto.emit(produto.servico)
                    delay(100)
                    focoDescricao.requestFocus()
                    delay(100)
                    focoPreco.requestFocus()
                    delay(100)
                    focoNome.requestFocus()

                }

            }
            delay(1000)
            _estadoLoadProduto.emit(EstadosDeLoadCaregamento.Caregado(Unit))

        }
    }
    suspend fun adicionarProduto(produto: ProdutoServico,callback:()-> Unit){
        if(id==null){
            Log.d(TAG,"adicionando produto id e null" )
            try {
                repositorio.inserirProdutoServico(validador.validarProduto(produto))
                callback()
            }catch (e: IllegalArgumentException){
                corotinesScope.launch {
                    snackbarHostState.showSnackbar(e.message.toString())
                }

            }
        }
        else{
            try {
                Log.d(TAG,"adicionando produto id e $id" )
                repositorio.atualizarProdutoServico(validador.validarProduto(ProdutoServico(id = id, servico = produto.servico, nome = produto.nome, preco = produto.preco, descrisao = produto.descrisao, ativo = produto.ativo)))
                callback()
            }catch (e: IllegalArgumentException){
                corotinesScope.launch {
                    snackbarHostState.showSnackbar(e.message.toString())
                }

            }

        }
    }

    fun produtoSelecionado(){
        corotinesScope.launch {
            _servicoProduto.emit(false)
        }
    }
    fun servicoSelecionado(){
        corotinesScope.launch {
            _servicoProduto.emit(true)
        }
    }
    @AssistedFactory
    interface Fabrica {
        fun criar(id: Int?): ViewModelAdicaoDeProdutos
    }
}

