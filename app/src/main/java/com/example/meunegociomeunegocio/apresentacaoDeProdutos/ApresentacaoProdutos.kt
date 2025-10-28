package com.example.meunegociomeunegocio.apresentacaoDeProdutos

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowSizeClass
import com.example.meunegociomeunegocio.viewModel.ViewModelProdutos

@Composable
fun ApresentacaoProdutos(modifier: Modifier=Modifier,
                         windowSize: WindowSizeClass,
                         vm: ViewModelProdutos ){

     ListaDeProdutosRequisitados(modifier,vm,windowSize)


           }


