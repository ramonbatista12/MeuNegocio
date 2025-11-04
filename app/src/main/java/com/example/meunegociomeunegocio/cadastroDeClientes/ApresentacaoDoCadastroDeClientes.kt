package com.example.meunegociomeunegocio.cadastroDeClientes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowSizeClass
import com.example.meunegociomeunegocio.viewModel.ViewModelCadastroDeCliente

@Composable
fun CadastroDeClientes(modifier: Modifier = Modifier,windowSizeClass: WindowSizeClass,vm: ViewModelCadastroDeCliente){
    when{
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)->{
            CadastroCompat(vm)
        }
        else ->{
            CadastroCompat(vm)
        }
    }

}