package com.example.meunegociomeunegocio.apresentacaoDeProdutos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import com.example.meunegociomeunegocio.repositorioRom.ProdutoServico
import com.example.meunegociomeunegocio.viewModel.ViewModelProdutos
import com.example.meunegociomeunegocio.utilitario.EstadosDeLoadCaregamento
import com.example.meunegociomeunegocio.viewModel.TealasDeProduto

@Composable
fun ApresentacaoProdutos(modifier: Modifier=Modifier,
                         windowSize: WindowSizeClass,
                         vm: ViewModelProdutos,acaoDeEdicaoDeProdutos:(Int)-> Unit ){
      when{
          windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) -> {
              Row(modifier = modifier) {
                  ListaDeProdutosExpandido(Modifier,vm,windowSize,acaoDeEdicaoDeProdutos)
                  VerticalDivider(Modifier.padding(horizontal = 5.dp))
                  DetalhesDeProdutosEspandido(vm,modifier)
                }
          }
          else -> {
              val telas=vm.telasDeProdutos.collectAsStateWithLifecycle()
              when(telas.value){
                  is TealasDeProduto.Lista -> {
                      ListaDeProdutos(modifier.padding(bottom = 70.dp),vm,windowSize,acaoDeEdicaoDeProdutos)
                  }
                  is TealasDeProduto.Descricao -> {
                      DetalhesDeProdutosCompate(vm,modifier)
                  }
              }


          }

      }

     MostrarProduto(vm)

           }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MostrarProduto(vm: ViewModelProdutos){
    val mostrarProduto =vm.mostraProduto.collectAsStateWithLifecycle()
    val produto =vm.produto.collectAsStateWithLifecycle(EstadosDeLoadCaregamento.load)
    val scrolState = rememberScrollState()
    if(mostrarProduto.value){
        ModalBottomSheet(onDismissRequest = { vm.ocultar() }, Modifier.height(700.dp)) {
            Column(modifier = Modifier.padding(all = 5.dp).verticalScroll(state = scrolState)) {
                when(produto.value){
                    is EstadosDeLoadCaregamento.load -> {
                        Text("Caregando.....", Modifier.padding(bottom = 5.dp))
                    }
                    is EstadosDeLoadCaregamento.Caregado<*> -> {
                        val produto = produto.value as EstadosDeLoadCaregamento.Caregado<ProdutoServico>

                            Text(text = " ${produto.obj.nome} ", Modifier.padding(bottom = 5.dp).align(Alignment.CenterHorizontally), fontSize = 20.sp)
                            Text("Descricao : ${produto.obj.descrisao} ", Modifier.padding(bottom = 5.dp))
                            Text("Tipo : ${if(produto.obj.servico) "Servico" else "Produto"} ", Modifier.padding(bottom = 5.dp))
                            Text("Preco $ : ${vm.formatarPreco(produto.obj.preco.toDouble())} ", Modifier.padding(bottom = 5.dp))


                    }
                    is EstadosDeLoadCaregamento.Empty -> {}
                    else -> {}
                }
            }

        }


    }
}