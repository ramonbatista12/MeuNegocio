package com.example.meunegociomeunegocio.apresentacaoDeClientes

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowSizeClass
import com.example.meunegociomeunegocio.viewModel.TelasInternasDeClientes
import com.example.meunegociomeunegocio.viewModel.TelasInternasDeClientes.ListaDeClientes
import com.example.meunegociomeunegocio.viewModel.ViewModelCliente

@Composable
fun ApresentacaoDeClientes(vm: ViewModelCliente,modifier: Modifier=Modifier,windowSize: WindowSizeClass){
    when{
        windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)->{}
        else -> ListaDeClientesCompat(vm)
    }

}

@Composable
private fun ListaDeClientesCompat(vm: ViewModelCliente,modifier: Modifier= Modifier){
    val telaVisualizada=vm.telaVisualizada.collectAsState()
    when(telaVisualizada.value){
        is TelasInternasDeClientes.ListaDeClientes->ListaDeClientes(vm=vm, modifier = modifier)
        is TelasInternasDeClientes.DadosDoCliente->DadosDeClientes(vm=vm)
        else -> {
            Log.d("ApresentacaoDeClientes","tela nao implementada")
        }

    }
}