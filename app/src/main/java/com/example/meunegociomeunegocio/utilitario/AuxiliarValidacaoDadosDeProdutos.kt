package com.example.meunegociomeunegocio.utilitario

import com.example.meunegociomeunegocio.repositorioRom.ProdutoServico

class AuxiliarValidacaoDadosDeProdutos {
    fun validarProduto(produto: ProdutoServico):ProdutoServico{
        val nome=validarNome(produto.nome)
        val preco=validarPreco(produto.preco.toString())
        val descricao=validarDescricao(produto.descrisao)
        return ProdutoServico(id=produto.id, nome = nome, preco = preco, descrisao = descricao, servico = produto.servico, ativo = produto.ativo)
    }
    private  fun validarNome(string: String):String{
        if(string.isBlank()) throw IllegalArgumentException("Nome nao pode estar vasio")
        return string
    }
    private fun validarPreco(string: String): Float{
        if(string.isBlank()) throw IllegalArgumentException("Preco nao pode estar vasio")
        if(string.equals("0.0")||string.equals("0.00")) throw IllegalArgumentException("Preco nao pode ter valor 0,0")
        return string.toFloat()

    }
    private fun validarDescricao(string: String):String{
        if(string.isBlank()) throw IllegalArgumentException("Descricao nao pode estar vasio")
        return string

    }
}