package com.example.meunegociomeunegocio.cadstroDeRequisicao

sealed class SelecaoDeClientes{
    object Nome: SelecaoDeClientes()
    object ListaDeSelecao: SelecaoDeClientes()
}

sealed class SelecaoDeProdutos{
    object ListaDeProdutos: SelecaoDeProdutos()
    object ProdutosSelecionados: SelecaoDeProdutos()

}