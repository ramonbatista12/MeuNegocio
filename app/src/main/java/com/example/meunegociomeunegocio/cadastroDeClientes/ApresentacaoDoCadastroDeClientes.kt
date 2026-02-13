package com.example.meunegociomeunegocio.cadastroDeClientes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import com.example.meunegociomeunegocio.loads.DialogoLoad
import com.example.meunegociomeunegocio.loads.TitulosDeLoad
import com.example.meunegociomeunegocio.viewModel.ViewModelCadastroDeCliente
/**
 * Ponto de entrada para apresentacao de clientes
 * */
@Composable
fun CadastroDeClientes(modifier: Modifier = Modifier,windowSizeClass: WindowSizeClass,vm: ViewModelCadastroDeCliente,acaoDeVoutar:()->Unit){
    val  loadClinete=vm.LoadCliente.collectAsStateWithLifecycle()
    when{
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)->{
            CadastroCompat(vm, acaoDeVoltar = acaoDeVoutar)
        }
        else ->{
            CadastroCompat(vm, acaoDeVoltar = acaoDeVoutar)
        }
    }
    DialogoLoad(TitulosDeLoad.Clientes.titulo,loadClinete.value)

}