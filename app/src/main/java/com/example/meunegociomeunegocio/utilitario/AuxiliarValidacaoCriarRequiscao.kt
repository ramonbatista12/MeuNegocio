package com.example.meunegociomeunegocio.utilitario

import com.example.meunegociomeunegocio.viewModel.ProdutoSelecionado

class AuxiliarValidacaoCriarRequiscao {
    fun validarListaDeProdutos(lista: List<ProdutoSelecionado>):List<ProdutoSelecionado>{
        if (lista.isEmpty()) throw IllegalArgumentException("A lista de produtos/servicos esta vasia")
        return lista
    }
    fun validarSelecaoClientes( cliente: Pair<Int,String>?): Int{
        if (cliente==null) throw IllegalArgumentException("Nenhum cliente selecionado")
        return cliente.first
    }

}