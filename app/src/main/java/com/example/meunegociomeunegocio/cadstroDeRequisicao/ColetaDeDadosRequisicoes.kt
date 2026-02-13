package com.example.meunegociomeunegocio.cadstroDeRequisicao

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import com.example.meunegociomeunegocio.R
import com.example.meunegociomeunegocio.apresentacaoDeClientes.ListaDeClientes
import com.example.meunegociomeunegocio.apresentacaoDeProdutos.ListaDeProdutos
import com.example.meunegociomeunegocio.apresentacaoRequisicoes.DialogoCriarPdf
import com.example.meunegociomeunegocio.utilitario.EstadoLoadAcoes
import com.example.meunegociomeunegocio.viewModel.IDialogoCriacaoPdf
import com.example.meunegociomeunegocio.viewModel.ProdutoSelecionado
import com.example.meunegociomeunegocio.viewModel.TelasInternas
import com.example.meunegociomeunegocio.viewModel.ViewModelCriarRequisicoes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ColetaDeDaDosRequisicao(vm: ViewModelCriarRequisicoes,windowSizeClass: WindowSizeClass,acaoDeVOutar:()-> Unit){
    val telas =vm.telaAtual.collectAsStateWithLifecycle(TelasInternas.SelecaoDeClientes)
    LaunchedEffect(Unit) {
        Log.d("ColetaDeDaDosRequisicao","ColetaDeDaDosRequisicao iniciada")
    }
    Box{
        when(telas.value){
            is TelasInternas.SelecaoDeClientes -> SelecaoDeClientes(vm,windowSizeClass,acaoDeVOutar)

            is TelasInternas.SelecaoDeProdutos -> SelecaoDeProdutos(vm,windowSizeClass)

            is TelasInternas.Observacoes -> Observacoes(vm)

            is TelasInternas.Confirmacao -> Confirmacao(vm, acaoDeVOutar = {acaoDeVOutar()})

            else -> {}

        }
        SnackbarHost(vm.snapshotState, modifier = Modifier.align(Alignment.Center))}
    }







@Composable
private fun SelecaoDeClientes(vm: ViewModelCriarRequisicoes,windowSizeClass: WindowSizeClass,aaoDeVOutar: () -> Unit){
    when{
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)-> SelecaoDeClientesExpandido(vm,windowSizeClass)
        else -> SelecaoDeClientesCompat(vm,windowSizeClass,aaoDeVOutar)
    }
    }

@Composable
private fun SelecaoDeClientesExpandido(vm: ViewModelCriarRequisicoes,windowSizeClass: WindowSizeClass) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Nome(vm,{},modifier = Modifier.fillMaxWidth(0.4f))
        VerticalDivider(Modifier.padding(horizontal = 15.dp))
        ListaDeSelecaoClientes(vm,windowSizeClass,modifier = Modifier.fillMaxWidth(0.4f))
    }
}
@Composable
private fun SelecaoDeClientesCompat(vm: ViewModelCriarRequisicoes,windowSizeClass: WindowSizeClass,acaoDeVOutar: () -> Unit){
    val selecaoDeClientes =vm.selecaoDeClientes.collectAsStateWithLifecycle()
    when(selecaoDeClientes.value){
        is SelecaoDeClientes.Nome -> {Nome(vm, acaoDeVOutar = acaoDeVOutar,modifier = Modifier.fillMaxWidth())}
        is SelecaoDeClientes.ListaDeSelecao -> {ListaDeSelecaoClientes(vm,windowSizeClass,modifier = Modifier.fillMaxWidth())}
    }

}
@Composable
private fun Nome (vm: ViewModelCriarRequisicoes,acaoDeVOutar: () -> Unit,modifier: Modifier= Modifier){
    val nome=vm.clienteSelecionado.collectAsStateWithLifecycle()
    val state =rememberTextFieldState()
    val corotine=rememberCoroutineScope()
    LaunchedEffect(nome.value) {
        if(nome.value!=null)
         state.setTextAndPlaceCursorAtEnd(nome.value!!.second)
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.padding(5.dp)) {
        Text(text = "Selecao de Clientes", Modifier.padding(top = 25.dp, bottom = 10.dp), fontSize = 25.sp, textAlign = TextAlign.Justify)
        NomeCliente(vm = vm,state = state, acaoSelecionarCliente = {corotine.launch{vm.irParaTelaDeSelecaoDeClientes()}}, modifier = Modifier.fillMaxWidth(0.98f))
        Box(modifier = Modifier.fillMaxWidth()) {
            Button({acaoDeVOutar()}, modifier = Modifier.align(Alignment.CenterStart)) {
                Text("Cancelar")
            }
            Button({corotine.launch { vm.prosimoEstagio() }}, modifier = Modifier.align(Alignment.CenterEnd)) {
                Text("Selecao de produtos")
            }
        }

    }
}

