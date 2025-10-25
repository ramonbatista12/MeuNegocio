package com.example.meunegociomeunegocio.apresentacaoDeClientes

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.meunegociomeunegocio.viewModel.TelasInternasDeClientes
import com.example.meunegociomeunegocio.viewModel.ViewModelCliente
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.window.core.layout.WindowSizeClass
import com.example.meunegociomeunegocio.R
import com.example.meunegociomeunegocio.repositorioRom.DadosDeClientes
import com.example.meunegociomeunegocio.repositorioRom.Endereco
import com.example.meunegociomeunegocio.repositorioRom.Telefone
import com.example.meunegociomeunegocio.utilitario.EstadosDeLoad
import com.example.meunegociomeunegocio.viewModel.TelasInternasDadosDeClientes
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@Composable
fun DadosDeClientes(vm: ViewModelCliente, windowSizeClass: WindowSizeClass, modifier: Modifier=Modifier){
    val id =(vm.telaVisualizada.collectAsState().value as TelasInternasDeClientes.DadosDoCliente).idCliente
    val load=  vm.dadosDocliente(id).collectAsState(initial = EstadosDeLoad.load )
    when(load.value){
        is EstadosDeLoad.load->{
            CircularProgressIndicator(modifier = modifier.size(40.dp))
        }
        is EstadosDeLoad.Caregado<*> ->{
            DadosDeClientesImpl(vm, windowSizeClass = windowSizeClass, dadosDeClientes = (load.value as EstadosDeLoad.Caregado<DadosDeClientes>).obj)
          }

        else -> {Log.d("DadosDeClientes","erro")}
    }
}


@Composable
private fun DadosDeClientesImpl(vm: ViewModelCliente,
                                modifier: Modifier= Modifier,
                                windowSizeClass: WindowSizeClass,
                                dadosDeClientes: DadosDeClientes){
    val coroutineScope = rememberCoroutineScope()
    val slecao=vm.daosClioente.collectAsState()

    Column (modifier=Modifier.padding(all = 5.dp)){
             IconButton(onClick = {coroutineScope.launch {vm.mudarTelaVisualizada(
                 TelasInternasDeClientes.ListaDeClientes)  }},modifier.padding(5.dp)) {
                 Icon( Icons.Default.ArrowBack , contentDescription = null)

             }
             Iniciais(dadosDeClientes.cliente.nome,
                     Modifier.align(Alignment.CenterHorizontally).padding(vertical = 20.dp),
                     size = if(windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)) 200.dp else 100.dp,
                     sp = if(windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)) 60 else 30)
             Nome(dadosDeClientes.cliente.nome, Modifier.align(Alignment.CenterHorizontally).padding(start = 5.dp, end = 5.dp, bottom = 3.dp))
             cpfCnpj(dadosDeClientes.cliente.cpf,dadosDeClientes.cliente.cnpj, Modifier.padding(start = 5.dp, end = 5.dp, bottom = 10.dp))
             BaraSelecao(modifier = Modifier.align(Alignment.CenterHorizontally), acao = {coroutineScope.launch { vm.mudarDadoDoCliente(it) }})
             when(slecao.value){
                 is TelasInternasDadosDeClientes.Telefone->ListaDeTelefones(Modifier.align(Alignment.CenterHorizontally).padding(top = 5.dp),dadosDeClientes.telefones)
                 is TelasInternasDadosDeClientes.Endereco->ListaDeEnderecos(Modifier.align(Alignment.CenterHorizontally).padding(top = 5.dp),dadosDeClientes.enderecos)
             }
    }
}
@Composable
 fun DadosDeClientesExpandido(vm: ViewModelCliente,
                                modifier: Modifier= Modifier,
                                windowSizeClass: WindowSizeClass,
                                ){
    val coroutineScope = rememberCoroutineScope()
    val sleDeClientes=vm.fluxoDeDadosDeClientesPainelExpandido.collectAsState(emptyFlow<DadosDeClientes>())
    val selecaoEndTel =vm.daosClioente.collectAsState()
    when(sleDeClientes.value){
        is EstadosDeLoad.Caregado<*> ->{
            val dadosDeClientes=(sleDeClientes.value as EstadosDeLoad.Caregado<DadosDeClientes>).obj
            Column (modifier=Modifier.padding(all = 5.dp)){

                Iniciais(dadosDeClientes.cliente.nome,
                    Modifier.align(Alignment.CenterHorizontally).padding(vertical = 20.dp),
                    size = if(windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)) 150.dp else 100.dp,
                    sp = if(windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)) 60 else 30)
                Nome(dadosDeClientes.cliente.nome, Modifier.align(Alignment.CenterHorizontally).padding(start = 5.dp, end = 5.dp, bottom = 3.dp))
                cpfCnpj(dadosDeClientes.cliente.cpf,dadosDeClientes.cliente.cnpj, Modifier.padding(start = 5.dp, end = 5.dp, bottom = 10.dp))
                BaraSelecao(modifier = Modifier.align(Alignment.CenterHorizontally), acao = {coroutineScope.launch { vm.mudarDadoDoCliente(it) }})
                when(selecaoEndTel.value){
                    is TelasInternasDadosDeClientes.Telefone->ListaDeTelefones(Modifier.align(Alignment.CenterHorizontally).padding(top = 5.dp),dadosDeClientes.telefones)
                    is TelasInternasDadosDeClientes.Endereco->ListaDeEnderecos(Modifier.align(Alignment.CenterHorizontally).padding(top = 5.dp),dadosDeClientes.enderecos)
                }
            }
        }
    }

}


