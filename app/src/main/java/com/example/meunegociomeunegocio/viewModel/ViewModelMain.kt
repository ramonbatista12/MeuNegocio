package com.example.meunegociomeunegocio.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ViewModelMain: ViewModel(){
    private val _permicaoDeNotificao = MutableStateFlow<Boolean>(false)
    val permicaoDeNotificacao=_permicaoDeNotificao
    private val corotineScope =viewModelScope
    fun atualizaPermicaoDeNotificao(permicao:Boolean)=viewModelScope.launch {  _permicaoDeNotificao.emit(permicao)}


}

