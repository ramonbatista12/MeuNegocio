package com.example.meunegociomeunegocio.viewModel

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meunegociomeunegocio.repositorioRom.ProdutoServico
import com.example.meunegociomeunegocio.repositorioRom.Repositorio
import com.example.meunegociomeunegocio.utilitario.AuxiliarValidacaoDadosDeProdutos
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

