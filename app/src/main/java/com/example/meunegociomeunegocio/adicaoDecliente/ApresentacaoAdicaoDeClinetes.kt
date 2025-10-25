package com.example.meunegociomeunegocio.adicaoDecliente

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowSizeClass
import com.example.meunegociomeunegocio.viewModel.ViewModelCliente

@Composable
fun ApresentacaoAdicaoDeClientes(modifier: Modifier=Modifier,windowSize: WindowSizeClass,viewModelCliente: ViewModelCliente){
    when{
        windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)->{}
        windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)->{}
        else->{}
    }
}

@Composable
private fun ApresetacaoDeClientesCompat(modifier: Modifier= Modifier,windowSizeClass: WindowSizeClass){

}


@Composable
private fun ApresetacaoDeClientesExpandido(){

}