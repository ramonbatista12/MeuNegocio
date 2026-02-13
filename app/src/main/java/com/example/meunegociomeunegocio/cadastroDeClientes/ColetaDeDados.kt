package com.example.meunegociomeunegocio.cadastroDeClientes

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.EntityDeleteOrUpdateAdapter
import com.example.meunegociomeunegocio.formatadoresDeTesto.FormatoCep
import com.example.meunegociomeunegocio.formatadoresDeTesto.FormatoCnpj
import com.example.meunegociomeunegocio.formatadoresDeTesto.FormatoCpf
import com.example.meunegociomeunegocio.formatadoresDeTesto.FormatoTelefone
import com.example.meunegociomeunegocio.repositorioRom.Cliente
import com.example.meunegociomeunegocio.repositorioRom.Endereco
import com.example.meunegociomeunegocio.repositorioRom.Telefone
import com.example.meunegociomeunegocio.utilitario.EstadosDeLoadCaregamento
import com.example.meunegociomeunegocio.viewModel.EstagiosDeCadastroClientes
import com.example.meunegociomeunegocio.viewModel.ViewModelCadastroDeCliente
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CadastroCompat(vm: ViewModelCadastroDeCliente,acaoDeVoltar:()->Unit){
     val estagios=vm.estagio.collectAsStateWithLifecycle()
       Column(horizontalAlignment = Alignment.CenterHorizontally) {
        when(estagios.value){
            is EstagiosDeCadastroClientes.Nome -> {
                CadastraCliente(vm=vm, acaoDevoutar =  acaoDeVoltar)
            }
            is EstagiosDeCadastroClientes.Endereco -> {
                CadastraEndereco(vm)
            }
            is EstagiosDeCadastroClientes.Telefone -> {
                CadastraTelefone(vm,acaoDevoutar = acaoDeVoltar)
            }
        }
           SnackbarHost(vm.snackbarHostState)
}




}

