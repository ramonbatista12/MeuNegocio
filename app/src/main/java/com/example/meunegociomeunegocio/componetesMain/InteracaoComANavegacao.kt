package com.example.meunegociomeunegocio.componetesMain

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.example.meunegociomeunegocio.navegacao.DestinosDeNavegacao
import com.example.meunegociomeunegocio.navegacao.IconesDeDestino
import com.example.meunegociomeunegocio.viewModel.ViewModelMain

@Composable
fun BaraLateral(windowSizeClass: WindowSizeClass){
    when{
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)->{
            BaraLateralLarguraMedia(windowSizeClass)
        }
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)->{
            BaraLateralLarguraEspandida(windowSizeClass)
        }
        else ->{}
    }
}

@Composable
private fun BaraLateralLarguraMedia(windowSizeClass: WindowSizeClass,modifier: Modifier=Modifier){
    when{
        windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND)->{

        }
        windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_EXPANDED_LOWER_BOUND)->{

        }
        else->{

        }
    }
}


@Composable
private fun BaraLateralLarguraEspandida(windowSizeClass: WindowSizeClass,modifier: Modifier=Modifier){
    when{
        windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND)->{

        }
        windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_EXPANDED_LOWER_BOUND)->{
            Column(modifier = modifier) {
                IconesDeDestino.listaDeIcones.forEach {
                    ItemDeIconesDeDestino(destino = it)
                }
            }
        }
        else->{

        }
    }
}

@Composable
private  fun ItemDeIconesDeDestino(modifier: Modifier=Modifier,destino: IconesDeDestino){
    NavigationDrawerItem(label = {},
                         modifier = modifier,
                         selected = false,
                         onClick = {},
                         icon = {
                             Icon(painterResource(destino.idIcone),contentDescription = null)
                         })

}

@Composable
fun BarraInferior(windowSizeClass: WindowSizeClass,
                  modifier: Modifier= Modifier,
                  acaoDeNavegacao: (DestinosDeNavegacao)->Unit,
                  vm:ViewModelMain ){
    val estadoDaBAra=vm.estadoSelecaoBarasNavegaveis.collectAsState()
    BottomAppBar(modifier = modifier.padding(end = 5.dp, start = 5.dp), containerColor = MaterialTheme.colorScheme.background) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        IconesDeDestino.listaDeIcones.forEach {
            ItemButtonBarCompat(destino = it,
                                acaoDeNavegacao ={ acaoDeNavegacao(it)
                                                   vm.atualizaEstadoSelecaoBarasNavegaveis(it)},
                                estadoDaBara = estadoDaBAra )
        }

    }
    }
}

@Composable
private fun ItemButtonBarCompat(modifier: Modifier= Modifier,
                                destino: IconesDeDestino,
                                acaoDeNavegacao: (DestinosDeNavegacao)->Unit,
                                estadoDaBara: State<DestinosDeNavegacao>){
    val corDeSelecao = MaterialTheme.colorScheme.primary
    LaunchedEffect(estadoDaBara.value) {
        Log.d("ViewModelMain","estado da bara alterado para ${estadoDaBara.value}")
    }
    Icon(painterResource(destino.idIcone),
        contentDescription = "",
        modifier =modifier.clickable(onClick = {acaoDeNavegacao(destino.rota)},
        ).clip(CircleShape),
        tint = if(estadoDaBara.value==destino.rota) corDeSelecao else MaterialTheme.colorScheme.onBackground)

}

@Composable
private fun ItemButtonBarMedio(){

}

