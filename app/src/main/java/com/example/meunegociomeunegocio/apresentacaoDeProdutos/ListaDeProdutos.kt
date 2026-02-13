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
import androidx.compose.foundation.layout.Arrangement
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

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import com.example.meunegociomeunegocio.R
import com.example.meunegociomeunegocio.loads.ItemDelLoadTabelas
import com.example.meunegociomeunegocio.repositorioRom.ProdutoRequisitado

import com.example.meunegociomeunegocio.repositorioRom.ProdutoServico
import com.example.meunegociomeunegocio.utilitario.DialogoExclusao
import com.example.meunegociomeunegocio.utilitario.EstadosDeLoadCaregamento
import com.example.meunegociomeunegocio.viewModel.Pesquisa
import com.example.meunegociomeunegocio.viewModel.ViewModelCriarRequisicoes
import com.example.meunegociomeunegocio.viewModel.ViewModelRequisicoes
import kotlinx.coroutines.launch

public fun String.formatarPreco():String{
    return String.format("%.2f",this.toDouble()).replace(".",",")
}

public fun String.formatData():String{
    if(!this.matches(Regex("\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{2}:\\d{2}:\\d{2}"))) throw IllegalArgumentException("data invalaida${this} .Essa funcao valida e feita para reordenara o padaro yyyy-mm-dd hh:mm:ss para o padrao dd/mm/yyyy hh:mm:ss")
    val split =this.split(" ")
    val data =split[0].split("-")
    val  dataFormatada ="${data[2]}/${data[1]}/${data[0]} ${split[1]}"
    return "$dataFormatada "
}

@Composable
fun ListaDeProdutos(modifier: Modifier=Modifier, vm: ViewModelProdutos, windowSize: WindowSizeClass,acaoDeEdicaoDoProduto: (Int) -> Unit){
    val estadosDeLoadCaregamento=vm.produtos.collectAsState(initial = EstadosDeLoadCaregamento.load)
    Column(modifier = modifier.padding(horizontal = 5.dp)) {
      BaraDePesquisaProdutos(modifier = Modifier.padding(vertical = 5.5.dp, horizontal = 5.dp).fillMaxWidth(),vm = vm,windowSize,acaoDeEdicaoDoProduto)
    LazyColumn {
        stickyHeader{
            when(estadosDeLoadCaregamento.value){
                is EstadosDeLoadCaregamento.Caregado<*>->{
                    Cabesalho(windowSize)
                }
                else -> {}
            }

        }
        when(estadosDeLoadCaregamento.value){
            is EstadosDeLoadCaregamento.Empty -> {
                item{
                Row(modifier=Modifier.fillMaxSize().padding(top = 100.dp),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Text("Lista vaia")
                    }
                }

               }
            is EstadosDeLoadCaregamento.Erro->{}
            is EstadosDeLoadCaregamento.load -> {
                items(count = 5) {
                ItemDelLoadTabelas()

            }}
            is EstadosDeLoadCaregamento.Caregado<*> -> {
                val lista =estadosDeLoadCaregamento.value as EstadosDeLoadCaregamento.Caregado<List<ProdutoServico>>
                items(items = lista.obj){
                    ItemProduto(windowSize=windowSize, produto = it, acaoMostrarProduto = {vm.mostraDescricao(it)}, acaoFormatacao = {vm.formatarPreco(it)},acacaoExclusao = {vm.abrirDialogo(it)},acaoDeEdicaoDoProduto = acaoDeEdicaoDoProduto )
                }
            }

        }




    }
        DialogoExclusao(mensagem = "voce deseja excluir esse produto","Ouve um erro ao excluir o produto voce pode tentar novamente se quiser","O produto foi excluido com sucesso",vm)
}


}

