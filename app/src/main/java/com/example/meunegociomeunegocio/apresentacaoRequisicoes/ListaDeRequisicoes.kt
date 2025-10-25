package com.example.meunegociomeunegocio.apresentacaoRequisicoes

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import com.example.meunegociomeunegocio.apresentacaoDeProdutos.ListaDeProdutos
import com.example.meunegociomeunegocio.apresentacaoDeProdutos.ListaDeProdutos
import com.example.meunegociomeunegocio.repositorioRom.DadosDaRequisicao
import com.example.meunegociomeunegocio.repositorioRom.Estado
import com.example.meunegociomeunegocio.repositorioRom.EstadoRequisicao
import com.example.meunegociomeunegocio.repositorioRom.Mudanca
import com.example.meunegociomeunegocio.repositorioRom.Requisicao
import com.example.meunegociomeunegocio.repositorioRom.RequisicaoEProdutosRequeridos
import com.example.meunegociomeunegocio.viewModel.ListaHistorico
import com.example.meunegociomeunegocio.viewModel.TelasInternasDeRequisicoes
import com.example.meunegociomeunegocio.viewModel.ViewModelRequisicoes

@Composable
fun ListaDeRequisicoes(windowSizeClass: WindowSizeClass,vm: ViewModelRequisicoes){

    val lista =vm.fluxoTodasAsRequisicoes.collectAsStateWithLifecycle(emptyList())
    if(!windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND))
        ListaCompat(windowSizeClass = windowSizeClass, vm = vm)
    else{}
}


@Composable
private fun ListaCompat(windowSizeClass: WindowSizeClass,vm: ViewModelRequisicoes){
    val lista =vm.fluxoTodasAsRequisicoes.collectAsStateWithLifecycle(emptyList())
    val telaInterna=vm.telaInternasRequisicoes.collectAsStateWithLifecycle()

    when(telaInterna.value){
        is TelasInternasDeRequisicoes.Lista->{
            Lista(lista = lista.value,windowSizeClass = windowSizeClass,acao = {
                Log.d("listaconmpat","mudanado tela interna")
                vm.mostrarRequisicao(it) })}
        is TelasInternasDeRequisicoes.Requisicao -> {
            ExibicaoDaRequisicao({vm.voutarALista()},vm,windowSizeClass)
        }
    }

}
@Composable
private fun Lista(lista: List<DadosDaRequisicao>,windowSizeClass: WindowSizeClass,acao:(Int)->Unit){
    LazyColumn {
    stickyHeader {
        CabesalhoRequisicao(windowSizeClass = windowSizeClass)
    }

    items(items = lista) {
        ItemRwquisicao(it, windowSizeClass = windowSizeClass, acao = acao)
    }
}}

@Composable
private fun ExibicaoDaRequisicao(acaoDeVoultar:()->Unit={},vm: ViewModelRequisicoes,windowSizeClass: WindowSizeClass){
        val requisicao=vm.fluxoDadosDeRequisicao.collectAsStateWithLifecycle(null)
        val produtos =vm.fluxoProdutosRequisitados.collectAsStateWithLifecycle(emptyList())
        val estadoListaHistorico =vm.estadoListaHistorico.collectAsStateWithLifecycle(ListaHistorico.Lista)

        if(requisicao.value!=null)
        Column(modifier = Modifier.fillMaxWidth()) {
            Row {IconButton({acaoDeVoultar()}) {
                Icon(Icons.Outlined.ArrowDropDown, contentDescription = null)
            }
            NumeroDeRequisicoes(requisicao.value!!.requisicao.id)}
            NomeCliente(requisicao.value!!.cliente.nome, modifier = Modifier.padding(bottom = 5.dp))
            Observacao(requisicao.value!!.requisicao.obs, modifier = Modifier.padding(bottom = 5.dp))
            Descricao(requisicao.value!!.requisicao.desc, modifier = Modifier.padding(bottom = 5.dp))
            Estado(requisicao.value!!.estado.descricao)
            Row(modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 5.dp)) {
                OutlinedButton({
                    vm.verLista()
                }) {
                    Text("Lista de produtos")
                }
                Spacer(modifier = Modifier.padding(5.dp))
                OutlinedButton({
                    vm.verHistorico()
                }) {
                    Text("Historico")
                }
            }
            when(estadoListaHistorico.value){
                is ListaHistorico.Lista->{ListaDeProdutos(listaDeProdutos = produtos.value, windowSize =windowSizeClass)}
                is ListaHistorico.Historico->{ListaDeEstados(vm)}
            }
        }

}

@Composable
private fun NumeroDeRequisicoes(numero:Int,modifier: Modifier=Modifier){
    Row(modifier = modifier) {
        Text("N : ", Modifier.padding(5.dp))
        Text(numero.toString(), Modifier.padding(5.dp))
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
    val estados =vm.fluxoHistoricoDeMudancas.collectAsStateWithLifecycle(emptyList())
    LazyColumn {
        stickyHeader {
            CabesalhoEstado()
        }
        items(items = estados.value) {
            ItemHistorico(it)

        }
    }
}

@Composable
private fun ListaExpandida(windowSizeClass: WindowSizeClass,vm: ViewModelRequisicoes){
    val lista =vm.fluxoTodasAsRequisicoes.collectAsStateWithLifecycle(emptyList())
    LazyColumn {
        stickyHeader {
            CabesalhoRequisicao(windowSizeClass = windowSizeClass)
        }

        items(items = lista.value) {
            ItemRwquisicao(it, windowSizeClass = windowSizeClass)
        }
    }
}

@Composable
private fun ItemRwquisicao(dados: DadosDaRequisicao,windowSizeClass: WindowSizeClass,
                           acao:(Int)->Unit= {}){



    Card(modifier= Modifier.fillMaxWidth().height( 70.dp).padding(top = 5.dp, start = 3.dp, end = 3.dp).clickable(onClick = {acao(dados.requisicao.id)}),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)) {
        Box(modifier = Modifier.fillMaxSize()){


            Row(Modifier.padding(top = 3.dp, start = 5.dp, end = 5.dp).align(Alignment.CenterStart )) {

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
private fun CabesalhoRequisicao(modifier: Modifier=Modifier,windowSizeClass: WindowSizeClass){
    OutlinedCard (modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxSize().padding(top = 10.dp, bottom = 10.dp)){
            Row(modifier = Modifier.align(Alignment.CenterStart).padding(start = 5.dp, end = 5.dp)) {

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
    OutlinedCard (modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxSize().padding(top = 10.dp, bottom = 10.dp)){
            Row(modifier = Modifier.align(Alignment.CenterStart).padding(start = 5.dp, end = 5.dp)) {

                Text( "data", modifier = Modifier.width(250.dp))
                Spacer(Modifier.padding(10.dp))
                Text("Estado", modifier = Modifier.width(70.dp))





            }
        }
    }
}
@Composable
private fun ItemHistorico(dados: Mudanca){



    Card(modifier= Modifier.fillMaxWidth().height( 70.dp).padding(top = 5.dp, start = 3.dp, end = 3.dp).clickable(onClick = {}),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)) {
        Box(modifier = Modifier.fillMaxSize()){


            Row(Modifier.padding(top = 3.dp, start = 5.dp, end = 5.dp).align(Alignment.CenterStart )) {

                Text(dados.data, maxLines = 2,modifier=Modifier.width(250.dp), overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.padding(10.dp))
                Text(dados.idEstNovo.descricao, maxLines = 1, modifier = Modifier.width(90.dp), overflow = TextOverflow.Ellipsis)



            }


            HorizontalDivider(Modifier.fillMaxWidth())
        }
    }

}