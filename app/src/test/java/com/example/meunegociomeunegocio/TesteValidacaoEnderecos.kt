package com.example.meunegociomeunegocio

import com.example.meunegociomeunegocio.repositorioRom.Endereco
import com.example.meunegociomeunegocio.viewModel.AuxiliarValidacaoDadosDeClientes
import kotlinx.coroutines.runBlocking
import org.junit.Test

class TesteValidacaoEnderecos {
    @Test
    fun teste()= runBlocking{
        val aux = AuxiliarValidacaoDadosDeClientes()
        val enderecosErrados = listOf(
            // 1. Rua em branco
            Endereco(id = 0, idCli = 1, cep = "50000-123", bairro = "Bairro Válido", cidade = "Recife", estado = "PE", rua = "   ", complemento = "casa" ,numero = "123"),

            // 2. Número com letras
            Endereco(id = 0, idCli = 1, cep = "50000-123", bairro = "Bairro Válido", cidade = "Recife", estado = "PE", rua = "Rua das Flores",complemento = " casa" , numero = "123-A"),

            // 3. CEP com formato inválido
            Endereco(id = 0, idCli = 1, cep = "50000123", bairro = "Bairro Válido", cidade = "Recife", estado = "PE", rua = "Rua das Flores",complemento = "casa"  ,numero = "123"),

            // 4. Cidade em branco
            Endereco(id = 0, idCli = 1, cep = "50000-123", bairro = "Bairro Válido", cidade = "", estado = "PE", rua = "Rua das Flores",complemento = "casa" , numero = "123"),

            // 5. CEP com tamanho incorreto
            Endereco(id = 0, idCli = 1, cep = "5000-123", bairro = "Bairro Válido", cidade = "Recife", estado = "PE", rua = "Rua das Flores",complemento = "casa"  ,numero = "123")
        )
        enderecosErrados.forEach {
            try {
                aux.validarEndereco(it)
            }catch (e: Exception){
                println(e.message)
            }
        }
    }
}