@Composable
private fun  CadastraCliente(vm: ViewModelCadastroDeCliente,acaoDevoutar:()->Unit={},longDelay:Long=100){
    val cliente =vm.cliente.collectAsStateWithLifecycle()
    val nome= rememberTextFieldState()
    val cpf=rememberTextFieldState()
    val cnpj=rememberTextFieldState()
    val  focoRequestiClinete = remember { FocusRequester() }
    val loadCliente =vm.LoadCliente.collectAsStateWithLifecycle()
    LaunchedEffect(cliente.value) {
        if(cliente.value!=null){
            val cliente = cliente.value
            nome.setTextAndPlaceCursorAtEnd(cliente!!.nome)
            cpf.setTextAndPlaceCursorAtEnd(cliente.cpf?:"")
            cnpj.setTextAndPlaceCursorAtEnd(cliente.cnpj?:"")
        }

    }
    LaunchedEffect(loadCliente.value) {
        when(loadCliente.value){
            is EstadosDeLoadCaregamento.Caregado<*>-> {
                delay(longDelay)
                focoRequestiClinete.requestFocus()
            }
            else -> {}
        }
    }
    Column(modifier =Modifier.fillMaxWidth().padding(bottom = 70.dp,top = 10.dp, start = 5.dp, end = 5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Cadastro de cliente", fontSize = 25.sp, modifier = Modifier.padding(bottom = 10.dp,top=20.dp))
        Spacer(Modifier.padding(3.dp))
        OutlinedTextField(state = nome, modifier = Modifier.padding( horizontal = 5.dp).fillMaxWidth(0.98f).focusRequester(focoRequestiClinete), label = {Text("Nome")})
        Spacer(Modifier.padding(3.dp))
        OutlinedTextField(state = cpf, modifier = Modifier.padding( horizontal = 5.dp).fillMaxWidth(0.98f), label = {Text("Cpf")},
                          inputTransformation = FormatoCpf() )
        Spacer(Modifier.padding(3.dp))
        OutlinedTextField(state = cnpj, modifier = Modifier.padding( horizontal = 5.dp).fillMaxWidth(0.98f), label = {Text("Cnpj")}, inputTransformation = FormatoCnpj())

        Box(Modifier.fillMaxWidth()) {
            Button(onClick = acaoDevoutar, modifier = Modifier.align(Alignment.CenterStart).padding(20.dp)
            ) {
                Text("Cancelar")
            }
            Button(onClick = {
                vm.guardaClienteCriado(Cliente(0,nome.text.toString(),cpf.text.toString(),cnpj.text.toString()))
                vm.prosimoEstagioDeCadastro()}, modifier = Modifier.align(Alignment.CenterEnd).padding(20.dp)
            ) {
                Text("Endereco")
            }
        }

    }

}

@Composable
private fun  CadastraTelefone(vm: ViewModelCadastroDeCliente,acaoDevoutar:()->Unit={},longDelay:Long=100){
    val telefone= rememberTextFieldState()
    val telefoneSalvo =vm.telefone.collectAsStateWithLifecycle()
    val coroutineScope= rememberCoroutineScope()
    val focusRequesterTelefone =remember { FocusRequester() }
    val loadCliente =vm.LoadCliente.collectAsStateWithLifecycle()
    LaunchedEffect(telefoneSalvo.value) {
        if(telefoneSalvo.value!=null) {
            telefone.setTextAndPlaceCursorAtEnd(telefoneSalvo.value!!.ddd+" "+telefoneSalvo.value!!.numero)
        }
    }
    LaunchedEffect(loadCliente.value) {
        when(loadCliente.value){
            is EstadosDeLoadCaregamento.Caregado<*> -> {
                delay(longDelay)
                focusRequesterTelefone.requestFocus()}
            else -> {}
        }
    }
    Column(modifier =Modifier.fillMaxWidth().padding(bottom = 70.dp,top = 10.dp, start = 5.dp, end = 5.dp).focusRequester(focusRequesterTelefone), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Cadastro de telefone", fontSize = 25.sp, modifier = Modifier.padding(bottom = 10.dp,top=20.dp))

        OutlinedTextField(state = telefone, label = {Text("Numero do telefone")}, modifier = Modifier.fillMaxWidth(0.98f),
                          inputTransformation = FormatoTelefone())
        Spacer(Modifier.padding(5.dp))



        Box(Modifier.fillMaxWidth().padding(bottom = 20.dp, start = 20.dp, end = 20.dp)) {
            Button(onClick = {vm.estagioDeCadastroAnterior()}, modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text("Endereco")
            }

            Button(onClick = {
                coroutineScope.launch {
                    val split=telefone.text.toString().split(" ")
                    vm.guardaTelefoneCriado(if(split.size==1)Telefone(0,0,"","") else Telefone(0,0,split[0],split[1]))
                    vm.salvar({ acaoDevoutar() })
                }


                                     }, modifier = Modifier.align(Alignment.CenterEnd).padding(20.dp)
            ) {
                Text("salvar")
            }
        }
    }

}

@Composable
private fun  CadastraEndereco(vm: ViewModelCadastroDeCliente,longDelay:Long=100){
    val endereco =vm.endereco.collectAsStateWithLifecycle()
    val rua= rememberTextFieldState()
    val numero= rememberTextFieldState()
    val cidade= rememberTextFieldState()
    val estado= rememberTextFieldState()
    val cep= rememberTextFieldState()
    val bairo= rememberTextFieldState()
    val complemento= rememberTextFieldState()
    val focusRequesterRua = remember { FocusRequester() }
    val loadCliente =vm.LoadCliente.collectAsStateWithLifecycle()
    LaunchedEffect(endereco.value) {
        if(endereco.value!=null){
            val endereco = endereco.value
            rua.setTextAndPlaceCursorAtEnd(endereco!!.rua)
            numero.setTextAndPlaceCursorAtEnd(endereco.numero)
            cidade.setTextAndPlaceCursorAtEnd(endereco.cidade)
            estado.setTextAndPlaceCursorAtEnd(endereco.estado)
            cep.setTextAndPlaceCursorAtEnd(endereco.cep)
            bairo.setTextAndPlaceCursorAtEnd(endereco.bairro)
            complemento.setTextAndPlaceCursorAtEnd(endereco.complemento)
        }
    }
    LaunchedEffect(loadCliente.value) {
        when(loadCliente.value){
            is EstadosDeLoadCaregamento.Caregado<*> -> {
                delay(longDelay)
                focusRequesterRua.requestFocus()}
            else -> {}}
    }
    Column(modifier =Modifier.fillMaxWidth().padding(bottom = 70.dp,top = 10.dp, start = 5.dp, end = 5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Cadastro de Endereco", fontSize = 25.sp, modifier = Modifier.padding(bottom = 10.dp,top=20.dp))
        OutlinedTextField(state = rua, label = {Text("Rua")}, modifier = Modifier.fillMaxWidth(0.98f).focusRequester(focusRequesterRua))
        Spacer(Modifier.padding(3.dp))
        OutlinedTextField(state = numero, label = {Text("Numero")}, modifier = Modifier.fillMaxWidth(0.98f))
        Spacer(Modifier.padding(3.dp))
        OutlinedTextField(state = cidade, label = {Text("Cidade")}, modifier = Modifier.fillMaxWidth(0.98f))
        Spacer(Modifier.padding(3.dp))
        OutlinedTextField(state = estado, label = {Text("Rstado")}, modifier = Modifier.fillMaxWidth(0.98f))
        Spacer(Modifier.padding(3.dp))
        OutlinedTextField(state = cep, label = {Text("Cep")}, modifier = Modifier.fillMaxWidth(0.98f),inputTransformation = FormatoCep())
        Spacer(Modifier.padding(3.dp))
        OutlinedTextField(state = bairo, label = {Text("Bairo")}, modifier = Modifier.fillMaxWidth(0.98f))
        Spacer(Modifier.padding(3.dp))
        OutlinedTextField(state = complemento, label = {Text("Complemento")}, modifier = Modifier.fillMaxWidth(0.98f))




        Box(Modifier.fillMaxWidth()) {
            Button(onClick = {
                vm.guardarEnderecoCriado(Endereco(0,0, cep = cep.text.toString(),
                                                       bairro = bairo.text.toString(),
                                                       cidade = cidade.text.toString(),
                                                       estado = estado.text.toString(),
                                                       rua = rua.text.toString(),
                                                       numero = numero.text.toString(),
                                                       complemento = complemento.text.toString()))
                vm.estagioDeCadastroAnterior()}, modifier = Modifier.align(Alignment.CenterStart).padding(20.dp)
            ) {
                Text("Cliente")
            }

            Button(onClick = {
                vm.guardarEnderecoCriado(Endereco(id=0,
                                                  idCli=0,
                                                  cidade =cidade.text.toString(),
                                                  estado = estado.text.toString(),
                                                  cep = cep.text.toString(),
                                                  bairro = bairo.text.toString(),
                                                  rua = rua.text.toString(),
                                                  numero = numero.text.toString(),
                                                  complemento = complemento.text.toString()
                                                      ))
                vm.prosimoEstagioDeCadastro()}, modifier = Modifier.align(Alignment.CenterEnd).padding(20.dp)
            ) {
                Text("Telefone")
            }
        }
    }

}