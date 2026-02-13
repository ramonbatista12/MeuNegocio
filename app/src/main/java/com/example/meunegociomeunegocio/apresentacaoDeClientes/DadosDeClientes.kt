package com.example.meunegociomeunegocio.apresentacaoDeClientes

import android.annotation.SuppressLint
import android.net.Uri
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
import androidx.compose.material3.Card

import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import com.example.meunegociomeunegocio.R
import com.example.meunegociomeunegocio.apresentacaoRequisicoes.DialogoCriarPdf
import com.example.meunegociomeunegocio.loads.DialogoLoad
import com.example.meunegociomeunegocio.loads.LoadBox3pontinhos
import com.example.meunegociomeunegocio.loads.TitulosDeLoad
import com.example.meunegociomeunegocio.repositorioRom.DadosDeClientes
import com.example.meunegociomeunegocio.repositorioRom.Endereco
import com.example.meunegociomeunegocio.repositorioRom.Telefone
import com.example.meunegociomeunegocio.utilitario.EstadoLoadAcoes
import com.example.meunegociomeunegocio.utilitario.EstadosDeLoadCaregamento
import com.example.meunegociomeunegocio.viewModel.IDialogoCriacaoPdf
import com.example.meunegociomeunegocio.viewModel.TelasInternasDadosDeClientes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@Composable
fun DadosDeClientes(vm: ViewModelCliente, windowSizeClass: WindowSizeClass, modifier: Modifier=Modifier,acaoEdicao: (Int) -> Unit){
    val id =(vm.telaVisualizada.collectAsState().value as TelasInternasDeClientes.DadosDoCliente).idCliente
    val load=  vm.dadosDocliente.collectAsStateWithLifecycle(EstadosDeLoadCaregamento.load)
    when(load.value){
        is EstadosDeLoadCaregamento.load->{
            if(!windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND))
            DialogoLoad("Dados do cliente", EstadosDeLoadCaregamento.load)
            else Box(modifier = Modifier.fillMaxSize()) { LoadBox3pontinhos(Modifier.align(Alignment.Center), titulo = TitulosDeLoad.Clientes.titulo, estado = EstadosDeLoadCaregamento.load) }
        }
        is EstadosDeLoadCaregamento.Caregado<*> ->{
            DadosDeClientesImpl(vm, windowSizeClass = windowSizeClass, dadosDeClientes = (load.value as EstadosDeLoadCaregamento.Caregado<DadosDeClientes>).obj, acaoEdicao = acaoEdicao)
          }

        else -> {Log.d("DadosDeClientes","erro")}
    }
}


