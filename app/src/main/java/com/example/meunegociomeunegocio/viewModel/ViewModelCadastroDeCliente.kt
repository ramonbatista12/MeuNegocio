package com.example.meunegociomeunegocio.viewModel


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meunegociomeunegocio.repositorioRom.Cliente
import com.example.meunegociomeunegocio.repositorioRom.Endereco
import com.example.meunegociomeunegocio.repositorioRom.Repositorio
import com.example.meunegociomeunegocio.repositorioRom.Telefone
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Delay
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
@HiltViewModel
class ViewModelCadastroDeCliente@Inject constructor(private val repositorio: Repositorio) : ViewModel() {
     val Tag ="ViewModelCadastroDeCliente"
     val coroutineScope=viewModelScope
     val snackbarHostState = SnackbarHostState()
     private val _id = MutableStateFlow<Long>(0)
     private val estagios = GerenciadorDeEstagios()
     val clienteAdicionado= MutableStateFlow<Boolean>(false)
     val enderecoAdicionado= MutableStateFlow<Boolean>(false)
     val telefoneAdicionado= MutableStateFlow<Boolean>(false)
     val cliente= MutableStateFlow<Cliente?>(null)
     val endereco= MutableStateFlow<Endereco?>(null)
     val telefone= MutableStateFlow<Telefone?>(null)
     val id =_id
     val estagio=estagios.fluxoDeEstagios
     suspend fun adicionarCliente(cliente: Cliente?) {


              val idAux= repositorio.inserirCliente( AuxiliarValidacaoDadosDeClientes().validarCliente(cliente))
                _id.emit(idAux)






    }
     suspend fun adicionarEndereco(endereco: Endereco?){
        repositorio.inserirEndereco(AuxiliarValidacaoDadosDeClientes().validarEndereco(endereco,_id.value.toInt()))}
     suspend fun adicionarTelefone(telefone: Telefone?){
         Log.d(Tag,"adicionarTelefone foi chamado")
      repositorio.inserirTelefone(AuxiliarValidacaoDadosDeClientes().validarTelefone(telefone,_id.value.toInt()))}
    suspend fun salvar(callback:()->Unit){
            if(!clienteAdicionado.value)
             try {
                 adicionarCliente(cliente.value)
                 clienteAdicionado.emit(true)
             }catch (e: Exception){
                 coroutineScope.launch { snackbarHostState.showSnackbar(e.message.toString()) }
                 delay(1000)
                 estagios.irAoEstagio(EstagiosDeCadastroClientes.Nome)
                 clienteAdicionado.emit(false)
                 return
             }
        if(!enderecoAdicionado.value)
            try {
                adicionarEndereco(endereco.value)
                enderecoAdicionado.emit(true)
            }catch (e: Exception){
                coroutineScope.launch {snackbarHostState.showSnackbar(e.message.toString())  }
                estagios.irAoEstagio(EstagiosDeCadastroClientes.Endereco)
                enderecoAdicionado.emit(false)
                return
            }
        if(!telefoneAdicionado.value)
            try {
                adicionarTelefone(telefone.value)
                enderecoAdicionado.emit(true)
            }catch (e: Exception){
                coroutineScope.launch {snackbarHostState.showSnackbar(e.message.toString())}
                estagios.irAoEstagio(EstagiosDeCadastroClientes.Telefone)
                enderecoAdicionado.emit(false)
                return
            }
            coroutineScope.launch {snackbarHostState.showSnackbar("Dados foram salvos no banco de dados")
                                   callback() }




    }
     fun  prosimoEstagioDeCadastro(){
        coroutineScope.launch {
            estagios.proximo()
        }
    }
     fun  estagioDeCadastroAnterior(){
        coroutineScope.launch {
            estagios.anterior()
        }
    }
     fun guardaClienteCriado(c: Cliente){
        coroutineScope.launch {
            cliente.emit(c)
        }
    }
     fun guardaTelefoneCriado(t: Telefone){
        coroutineScope.launch {
            telefone.emit(t)
        }
    }
     fun guardarEnderecoCriado(e: Endereco){
        coroutineScope.launch {
            endereco.emit(e)
        }
    }
}

