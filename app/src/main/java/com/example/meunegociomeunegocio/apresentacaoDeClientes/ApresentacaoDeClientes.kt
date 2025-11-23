package com.example.meunegociomeunegocio.apresentacaoDeClientes

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.example.meunegociomeunegocio.viewModel.TelasInternasDeClientes
import com.example.meunegociomeunegocio.viewModel.TelasInternasDeClientes.ListaDeClientes
import com.example.meunegociomeunegocio.viewModel.ViewModelCliente

@Composable
fun ApresentacaoDeClientes(vm: ViewModelCliente,modifier: Modifier=Modifier,windowSize: WindowSizeClass){
    when{
        windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)->{
            ListaDeClientesExpandida(modifier,vm,windowSize)
        }
        else -> ListaDeClientesCompat(vm,modifier,windowSize)
    }

}

@Composable
private fun ListaDeClientesCompat(vm: ViewModelCliente,modifier: Modifier= Modifier,windowSizeClass: WindowSizeClass){
    val telaVisualizada=vm.telaVisualizada.collectAsState()
    when(telaVisualizada.value){
        is ListaDeClientes->ListaDeClientes(vm=vm, modifier = modifier)
        is TelasInternasDeClientes.DadosDoCliente->DadosDeClientes(vm=vm, windowSizeClass = windowSizeClass, modifier = modifier)
        else -> {
            Log.d("ApresentacaoDeClientes","tela nao implementada")
        }

    }
}

@Composable
private fun ListaDeClientesExpandida(modifier: Modifier= Modifier,vm: ViewModelCliente,windowSizeClass: WindowSizeClass){
    Row(modifier = modifier.padding(horizontal = 5.dp)) {
        ListaDeClientes(Modifier.fillMaxWidth(0.4f), vm = vm)
        VerticalDivider(Modifier.padding(horizontal = 15.dp))
        DadosDeClientesExpandido(vm, windowSizeClass = windowSizeClass, modifier = Modifier.fillMaxWidth(0.4f))

    }
}