@Composable
fun ListaDeProdutos(modifier: Modifier=Modifier, vm: ViewModelCriarRequisicoes, windowSize: WindowSizeClass,acaoDeEdicaoDoProduto: (Int) -> Unit){
    val estadosDeLoadCaregamento=vm.fluxoDeProdutos.collectAsState(initial = EstadosDeLoadCaregamento.load)
    val coroutineScope =rememberCoroutineScope()
    Column(modifier = modifier.padding(horizontal = 5.dp)) {
        if(!windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND))
            IconButton({coroutineScope.launch { vm.irParaListaDeProdutosSelecionados() }}) {
                Icon(painterResource(R.drawable.baseline_arrow_back_24), null)
            }
        BaraDePesquisaProdutos(modifier = Modifier.padding(vertical = 5.5.dp, horizontal = 5.dp).fillMaxWidth(),vm = vm,windowSize, acaoDeEdicaoDoProduto = acaoDeEdicaoDoProduto)
        LazyColumn {
            stickyHeader{
                Cabesalho(windowSize)
            }
            when(estadosDeLoadCaregamento.value){
                is EstadosDeLoadCaregamento.Empty -> {
                }
                is EstadosDeLoadCaregamento.Erro->{}
                is EstadosDeLoadCaregamento.load -> {
                    items(count = 5) {
                        ItemDelLoadTabelas()

                    }}
                is EstadosDeLoadCaregamento.Caregado<*> -> {
                    val lista =estadosDeLoadCaregamento.value as EstadosDeLoadCaregamento.Caregado<List<ProdutoServico>>
                    items(items = lista.obj){
                        ItemProduto(windowSize=windowSize, produto = it,
                                    acaoSelecionarProduto = {id,nm->
                                        coroutineScope.launch {vm.selecionarProduto(id,nm)
                                                               vm.irParaListaDeProdutosSelecionados()} }, acaoFormatacao = {it.toString().formatarPreco()})
                    }
                }

            }




        }
    }


}

@Composable
fun ListaDeProdutosExpandido(modifier: Modifier=Modifier, vm: ViewModelProdutos, windowSize: WindowSizeClass,acaoDeEdicaoDoProduto: (Int) -> Unit){
    val estadosDeLoadCaregamento=vm.produtos.collectAsState(initial = EstadosDeLoadCaregamento.load)
    Column(modifier = modifier.padding(horizontal = 5.dp).fillMaxWidth(0.4f)) {
        BaraDePesquisaProdutos(modifier = Modifier.fillMaxWidth().padding(vertical = 5.5.dp, horizontal = 5.dp),vm = vm,windowSize,acaoDeEdicaoDoProduto)
        LazyColumn {
            stickyHeader{
                Cabesalho(windowSize)
            }
            when(estadosDeLoadCaregamento.value){
                is EstadosDeLoadCaregamento.Empty -> {
                    item {
                        Row(modifier=Modifier.fillMaxSize().padding(top = 100.dp),verticalAlignment = Alignment.CenterVertically,horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center)  {
                            Text("Lista vasia")
                        }



                    }
                }
                is EstadosDeLoadCaregamento.Erro->{}
                is EstadosDeLoadCaregamento.load -> {
                    items(count = 5) {
                        ItemDelLoadTabelas()

                    }}
                is EstadosDeLoadCaregamento.Caregado<*> -> {
                    val lista =estadosDeLoadCaregamento.value as EstadosDeLoadCaregamento.Caregado<List<ProdutoServico>>
                    items(items = lista.obj){
                        ItemProduto(windowSize=windowSize, produto = it, acaoMostrarProduto = {vm.mostraDescricao(it)}, acaoFormatacao = {vm.formatarPreco(it)}, acacaoExclusao = {vm.abrirDialogo(it)}, acaoDeEdicaoDoProduto =acaoDeEdicaoDoProduto )
                    }
                }

            }




        }
        DialogoExclusao(mensagem = "voce deseja excluir esse produto","Ouve um erro ao excluir o produto voce pode tentar novamente se quiser","O produto foi excluido com sucesso",vm)
    }


}

