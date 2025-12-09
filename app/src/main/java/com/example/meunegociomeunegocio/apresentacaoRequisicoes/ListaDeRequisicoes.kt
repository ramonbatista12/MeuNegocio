 package com.example.meunegociomeunegocio.apresentacaoRequisicoes

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card

import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import com.example.meunegociomeunegocio.R
import com.example.meunegociomeunegocio.apresentacaoDeProdutos.ListaDeProdutosRequisitados
import com.example.meunegociomeunegocio.apresentacaoDeProdutos.formatData
import com.example.meunegociomeunegocio.apresentacaoDeProdutos.formatarPreco
import com.example.meunegociomeunegocio.loads.ItemDelLoadTabelas
import com.example.meunegociomeunegocio.repositorioRom.DadosDaRequisicao
import com.example.meunegociomeunegocio.repositorioRom.Mudanca
import com.example.meunegociomeunegocio.utilitario.EstadoLoadAcoes
import com.example.meunegociomeunegocio.utilitario.EstadosDeLoadCaregamento
import com.example.meunegociomeunegocio.viewModel.FiltroDePesquisaRequisicoes
import com.example.meunegociomeunegocio.viewModel.IDialogoCriacaoPdf
import com.example.meunegociomeunegocio.viewModel.ListaHistorico
import com.example.meunegociomeunegocio.viewModel.SelecaoDeFiltros
import com.example.meunegociomeunegocio.viewModel.TelasInternasDeRequisicoes
import com.example.meunegociomeunegocio.viewModel.ViewModelRequisicoes
import kotlinx.coroutines.launch

