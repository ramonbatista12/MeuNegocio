package com.example.meunegociomeunegocio.apresentacaoDeClientes

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meunegociomeunegocio.R
import com.example.meunegociomeunegocio.loads.LoadClientes
import com.example.meunegociomeunegocio.repositorioRom.Cliente
import com.example.meunegociomeunegocio.utilitario.EstadosDeLoadCaregamento
import com.example.meunegociomeunegocio.viewModel.Pesquisa
import com.example.meunegociomeunegocio.viewModel.TelasInternasDeClientes
import com.example.meunegociomeunegocio.viewModel.ViewModelCliente
import com.example.meunegociomeunegocio.viewModel.ViewModelCriarRequisicoes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaDeClientes(modifier: Modifier= Modifier,vm:ViewModelCliente){
    val coroutineScope= rememberCoroutineScope()
    LaunchedEffect(Unit) {
        Log.d("ListaDeClientes","LaunchedEffect")
    }
    val estadoBaraDePesquisa =remember { mutableStateOf(false) }
    val estadoDeLoad=vm.fluxoDeCliente.collectAsState(EstadosDeLoadCaregamento.load)
    Column(modifier=modifier) {
        BaraDePesquisaClientes(modifier = Modifier.padding(vertical = 5.5.dp, horizontal = 5.dp).fillMaxWidth(),vm = vm)
        LazyColumn(modifier= Modifier) {
            when(estadoDeLoad.value){
                is EstadosDeLoadCaregamento.load -> {
                    items(count = 5) {
                        LoadClientes()
                    }
                }
                is EstadosDeLoadCaregamento.Empty -> {
                    item {
                    Text("Nenhum cliente encontrado")

                }}
                is EstadosDeLoadCaregamento.Erro -> {}
                is EstadosDeLoadCaregamento.Caregado<*> -> {
                    val lista=estadoDeLoad.value as EstadosDeLoadCaregamento.Caregado<List<Cliente>>
                    items(lista.obj) {
                        ItemsDeClientes(it, acao = {coroutineScope.launch {  vm.mudarTelaVisualizada(TelasInternasDeClientes.DadosDoCliente(it))}})
                    }
                }
            }



        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaDeClientes(modifier: Modifier= Modifier,vm: ViewModelCriarRequisicoes){
    val coroutineScope= rememberCoroutineScope()
    LaunchedEffect(Unit) {
        Log.d("ListaDeClientes","LaunchedEffect")
    }
    val estadoBaraDePesquisa =remember { mutableStateOf(false) }
    val estadoDeLoad=vm.fluxoDeClientes.collectAsState(EstadosDeLoadCaregamento.load)
    Column(modifier=modifier) {
        BaraDePesquisaClientes(modifier = Modifier.padding(vertical = 5.5.dp, horizontal = 5.dp).fillMaxWidth(),vm = vm,{ coroutineScope.launch { vm.irparaTelaDeVisualizacaoDeNomeDeCliente()}})
        LazyColumn(modifier= Modifier) {
            when(estadoDeLoad.value){
                is EstadosDeLoadCaregamento.load -> {
                    items(count = 5) {
                        LoadClientes()
                    }
                }
                is EstadosDeLoadCaregamento.Empty -> {
                    item {
                        Text("Nenhum cliente encontrado")

                    }}
                is EstadosDeLoadCaregamento.Erro -> {}
                is EstadosDeLoadCaregamento.Caregado<*> -> {
                    val lista=estadoDeLoad.value as EstadosDeLoadCaregamento.Caregado<List<Cliente>>
                    items(lista.obj) {cli->
                        ItemsDeClientes(cli, acao = {coroutineScope.launch { vm.selecionarCliente(Pair(cli.id,cli.nome))
                                                                              vm.irparaTelaDeVisualizacaoDeNomeDeCliente()}})
                    }
                }
            }



        }
    }

}

@Composable
private fun ItemsDeClientes(cli: Cliente,acao:(Int)-> Unit){
    Column(modifier = Modifier.fillMaxWidth().padding(top = 3.dp, start = 5.dp, end = 5.dp, bottom = 3.dp).clickable(onClick = {acao(cli.id)})) {

     Row(modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp), verticalAlignment = Alignment.CenterVertically) {
        CirculoDeInicias(nome = cli.nome,cli.id)
        Spacer(Modifier.padding(10.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = cli.nome, fontSize = 20.sp)
            Text(text = when{
                cli.cpf!=null->cli.cpf
                cli.cnpj!=null->cli.cnpj
                else->""
            }, fontSize = 9.sp
            )}
        }

    }

    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BaraDePesquisaClientes(modifier: Modifier= Modifier, vm: ViewModelCliente){
    val coroutineScope =rememberCoroutineScope()
    val estadoDaBara= remember { mutableStateOf(false) }
    val texto =remember { mutableStateOf("") }
    val pesquisa =vm.fluxoDePesquisa.collectAsState(emptyList())
    SearchBar(modifier = modifier,
              inputField = { SearchBarDefaults.InputField(query = texto.value,
                                                          onQueryChange = {texto.value=it
                                                              vm.mudarPesquisa(
                                                                  Pesquisa(texto.value))
                                                                          },
                                                          onSearch = {
                                                              vm.mudarPesquisa(
                                                                  Pesquisa(texto.value))
                                                          },
                                                          expanded = estadoDaBara.value,
                                                          onExpandedChange = {estadoDaBara.value=it},
                                                          placeholder = {Text("Pesquisar")},
                                                          leadingIcon ={ Icon(painterResource(R.drawable.baseline_search_24),null) },
                                                          trailingIcon = {Icon(painterResource(R.drawable.baseline_close_24),null,
                                                              Modifier.clickable(onClick = {estadoDaBara.value=false}))}) },
              expanded = estadoDaBara.value,
              onExpandedChange = {estadoDaBara.value=!estadoDaBara.value},
              colors = SearchBarDefaults.colors(containerColor = MaterialTheme.colorScheme.background)){
                   LazyColumn {
                        items(items = pesquisa.value) {
                            ItemsDeClientes(it.cliente,{coroutineScope.launch {  vm.mudarTelaVisualizada(TelasInternasDeClientes.DadosDoCliente(it))}})
                        }
                   }
                  }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BaraDePesquisaClientes(modifier: Modifier= Modifier, vm: ViewModelCriarRequisicoes,acaoDeVoltar:()->Unit){
    val coroutineScope =rememberCoroutineScope()
    val estadoDaBara= remember { mutableStateOf(false) }
    val texto =remember { mutableStateOf("") }
    val pesquisa =vm.pesquisaDeClientes.collectAsState(emptyList())
    SearchBar(modifier = modifier,
        inputField = { SearchBarDefaults.InputField(query = texto.value,
            onQueryChange = {texto.value=it
               coroutineScope.launch { vm.mudarPesquisa(
                                       Pesquisa(texto.value)) }
            },
            onSearch = {
                coroutineScope.launch {vm.mudarPesquisa(
                                      Pesquisa(texto.value)) }

            },
            expanded = estadoDaBara.value,
            onExpandedChange = {estadoDaBara.value=it},
            placeholder = {Text("Pesquisar")},
            leadingIcon ={ Icon(painterResource(R.drawable.baseline_search_24),null) },
            trailingIcon = {Icon(painterResource(R.drawable.baseline_close_24),null,
                Modifier.clickable(onClick = {estadoDaBara.value=false}))}) },
        expanded = estadoDaBara.value,
        onExpandedChange = {estadoDaBara.value=!estadoDaBara.value},
        colors = SearchBarDefaults.colors(containerColor = MaterialTheme.colorScheme.background)){
        LazyColumn {
            items(items = pesquisa.value) {dataCli->
                ItemsDeClientes(dataCli.cliente,{coroutineScope.launch { vm.selecionarCliente(Pair(dataCli.cliente.id,dataCli.cliente.nome))
                                                                          acaoDeVoltar()}})
            }
        }
    }

}

@Composable
private fun CirculoDeInicias(nome: String,id:Int=0){
    val iniciais= remember{mutableStateOf("")}
    val color= remember{ mutableStateOf(Cores.lisColors[0])}
    LaunchedEffect(nome) {
        Log.d("CirculoDeInicias","LaunchedEffect !nome :$nome !  $id")
        val split =nome.split(Regex("\\s"))
        if (split.size>=2){
            val inicialPrimeiroNome =split[0].first()
            val inicialsegundoNome= if(split[1].isEmpty()) "" else split[1].first()
            iniciais.value=inicialPrimeiroNome.toString()+inicialsegundoNome.toString()
        }
        else
            iniciais.value=split[0].first().toString()

    }
    LaunchedEffect(iniciais) {
        val char =iniciais.value.subSequence(0,1)
        val indice = char[0].code % Cores.lisColors.size
        Log.d("CirculoDeInicias","LaunchedEffect $id w$indice ")
        color.value=Cores.lisColors[indice]
    }
    Box(modifier = Modifier.size(40.dp)
                           .clip(CircleShape)
                           .border(1.dp, color = androidx.compose.ui.graphics.Color.Black, shape = CircleShape)
                           .background(color.value)){
        Text(text = iniciais.value, modifier = Modifier.align(Alignment.Center), fontSize = 20.sp)
    }

}

object Cores{
    val lisColors =listOf<Color>(Color.Cyan,Color.Red,Color.Green,Color.Yellow,Color.Magenta)
}