@Composable
private fun ListaDeSelecaoClientes(vm: ViewModelCriarRequisicoes,windowSizeClass: WindowSizeClass,modifier: Modifier=Modifier){
    val coroutineScope =rememberCoroutineScope()
    Column {
        Box(modifier = modifier){
            if(!windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND))
            IconButton(onClick = { coroutineScope.launch{vm.irparaTelaDeVisualizacaoDeNomeDeCliente()} }) {
                Icon(painterResource(R.drawable.baseline_arrow_back_24), null)
            }
            Text("Selecione o cliente", modifier = Modifier.align(Alignment.Center))
        }

        ListaDeClientes(vm=vm)
    }

}

@Composable
private fun SelecaoDeProdutos(vm: ViewModelCriarRequisicoes,windowSizeClass: WindowSizeClass){
     when{
         windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)->{SelecaoDeProdutosEspandido(vm,windowSizeClass)}
         else -> SelecaoDeProdutosCompat(vm,windowSizeClass)

     }

}
@Composable
private fun SelecaoDeProdutosEspandido(vm: ViewModelCriarRequisicoes,windowSizeClass: WindowSizeClass){
    Row(modifier = Modifier.fillMaxWidth()) {
        ListaDeProdutosSelecionados(vm,windowSizeClass, modifier = Modifier.fillMaxWidth(0.4f))
        VerticalDivider(Modifier.padding(horizontal = 15.dp))
        ListaDeProdutos(modifier = Modifier,vm = vm, windowSize = windowSizeClass,{})
    }
}
@Composable
private fun SelecaoDeProdutosCompat(vm: ViewModelCriarRequisicoes,windowSizeClass: WindowSizeClass){
    val estadoSelecaoDeProdutos=vm.estadosDeSelecaoDeProdutos.collectAsStateWithLifecycle()


        when(estadoSelecaoDeProdutos.value){
            is SelecaoDeProdutos.ProdutosSelecionados->{
                ListaDeProdutosSelecionados(vm,windowSizeClass = windowSizeClass)
            }
            is SelecaoDeProdutos.ListaDeProdutos->{
                ListaDeProdutos(vm = vm, windowSize = windowSizeClass, acaoDeEdicaoDoProduto = {})
            }
        }



}
@Composable
private fun ListaDeProdutosSelecionados(vm: ViewModelCriarRequisicoes,windowSizeClass: WindowSizeClass,modifier: Modifier=Modifier){
    val coroutineScope =rememberCoroutineScope()
    val  produtosEquantidades=vm.produtosSelecionado.collectAsStateWithLifecycle()
    val produto = remember { mutableStateOf<ProdutoSelecionado>(ProdutoSelecionado(0,"Agua com gaz",1)) }
    Column(modifier = modifier.padding(bottom = 70.dp)) {
    Text(text = "Seleção produtos/Serviços", fontSize = 25.sp , textAlign = TextAlign.Justify,modifier= Modifier.align(Alignment.CenterHorizontally).padding(top = 20.dp, bottom = 10.dp, start = 5.dp, end = 5.dp))

    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally,
               modifier = Modifier.weight(0.1f)) {
        stickyHeader {
            if(!windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND))
            OutlinedCard(onClick = {coroutineScope.launch { vm.irParaSelecaoDosProdutos() }},
                         colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.background),
                         modifier = Modifier.padding(start = 10.dp ,end = 10.dp, bottom = 3.dp).fillMaxWidth(0.98f),) {
                Row(modifier = Modifier.fillMaxWidth().height(60.dp),horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Icon(painterResource(R.drawable.baseline_add_24),null, tint = Color.Blue)
                    Text("Adicionar produto/serviço", color = Color.Black, fontSize = 20.sp,modifier = Modifier.clickable { coroutineScope.launch { vm.irParaSelecaoDosProdutos() }})
                }
            }
        }
        itemsIndexed(items = produtosEquantidades.value) {id,prod->
            cardDeselecaoDeprodutosImpCompat(prod,windowSizeClass,
                                             acaoDeRemoverProduto = {coroutineScope.launch {vm.removereProduto(prod)  }},
                                             acaoDeAlmentarQuantidade = {coroutineScope.launch { vm.almentarQuantidade(id) }},
                                             acaoDeDiminuirQuantidade = {coroutineScope.launch { vm.diminuirQuantidade(id) }},
                                             modifier = Modifier.align(Alignment.CenterHorizontally))

        }
        if(produtosEquantidades.value.isEmpty()&&
           windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) )
          item {
              Box(Modifier.fillMaxWidth(0.95f).height(70.dp)){
                  Text("Nenhum produto selecionado", modifier = Modifier.align(Alignment.Center))

              }
          }

    }

    Box(modifier = Modifier.fillMaxWidth().padding(start = 20.dp,end=20.dp ,top =5.dp, bottom = 20.dp)){
        Button(onClick = { coroutineScope.launch { vm.estagioAnterior() } }, modifier = Modifier.align(
            Alignment.CenterStart)){
            Text("Selecionar Cliente")
        }
        Button(onClick = { coroutineScope.launch { vm.prosimoEstagio() } }, modifier = Modifier.align(
            Alignment.CenterEnd)){
            Text("Observacoes")
        }
    }
}
}
@Composable
private fun cardeDeSelecaoDePRodutos(produto:ProdutoSelecionado,windowSizeClass: WindowSizeClass){
    when{
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)->{}
        else -> {}

    }
}
@Composable
private fun cardDeselecaoDeprodutosImpCompat(produto: ProdutoSelecionado,
                                             windowSizeClass: WindowSizeClass,
                                             acaoDeAlmentarQuantidade:()-> Unit,
                                             acaoDeDiminuirQuantidade: () -> Unit,
                                             acaoDeRemoverProduto: () -> Unit,modifier: Modifier=Modifier) {
    OutlinedCard(modifier.fillMaxWidth(0.98f).padding(start = 10.dp, end = 10.dp, bottom = 3.dp),
                 colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.background)) {
        Box(modifier = Modifier.fillMaxWidth().padding(5.dp)){
            Column(Modifier.padding(start = 5.dp, top = 2.dp, bottom = 2.dp)) {
                Text(text ="Produto : ${produto.nome}",fontSize = 15.sp , modifier = Modifier.padding(end = 5.dp, start = 5.dp, bottom = 2.dp,))
                Text(text = "Quantidade : ${produto.quantidade}")

            }
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.align(Alignment.CenterEnd)) {
                IconButton(onClick = { acaoDeAlmentarQuantidade() }) {
                    Icon(painterResource(R.drawable.baseline_exposure_plus_1_24),"mais um")
                }

                if(produto.quantidade==1)
                    IconButton({acaoDeRemoverProduto()}) {
                        Icon(painterResource(R.drawable.baseline_delete_24),"remover")

                    }
                else
                    IconButton({acaoDeDiminuirQuantidade()}) {
                        Icon(painterResource(R.drawable.ic_menos_uma),"menos um")
                    }
            }
        }
    }
}

