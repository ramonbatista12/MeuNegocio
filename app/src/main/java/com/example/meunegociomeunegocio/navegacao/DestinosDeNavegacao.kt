package com.example.meunegociomeunegocio.navegacao

import kotlinx.serialization.Serializable
/**
 * descreve os posiveis destinos de navegacao
 * disponiveis na aplicacao
 * */
@Serializable
sealed class DestinosDeNavegacao{
    @Serializable
    object Produtos: DestinosDeNavegacao()
    @Serializable
    object Clientes: DestinosDeNavegacao()
    @Serializable
    object Requisicoes: DestinosDeNavegacao()
    @Serializable
    object AdicaoDeCleintes: DestinosDeNavegacao()
    @Serializable
    /**
     * descreve os dialogogos gerenciados por Navigra
     * eles tabem sao um sub tipo de DestinoDeNavegacao
    * */
    sealed class Dialogos: DestinosDeNavegacao(){
        @Serializable
        object NovoProduto: Dialogos()
        @Serializable
        object NovoCliente: Dialogos()
        @Serializable
        object NovaRequisicao: Dialogos()
        @Serializable
        object EditarProduto: Dialogos()
        @Serializable
        object EditarCliente: Dialogos()
    }


}