@Composable
fun ListaDeProdutosRequisitados(modifier: Modifier=Modifier, vm: ViewModelRequisicoes, windowSize: WindowSizeClass){
    val produtos =vm.fluxoProdutosRequisitados.collectAsStateWithLifecycle(EstadosDeLoadCaregamento.load)
     when(produtos.value){
         is EstadosDeLoadCaregamento.Empty -> {}
         is EstadosDeLoadCaregamento.Caregado<*> -> {
             val produtos =produtos.value as EstadosDeLoadCaregamento.Caregado<List<ProdutoRequisitado>>
             Column(modifier = modifier.padding(horizontal = 5.dp)) {

                 LazyColumn {
                     stickyHeader{
                         if(windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) || windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND))
                         CabesalhoProdutoSolicitado(windowSize)
                     }
                     items(items = produtos.obj){
                         ItemProdutoRequisitado(windowSize=windowSize, produto = it, acaoFormatacao = {  it.toString().formatarPreco()})
                     }

                 }
             }}
         is EstadosDeLoadCaregamento.load -> {}
         else -> {}
     }



}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BaraDePesquisaProdutos(modifier: Modifier= Modifier, vm: ViewModelProdutos,windowSizeClass: WindowSizeClass,acaoDeEdicaoDoProduto:(Int)->Unit){
    val coroutineScope =rememberCoroutineScope()
    val estadoDaBara= remember { mutableStateOf(false) }
    val texto =remember { mutableStateOf("") }
    //val pesquisa =vm.fluxoDePesquisa.collectAsState(emptyList())
    SearchBar(modifier = modifier,
        inputField = { SearchBarDefaults.InputField(query = texto.value,
            onQueryChange = {texto.value=it
                            coroutineScope.launch { vm.mudarPesquisa(Pesquisa(it)) }
            },
            onSearch = {

            },
            expanded = estadoDaBara.value,
            onExpandedChange = {estadoDaBara.value=it},
            placeholder = {Text("Pesquisar")},
            leadingIcon ={ Icon(painterResource(R.drawable.baseline_search_24),null) },
            trailingIcon = {Icon(painterResource(R.drawable.baseline_close_24),null,
                Modifier.clickable(onClick = {estadoDaBara.value=false
                                              texto.value=""
                }))}) },
        expanded = estadoDaBara.value,
        onExpandedChange = {estadoDaBara.value=!estadoDaBara.value},
        colors = SearchBarDefaults.colors(containerColor = MaterialTheme.colorScheme.background) ){
        val pesquisa =vm.pesquisaDeProduto.collectAsState(emptyList())
        LazyColumn {
            stickyHeader {
                Cabesalho(windowSizeClass)
            }
         items(items = pesquisa.value) {
             ItemProduto(windowSize=windowSizeClass,
                         produto = it,
                         acaoMostrarProduto = {id->vm.mostraDescricao(id)},
                         acaoFormatacao = {it.toString().formatarPreco()},
                         acacaoExclusao = {id->vm.abrirDialogo(id)}, acaoDeEdicaoDoProduto = acaoDeEdicaoDoProduto)}
         }
        }
    }



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BaraDePesquisaProdutos(modifier: Modifier= Modifier, vm: ViewModelCriarRequisicoes,windowSizeClass: WindowSizeClass,acaoDeEdicaoDoProduto: (Int) -> Unit){
    val coroutineScope =rememberCoroutineScope()
    val estadoDaBara= remember { mutableStateOf(false) }
    val texto =remember { mutableStateOf("") }
    //val pesquisa =vm.fluxoDePesquisa.collectAsState(emptyList())
    SearchBar(modifier = modifier,
        inputField = { SearchBarDefaults.InputField(query = texto.value,
            onQueryChange = {texto.value=it
            coroutineScope.launch { vm.mudarPesquisa(Pesquisa(it)) }
            },
            onSearch = {
                coroutineScope.launch { vm.mudarPesquisa(Pesquisa(it)) }

            },
            expanded = estadoDaBara.value,
            onExpandedChange = {estadoDaBara.value=it},
            placeholder = {Text("Pesquisar")},
            leadingIcon ={ Icon(painterResource(R.drawable.baseline_search_24),null) },
            trailingIcon = {Icon(painterResource(R.drawable.baseline_close_24),null,
                Modifier.clickable(onClick = {estadoDaBara.value=false}))}) },
        expanded = estadoDaBara.value,
        onExpandedChange = {estadoDaBara.value=!estadoDaBara.value},
        colors = SearchBarDefaults.colors(containerColor = MaterialTheme.colorScheme.background) ){
        val pesqisa =vm.pesquisaProdutos.collectAsState(emptyList())
        LazyColumn {
            stickyHeader {
                Cabesalho(windowSizeClass)
            }
            items(items = pesqisa.value) {
               ItemProduto(windowSize = windowSizeClass, produto = it, acaoMostrarProduto = {}, acaoFormatacao = {it.toString().formatarPreco()},acacaoExclusao = {}, acaoDeEdicaoDoProduto = acaoDeEdicaoDoProduto)
            }
        }
    }

}

