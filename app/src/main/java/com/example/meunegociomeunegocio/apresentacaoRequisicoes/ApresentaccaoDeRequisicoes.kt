package com.example.meunegociomeunegocio.apresentacaoRequisicoes

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.example.meunegociomeunegocio.viewModel.ViewModelRequisicoes

@Composable
fun ApresentacaoDeRequisicao(modifier: Modifier=Modifier,windowSize:WindowSizeClass,vm: ViewModelRequisicoes,acaoNavegarEdicao:(int : Int)-> Unit){
     ListaDeRequisicoes(windowSizeClass = windowSize, vm = vm,modifier.padding(top=5.dp),acaoNavegarEdicao)
}