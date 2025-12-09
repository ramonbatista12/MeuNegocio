package com.example.meunegociomeunegocio.apresentacaoDeProdutos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.meunegociomeunegocio.R
import com.example.meunegociomeunegocio.repositorioRom.ProdutoServico
import com.example.meunegociomeunegocio.utilitario.EstadosDeLoadCaregamento
import com.example.meunegociomeunegocio.viewModel.ViewModelProdutos

@Composable
fun DetalhesDeProdutosCompate(vm:ViewModelProdutos,modifier: Modifier= Modifier){
    val produto =vm.produto.collectAsStateWithLifecycle(EstadosDeLoadCaregamento.load)
    Column(modifier = modifier) {
        when(produto.value){
            is EstadosDeLoadCaregamento.Caregado<*> -> {
                val produto =produto.value as EstadosDeLoadCaregamento.Caregado<ProdutoServico>
                IconButton({vm.mostraLista()}){
                    Icon(painterResource(R.drawable.baseline_arrow_drop_down_24),"mostraLista")
                }
                NomeProduto(produto.obj.nome,produto.obj.servico, Modifier.align(Alignment.CenterHorizontally).padding(horizontal = 10.dp, vertical = 5.dp))
                Preco(produto.obj.preco, Modifier.padding(horizontal = 10.dp, vertical = 5.dp))
                Descricao(produto.obj.descrisao,Modifier.padding(horizontal = 10.dp, vertical = 5.dp))
            }
            is EstadosDeLoadCaregamento.Erro -> {}
            is EstadosDeLoadCaregamento.load -> {
                LinearProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
            }
            is EstadosDeLoadCaregamento.Empty -> {}
        }

    }
}

@Composable
fun DetalhesDeProdutosEspandido(vm:ViewModelProdutos,modifier: Modifier= Modifier){
    val produto =vm.produto.collectAsStateWithLifecycle(EstadosDeLoadCaregamento.load)
    Column(modifier = modifier) {
        when(produto.value){
            is EstadosDeLoadCaregamento.Caregado<*> -> {
                val produto =produto.value as EstadosDeLoadCaregamento.Caregado<ProdutoServico>

                NomeProduto(produto.obj.nome,produto.obj.servico, Modifier.align(Alignment.CenterHorizontally).padding(horizontal = 10.dp, vertical = 5.dp))
                Preco(produto.obj.preco, Modifier.padding(horizontal = 10.dp, vertical = 5.dp))
                Descricao(produto.obj.descrisao,Modifier.padding(horizontal = 10.dp, vertical = 5.dp))
            }
            is EstadosDeLoadCaregamento.Erro -> {}
            is EstadosDeLoadCaregamento.load -> {
                LinearProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
            }
            is EstadosDeLoadCaregamento.Empty -> {}
        }

    }
}


@Composable
private fun NomeProduto(nome:String,servico:Boolean,modifier: Modifier= Modifier){

    Text("${if(servico)"Serviço" else "Produto"} : $nome ", fontSize = 20.sp ,modifier = modifier)
}

@Composable
private fun Descricao(descricao:String,modifier: Modifier= Modifier){
    Text("Descricao : $descricao",modifier=modifier)
}
@Composable
private fun Preco(p: Float,modifier: Modifier= Modifier){
    Text("Preco : ${ p.toString().formatarPreco() }", modifier = modifier)
}