@Composable
private fun DadosDeClientesImpl(vm: ViewModelCliente,
                                modifier: Modifier= Modifier,
                                windowSizeClass: WindowSizeClass,
                                dadosDeClientes: DadosDeClientes,
                                acaoEdicao: (Int) -> Unit){
    val coroutineScope = rememberCoroutineScope()
    val slecao=vm.daosClioente.collectAsState()
    Box(){

        IconButton(onClick = {coroutineScope.launch {vm.mudarTelaVisualizada(
        TelasInternasDeClientes.ListaDeClientes)  }},modifier.padding(5.dp).align(Alignment.TopStart)) {
        Icon(painterResource( R.drawable.baseline_arrow_back_24 ), contentDescription = null)

    }
    Row(Modifier.align(Alignment.TopEnd)) {
        IconButton(onClick = {coroutineScope.launch {
            Log.d("idcliente","id1 e aqui ${dadosDeClientes.cliente.id}")
            acaoEdicao(dadosDeClientes.cliente.id) }},modifier.padding(5.dp)) {
            Icon(painterResource(R.drawable.create_24), contentDescription = "Editar cliente")

        }
        IconButton(onClick = {},modifier.padding(5.dp)) {
            Icon(painterResource(R.drawable.baseline_delete_24), contentDescription = "Editar cliente")

        }

    }

        Column (modifier=Modifier.padding(all = 5.dp).padding(top = 50.dp)){

            Iniciais(dadosDeClientes.cliente.nome,
                Modifier.align(Alignment.CenterHorizontally).padding(vertical = 20.dp),
                size = if(windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)) 200.dp else 100.dp,
                sp = if(windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)) 60 else 30)
            Nome(dadosDeClientes.cliente.nome, Modifier.align(Alignment.CenterHorizontally).padding(start = 5.dp, end = 5.dp, bottom = 3.dp))

            BaraSelecao(modifier = Modifier.align(Alignment.CenterHorizontally), acao = {coroutineScope.launch { vm.mudarDadoDoCliente(it) }},windowSizeClass)
            when(slecao.value){
                is TelasInternasDadosDeClientes.Telefone->ListaDeTelefones(Modifier.align(Alignment.CenterHorizontally).padding(top = 5.dp),dadosDeClientes.telefones)
                is TelasInternasDadosDeClientes.Endereco->ListaDeEnderecos(Modifier.align(Alignment.CenterHorizontally).padding(top = 5.dp),dadosDeClientes.enderecos)
                is TelasInternasDadosDeClientes.Documentos -> {
                    Card(modifier = Modifier.fillMaxSize().padding(5.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)) {
                        Box(modifier = Modifier.fillMaxWidth()){
                            HorizontalDivider(Modifier.align(Alignment.TopCenter))
                            Column(Modifier.align(Alignment.CenterStart).padding(start = 5.dp,top=10.dp)) {
                            cpfCnpj(dadosDeClientes.cliente.cpf,dadosDeClientes.cliente.cnpj, Modifier.padding(start = 5.dp, end = 5.dp, bottom = 10.dp))
                            }
                        }
                    }
                }
            }
        }
    }

}
@Composable
 fun DadosDeClientesExpandido(vm: ViewModelCliente,
                                modifier: Modifier= Modifier,
                                windowSizeClass: WindowSizeClass,
                                acaoEdicao: (Int) -> Unit
                                ){
    val coroutineScope = rememberCoroutineScope()
    val sleDeClientes=vm.fluxoDeDadosDeClientesPainelExpandido.collectAsState(emptyFlow<DadosDeClientes>())
    val selecaoEndTel =vm.daosClioente.collectAsState()
    when(sleDeClientes.value){
        is EstadosDeLoadCaregamento.Caregado<*> ->{
            val dadosDeClientes=(sleDeClientes.value as EstadosDeLoadCaregamento.Caregado<DadosDeClientes>).obj
            Column (modifier=Modifier.padding(all = 5.dp)){
               Row(Modifier.align(Alignment.End)) {
                   IconButton(onClick = {acaoEdicao(dadosDeClientes.cliente.id)}) {
                       Icon(painterResource(R.drawable.create_24),"Editar cliente")
                   }
                   IconButton(onClick = {}) {
                       Icon(painterResource(R.drawable.baseline_delete_24),"Editar cliente")
                   }
               }
                Iniciais(dadosDeClientes.cliente.nome,
                    Modifier.align(Alignment.CenterHorizontally).padding(vertical = 20.dp),
                    size = if(windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)) 150.dp else 100.dp,
                    sp = if(windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)) 60 else 30)
                Nome(dadosDeClientes.cliente.nome, Modifier.align(Alignment.CenterHorizontally).padding(start = 5.dp, end = 5.dp, bottom = 3.dp))

                BaraSelecao(modifier = Modifier.align(Alignment.CenterHorizontally), acao = {coroutineScope.launch { vm.mudarDadoDoCliente(it) }},windowSizeClass)
                when(selecaoEndTel.value){
                    is TelasInternasDadosDeClientes.Telefone->ListaDeTelefones(Modifier.align(Alignment.CenterHorizontally).padding(top = 5.dp),dadosDeClientes.telefones)
                    is TelasInternasDadosDeClientes.Endereco->ListaDeEnderecos(Modifier.align(Alignment.CenterHorizontally).padding(top = 5.dp),dadosDeClientes.enderecos)
                    is TelasInternasDadosDeClientes.Documentos->{
                        Card(modifier = Modifier.fillMaxSize().padding(5.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)) {
                            Box(modifier = Modifier.fillMaxWidth()){
                                HorizontalDivider(Modifier.align(Alignment.TopCenter))
                                Column(Modifier.align(Alignment.CenterStart).padding(start = 5.dp,top=10.dp)) {
                                    cpfCnpj(dadosDeClientes.cliente.cpf,dadosDeClientes.cliente.cnpj, Modifier.padding(start = 5.dp, end = 5.dp, bottom = 10.dp))
                                }
                            }
                        }

                    }
                }
            }
        }
        is EstadosDeLoadCaregamento.load->{
            Box(Modifier.fillMaxSize()){
                LoadBox3pontinhos(modifier=Modifier.align(Alignment.Center), titulo = TitulosDeLoad.Clientes.titulo, estado = EstadosDeLoadCaregamento.load)
            }
        }
        is EstadosDeLoadCaregamento.Empty->{}
        is EstadosDeLoadCaregamento.Erro->{}
    }

}
/**
 * Apresenta a s inicias dentro de um cirlculo
 * */