@Composable
private fun Observacoes(vm: ViewModelCriarRequisicoes){
    val descricao=rememberTextFieldState()
    val observacoes=rememberTextFieldState()
    val coroutineScope =rememberCoroutineScope()
    val observacoesSalvas =vm.observacoes.collectAsStateWithLifecycle()
    LaunchedEffect(observacoesSalvas.value) {
        if(observacoesSalvas.value!=null) {
            descricao.setTextAndPlaceCursorAtEnd(observacoesSalvas.value!!.first)
            observacoes.setTextAndPlaceCursorAtEnd(observacoesSalvas.value!!.second)
        }
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Observacoes", fontSize = 25.sp, textAlign = TextAlign.Justify, modifier = Modifier.padding(top = 20.dp, bottom = 10.dp, start = 5.dp, end = 5.dp).align(Alignment.CenterHorizontally))
         OutlinedTextField(state = descricao, label = { Text("Descricao")}, modifier = Modifier.fillMaxWidth(0.98f).padding(bottom = 10.dp, start = 5.dp, end = 5.dp))
         OutlinedTextField(state = observacoes, label = {Text("Observacoes")}, modifier = Modifier.fillMaxWidth(0.98f).padding(bottom = 10.dp, start = 5.dp, end = 5.dp))

        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp,vertical = 5.dp)){
            Button({coroutineScope.launch { vm.estagioAnterior() }}, modifier = Modifier.align(Alignment.CenterStart)) {
                Text("Selecao Produtos/Serviços")
            }
            Button({
                vm.salvarObservacoe(observacoes.text.toString(),descricao.text.toString())

                vm.salvarRequisicao()}, modifier = Modifier.align(Alignment.CenterEnd)) {
                Text("Confirmar")
            }
        }
    }
}