@Composable
private fun Iniciais(nome:String,modifier: Modifier= Modifier,size:Dp=100.dp,sp:Int=30){
    val iniciais= remember{mutableStateOf("")}
    val color= remember{ mutableStateOf(Cores.lisColors[0])}
    LaunchedEffect(nome) {
        val split =nome.split(Regex("\\s"))
        if (split.size>=2){
            iniciais.value=split[0].first().toString()+split[1].first().toString()

        }
        else
            iniciais.value=split[0].first().toString()

    }
    LaunchedEffect(iniciais) {
        val char =iniciais.value.subSequence(0,1)
        val indice = char[0].code % Cores.lisColors.size

        color.value=Cores.lisColors[indice]
    }
    Box(modifier = modifier.size(size)
        .clip(CircleShape)
        .border(1.dp, color = androidx.compose.ui.graphics.Color.Black, shape = CircleShape)
        .background(color.value)){
        Text(text = iniciais.value, modifier = Modifier.align(Alignment.Center), fontSize = sp.sp)
    }
}
@Composable
private fun Nome(nome: String,modifier: Modifier= Modifier){
    Text(text = nome, modifier = modifier.padding(bottom = 5.dp), fontSize = 30.sp, maxLines = 2)
}

@Composable
private fun BaraSelecao(modifier: Modifier= Modifier,acao: (TelasInternasDadosDeClientes)->Unit){
    Row(modifier=modifier, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        BaraDeSelecao.listaDeSelecao.forEach {
            ItemDeSelecao(item = it, acao = {acao(it.selecao)})
        }
    }
}

@Composable
private fun cpfCnpj(cpf:String?,cnpj:String?,modifier: Modifier= Modifier){
    if(cpf==null&&cnpj==null) return
    if(cnpj==null) Text(text = "cpf :"+cpf,modifier=modifier, fontSize = 20.sp)
    else  if(cpf==null) Text(text = "cnpj :"+ cnpj, modifier = modifier, fontSize = 20.sp)
    else if(cpf!=null&&cnpj!=null) Column(modifier = modifier) {
        Text(text = "cpf :"+cpf,modifier=modifier.padding(bottom = 3.dp), fontSize = 20.sp)
        Text(text = "cnpj :"+ cnpj, modifier = modifier, fontSize = 20.sp)
    }
}
@Composable
private fun ItemDeSelecao(acao:()->Unit, modifier: Modifier= Modifier,item: BaraDeSelecao){
    OutlinedButton(onClick = { acao()}) {
        Text(item.nome,modifier= Modifier.padding(end = 5.dp))
        Icon( painterResource(id = item.icone), contentDescription = null)


    }
}

sealed class BaraDeSelecao(val icone:Int, val nome: String, val selecao: TelasInternasDadosDeClientes){
object SelecaoTelefone: BaraDeSelecao(R.drawable.telefone,"Telefone" ,TelasInternasDadosDeClientes.Telefone)
    object SelecaoEndereco: BaraDeSelecao(R.drawable.endereco,"Endereco" ,TelasInternasDadosDeClientes.Endereco)
    companion object{
        val listaDeSelecao= listOf(SelecaoTelefone,SelecaoEndereco)
    }
}

@Composable
private fun ListaDeEnderecos(modifier: Modifier,list: List<Endereco>){
    LazyColumn(modifier) {
        items(items = list){
            Endereco(it)
        }
    }
}
@Composable
private fun Endereco(endr: Endereco ){

    Card(modifier = Modifier.fillMaxSize().padding(5.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)) {
     Box(modifier = Modifier.fillMaxWidth()){
        Column(Modifier.padding(bottom = 5.dp, end = 70.dp).align(Alignment.CenterStart)) {
         Text("Rua : ${endr.rua}  N: ${endr.numero}")
         Text("cep : ${endr.cep}")
         Text("${endr.bairro}-${endr.estado}-${endr.cidade}", maxLines = 3)}
         Row(modifier = Modifier.align(Alignment.CenterEnd)) {
             IconButton(onClick = {}, Modifier.size(30.dp)) {
                 Icon(painterResource(R.drawable.endereco),null)
             }
             IconButton(onClick = {},Modifier.size(30.dp)) {
                 Icon(Icons.Default.Create,null)
             }
             IconButton(onClick = {},Modifier.size(30.dp)) {
                 Icon(Icons.Default.Delete,null)
             }
         }
         HorizontalDivider()
     }
    }
}
@Composable
private fun ListaDeTelefones(modifier: Modifier= Modifier,list: List<Telefone> ){
    LazyColumn(modifier = modifier) {
        items(items = list) {
          Telefone(it)
        }
    }
}
@Composable
private fun Telefone(telefone: Telefone
){


    Card(modifier = Modifier.fillMaxSize().padding(5.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)) {
        Box(modifier = Modifier.fillMaxWidth()){
            Column(Modifier.padding(bottom = 5.dp, end = 70.dp).align(Alignment.CenterStart)) {
                Text(" ${telefone.ddd} ${telefone.numero}")

            }
            Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                IconButton(onClick = {}, Modifier.size(30.dp)) {
                    Icon(painterResource(R.drawable.telefone),null)
                }
                IconButton(onClick = {},Modifier.size(30.dp)) {
                    Icon(Icons.Default.Create,null)
                }
                IconButton(onClick = {},Modifier.size(30.dp)) {
                    Icon(Icons.Default.Delete,null)
                }
            }
            HorizontalDivider()
        }
    }
}
@Preview
@Composable
fun previaDadosClientes(){}
