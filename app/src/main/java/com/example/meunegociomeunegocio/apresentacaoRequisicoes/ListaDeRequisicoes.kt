package com.example.meunegociomeunegocio.apresentacaoRequisicoes

import android.util.Log
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
import androidx.compose.material3.Card

import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import com.example.meunegociomeunegocio.R
import com.example.meunegociomeunegocio.apresentacaoDeProdutos.ListaDeProdutosRequisitados
import com.example.meunegociomeunegocio.apresentacaoDeProdutos.formatData
import com.example.meunegociomeunegocio.apresentacaoDeProdutos.formatarPreco
import com.example.meunegociomeunegocio.loads.ItemDelLoadTabelas
import com.example.meunegociomeunegocio.repositorioRom.DadosDaRequisicao
import com.example.meunegociomeunegocio.repositorioRom.Mudanca
import com.example.meunegociomeunegocio.utilitario.EstadosDeLoad
import com.example.meunegociomeunegocio.viewModel.ListaHistorico
import com.example.meunegociomeunegocio.viewModel.TelasInternasDeRequisicoes
import com.example.meunegociomeunegocio.viewModel.ViewModelRequisicoes

@Composable
fun ListaDeRequisicoes(windowSizeClass: WindowSizeClass,vm: ViewModelRequisicoes,modifier: Modifier=Modifier){


    if(!windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND))
        ListaCompat(windowSizeClass = windowSizeClass, vm = vm,modifier)
    else{
        ListaExpandida(windowSizeClass = windowSizeClass, vm = vm,modifier)
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
private fun  ListaExpandida(windowSizeClass: WindowSizeClass,vm: ViewModelRequisicoes,modifier: Modifier=Modifier){

    val telaInterna=vm.telaInternasRequisicoes.collectAsStateWithLifecycle()
    Row(modifier = Modifier.fillMaxWidth()) {
        Lista(modifier= Modifier.fillMaxWidth(0.4f),vm,windowSizeClass = windowSizeClass, acao = { vm.mudarId(it)
                                                                            vm.mostrarRequisicao(it) })
        VerticalDivider(Modifier.padding(horizontal = 10.dp))
        ExibicaoDaRequisicao(modifier=modifier,acaoDeVoultar = {vm.voutarALista()},vm=vm, windowSizeClass =windowSizeClass)

    }
}
@Composable
private fun Lista(modifier:Modifier=Modifier, vm: ViewModelRequisicoes, windowSizeClass: WindowSizeClass, acao:(Int)->Unit){
    val estadosDeLoad =vm.fluxoTodasAsRequisicoes.collectAsStateWithLifecycle(EstadosDeLoad.load)
    LazyColumn(modifier = modifier) {
        stickyHeader {
            CabesalhoRequisicao(windowSizeClass = windowSizeClass)
        }
        when(estadosDeLoad.value){
            is EstadosDeLoad.load -> {
                items(count = 5) {
                    ItemDelLoadTabelas()
            }}
            is EstadosDeLoad.Empty -> {}
            is EstadosDeLoad.Caregado<*> -> {
                val lista = estadosDeLoad.value as EstadosDeLoad.Caregado<List<DadosDaRequisicao>>
                items(items = lista.obj) {
                    ItemRwquisicao(it, windowSizeClass = windowSizeClass, acao = acao)
                }
            }
                is EstadosDeLoad.Erro -> {}

            }

        }



    }



@Composable
private fun ExibicaoDaRequisicao(modifier: Modifier=Modifier,acaoDeVoultar:()->Unit={},vm: ViewModelRequisicoes,windowSizeClass: WindowSizeClass){
        val requisicao=vm.fluxoDadosDeRequisicao.collectAsStateWithLifecycle(EstadosDeLoad.load)
        val estadoListaHistorico =vm.estadoListaHistorico.collectAsStateWithLifecycle(ListaHistorico.Lista)

        when(requisicao.value){
            is EstadosDeLoad.Empty -> {}
            is EstadosDeLoad.Caregado<*> -> {
                val requisicao =requisicao.value as EstadosDeLoad.Caregado<DadosDaRequisicao>
                Column(modifier = modifier.fillMaxWidth().padding(bottom = 70.dp)) {
                Box(modifier = Modifier.fillMaxWidth()){
                    if(!windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND))
                    IconButton({acaoDeVoultar()},modifier= Modifier.align(Alignment.CenterStart)) {
                    Icon(painterResource(R.drawable.baseline_arrow_drop_down_24),contentDescription = null,modifier=Modifier.size(25.dp))
                }
                    NumeroDeRequisicoes(requisicao.obj.requisicao.id,modifier= Modifier.align(Alignment.Center))
                 IconButton(onClick = {},modifier= Modifier.align(Alignment.CenterEnd)) {
                     Icon(painter = painterResource(R.drawable.recibo_2_24), contentDescription = null,modifier=Modifier.size(20.dp))
                 }


                }

                NomeCliente(requisicao.obj.cliente.nome, modifier = Modifier.padding(bottom = 5.dp))
                Observacao(requisicao.obj.requisicao.obs, modifier = Modifier.padding(bottom = 5.dp))
                Descricao(requisicao.obj.requisicao.desc, modifier = Modifier.padding(bottom = 5.dp))
                Custo(vm, modifier = Modifier.padding(bottom = 5.dp))
                Estado(requisicao.obj.estado.descricao)
                HorizontalDivider()
                Row(modifier = Modifier.padding(vertical = 10.dp, horizontal = 5.dp).align(Alignment.CenterHorizontally)) {
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

                }
                HorizontalDivider()
                Row(modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 5.dp)) {
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
                    is ListaHistorico.Lista->{ListaDeProdutosRequisitados(vm=vm, windowSize =windowSizeClass)}
                    is ListaHistorico.Historico->{ListaDeEstados(vm)}
                }
            }}
            is EstadosDeLoad.load -> {}
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
    val custo =vm.valorTotalRequisicao.collectAsStateWithLifecycle(EstadosDeLoad.load)
    when(custo.value){
        is EstadosDeLoad.load -> {}
        is EstadosDeLoad.Empty -> {}
        is EstadosDeLoad.Caregado<*> -> {
            val custo =custo.value as EstadosDeLoad.Caregado<Double>
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
    val estados =vm.fluxoHistoricoDeMudancas.collectAsStateWithLifecycle(EstadosDeLoad.load)
    when(estados.value){
        is EstadosDeLoad.load -> {
            Text("Caregando")
        }
        is EstadosDeLoad.Empty -> {
            Column{
                Text("O historico para essa requisicao esta vasio")
            }
        }
        is EstadosDeLoad.Caregado<*> -> {
            val estados =estados.value as EstadosDeLoad.Caregado<List<Mudanca>>
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
    val estadosDeLoad =vm.fluxoTodasAsRequisicoes.collectAsStateWithLifecycle(EstadosDeLoad.load)
    when(estadosDeLoad.value){
        is EstadosDeLoad.Empty -> {}
        is EstadosDeLoad.Erro -> {}
        is EstadosDeLoad.load -> {}
        is EstadosDeLoad.Caregado<*> -> {
            val lista =estadosDeLoad.value as EstadosDeLoad.Caregado<List<DadosDaRequisicao>>
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
                    Icon(painterResource(R.drawable.create_24),modifier= Modifier.size(20.dp),contentDescription = "")
                }

                IconButton ({},modifier= Modifier.size(30.dp).padding(5.dp)) {
                    Icon(painterResource(R.drawable.baseline_delete_24),modifier= Modifier.size(20.dp), contentDescription = "")
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

                Text(dados.data.formatData(), maxLines = 2,modifier=Modifier.width(250.dp), overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.padding(10.dp))
                Text(dados.idEstNovo.descricao, maxLines = 1, modifier = Modifier.width(90.dp), overflow = TextOverflow.Ellipsis)



            }


            HorizontalDivider(Modifier.fillMaxWidth())
        }
    }

}