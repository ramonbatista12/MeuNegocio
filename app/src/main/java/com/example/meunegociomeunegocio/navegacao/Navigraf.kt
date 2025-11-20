package com.example.meunegociomeunegocio.navegacao

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.window.core.layout.WindowSizeClass
import com.example.meunegociomeunegocio.adicaoDeProdutos.AdicaoDePRodutos
import com.example.meunegociomeunegocio.apresentacaoDeClientes.ApresentacaoDeClientes
import com.example.meunegociomeunegocio.apresentacaoDeProdutos.ApresentacaoProdutos
import com.example.meunegociomeunegocio.apresentacaoRequisicoes.ApresentacaoDeRequisicao
import com.example.meunegociomeunegocio.cadastroDeClientes.CadastroDeClientes
import com.example.meunegociomeunegocio.cadstroDeRequisicao.ApresentacaoCriarRequisicao
import com.example.meunegociomeunegocio.repositorioRom.EstadoRequisicao
import com.example.meunegociomeunegocio.viewModel.ViewModelCliente
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun Navigraf(navController: NavHostController,windowSize: WindowSizeClass,modifier: Modifier=Modifier,
             avisoDeDestino:(DestinosDeNavegacao)->Unit,
             acaoOcultarBotaoDeAdicionar:()->Unit,
             acaoMostrarBotaoDeAdicionar:()->Unit){
    val Tag ="Navigraf"
    NavHost(navController=navController, startDestination = DestinosDeNavegacao.Requisicoes,
            modifier = modifier){
        composable<DestinosDeNavegacao.Requisicoes>{
            LaunchedEffect(Unit) {
                avisoDeDestino(DestinosDeNavegacao.Requisicoes)
                acaoMostrarBotaoDeAdicionar()
            }
            ApresentacaoDeRequisicao(modifier = modifier,windowSize = windowSize,hiltViewModel())
        }

        composable<DestinosDeNavegacao.Clientes> {
            LaunchedEffect(Unit) {
                avisoDeDestino(DestinosDeNavegacao.Clientes)
                acaoMostrarBotaoDeAdicionar()
            }
            ApresentacaoDeClientes(vm = hiltViewModel(),modifier,windowSize = windowSize)
        }
        composable<DestinosDeNavegacao.Produtos> {
            LaunchedEffect(Unit) {
                avisoDeDestino(DestinosDeNavegacao.Produtos)
                acaoMostrarBotaoDeAdicionar()
            }
            ApresentacaoProdutos(vm = hiltViewModel(), modifier = modifier,windowSize = windowSize)
        }
        composable<DestinosDeNavegacao.AdicaoDeCleintes>{
            LaunchedEffect(Unit){
                acaoOcultarBotaoDeAdicionar()
            }
            CadastroDeClientes(windowSizeClass = windowSize,vm=hiltViewModel(), acaoDeVoutar = {navController.popBackStack()})
        }
        composable<DestinosDeNavegacao.AdicaoDeProdutos>{
            LaunchedEffect(Unit) {
                acaoOcultarBotaoDeAdicionar()
            }
            AdicaoDePRodutos(vm = hiltViewModel(),acaoDeVoutar = {navController.popBackStack()})
        }

        composable<DestinosDeNavegacao.AdicaoDeRequisicoes> {
            LaunchedEffect(Unit) {
                acaoOcultarBotaoDeAdicionar()
                Log.d(Tag,"AdicaoDeRequisicoes")
            }
            ApresentacaoCriarRequisicao(hiltViewModel(),windowSize,acaoDeVoutar = {navController.popBackStack()})
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