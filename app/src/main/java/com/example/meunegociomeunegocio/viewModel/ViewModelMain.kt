package com.example.meunegociomeunegocio.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meunegociomeunegocio.navegacao.DestinosDeNavegacao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ViewModelMain: ViewModel(){
    private val Tag="ViewModelMain"
    private val _permicaoDeNotificao = MutableStateFlow<Boolean>(false)
    private val _estadoSelecaoBarasNavegaveis =MutableStateFlow<DestinosDeNavegacao>(DestinosDeNavegacao.Requisicoes)
    val permicaoDeNotificacao=_permicaoDeNotificao
    val estadoSelecaoBarasNavegaveis=_estadoSelecaoBarasNavegaveis
    private val corotineScope =viewModelScope
    fun atualizaPermicaoDeNotificao(permicao:Boolean)=viewModelScope.launch {
        Log.d(Tag,"atualizaPermicaoDeNotificao")
        _permicaoDeNotificao.emit(permicao)}
    fun atualizaEstadoSelecaoBarasNavegaveis(destino: DestinosDeNavegacao){viewModelScope.launch { _estadoSelecaoBarasNavegaveis.value=destino}}


}