@Composable
fun ListaDeRequisicoes(windowSizeClass: WindowSizeClass,vm: ViewModelRequisicoes,modifier: Modifier=Modifier){


    if(!windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND))
        ListaCompat(windowSizeClass = windowSizeClass, vm = vm,modifier)
    else{
        ListaExpandida(windowSizeClass = windowSizeClass, vm = vm,modifier)
    }
    SnackbarHost(vm.snackbarHostState)
}

 @Composable
 fun DialogoCriarPdf(interfaceDeDialogo: IDialogoCriacaoPdf,acaoFimDeEmvio:(()->Unit)?=null) {
     val dialogoMontarPdf =
         interfaceDeDialogo.caixaDeDialogoCriarPdf.collectAsStateWithLifecycle(initialValue = false)
     val intenteEnvioRequisicao =
         rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
          acaoFimDeEmvio?.let {
              it.invoke()
          }

         }
     val envioDasRequisicoes =
         interfaceDeDialogo.envioDerequisicao.collectAsStateWithLifecycle(null)
     LaunchedEffect(envioDasRequisicoes.value) {
         if (envioDasRequisicoes.value != null) {
             try {
                 Log.d("ExibicaoDaRequisicao", "lancando intent")
                 val intent = Intent(ACTION_SEND)
                 intent.type = "application/pdf"
                 intent.putExtra(Intent.EXTRA_STREAM, envioDasRequisicoes.value)
                 intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                 intenteEnvioRequisicao.launch(Intent.createChooser(intent, "Enviar Arquivo"))
             } catch (e: Exception) {
                 Log.d("ExibicaoDaRequisicao", "erro ao enviar arquivo")
             }

         }
     }
     val criarPdf =
         rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/pdf")) { uri ->

             Log.d("criarPdf", "valor da uri recebida= $uri")
             interfaceDeDialogo.criarPdf(uri)
         }
     val estados =
         interfaceDeDialogo.estadosDeCriacaoDePdf.collectAsStateWithLifecycle(EstadoLoadAcoes.Iniciando)
     LaunchedEffect(estados.value) {
         if (estados.value == EstadoLoadAcoes.Sucesso) {

             interfaceDeDialogo.fecharDialogo()
         }
     }

     val textoInical =
         "Um arquivo PDF será gerado com os dados da requisição atual. Escolha onde deseja salvar o arquivo."
     val textoErro = "Houve um erro ao criar o arquivo. Você pode tentar novamente se quiser."
     if (dialogoMontarPdf.value)
         Dialog(onDismissRequest = {
             if(acaoFimDeEmvio!=null){
                 acaoFimDeEmvio.invoke()
             }

             interfaceDeDialogo.fecharDialogo() }) {
             OutlinedCard(
                 onClick = {},
                 colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                 modifier = Modifier
                     .height(300.dp)
                     .width(450.dp)
             ) {

                 Box(modifier = Modifier
                     .fillMaxSize()
                     .padding(5.dp)) {
                     Column(modifier = Modifier
                         .fillMaxWidth()
                         .align(Alignment.TopCenter)) {
                         Text(
                             "Criar pdf",
                             modifier = Modifier
                                 .align(Alignment.CenterHorizontally)
                                 .padding(top = 5.dp),
                             fontSize = 20.sp
                         )
                         HorizontalDivider(Modifier.padding(vertical = 5.dp))
                         when (estados.value) {
                             is EstadoLoadAcoes.Criando -> {
                                 CircularProgressIndicator(
                                     Modifier
                                         .align(Alignment.CenterHorizontally)
                                         .padding(20.dp)
                                         .size(60.dp)
                                 )
                             }

                             is EstadoLoadAcoes.Erro -> {
                                 Text(textoErro)
                             }

                             is EstadoLoadAcoes.Iniciando -> {
                                 Text(textoInical)
                             }

                             is EstadoLoadAcoes.Sucesso -> {}
                         }
                     }
                     BotoesDialogo(
                         interfaceDeDialogo,
                         modifier = Modifier.align(Alignment.BottomCenter),
                         acaoDeProceguir = { criarPdf.launch("orcamento_.pdf") },
                         acaoCancelar = { interfaceDeDialogo.fecharDialogo() })
                 }

             }
         }
 }

 @Composable//
 private fun BotoesDialogo(
     interfaceDeDialogo: IDialogoCriacaoPdf,
     modifier: Modifier = Modifier,
     acaoDeProceguir: () -> Unit,
     acaoCancelar: () -> Unit
 ) {
     val estadosDeCriacaoDePdf =
         interfaceDeDialogo.estadosDeCriacaoDePdf.collectAsStateWithLifecycle(EstadoLoadAcoes.Iniciando)
     Box(modifier.fillMaxWidth()) {
         when (estadosDeCriacaoDePdf.value) {
             is EstadoLoadAcoes.Iniciando -> {
                 Button(
                     onClick = { acaoCancelar() },
                     colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                     modifier = Modifier
                         .align(
                             Alignment.CenterStart
                         )
                         .padding(20.dp)
                 ) {
                     Text(text = "Cancelar", color = Color.White)
                 }

                 Button(
                     onClick = { acaoDeProceguir() },
                     colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                     modifier = Modifier
                         .align(
                             Alignment.CenterEnd
                         )
                         .padding(20.dp)
                 ) {
                     Text(text = "Ok", color = Color.White)
                 }
             }

             is EstadoLoadAcoes.Erro -> {
                 Button(
                     onClick = { acaoCancelar() },
                     colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                     modifier = Modifier
                         .align(
                             Alignment.CenterStart
                         )
                         .padding(20.dp)
                 ) {
                     Text(text = "Cancelar", color = Color.White)
                 }

                 Button(
                     onClick = { acaoDeProceguir() },
                     colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                     modifier = Modifier
                         .align(
                             Alignment.CenterEnd
                         )
                         .padding(20.dp)
                 ) {
                     Text(text = "Tentar novamente", color = Color.White)
                 }
             }

             is EstadoLoadAcoes.Criando -> {}
             is EstadoLoadAcoes.Sucesso -> {}
         }

     }
 }



