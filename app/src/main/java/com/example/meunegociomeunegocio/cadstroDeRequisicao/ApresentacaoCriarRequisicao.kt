package com.example.meunegociomeunegocio.cadstroDeRequisicao

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.window.core.layout.WindowSizeClass
import com.example.meunegociomeunegocio.viewModel.ViewModelCriarRequisicoes

@Composable
fun ApresentacaoCriarRequisicao(vm: ViewModelCriarRequisicoes,windowSizeClass: WindowSizeClass,acaoDeVoutar:()->Unit={}){
    LaunchedEffect(Unit){
        Log.d("ApresentacaoCriarRequisicao","ApresentacaoCriarRequisicao iniciada")
    }
   ColetaDeDaDosRequisicao(vm = vm,windowSizeClass = windowSizeClass,acaoDeVoutar )
}