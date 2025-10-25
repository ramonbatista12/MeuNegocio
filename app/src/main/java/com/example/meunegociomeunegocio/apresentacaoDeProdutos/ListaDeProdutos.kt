package com.example.meunegociomeunegocio.apresentacaoDeProdutos

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.meunegociomeunegocio.viewModel.ViewModelProdutos


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment

import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.example.meunegociomeunegocio.repositorioRom.ProdutoRequisitado

import com.example.meunegociomeunegocio.repositorioRom.ProdutoServico
import com.example.meunegociomeunegocio.viewModel.Pesquisa
import com.example.meunegociomeunegocio.viewModel.TelasInternasDeClientes
import com.example.meunegociomeunegocio.viewModel.ViewModelCliente
import kotlinx.coroutines.launch


@Composable
fun ListaDeProdutos(modifier: Modifier=Modifier,vm: ViewModelProdutos,windowSize: WindowSizeClass){
    val listaDeProdutos=vm.produtos.collectAsState(initial = emptyList())
    Column(modifier = modifier.padding(horizontal = 5.dp)) {
      BaraDePesquisaProdutos(modifier = Modifier.padding(vertical = 5.5.dp, horizontal = 5.dp).fillMaxWidth(),vm = vm)
    LazyColumn {
        stickyHeader{
            Cabesalho(windowSize)
        }
        items(items = listaDeProdutos.value){
            ItemProduto(windowSize=windowSize, produto = it)
        }

    }
}


}

@Composable
fun ListaDeProdutos(modifier: Modifier=Modifier, listaDeProdutos: List<ProdutoRequisitado>, windowSize: WindowSizeClass){

    Column(modifier = modifier.padding(horizontal = 5.dp)) {

        LazyColumn {
            stickyHeader{
                CabesalhoProdutoSolicitado(windowSize)
            }
            items(items = listaDeProdutos){
                ItemProdutoRequisitado(windowSize=windowSize, produto = it)
            }

        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BaraDePesquisaProdutos(modifier: Modifier= Modifier, vm: ViewModelProdutos){
    val coroutineScope =rememberCoroutineScope()
    val estadoDaBara= remember { mutableStateOf(false) }
    val texto =remember { mutableStateOf("") }
    //val pesquisa =vm.fluxoDePesquisa.collectAsState(emptyList())
    SearchBar(modifier = modifier,
        inputField = { SearchBarDefaults.InputField(query = texto.value,
            onQueryChange = {texto.value=it

            },
            onSearch = {

            },
            expanded = estadoDaBara.value,
            onExpandedChange = {estadoDaBara.value=it},
            placeholder = {Text("Pesquisar")},
            leadingIcon ={ Icon(Icons.Default.Search,null) },
            trailingIcon = {Icon(Icons.Default.Close,null,
                Modifier.clickable(onClick = {estadoDaBara.value=false}))}) },
        expanded = estadoDaBara.value,
        onExpandedChange = {estadoDaBara.value=!estadoDaBara.value}){
        LazyColumn {
            items(count = 0) {

            }
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
                when{
                    windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)|| windowSize.isWidthAtLeastBreakpoint(
                        WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)->Text(produto.nome, maxLines = 2,modifier=Modifier.width(150.dp), overflow = TextOverflow.Ellipsis)
                    else->Text(produto.nome, maxLines = 2,modifier=Modifier.width(70.dp), overflow = TextOverflow.Ellipsis)
                }

                Spacer(Modifier.padding(10.dp))
                Text(produto.preco.toString(), Modifier.width(70.dp))
                Spacer(Modifier.padding(10.dp))
                if(windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)||windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND))
                    Text(produto.descrisao, maxLines = 2, modifier = Modifier.fillMaxWidth().padding(end = 90.dp), overflow = TextOverflow.Ellipsis)
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
                        Row {Text(if(produto.servico)"Serviço :" else "Produto :")
                            Text(produto.nome)}
                        Text(text = produto.descrisao, maxLines = 5,modifier =  Modifier)
                    }}}
            HorizontalDivider(Modifier.fillMaxWidth())
        }
    }
}


@Composable
private fun ItemProdutoRequisitado(windowSize: WindowSizeClass, modifier: Modifier=Modifier,
                        produto: ProdutoRequisitado ){

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
                when{
                    windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)|| windowSize.isWidthAtLeastBreakpoint(
                        WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)->Text(produto.nomePrd, maxLines = 2,modifier=Modifier.width(150.dp), overflow = TextOverflow.Ellipsis)
                    else->Text(produto.nomePrd, maxLines = 2,modifier=Modifier.width(70.dp), overflow = TextOverflow.Ellipsis)
                }

                Spacer(Modifier.padding(10.dp))
                Text(produto.preco.toString(), Modifier.width(70.dp))
                Spacer(Modifier.padding(10.dp))
                Text(produto.qnt.toString(), Modifier.width(50.dp))
                Spacer(Modifier.padding(10.dp))
                Text(produto.total.toString(), Modifier.width(70.dp))


            }
            FlowRow(Modifier.align(if(!expandidido.value)Alignment.CenterEnd else Alignment.TopEnd)) {
                IconButton ({},modifier= Modifier.size(30.dp).padding(end = 3.dp)) {
                    Icon(Icons.Outlined.Create,modifier= Modifier.size(20.dp),contentDescription = "")
                }

                IconButton ({},modifier= Modifier.size(30.dp).padding(5.dp)) {
                    Icon(Icons.Outlined.Delete,modifier= Modifier.size(20.dp), contentDescription = "")
                }

            }

            HorizontalDivider(Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun Cabesalho(windowSizeClass: WindowSizeClass){
    OutlinedCard (modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxSize().padding(top = 10.dp, bottom = 10.dp)){
            Row(modifier = Modifier.align(Alignment.CenterStart).padding(start = 5.dp, end = 5.dp)) {
                when{
                    windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) || windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) ->{
                        Text("Produto", modifier = Modifier.width(150.dp))

                    }
                    else->Text("Produto", modifier = Modifier.width(70.dp))
                }

                Spacer(Modifier.padding(10.dp))
                Text("Preco", modifier = Modifier.width(70.dp))
                Spacer(Modifier.padding(10.dp))
                Text("Descricao")


            }
        }
    }
}

@Composable
private fun CabesalhoProdutoSolicitado(windowSizeClass: WindowSizeClass){
    OutlinedCard (modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxSize().padding(top = 10.dp, bottom = 10.dp)){
            Row(modifier = Modifier.align(Alignment.CenterStart).padding(start = 5.dp, end = 5.dp)) {
                when{
                    windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) || windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) ->{
                        Text("Produto", modifier = Modifier.width(150.dp))

                    }
                    else->Text("Produto", modifier = Modifier.width(70.dp))
                }

                Spacer(Modifier.padding(10.dp))
                Text("Preco", modifier = Modifier.width(70.dp))
                Spacer(Modifier.padding(10.dp))
                Text("qnt", modifier = Modifier.width(70.dp))
                Spacer(Modifier.padding(10.dp))
                Text("total")


            }
        }
    }
}
