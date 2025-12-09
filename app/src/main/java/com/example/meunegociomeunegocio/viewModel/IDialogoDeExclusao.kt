package com.example.meunegociomeunegocio.viewModel

import com.example.meunegociomeunegocio.utilitario.EstadoLoadAcoes
import com.example.meunegociomeunegocio.utilitario.EstadosDeLoadCaregamento
import kotlinx.coroutines.flow.MutableStateFlow

interface IDialogoDeExclusao {
    val abrirDialogoDeExclusao: MutableStateFlow<Boolean>
    val estadoDeExclusao: MutableStateFlow<EstadoLoadAcoes>
    val idExclusao: MutableStateFlow<Int>
    fun excluir()
    fun fecharDialogo()
    fun abrirDialogo(id:Int)
}