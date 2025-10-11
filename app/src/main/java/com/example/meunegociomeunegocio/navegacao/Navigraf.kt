package com.example.meunegociomeunegocio.navegacao

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.window.core.layout.WindowSizeClass

@Composable
fun Navigraf(navController: NavHostController,windowSize: WindowSizeClass,modifier: Modifier=Modifier){
    NavHost(navController=navController, startDestination = DestinosDeNavegacao.Requisicoes,
            modifier = modifier){
        composable<DestinosDeNavegacao.Requisicoes>{

        }
        composable<DestinosDeNavegacao.Clientes> {

        }
        composable<DestinosDeNavegacao.Produtos> {

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