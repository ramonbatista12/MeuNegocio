package com.example.meunegociomeunegocio.utilitario

import com.example.meunegociomeunegocio.repositorioRom.Cliente
import com.example.meunegociomeunegocio.repositorioRom.Endereco
import com.example.meunegociomeunegocio.repositorioRom.Telefone

class AuxiliarValidacaoDadosDeClientes {
    fun  validarCliente(cliente: Cliente?): Cliente{
        if(cliente==null) throw IllegalArgumentException("Cliente nao pode estar vasio")
        val nome= validarNome(cliente.nome)
        val cpf =  validarCPF(cliente.cpf)
        val cnpj =  validarCNPJ(cliente.cnpj)
        return Cliente(0,nome=nome,cpf =cpf, cnpj = cnpj)
    }
    fun validarEndereco(endereco: Endereco?,id: Int): Endereco{
        if(endereco==null) throw IllegalArgumentException("Endereco esta vasio e e opcional e nao sera adicionado")
        val rua =validarRua(endereco.rua)
        val numero =validarNumero(endereco.numero)
        val cidade=validarCidade(endereco.cidade)
        val estado =validarEstado(endereco.estado)
        val bairo=validarBairo(endereco.bairro)
        val cep =validarCep(endereco.cep)
        return Endereco(0, idCli = id, cep = cep, bairro = bairo, cidade = cidade, estado = estado, rua = rua, numero = numero, complemento = endereco.complemento)
    }

    private fun validarCNPJ(string: String?): String? {
        if(string==null) return string
        if(string.isBlank()) return null
        if(string.length!=18) throw IllegalArgumentException("CNPJ invalido pois nao conten o numero coreto de digitos")
        if(!string.matches(Regex("\\d{2}\\.\\d{3}\\s.\\d{3}/\\d{4}-\\d{2}"))) throw IllegalArgumentException("CNPJ invalido pois nao esta no formato correto")
        return string
    }
    fun validarTelefone(telefone: Telefone?,id: Int): Telefone{
        if(telefone==null) throw IllegalArgumentException("Telefone nao pode estar vasio")
        val ddd=validarDDD(telefone.ddd)
        val celular=validarCelular(telefone.numero)
        return Telefone(0, idCli = id, ddd = ddd, numero = celular)

    }

    private fun validarCPF(string: String?):String? {
        if(string==null) return string
        if(string.isBlank()) return null
        if(string.length!=14) throw IllegalArgumentException("CPF invalido pois nao conten o numero coreto de digitos  ")
        if(!string.matches(Regex("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}"))) throw IllegalArgumentException("CPF invalido pois nao esta no formato correto")
        return string
    }

    private  fun validarNome(nome: String): String{
        if(nome.isBlank()) throw IllegalArgumentException("Nome nao pode estar vasio")
        return nome
    }
    private  fun validarRua(string: String): String{
        if(string.isBlank()) throw IllegalArgumentException("Rua nao pode estar vasia")
        return string
    }
    private  fun validarNumero(string: String): String{
        if(string.isBlank()) throw IllegalArgumentException("Numero nao pode estar vasio")
        if(string.contains(Regex("[a-zA-Z]"))) throw IllegalArgumentException("Numero nao pode conter letras caso seja nessesario adicione um complemento ")
        return string
    }


    private  fun validarEstado(string: String): String{
        if(string.isBlank()) throw IllegalArgumentException("Estado nao pode estar vasio")
        return string
    }
    private  fun validarCidade(string: String): String{
        if(string.isBlank()) throw IllegalArgumentException("Cidade nao pode estar vasia")
        return string
    }
    private  fun validarBairo(string: String): String{
        if(string.isBlank()) throw IllegalArgumentException("Bairro nao pode estar vasio")
        return string
    }
    private  fun validarCep(string:String): String{
        if (string.isBlank()) throw IllegalArgumentException("Cep nao pode ser vasio")
        if(!string.matches(Regex("\\d{5}-\\d{3}"))) throw IllegalArgumentException("Cep nao posui o formato valido ")
        return string
    }

    private  fun validarDDD(string: String): String{
        if(string.isBlank()) throw IllegalArgumentException("DDD nao pode estar vasio")
        if (!string.matches(Regex("\\d{2}"))) throw IllegalArgumentException("DDD nao posui o formato valido e nesesario que seja dois digitos")
        return string
    }
    private  fun validarCelular(string: String):String{
        if(string.isBlank()) throw IllegalArgumentException("Celular nao pode estar vasio")
        if (!string.matches(Regex("\\d{5}-\\d{4}"))) throw IllegalArgumentException("Numero nao posui o formato valido e nesesario que tenha 9 digitos")
        return string
    }


}