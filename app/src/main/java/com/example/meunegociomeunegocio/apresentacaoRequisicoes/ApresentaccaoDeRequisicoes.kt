package com.example.meunegociomeunegocio.apresentacaoRequisicoes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowSizeClass
import com.example.meunegociomeunegocio.viewModel.ViewModelRequisicoes

@Composable
fun ApresentacaoDeRequisicao(modifier: Modifier=Modifier,windowSize:WindowSizeClass,vm: ViewModelRequisicoes){
     ListaDeRequisicoes(windowSizeClass = windowSize, vm = vm)
}