class AuxiliarValidacaoDadosDeClientes{
    suspend fun  validarCliente(cliente: Cliente?): Cliente{
       if(cliente==null) throw IllegalArgumentException("Cliente nao pode estar vasio")
       val nome= validarNome(cliente.nome)
       val cpf =  validarCPF(cliente.cpf)
       val cnpj =  validarCNPJ(cliente.cnpj)
       return Cliente(0,nome=nome,cpf =cpf, cnpj = cnpj)
    }
    suspend fun validarEndereco(endereco: Endereco?,id: Int): Endereco{
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
        if(!string.matches(Regex("\\d{2}.\\d{3}.\\d{3}/\\d{4}-\\d{2}"))) throw IllegalArgumentException("CNPJ invalido pois nao esta no formato correto")
        return string
    }
    suspend fun validarTelefone(telefone: Telefone?,id: Int): Telefone{
        if(telefone==null) throw IllegalArgumentException("Telefone nao pode estar vasio")
        val ddd=validarDDD(telefone.ddd)
        val celular=validarCelular(telefone.numero)
        return Telefone(0, idCli = id, ddd = ddd, numero = celular)

    }

    private fun validarCPF(string: String?):String? {
        if(string==null) return string
        if(string.isBlank()) return null
        if(string.length!=14) throw IllegalArgumentException("CPF invalido pois nao conten o numero coreto de digitos  ")
        if(!string.matches(Regex("\\d{3}.\\d{3}.\\d{3}-\\d{2}"))) throw IllegalArgumentException("CPF invalido pois nao esta no formato correto")
        return string
    }

    private suspend fun validarNome(nome: String): String{
       if(nome.isBlank()) throw IllegalArgumentException("Nome nao pode estar vasio")
       return nome
    }
    private suspend fun validarRua(string: String): String{
        if(string.isBlank()) throw IllegalArgumentException("Rua nao pode estar vasia")
    return string
    }
    private suspend fun validarNumero(string: String): String{
        if(string.isBlank()) throw IllegalArgumentException("Numero nao pode estar vasio")
        if(string.contains(Regex("[a-zA-Z]"))) throw IllegalArgumentException("Numero nao pode conter letras caso seja nessesario adicione um complemento ")
      return string
    }


    private suspend fun validarEstado(string: String): String{
        if(string.isBlank()) throw IllegalArgumentException("Estado nao pode estar vasio")
        return string
    }
    private suspend fun validarCidade(string: String): String{
         if(string.isBlank()) throw IllegalArgumentException("Cidade nao pode estar vasia")
        return string
    }
    private suspend fun validarBairo(string: String): String{
        if(string.isBlank()) throw IllegalArgumentException("Bairro nao pode estar vasio")
        return string
    }
    private suspend fun validarCep(string:String): String{
        if (string.isBlank()) throw IllegalArgumentException("Cep nao pode ser vasio")
        if(!string.matches(Regex("\\d{5}-\\d{3}"))) throw IllegalArgumentException("Cep nao posui o formato valido ")
        return string
    }

    private suspend fun validarDDD(string: String): String{
        if(string.isBlank()) throw IllegalArgumentException("DDD nao pode estar vasio")
        if (!string.matches(Regex("\\d{2}"))) throw IllegalArgumentException("DDD nao posui o formato valido e nesesario que seja dois digitos")
        return string
    }
    private suspend fun validarCelular(string: String):String{
        if(string.isBlank()) throw IllegalArgumentException("Celular nao pode estar vasio")
        if (!string.matches(Regex("\\d{5}-\\d{4}"))) throw IllegalArgumentException("Numero nao posui o formato valido e nesesario que tenha 9 digitos")
        return string
    }


}
class GerenciadorDeEstagios{
    val fluxoDeEstagios=MutableStateFlow<EstagiosDeCadastroClientes>(EstagiosDeCadastroClientes.Nome)
     suspend fun proximo(){
         val atual =fluxoDeEstagios.value
         val prosimo =atual.proximo()
         if(prosimo!=null)
             fluxoDeEstagios.emit(prosimo as EstagiosDeCadastroClientes)
     }
    @SuppressLint("SuspiciousIndentation")
    suspend fun  anterior(){
        val atual =fluxoDeEstagios.value
        val anterior =atual.anterio()
        if(anterior!=null)
        fluxoDeEstagios.emit(anterior as EstagiosDeCadastroClientes)
    }
    suspend fun irAoEstagio(e: EstagiosDeCadastroClientes){
        fluxoDeEstagios.emit(e)
    }
}
sealed class EstagiosDeCadastroClientes: Estagio{
    object Nome: EstagiosDeCadastroClientes(){
        override fun proximo(): Estagio? {
            return Endereco
        }
        override fun anterio(): Estagio? {
            return null
        }

    }
    object Endereco: EstagiosDeCadastroClientes(){
        override fun proximo(): Estagio? {
            return Telefone
        }
        override fun anterio(): Estagio? {
            return Nome
        }
    }
    object Telefone: EstagiosDeCadastroClientes(){
        override fun proximo(): Estagio? {
            return null
        }

        override fun anterio(): Estagio? {
            return Endereco
        }
    }


}

interface Estagio{
    public fun proximo(): Estagio?
    public fun anterio(): Estagio?
}