@Composable
private fun ListaCompat(windowSizeClass: WindowSizeClass,vm: ViewModelRequisicoes,modifier: Modifier=Modifier){

    val telaInterna=vm.telaInternasRequisicoes.collectAsStateWithLifecycle()

    when(telaInterna.value){
        is TelasInternasDeRequisicoes.Lista->{
            Lista(modifier=modifier,vm,windowSizeClass = windowSizeClass,acao = {
                Log.d("listaconmpat","mudanado tela interna")
                vm.mudarId(it)
                vm.mostrarRequisicao(it) })}
        is TelasInternasDeRequisicoes.Requisicao -> {
            ExibicaoDaRequisicao(modifier=modifier,acaoDeVoultar = {vm.voutarALista()},vm=vm, windowSizeClass =windowSizeClass)
        }
    }

}

@Composable
private fun   ListaExpandida(windowSizeClass: WindowSizeClass,vm: ViewModelRequisicoes,modifier: Modifier=Modifier){

    val telaInterna=vm.telaInternasRequisicoes.collectAsStateWithLifecycle()
    Row(modifier = Modifier.fillMaxWidth()) {
        Lista(modifier= Modifier.fillMaxWidth(0.4f),vm,windowSizeClass = windowSizeClass, acao = { vm.mudarId(it)
                                                                            vm.mostrarRequisicao(it) })
        VerticalDivider(Modifier.padding(horizontal = 15.dp))
        ExibicaoDaRequisicao(modifier=modifier,acaoDeVoultar = {vm.voutarALista()},vm=vm, windowSizeClass =windowSizeClass)

    }
}
@Composable
private fun Lista(modifier:Modifier=Modifier, vm: ViewModelRequisicoes, windowSizeClass: WindowSizeClass, acao:(Int)->Unit){
    val estadosDeLoadCaregamento =vm.fluxoTodasAsRequisicoes.collectAsStateWithLifecycle(EstadosDeLoadCaregamento.load)
    Column(modifier = modifier.padding(top = 10.dp)) {
    BarraDePesquisaRequisicoes(vm = vm, Modifier
        .align(Alignment.CenterHorizontally)
        .fillMaxWidth()
        .padding(horizontal = 5.dp),windowSizeClass)
    LazyColumn(modifier = Modifier) {
        stickyHeader {
            CabesalhoRequisicao(windowSizeClass = windowSizeClass)
        }
        when(estadosDeLoadCaregamento.value){
            is EstadosDeLoadCaregamento.load -> {
                items(count = 5) {
                    ItemDelLoadTabelas()
            }}
            is EstadosDeLoadCaregamento.Empty -> {}
            is EstadosDeLoadCaregamento.Caregado<*> -> {
                val lista = estadosDeLoadCaregamento.value as EstadosDeLoadCaregamento.Caregado<List<DadosDaRequisicao>>
                items(items = lista.obj) {
                    ItemRwquisicao(it, windowSizeClass = windowSizeClass, acao = acao)
                }
            }
                is EstadosDeLoadCaregamento.Erro -> {}

            }

        }



    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BarraDePesquisaRequisicoes(vm: ViewModelRequisicoes,modifier: Modifier=Modifier,windowSizeClass: WindowSizeClass){
    val  searchBarState = rememberSearchBarState()
    val query =remember { mutableStateOf("") }
    val expandido=remember { mutableStateOf(false) }
    val coroutineScope =rememberCoroutineScope()
    val interacao =remember { MutableInteractionSource() }
    val espancaoDoFiltro=remember { mutableStateOf(false) }
    val filtroSelecionado=remember { mutableStateOf<SelecaoDeFiltros>(SelecaoDeFiltros.Cliente) }
    SearchBar(expanded = expandido.value,
              onExpandedChange = {expandido.value=it},
              inputField ={ SearchBarDefaults.InputField(query =query.value,
                                                         expanded =expandido.value,
                                                         onSearch = {when(filtroSelecionado.value){
                                                             is SelecaoDeFiltros.Cliente -> vm.mudarFiltro(FiltroDePesquisaRequisicoes.Clientes(it))
                                                             is SelecaoDeFiltros.Estado -> vm.mudarFiltro(FiltroDePesquisaRequisicoes.Estado(it))
                                                             is SelecaoDeFiltros.Data -> vm.mudarFiltro(FiltroDePesquisaRequisicoes.Data(it))
                                                         }},
                                                         onExpandedChange = {coroutineScope.launch {
                                                                             expandido.value=true
                                                                             searchBarState.animateToExpanded()}},
                                                         onQueryChange = {query.value=it
                                                                          when(filtroSelecionado.value){
                                                                              is SelecaoDeFiltros.Cliente -> vm.mudarFiltro(FiltroDePesquisaRequisicoes.Clientes(it))
                                                                              is SelecaoDeFiltros.Estado -> vm.mudarFiltro(FiltroDePesquisaRequisicoes.Estado(it))
                                                                              is SelecaoDeFiltros.Data -> vm.mudarFiltro(FiltroDePesquisaRequisicoes.Data(it))
                                                                           }
                                                                         },
                                                         placeholder = {Text("Pesquisa")},
                                                         leadingIcon = {Icon(painterResource(R.drawable.baseline_search_24),"")},
                                                         trailingIcon = {
                                                                 Row {
                                                                     if(expandido.value)
                                                                     MenuFiltro(expanded = espancaoDoFiltro, acaoDeFiltro = {
                                                                         filtroSelecionado.value=it

                                                                     })
                                                                     Spacer(Modifier.padding(end = 10.dp))
                                                                     Icon(painterResource(R.drawable.baseline_close_24),"",
                                                                         Modifier.clickable(onClick = {
                                                                             coroutineScope.launch {
                                                                                 vm.limparFiltro()
                                                                                 expandido.value=false
                                                                                 searchBarState.animateToCollapsed()
                                                                                 query.value=""
                                                                             }
                                                                         }))
                                                                 }




                                                         } )

                                                            },
              modifier = modifier,
              colors = SearchBarDefaults.colors(containerColor = MaterialTheme.colorScheme.background)){
           val pesquisa=vm.fluxoDePesquisaRequisicoes.collectAsStateWithLifecycle(EstadosDeLoadCaregamento.Empty)
           LazyColumn { 
               stickyHeader {
                   CabesalhoRequisicao(windowSizeClass = windowSizeClass)
               }
               when(pesquisa.value){

                   is EstadosDeLoadCaregamento.Caregado<*> -> {
                       val lista =pesquisa.value as EstadosDeLoadCaregamento.Caregado<List<DadosDaRequisicao>>
                       items(items = lista.obj) {
                           ItemRwquisicao(it, windowSizeClass = windowSizeClass,
                               acao = {
                                   coroutineScope.launch {
                                       vm.limparFiltro()
                                       expandido.value=false
                                       searchBarState.animateToCollapsed()
                                       query.value=""
                                       vm.mudarId(it)
                                       vm.mostrarRequisicao(it)
                                   }

                               })
                       }
                   }
                   else -> {}
               }

           }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MenuFiltro(modifier: Modifier= Modifier,expanded: MutableState<Boolean>,acaoDeFiltro:(SelecaoDeFiltros)->Unit={}){

    ExposedDropdownMenuBox(
        onExpandedChange = {expanded.value=!expanded.value},
        expanded = expanded.value,
        modifier = modifier
    ) {
        if(!expanded.value)
        Icon(painterResource(R.drawable.baseline_filter_list_24),"", Modifier.clickable(onClick = {expanded.value=true}))
        DropdownMenu(expanded=expanded.value, onDismissRequest = {expanded.value=false}){
          Row(modifier = Modifier
              .clickable(onClick = {
                  acaoDeFiltro(SelecaoDeFiltros.Cliente)
                  expanded.value = false
              })
              .width(70.dp)) {Text("Cliente")}
          Row(modifier = Modifier
              .clickable(onClick = {
                  acaoDeFiltro(SelecaoDeFiltros.Estado)
                  expanded.value = false
              })
              .width(70.dp)) {  Text("Estado")}
          Row(modifier = Modifier
              .clickable(onClick = {
                  acaoDeFiltro(SelecaoDeFiltros.Data)
                  expanded.value = false
              })
              .width(70.dp)) {  Text("Data")}
        }
    }
}

@Composable
private fun ExibicaoDaRequisicao(modifier: Modifier=Modifier,acaoDeVoultar:()->Unit={},vm: ViewModelRequisicoes,windowSizeClass: WindowSizeClass){
        val requisicao=vm.fluxoDadosDeRequisicao.collectAsStateWithLifecycle(EstadosDeLoadCaregamento.load)
        val estadoListaHistorico =vm.estadoListaHistorico.collectAsStateWithLifecycle(ListaHistorico.Lista)
        val envioDasRequisicoes =vm.envioDerequisicao.collectAsStateWithLifecycle(null)
        val  context =LocalContext.current

        when(requisicao.value){
            is EstadosDeLoadCaregamento.Empty -> {}
            is EstadosDeLoadCaregamento.Caregado<*> -> {
                val requisicao =requisicao.value as EstadosDeLoadCaregamento.Caregado<DadosDaRequisicao>
                Column(modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 70.dp)) {
                Box(modifier = Modifier.fillMaxWidth()){
                    if(!windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND))
                    IconButton({acaoDeVoultar()},modifier= Modifier.align(Alignment.CenterStart)) {
                    Icon(painterResource(R.drawable.baseline_arrow_drop_down_24),contentDescription = null,modifier=Modifier.size(25.dp))
                }
                    NumeroDeRequisicoes(requisicao.obj.requisicao.id,modifier= Modifier.align(Alignment.Center))
                 IconButton(onClick = {vm.abrirDialogo()},modifier= Modifier.align(Alignment.CenterEnd)) {
                     Icon(painter = painterResource(R.drawable.recibo_2_24), contentDescription = null,modifier=Modifier.size(20.dp))
                 }


                }

                NomeCliente(requisicao.obj.cliente.nome, modifier = Modifier.padding(bottom = 5.dp))
                Observacao(requisicao.obj.requisicao.obs, modifier = Modifier.padding(bottom = 5.dp))
                Descricao(requisicao.obj.requisicao.desc, modifier = Modifier.padding(bottom = 5.dp))
                Custo(vm, modifier = Modifier.padding(bottom = 5.dp))
                Estado(requisicao.obj.estado.descricao)
                if(requisicao.obj.estado.descricao!="Cancelado") {
                HorizontalDivider()
                Row(modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 5.dp)
                    .align(Alignment.CenterHorizontally)) {
                    OutlinedButton({
                    }) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    OutlinedButton({

                    }) {
                        Text("Confirmar")
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    OutlinedButton({
                        vm.verLista()
                    }) {
                        Text("Entreque")
                    }
                    Spacer(modifier = Modifier.padding(5.dp))

                }}
                HorizontalDivider()
                Row(modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 5.dp)) {
                    OutlinedButton({
                        vm.verLista()
                    }) {
                        Text("Lista de produtos")
                        Icon(painter = painterResource(R.drawable.ic_shoping), contentDescription = null, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    OutlinedButton({
                        vm.verHistorico()
                    }) {
                        Text("Historico")
                        Icon(painter = painterResource(R.drawable.historico), contentDescription = null, modifier = Modifier.size(20.dp))
                    }
                }
                when(estadoListaHistorico.value){
                    is ListaHistorico.Lista->{
                        ListaDeProdutosRequisitados(vm=vm, windowSize =windowSizeClass)
                    }
                    is ListaHistorico.Historico->{ListaDeEstados(vm)}
                }

            }
                DialogoCriarPdf(vm,)
            }
            is EstadosDeLoadCaregamento.load -> {}
            else -> {}
        }



}



@Composable
private fun NumeroDeRequisicoes(numero:Int,modifier: Modifier=Modifier){
    Row(modifier = modifier) {
        Text("Orcamento numero : ", Modifier.padding(5.dp), fontSize = 20.sp)
        Text(numero.toString(), Modifier.padding(5.dp), fontSize = 20.sp)
    }
}

@Composable
private fun Observacao(obs:String,modifier: Modifier=Modifier){
    Row(modifier = modifier) {
        Text("Observasoes : "+obs, Modifier.padding(5.dp))

    }
}

@Composable
private fun Descricao(dsc:String,modifier: Modifier=Modifier){
    Row(modifier = modifier) {
        Text("Descricao : "+dsc, Modifier.padding(5.dp))

    }
}

@Composable
private fun Custo(vm: ViewModelRequisicoes,modifier: Modifier=Modifier){
    val custo =vm.valorTotalRequisicao.collectAsStateWithLifecycle(EstadosDeLoadCaregamento.load)
    when(custo.value){
        is EstadosDeLoadCaregamento.load -> {}
        is EstadosDeLoadCaregamento.Empty -> {}
        is EstadosDeLoadCaregamento.Caregado<*> -> {
            val custo =custo.value as EstadosDeLoadCaregamento.Caregado<Double>
            Row(modifier = modifier) {
            Text("Total $ : "+custo.obj.toString().formatarPreco(), Modifier.padding(5.dp))

        }}
        else -> {}
    }

}

@Composable
private fun Estado(estado:String,modifier: Modifier=Modifier){
    Row(modifier = modifier) {
        Text("Estado  : ", Modifier.padding(5.dp))
        Text(estado, Modifier.padding(5.dp))
    }
}

@Composable
private fun NomeCliente(nome:String,modifier: Modifier=Modifier){
    Row(modifier = modifier) {
       Text("Nome do cliente : "+nome , Modifier.padding(5.dp))
    }
}
@Composable
private fun ListaDeEstados(vm: ViewModelRequisicoes){
    val estados =vm.fluxoHistoricoDeMudancas.collectAsStateWithLifecycle(EstadosDeLoadCaregamento.load)
    when(estados.value){
        is EstadosDeLoadCaregamento.load -> {
            Text("Caregando")
        }
        is EstadosDeLoadCaregamento.Empty -> {
            Column{
                Text("O historico para essa requisicao esta vasio")
            }
        }
        is EstadosDeLoadCaregamento.Caregado<*> -> {
            val estados =estados.value as EstadosDeLoadCaregamento.Caregado<List<Mudanca>>
            LazyColumn {
            stickyHeader {
                CabesalhoEstado()
            }
            items(items = estados.obj) {
                ItemHistorico(it)

            }
        }}
        else->{
            Column{
                Text("O historico para essa requisicao esta Indisponivel")
            }
        }
        }



}

@Composable
private fun ListaExpandida(windowSizeClass: WindowSizeClass,vm: ViewModelRequisicoes){
    val estadosDeLoadCaregamento =vm.fluxoTodasAsRequisicoes.collectAsStateWithLifecycle(EstadosDeLoadCaregamento.load)
    when(estadosDeLoadCaregamento.value){
        is EstadosDeLoadCaregamento.Empty -> {}
        is EstadosDeLoadCaregamento.Erro -> {}
        is EstadosDeLoadCaregamento.load -> {}
        is EstadosDeLoadCaregamento.Caregado<*> -> {
            val lista =estadosDeLoadCaregamento.value as EstadosDeLoadCaregamento.Caregado<List<DadosDaRequisicao>>
            LazyColumn {
                stickyHeader {
                    CabesalhoRequisicao(windowSizeClass = windowSizeClass)
                }

                items(items = lista.obj) {
                    ItemRwquisicao(it, windowSizeClass = windowSizeClass)
                }
            }
        }
    }

}

@Composable
private fun ItemRwquisicao(dados: DadosDaRequisicao,windowSizeClass: WindowSizeClass,
                           acao:(Int)->Unit= {}){



    Card(modifier= Modifier
        .fillMaxWidth()
        .height(70.dp)
        .padding(top = 5.dp, start = 3.dp, end = 3.dp)
        .clickable(onClick = { acao(dados.requisicao.id) }),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)) {
        Box(modifier = Modifier.fillMaxSize()){


            Row(Modifier
                .padding(top = 3.dp, start = 5.dp, end = 5.dp)
                .align(Alignment.CenterStart)) {

               Text(dados.requisicao.id.toString(), maxLines = 2,modifier=Modifier.width(75.dp), overflow = TextOverflow.Ellipsis)


                Spacer(Modifier.padding(10.dp))
                when{
                   windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)||
                   windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)->Text(dados.cliente.nome, Modifier.width(200.dp),overflow = TextOverflow.Ellipsis)
                   else -> Text(dados.cliente.nome, Modifier.width(70.dp),overflow = TextOverflow.Ellipsis)
                }

                Spacer(Modifier.padding(10.dp))
                Text(dados.estado.descricao, maxLines = 1, modifier = Modifier.width(90.dp), overflow = TextOverflow.Ellipsis)



            }
            FlowRow(Modifier.align(Alignment.CenterEnd )) {
                IconButton ({},modifier= Modifier
                    .size(30.dp)
                    .padding(end = 3.dp)) {
                    Icon(painterResource(R.drawable.create_24),modifier= Modifier.size(20.dp),contentDescription = "")
                }

                IconButton ({},modifier= Modifier
                    .size(30.dp)
                    .padding(5.dp)) {
                    Icon(painterResource(R.drawable.baseline_delete_24),modifier= Modifier.size(20.dp), contentDescription = "")
                }

            }

            HorizontalDivider(Modifier.fillMaxWidth())
        }
    }

}

