package com.example.meunegociomeunegocio.cadstroDeRequisicao

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import com.example.meunegociomeunegocio.loads.DialogoLoad
import com.example.meunegociomeunegocio.loads.TitulosDeLoad
import com.example.meunegociomeunegocio.viewModel.ViewModelCriarRequisicoes

@Composable
fun ApresentacaoCriarRequisicao(vm: ViewModelCriarRequisicoes,windowSizeClass: WindowSizeClass,acaoDeVoutar:()->Unit={}){

   var looadRequisicao=vm.estadoDeLoadRequisicoes.collectAsStateWithLifecycle()
   ColetaDeDaDosRequisicao(vm = vm,windowSizeClass = windowSizeClass,acaoDeVoutar )
    DialogoLoad(TitulosDeLoad.Requisicoes.titulo,looadRequisicao.value, Color.Blue)
}