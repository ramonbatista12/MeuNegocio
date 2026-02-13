package com.example.meunegociomeunegocio.navegacao

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import androidx.window.core.layout.WindowSizeClass
import com.example.meunegociomeunegocio.adicaoDeProdutos.AdicaoDePRodutos
import com.example.meunegociomeunegocio.apresentacaoDeClientes.ApresentacaoDeClientes
import com.example.meunegociomeunegocio.apresentacaoDeProdutos.ApresentacaoProdutos
import com.example.meunegociomeunegocio.apresentacaoRequisicoes.ApresentacaoDeRequisicao
import com.example.meunegociomeunegocio.cadastroDeClientes.CadastroDeClientes
import com.example.meunegociomeunegocio.cadstroDeRequisicao.ApresentacaoCriarRequisicao
import com.example.meunegociomeunegocio.viewModel.ViewModelAdicaoDeProdutos
import com.example.meunegociomeunegocio.viewModel.ViewModelCadastroDeCliente
import com.example.meunegociomeunegocio.viewModel.ViewModelCriarRequisicoes

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
            ApresentacaoDeRequisicao(modifier = modifier,windowSize = windowSize,hiltViewModel(),{navController.navigate(DestinosDeNavegacao.AdicaoDeRequisicoes(it))})
        }

        composable<DestinosDeNavegacao.Clientes> {
            LaunchedEffect(Unit) {
                avisoDeDestino(DestinosDeNavegacao.Clientes)
                acaoMostrarBotaoDeAdicionar()
            }
            ApresentacaoDeClientes(vm = hiltViewModel(),modifier,windowSize = windowSize,{it->
                Log.d("idcliente","na chamada do navigrafic o id cliente e ${it}")
                navController.navigate(DestinosDeNavegacao.AdicaoDeCleintes(it))})
        }
        composable<DestinosDeNavegacao.Produtos> {
            LaunchedEffect(Unit) {
                avisoDeDestino(DestinosDeNavegacao.Produtos)
                acaoMostrarBotaoDeAdicionar()
            }
            ApresentacaoProdutos(vm = hiltViewModel(), modifier = modifier,windowSize = windowSize, acaoDeEdicaoDeProdutos = {navController.navigate(DestinosDeNavegacao.AdicaoDeProdutos(it))})
        }
        composable<DestinosDeNavegacao.AdicaoDeCleintes>{
            LaunchedEffect(Unit){
                acaoOcultarBotaoDeAdicionar()
            }
            var cliente =it.toRoute<DestinosDeNavegacao.AdicaoDeCleintes>()
            CadastroDeClientes(windowSizeClass = windowSize,vm=hiltViewModel<ViewModelCadastroDeCliente,ViewModelCadastroDeCliente.Fabrica>{it.criar(cliente.idCliente)}, acaoDeVoutar = { navController.popBackStack() })
        }
        composable<DestinosDeNavegacao.AdicaoDeProdutos>{
            LaunchedEffect(Unit) {
                acaoOcultarBotaoDeAdicionar()
            }
            var prodId = it.toRoute<DestinosDeNavegacao.AdicaoDeProdutos>()
            AdicaoDePRodutos(vm = hiltViewModel<ViewModelAdicaoDeProdutos, ViewModelAdicaoDeProdutos.Fabrica>{it.criar(prodId.idProduto)},acaoDeVoutar = {navController.popBackStack()})
        }

        composable<DestinosDeNavegacao.AdicaoDeRequisicoes> {
            LaunchedEffect(Unit) {
                acaoOcultarBotaoDeAdicionar()
                Log.d(Tag,"AdicaoDeRequisicoes")
            }
            var adicoaRequisicao : DestinosDeNavegacao.AdicaoDeRequisicoes =it.toRoute<DestinosDeNavegacao.AdicaoDeRequisicoes>()
            Log.d(Tag,"AdicaoDeRequisicoes ${adicoaRequisicao}")
            ApresentacaoCriarRequisicao(hiltViewModel<ViewModelCriarRequisicoes,ViewModelCriarRequisicoes.Factorory>{it.criar(adicoaRequisicao.idRequisicao)},windowSize,acaoDeVoutar = {navController.popBackStack()})
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