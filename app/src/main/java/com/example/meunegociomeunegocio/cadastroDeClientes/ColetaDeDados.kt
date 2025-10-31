package com.example.meunegociomeunegocio.cadastroDeClientes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.meunegociomeunegocio.formatadoresDeTesto.FormatoCep
import com.example.meunegociomeunegocio.formatadoresDeTesto.FormatoCnpj
import com.example.meunegociomeunegocio.formatadoresDeTesto.FormatoCpf
import com.example.meunegociomeunegocio.formatadoresDeTesto.FormatoTelefone
import com.example.meunegociomeunegocio.repositorioRom.Endereco
import com.example.meunegociomeunegocio.viewModel.EstagiosDeCadastroClientes
import com.example.meunegociomeunegocio.viewModel.ViewModelCadastroDeCliente

@Composable
fun CadastroCompat(vm: ViewModelCadastroDeCliente){
     val estagios=vm.estagio.collectAsStateWithLifecycle()
    when(estagios.value){
        is EstagiosDeCadastroClientes.Nome -> {
            CadastraCliente(vm)
        }
        is EstagiosDeCadastroClientes.Endereco -> {
            CadastraEndereco(vm)
        }
        is EstagiosDeCadastroClientes.Telefone -> {
            CadastraTelefone(vm)
        }
    }


}

@Composable
private fun  CadastraCliente(vm: ViewModelCadastroDeCliente){
    val nome= rememberTextFieldState()
    val cpf=rememberTextFieldState()
    val cnpj=rememberTextFieldState()
    Column(modifier =Modifier.fillMaxWidth().padding(bottom = 70.dp,top = 10.dp, start = 5.dp, end = 5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Cadastro de cliente")
        Spacer(Modifier.padding(3.dp))
        OutlinedTextField(state = nome, modifier = Modifier.padding( horizontal = 5.dp).fillMaxWidth(0.8f), label = {Text("Nome")})
        Spacer(Modifier.padding(3.dp))
        OutlinedTextField(state = cpf, modifier = Modifier.padding( horizontal = 5.dp).fillMaxWidth(0.8f), label = {Text("Cpf")},
                          inputTransformation = FormatoCpf() )
        Spacer(Modifier.padding(3.dp))
        OutlinedTextField(state = cnpj, modifier = Modifier.padding( horizontal = 5.dp).fillMaxWidth(0.8f), label = {Text("Cnpj")}, inputTransformation = FormatoCnpj())

        Box(Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = {vm.prosimoEstagioDeCadastro()}, modifier = Modifier.align(Alignment.CenterEnd).padding(20.dp)
            ) {
                Text("Prosimo")
            }
        }
    }

}

@Composable
private fun  CadastraTelefone(vm: ViewModelCadastroDeCliente){
    val telefone= rememberTextFieldState()

    Column(modifier =Modifier.fillMaxWidth().padding(bottom = 70.dp,top = 10.dp, start = 5.dp, end = 5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Cadastro de telefone")
        Spacer(Modifier.padding(10.dp))
        OutlinedTextField(state = telefone, label = {Text("Numero do telefone")}, modifier = Modifier.fillMaxWidth(0.8f),
                          inputTransformation = FormatoTelefone())
        Spacer(Modifier.padding(10.dp))



        Box(Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = {vm.estagioDeCadastroAnterior()}, modifier = Modifier.align(Alignment.CenterStart).padding(20.dp)
            ) {
                Text("Anterior")
            }

            OutlinedButton(onClick = {vm.prosimoEstagioDeCadastro()}, modifier = Modifier.align(Alignment.CenterEnd).padding(20.dp)
            ) {
                Text("Prosimo")
            }
        }
    }

}

@Composable
private fun  CadastraEndereco(vm: ViewModelCadastroDeCliente){

    val rua= rememberTextFieldState()
    val numero= rememberTextFieldState()
    val cidade= rememberTextFieldState()
    val estado= rememberTextFieldState()
    val cep= rememberTextFieldState()
    val bairo= rememberTextFieldState()
    val complemento= rememberTextFieldState()
    Column(modifier =Modifier.fillMaxWidth().padding(bottom = 70.dp,top = 10.dp, start = 5.dp, end = 5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Cadastro de Endereco")
        OutlinedTextField(state = rua, label = {Text("Rua")}, modifier = Modifier.fillMaxWidth(0.8f))
        Spacer(Modifier.padding(3.dp))
        OutlinedTextField(state = numero, label = {Text("Numero")}, modifier = Modifier.fillMaxWidth(0.8f))
        Spacer(Modifier.padding(3.dp))
        OutlinedTextField(state = cidade, label = {Text("Cidade")}, modifier = Modifier.fillMaxWidth(0.8f))
        Spacer(Modifier.padding(3.dp))
        OutlinedTextField(state = estado, label = {Text("Rstado")}, modifier = Modifier.fillMaxWidth(0.8f))
        Spacer(Modifier.padding(3.dp))
        OutlinedTextField(state = cep, label = {Text("Cep")}, modifier = Modifier.fillMaxWidth(0.8f),inputTransformation = FormatoCep())
        Spacer(Modifier.padding(3.dp))
        OutlinedTextField(state = bairo, label = {Text("Bairo")}, modifier = Modifier.fillMaxWidth(0.8f))
        Spacer(Modifier.padding(3.dp))
        OutlinedTextField(state = complemento, label = {Text("Complemento")}, modifier = Modifier.fillMaxWidth(0.8f))




        Box(Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = {vm.estagioDeCadastroAnterior()}, modifier = Modifier.align(Alignment.CenterStart).padding(20.dp)
            ) {
                Text("Anterior")
            }

            OutlinedButton(onClick = {vm.prosimoEstagioDeCadastro()}, modifier = Modifier.align(Alignment.CenterEnd).padding(20.dp)
            ) {
                Text("Prosimo")
            }
        }
    }

}