package com.example.meunegociomeunegocio
import com.example.meunegociomeunegocio.repositorioRom.Telefone
import com.example.meunegociomeunegocio.viewModel.AuxiliarValidacaoDadosDeClientes
import kotlinx.coroutines.runBlocking
import org.junit.Test
class TesteValidacaoTelefone {
    val aux = AuxiliarValidacaoDadosDeClientes()
    @Test
    fun teste()= runBlocking{
        val invalidos =listOf<Telefone>(
                Telefone(id = 4, idCli = 1, ddd = "11", numero = "12345"),       // Número muito curto
                Telefone(id = 5, idCli = 1, ddd = "A1", numero = "988776655"),  // DDD com letra
                Telefone(id = 6, idCli = 1, ddd = "1", numero = "988776655")    // DDD muito curto
        )

        invalidos.forEach {
            try {
                aux.validarTelefone(it,0)
            }catch (e: Exception){
                println(e.message.toString())
            }
        }
    }
}