@Composable
private fun ItemProduto(windowSize: WindowSizeClass, modifier: Modifier=Modifier,
                        produto: ProdutoServico ,
                        acaoMostrarProduto:(id:Int)->Unit= {},
                        acaoFormatacao:(preco:Double)->String={""},
                        acacaoExclusao:(id:Int)->Unit,
                        acaoDeEdicaoDoProduto:(Int)-> Unit){

    val expandidido = remember{mutableStateOf(false)}
    LaunchedEffect(windowSize) {
        Log.d("teste","teste windowSize clas mudo")
        if(windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND))
            Log.d("texte","window size class medio")
        if(windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND))
            Log.d("texte","window size class expandido")
    }

    Card(modifier= Modifier.fillMaxWidth().height(if(expandidido.value) 180.dp else 70.dp).padding(top = 5.dp, start = 3.dp, end = 3.dp).clickable(onClick = { acaoMostrarProduto(produto.id)}),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)) {
        Box(modifier = Modifier.fillMaxSize()){


            Row(Modifier.padding(top = 3.dp, start = 5.dp, end = 5.dp).align(if(!expandidido.value)Alignment.CenterStart else Alignment.TopStart)) {
                when{
                    windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)|| windowSize.isWidthAtLeastBreakpoint(
                        WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)->Text(produto.nome, maxLines = 2,modifier=Modifier.width(150.dp), overflow = TextOverflow.Ellipsis)
                    else->Text(produto.nome, maxLines = 2,modifier=Modifier.width(70.dp), overflow = TextOverflow.Ellipsis)
                }

                Spacer(Modifier.padding(10.dp))
                Text( acaoFormatacao(produto.preco.toDouble()), Modifier.width(70.dp))
                Spacer(Modifier.padding(10.dp))
                if(windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)||windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND))
                    Text(produto.descrisao, maxLines = 2, modifier = Modifier.fillMaxWidth().padding(end = 90.dp), overflow = TextOverflow.Ellipsis)
                else
                    Text(produto.descrisao, maxLines = 1, modifier = Modifier.width(90.dp), overflow = TextOverflow.Ellipsis)



            }
            FlowRow(Modifier.align(if(!expandidido.value)Alignment.CenterEnd else Alignment.TopEnd)) {
                IconButton ({acaoDeEdicaoDoProduto(produto.id)},modifier= Modifier.size(30.dp).padding(end = 3.dp)) {
                    Icon(painterResource(R.drawable.create_24),modifier= Modifier.size(20.dp),contentDescription = "")
                }

                IconButton ({acacaoExclusao(produto.id)},modifier= Modifier.size(30.dp).padding(5.dp)) {
                    Icon(painterResource(R.drawable.baseline_delete_24),modifier= Modifier.size(20.dp), contentDescription = "")
                }
              /*  IconButton(onClick = {expandidido.value=!expandidido.value}, modifier = Modifier.size(30.dp)) {
                    if(!expandidido.value)
                        Icon(Icons.Outlined.ArrowDropDown, contentDescription = "")
                    else
                        Icon(Icons.Outlined.KeyboardArrowUp, contentDescription = "", modifier = Modifier)
                }*/
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
private fun ItemProduto(windowSize: WindowSizeClass, modifier: Modifier=Modifier,
                        produto: ProdutoServico ,
                        acaoSelecionarProduto:(id:Int,nome:String)->Unit= {id,nm->},
                        acaoFormatacao:(preco:Double)->String={""},
){

    val expandidido = remember{mutableStateOf(false)}
    LaunchedEffect(windowSize) {
        Log.d("teste","teste windowSize clas mudo")
        if(windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND))
            Log.d("texte","window size class medio")
        if(windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND))
            Log.d("texte","window size class expandido")
    }

    Card(modifier= Modifier.fillMaxWidth().height(if(expandidido.value) 180.dp else 70.dp).padding(top = 5.dp, start = 3.dp, end = 3.dp).clickable(onClick = {acaoSelecionarProduto(produto.id,produto.nome) }),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)) {
        Box(modifier = Modifier.fillMaxSize()){


            Row(Modifier.padding(top = 3.dp, start = 5.dp, end = 5.dp).align(if(!expandidido.value)Alignment.CenterStart else Alignment.TopStart)) {
                when{
                    windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)|| windowSize.isWidthAtLeastBreakpoint(
                        WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)->Text(produto.nome, maxLines = 2,modifier=Modifier.width(150.dp), overflow = TextOverflow.Ellipsis)
                    else->Text(produto.nome, maxLines = 2,modifier=Modifier.width(70.dp), overflow = TextOverflow.Ellipsis)
                }

                Spacer(Modifier.padding(10.dp))
                Text( acaoFormatacao(produto.preco.toDouble()), Modifier.width(70.dp))
                Spacer(Modifier.padding(10.dp))
                if(windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)||windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND))
                    Text(produto.descrisao, maxLines = 2, modifier = Modifier.fillMaxWidth().padding(end = 90.dp), overflow = TextOverflow.Ellipsis)
                else
                    Text(produto.descrisao, maxLines = 1, modifier = Modifier.width(90.dp), overflow = TextOverflow.Ellipsis)



            }
            FlowRow(Modifier.align(if(!expandidido.value)Alignment.CenterEnd else Alignment.TopEnd)) {
                IconButton ({},modifier= Modifier.size(30.dp).padding(end = 3.dp)) {
                    Icon(painterResource(R.drawable.create_24),modifier= Modifier.size(20.dp),contentDescription = "")
                }

                IconButton ({},modifier= Modifier.size(30.dp).padding(5.dp)) {
                    Icon(painterResource(R.drawable.baseline_delete_24),modifier= Modifier.size(20.dp), contentDescription = "")
                }
                /*  IconButton(onClick = {expandidido.value=!expandidido.value}, modifier = Modifier.size(30.dp)) {
                      if(!expandidido.value)
                          Icon(Icons.Outlined.ArrowDropDown, contentDescription = "")
                      else
                          Icon(Icons.Outlined.KeyboardArrowUp, contentDescription = "", modifier = Modifier)
                  }*/
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
                        produto: ProdutoRequisitado ,
                        acaoFormatacao: (Double) -> String){

    val expandidido = remember{mutableStateOf(false)}
    LaunchedEffect(windowSize) {
        Log.d("teste","teste windowSize clas mudo")
        if(windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND))
            Log.d("texte","window size class medio")
        if(windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND))
            Log.d("texte","window size class expandido")
    }
    when{
       !windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)&&
       !windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)->{

           Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)) {
               Box(Modifier.fillMaxWidth().padding(5.dp)){
                   Column(modifier = Modifier.align(Alignment.CenterStart).padding(start = 5.dp)) {
                       Text("Produto : ${produto.nomePrd}")
                       Text("Preço :${produto.preco.toString().formatarPreco()}")
                       Text("Quntidade : ${produto.qnt.toString()}", Modifier)

                  }
                   HorizontalDivider(modifier=Modifier.align(Alignment.BottomCenter))
                  /* Row(Modifier.align(Alignment.BottomEnd)) {
                       IconButton ({},modifier= Modifier.size(30.dp).padding(end = 3.dp)) {
                           Icon(painterResource(R.drawable.create_24),modifier= Modifier.size(20.dp),contentDescription = "")
                       }

                       IconButton ({},modifier= Modifier.size(30.dp).padding(5.dp)) {
                           Icon(painterResource(R.drawable.baseline_delete_24),modifier= Modifier.size(20.dp), contentDescription = "")
                       }

                   }*/
               }

           }
       }

        else->{
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
                        Text(produto.preco.toString().formatarPreco(), Modifier.width(70.dp))
                        Spacer(Modifier.padding(10.dp))
                        Text(produto.qnt.toString(), Modifier.width(50.dp))
                        Spacer(Modifier.padding(10.dp))
                        Text(acaoFormatacao(produto.total.toDouble()), Modifier.width(70.dp))


                    }
                  /*  FlowRow(Modifier.align(if(!expandidido.value)Alignment.CenterEnd else Alignment.TopEnd)) {
                        IconButton ({},modifier= Modifier.size(30.dp).padding(end = 3.dp)) {
                            Icon(painterResource(R.drawable.create_24),modifier= Modifier.size(20.dp),contentDescription = "")
                        }

                        IconButton ({},modifier= Modifier.size(30.dp).padding(5.dp)) {
                            Icon(painterResource(R.drawable.baseline_delete_24),modifier= Modifier.size(20.dp), contentDescription = "")
                        }

                    }*/

                    HorizontalDivider(Modifier.fillMaxWidth())
                }
            }

        }
    }

}

@Composable
private fun Cabesalho(windowSizeClass: WindowSizeClass){
    Card (modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor= MaterialTheme.colorScheme.background)) {
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
    Card (modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor= MaterialTheme.colorScheme.background)) {
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
