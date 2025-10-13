package com.example.meunegociomeunegocio.apresentacaoDeClientes

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import com.example.meunegociomeunegocio.repositorioRom.DadosDeClientes
import com.example.meunegociomeunegocio.viewModel.TelasInternasDeClientes
import com.example.meunegociomeunegocio.viewModel.ViewModelCliente
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.meunegociomeunegocio.utilitario.EstadosDeLoad

@Composable
fun DadosDeClientes(vm: ViewModelCliente,modifier: Modifier=Modifier){
    val id =(vm.telaVisualizada.collectAsState().value as TelasInternasDeClientes.DadosDoCliente).idCliente
    val load=  vm.dasDeClientes(id).collectAsState(initial = EstadosDeLoad.load )
    when(load){
        is EstadosDeLoad.load->{
            CircularProgressIndicator(modifier = modifier.size(40.dp))
        }
        is EstadosDeLoad.Caregado<*> ->{

        }
        else -> {}
    }
}






