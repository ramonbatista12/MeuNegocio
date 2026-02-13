package com.example.meunegociomeunegocio.viewModel


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.meunegociomeunegocio.cadstroDeRequisicao.SelecaoDeClientes
import com.example.meunegociomeunegocio.cadstroDeRequisicao.SelecaoDeProdutos
import com.example.meunegociomeunegocio.repositorioRom.Cliente
import com.example.meunegociomeunegocio.repositorioRom.Endereco
import com.example.meunegociomeunegocio.repositorioRom.Repositorio
import com.example.meunegociomeunegocio.repositorioRom.Telefone
import com.example.meunegociomeunegocio.utilitario.AuxiliarValidacaoDadosDeClientes
import com.example.meunegociomeunegocio.utilitario.EstadosDeLoadCaregamento
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
@HiltViewModel(assistedFactory = ViewModelCadastroDeCliente.Fabrica::class)
class ViewModelCadastroDeCliente @AssistedInject constructor(private val repositorio: Repositorio,@Assisted val id1: Int?) : ViewModel() {
    val liveData = MutableLiveData<String>()
     val Tag ="ViewModelCadastroDeCliente"
     val coroutineScope=viewModelScope
     val snackbarHostState = SnackbarHostState()
     private val _id = MutableStateFlow<Int>(0)
     private val estagios = GerenciadorDeEstagios()
     private val validador =AuxiliarValidacaoDadosDeClientes()
     private val _loadCliente=MutableStateFlow<EstadosDeLoadCaregamento>(EstadosDeLoadCaregamento.Empty)
     val clienteAdicionado= MutableStateFlow<Boolean>(false)
     val enderecoAdicionado= MutableStateFlow<Boolean>(false)
     val telefoneAdicionado= MutableStateFlow<Boolean>(false)
     val cliente= MutableStateFlow<Cliente?>(null)
     val endereco= MutableStateFlow<Endereco?>(null)
     val telefone= MutableStateFlow<Telefone?>(null)
     val id =_id
     val estagio=estagios.fluxoDeEstagios
     val LoadCliente=_loadCliente
     init {
         if(id1!=null)
         coroutineScope.launch(Dispatchers.IO) {
             _loadCliente.emit(EstadosDeLoadCaregamento.load)
             Log.d(Tag,"id1 $id1")
             delay(1000)
             _id.value=id1!!
             val clineteAusiliar = repositorio.clientesPorId(id.value.toInt()) .first()
             val enderecoAuxiliar =repositorio.fluxoDeEnderecoPorId(id.value.toInt()).first()
             val telefoneAuxiliar=repositorio .fluxoDeTelefonePorId(id.value.toInt()).first()
             cliente.emit(clineteAusiliar)
             endereco.emit(enderecoAuxiliar)
             telefone.emit(telefoneAuxiliar)
             clienteAdicionado.emit(true)
             enderecoAdicionado.emit(true)
             telefoneAdicionado.emit(true)
             _loadCliente.emit(EstadosDeLoadCaregamento.Caregado(Unit))

         }
     }
     suspend fun adicionarCliente(cliente: Cliente?) {
                val idAux= repositorio.inserirCliente( validador.validarCliente(cliente))
                _id.emit(idAux.toInt())}

     suspend fun adicionarEndereco(endereco: Endereco?){
        repositorio.inserirEndereco(validador.validarEndereco(endereco,_id.value.toInt()))}

     suspend fun adicionarTelefone(telefone: Telefone?){
         Log.d(Tag,"adicionarTelefone foi chamado")
      repositorio.inserirTelefone(validador.validarTelefone(telefone,_id.value.toInt()))}

    suspend fun salvar(callback:()->Unit){
            if(!clienteAdicionado.value)
             try {
                 adicionarCliente(cliente.value)
                 clienteAdicionado.emit(true)
             }catch (e: Exception){
                 coroutineScope.launch { snackbarHostState.showSnackbar(e.message.toString()) }

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
                telefoneAdicionado.emit(true)
            }catch (e: Exception){
                coroutineScope.launch {snackbarHostState.showSnackbar(e.message.toString())}
                estagios.irAoEstagio(EstagiosDeCadastroClientes.Telefone)
                telefoneAdicionado.emit(false)
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

    @AssistedFactory
    interface Fabrica {
        fun criar(id: Int?): ViewModelCadastroDeCliente
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