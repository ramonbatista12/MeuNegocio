package com.example.meunegociomeunegocio
import com.example.meunegociomeunegocio.repositorioRom.Cliente
import com.example.meunegociomeunegocio.viewModel.AuxiliarValidacaoDadosDeClientes
import kotlinx.coroutines.runBlocking
import org.junit.Assert.fail
import org.junit.Test
class TesteValidacaoClinete {
    @Test
    fun teste()= runBlocking {
        val aux = AuxiliarValidacaoDadosDeClientes()
        val clientesErrados = listOf(
            // 1. Nome em branco
            Cliente(id = 0, nome = "   ", cpf = "123.456.789-00", cnpj = null),

            // 2. CPF com tamanho incorreto
            Cliente(id = 0, nome = "Empresa Válida", cpf = "123.456.789-0", cnpj = null),

            // 3. CNPJ com formato incorreto
            Cliente(id = 0, nome = "Loja de Teste", cpf = null, cnpj = "12.345678/0001-99"),

            // 4. CPF com letras
            Cliente(id = 0, nome = "Pessoa Física", cpf = "123.abc.789-00", cnpj = null),

            // 5. CNPJ com tamanho incorreto
            Cliente(id = 0, nome = "Comércio XYZ", cpf = null, cnpj = "12.345.678/0001-9")
        )
        for(c in clientesErrados) {
        try {
            aux.validarCliente(c)
            fail("A validação deveria ter falhado para o cliente '${c.nome}', mas foi aceita.")
        }catch (e: Exception){
            println(e.message.toString())
        }}
    }}