@Composable
private fun CabesalhoRequisicao(modifier: Modifier=Modifier,windowSizeClass: WindowSizeClass){
    Card (modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp, bottom = 10.dp)){
            Row(modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 5.dp, end = 5.dp)) {

                Text( "Req", modifier = Modifier.width(75.dp))
                Spacer(Modifier.padding(10.dp))
                when{
                    windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)||
                    windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)-> Text("Cliente", modifier = Modifier.width(200.dp))
                    else -> Text("Cliente", modifier = Modifier.width(70.dp))
                }

                Spacer(Modifier.padding(10.dp))
                Text("Estado")


            }
        }
    }
}

@Composable
private fun CabesalhoEstado(modifier: Modifier=Modifier){
    Card (modifier = Modifier.fillMaxWidth(),colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp, bottom = 10.dp)){
            Row(modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 5.dp, end = 5.dp)) {

                Text( "data de modificacao", modifier = Modifier.width(250.dp))
                Spacer(Modifier.padding(10.dp))
                Text("Estado", modifier = Modifier.width(70.dp))





            }
        }
    }
}
@Composable
private fun ItemHistorico(dados: Mudanca){



    Card(modifier= Modifier
        .fillMaxWidth()
        .height(70.dp)
        .padding(top = 5.dp, start = 3.dp, end = 3.dp)
        .clickable(onClick = {}),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)) {
        Box(modifier = Modifier.fillMaxSize()){


            Row(Modifier
                .padding(top = 3.dp, start = 5.dp, end = 5.dp)
                .align(Alignment.CenterStart)) {

                Text(dados.data.formatData(), maxLines = 2,modifier=Modifier.width(250.dp), overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.padding(10.dp))
                Text(dados.idEstNovo.descricao, maxLines = 1, modifier = Modifier.width(90.dp), overflow = TextOverflow.Ellipsis)



            }


            HorizontalDivider(Modifier.fillMaxWidth())
        }
    }

}