@Composable
private fun Confirmacao(vm: ViewModelCriarRequisicoes,acaoDeVOutar: () -> Unit){
    DialogoCriarPdf(vm, acaoFimDeEmvio = acaoDeVOutar)

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NomeCliente(vm: ViewModelCriarRequisicoes,modifier: Modifier= Modifier,pading: Int=5,state:TextFieldState= rememberTextFieldState(),acaoSelecionarCliente:()->Unit){
     val coroutineScope = rememberCoroutineScope()
     val espandadde = remember{ mutableStateOf("") }
     val textoObservado =vm.clienteSelecionado.collectAsStateWithLifecycle()

    OutlinedCard(onClick = { acaoSelecionarCliente()},modifier = modifier.fillMaxWidth(0.8f).padding(pading.dp), shape = RoundedCornerShape(4.dp)) {
        Row(modifier = Modifier.fillMaxWidth(0.8f).height(70.dp).padding(9.dp), verticalAlignment = Alignment.CenterVertically) {
            if(textoObservado.value==null){
            Icon(painter = painterResource(R.drawable.cliente_2_24 ),null,modifier= Modifier.size(20.dp))
                Spacer(modifier = Modifier.padding(2.dp))
            Text("Adicionar cliente")
            }
            else
             Text(text = textoObservado.value!!.second)
        }
    }
}

@Preview
@Composable
fun previasDoLoad(){
    Scaffold(Modifier.fillMaxSize()) {it->
        var pading =it;
        var interfaceDeDialogo = object : IDialogoCriacaoPdf{
            override fun limparEnvio() {

            }

            override fun criarPdf(uri: Uri?) {

            }

            override fun fecharDialogo() {

            }

            override fun abrirDialogo() {

            }

            override val estadosDeCriacaoDePdf: MutableStateFlow<EstadoLoadAcoes>
                get() = MutableStateFlow<EstadoLoadAcoes>(EstadoLoadAcoes.Criando)
            override val envioDerequisicao: MutableStateFlow<Uri?>
                get() = MutableStateFlow<Uri?>(null)
            override val caixaDeDialogoCriarPdf: MutableStateFlow<Boolean>
                get() = MutableStateFlow(true)
        }
        DialogoCriarPdf(interfaceDeDialogo)
    }

}