@Composable
private fun Iniciais(nome:String,modifier: Modifier= Modifier,size:Dp=100.dp,sp:Int=30){
    val iniciais= remember{mutableStateOf("")}
    val color= remember{ mutableStateOf(Cores.lisColors[0])}
    LaunchedEffect(nome) {
        Log.d("Iniciais","nome mudou $nome")
        val split =nome.split(Regex("\\s"))
        if (split.size>=2){
            val inicialPrimeiroNome =split[0].first()
            val inicialsegundoNome= if(split[1].isEmpty()) "" else split[1].first()
            iniciais.value=inicialPrimeiroNome.toString()+inicialsegundoNome.toString()
        }
        else
            iniciais.value=split[0].first().toString()

    }
    LaunchedEffect(iniciais.value) {
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
private fun BaraSelecao(modifier: Modifier= Modifier,acao: (TelasInternasDadosDeClientes)->Unit,windowSizeClass: WindowSizeClass){
    Row(modifier=modifier, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        BaraDeSelecao.listaDeSelecao.forEach {
            ItemDeSelecao(item = it, acao = {acao(it.selecao)}, windowSizeClass = windowSizeClass)
        }
    }
}
/**
 * Apresenta o cpf ou cnpj
 * */
@Composable
private fun cpfCnpj(cpf:String?,cnpj:String?,modifier: Modifier= Modifier){
    if(cpf==null&&cnpj==null) {
        Text(text = "Dados nao cadastrados ",modifier=modifier, fontSize = 20.sp)
     return
    }
    if(cnpj==null) Text(text = "cpf :"+cpf,modifier=modifier, fontSize = 20.sp)
    else  if(cpf==null) Text(text = "cnpj :"+ cnpj, modifier = modifier, fontSize = 20.sp)
    else if(cpf!=null&&cnpj!=null) Column(modifier = modifier) {
        Text(text = "cpf :"+cpf,modifier=modifier.padding(bottom = 3.dp), fontSize = 20.sp)
        Text(text = "cnpj :"+ cnpj, modifier = modifier, fontSize = 20.sp)
    }
}
@Composable
private fun ItemDeSelecao(acao:()->Unit, modifier: Modifier= Modifier,item: BaraDeSelecao,windowSizeClass: WindowSizeClass){
    OutlinedButton(onClick = { acao()}) {

        Text(if(windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)||windowSizeClass.isWidthAtLeastBreakpoint(
                WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND))item.nome
             else item.nomeCompat ,
            modifier= Modifier.padding(end = 5.dp))
        Icon( painterResource(id = item.icone), contentDescription = null)


    }
}
/**
 * representa as opcoes de selecao  dos dados dos clientes como lista de endereco e lista de telefone
 * */
sealed class BaraDeSelecao(val icone:Int, val nome: String, val nomeCompat: String, val selecao: TelasInternasDadosDeClientes){
object SelecaoTelefone: BaraDeSelecao(R.drawable.telefone,"Telefone","Tel" ,TelasInternasDadosDeClientes.Telefone)
    object SelecaoEndereco: BaraDeSelecao(R.drawable.endereco,"Endereco" ,"End",TelasInternasDadosDeClientes.Endereco)
    object SelecaoDocumentos: BaraDeSelecao(R.drawable.pessoa_identificaco,"Documento","Doc" ,TelasInternasDadosDeClientes.Documentos)
    companion object{
        val listaDeSelecao= listOf(SelecaoTelefone,SelecaoEndereco,SelecaoDocumentos)
    }
}
/**
 * lista de enderecos
 * */
@Composable
private fun ListaDeEnderecos(modifier: Modifier,list: List<Endereco>){
    LazyColumn(modifier) {
        items(items = list){
            Endereco(it)
        }
    }
}
/**
 * item da lista de endereco
 * */
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


         }
         HorizontalDivider()
     }
    }
}
/**
 * lista de telefones
 * */
@Composable
private fun ListaDeTelefones(modifier: Modifier= Modifier,list: List<Telefone> ){
    LazyColumn(modifier = modifier) {
        items(items = list) {
          Telefone(it)
        }
    }
}
/**
 * item da lista de telefone
 * */

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


            }
            HorizontalDivider()
        }
    }
}

@Composable
fun previaDadosClientes(){}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun previaDoLoadDialog(){
    Scaffold(Modifier.fillMaxSize()) {

        val objetoDeDialogo = object : IDialogoCriacaoPdf{
            override val caixaDeDialogoCriarPdf: MutableStateFlow<Boolean>
                get() = MutableStateFlow(true)
            override val envioDerequisicao: MutableStateFlow<Uri?>
                get() = MutableStateFlow<Uri?>(null)
            override val estadosDeCriacaoDePdf: MutableStateFlow<EstadoLoadAcoes>
                get() = MutableStateFlow(EstadoLoadAcoes.Criando)

            override fun abrirDialogo() {

            }

            override fun fecharDialogo() {
            }

            override fun criarPdf(uri: Uri?) {

            }

            override fun limparEnvio() {

            }
        }

        DialogoCriarPdf(objetoDeDialogo)

    }
}