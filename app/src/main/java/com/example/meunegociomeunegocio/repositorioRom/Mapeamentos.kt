package com.example.meunegociomeunegocio.repositorioRom

data class Cliente(val id: Int, val nome: String, val cpf: String?, val cnpj: String?)
data class Telefone(val id: Int,val idCli:Int,val ddd:String,val numero: String)
data class Endereco(val id: Int,val idCli:Int,val cidade:String,val estado:String,val bairro:String,val complemento:String,val rua:String,val numero:String,val cep: String)
data class DadosDeClientes(val cliente: Cliente, val telefones: List<Telefone>, val enderecos: List<Endereco>)
data class Estado(val id: Int, val descricao: String)
data class Requisicao(val id: Int, val idCli: Int, val idEs: Int, val desc: String, val obs: String)
data class ProdutoServico(val id: Int, val servico: Boolean, val nome: String, val descrisao: String, val preco: Float,val ativo:Boolean)
data class DadosDaRequisicao(val requisicao: Requisicao, val estado: Estado, val cliente: Cliente)
data class EstadoDaRequisicao(val id: Int, val estadoAntigo: Estado?, val estadoNovo: Estado )
data class ProdutoProRequisicao(val Requisicao: Requisicao, val produtos: List<ProdutoServico>)
data class Mudanca(val id: Int, val idReq: Int, val idEstAntigo: Estado?, val idEstNovo: Estado)
