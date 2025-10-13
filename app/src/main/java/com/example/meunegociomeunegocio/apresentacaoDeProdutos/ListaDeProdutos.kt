package com.example.meunegociomeunegocio.apresentacaoDeProdutos

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.meunegociomeunegocio.viewModel.ViewModelProdutos


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment

import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.example.meunegociomeunegocio.repositorioRom.ProdutoServico



@Composable
fun ListaDeProdutos(modifier: Modifier=Modifier,vm: ViewModelProdutos,windowSize: WindowSizeClass){
    val listaDeProdutos=vm.produtos.collectAsState(initial = emptyList())
    LazyColumn(modifier = modifier.padding(horizontal = 5.dp)) {
        stickyHeader{
            Cabesalho()
        }
        items(items = listaDeProdutos.value){
            ItemProduto(windowSize=windowSize, produto = it)
        }

    }



}

@Composable
private fun ItemProduto(windowSize: WindowSizeClass, modifier: Modifier=Modifier,
                        produto: ProdutoServico ){

    val expandidido = remember{mutableStateOf(false)}
    LaunchedEffect(windowSize) {
        Log.d("teste","teste windowSize clas mudo")
        if(windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND))
            Log.d("texte","window size class medio")
        if(windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND))
            Log.d("texte","window size class expandido")
    }

    Card(modifier= Modifier.fillMaxWidth().height(if(expandidido.value) 180.dp else 70.dp).padding(top = 5.dp, start = 3.dp, end = 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)) {
        Box(modifier = Modifier.fillMaxSize()){


            Row(Modifier.padding(top = 3.dp, start = 5.dp, end = 5.dp).align(if(!expandidido.value)Alignment.CenterStart else Alignment.TopStart)) {
                Text(produto.nome, maxLines = 2,modifier=Modifier.width(70.dp))
                Spacer(Modifier.padding(10.dp))
                Text(produto.preco.toString(), Modifier.width(70.dp))
                Spacer(Modifier.padding(10.dp))
                if(windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)||windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND))
                    Text(produto.descrisao, maxLines = 1, modifier = Modifier.fillMaxWidth().padding(end = 90.dp), overflow = TextOverflow.Ellipsis)
                else
                    Text(produto.descrisao, maxLines = 1, modifier = Modifier.width(90.dp), overflow = TextOverflow.Ellipsis)



            }
            FlowRow(Modifier.align(if(!expandidido.value)Alignment.CenterEnd else Alignment.TopEnd)) {
                IconButton ({},modifier= Modifier.size(30.dp).padding(end = 3.dp)) {
                    Icon(Icons.Outlined.Create,modifier= Modifier.size(20.dp),contentDescription = "")
                }

                IconButton ({},modifier= Modifier.size(30.dp).padding(5.dp)) {
                    Icon(Icons.Outlined.Delete,modifier= Modifier.size(20.dp), contentDescription = "")
                }
                IconButton(onClick = {expandidido.value=!expandidido.value}, modifier = Modifier.size(30.dp)) {
                    if(!expandidido.value)
                        Icon(Icons.Outlined.ArrowDropDown, contentDescription = "")
                    else
                        Icon(Icons.Outlined.KeyboardArrowUp, contentDescription = "", modifier = Modifier)
                }
            }
            Column(Modifier.fillMaxWidth().align(Alignment.CenterStart).padding(top = 3.dp)){
                AnimatedVisibility(visible = expandidido.value, Modifier){
                    Column (Modifier.fillMaxWidth().padding(start = 5.dp)){
                        Row {Text("Produto :")
                            Text(produto.nome)}
                        Text(text = produto.descrisao, maxLines = 5,modifier =  Modifier)
                    }}}
            HorizontalDivider(Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun Cabesalho(){
    OutlinedCard (modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxSize().padding(top = 10.dp, bottom = 10.dp)){
            Row(modifier = Modifier.align(Alignment.CenterStart).padding(start = 5.dp, end = 5.dp)) {
                Text("Produto", modifier = Modifier.width(70.dp))
                Spacer(Modifier.padding(10.dp))
                Text("Preco", modifier = Modifier.width(70.dp))
                Spacer(Modifier.padding(10.dp))
                Text("Descricao")


            }
        }
    }
}


