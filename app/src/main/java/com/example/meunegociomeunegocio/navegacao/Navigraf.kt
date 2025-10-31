package com.example.meunegociomeunegocio.navegacao

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.window.core.layout.WindowSizeClass
import com.example.meunegociomeunegocio.apresentacaoDeClientes.ApresentacaoDeClientes
import com.example.meunegociomeunegocio.apresentacaoDeProdutos.ApresentacaoProdutos
import com.example.meunegociomeunegocio.apresentacaoRequisicoes.ApresentacaoDeRequisicao
import com.example.meunegociomeunegocio.cadastroDeClientes.CadastroDeClientes
import com.example.meunegociomeunegocio.repositorioRom.EstadoRequisicao
import com.example.meunegociomeunegocio.viewModel.ViewModelCliente
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun Navigraf(navController: NavHostController,windowSize: WindowSizeClass,modifier: Modifier=Modifier){
    NavHost(navController=navController, startDestination = DestinosDeNavegacao.Requisicoes,
            modifier = modifier){
        composable<DestinosDeNavegacao.Requisicoes>{
            ApresentacaoDeRequisicao(modifier = modifier,windowSize = windowSize,hiltViewModel())
        }

        composable<DestinosDeNavegacao.Clientes> {
            ApresentacaoDeClientes(vm = hiltViewModel(),modifier,windowSize = windowSize)
        }
        composable<DestinosDeNavegacao.Produtos> {
            ApresentacaoProdutos(vm = hiltViewModel(), modifier = modifier,windowSize = windowSize)
        }
        composable<DestinosDeNavegacao.AdicaoDeCleintes>{
            CadastroDeClientes(windowSizeClass = windowSize,vm=hiltViewModel())
        }
        dialog<DestinosDeNavegacao.Dialogos.NovoCliente>{

        }
        dialog<DestinosDeNavegacao.Dialogos.NovoProduto>{

        }
        dialog<DestinosDeNavegacao.Dialogos.NovaRequisicao>{

        }
        dialog<DestinosDeNavegacao.Dialogos.EditarCliente>{

        }
        dialog<DestinosDeNavegacao.Dialogos.EditarProduto>{

        }


    }
}