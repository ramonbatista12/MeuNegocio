package com.example.meunegociomeunegocio.adicaoDeProdutos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.meunegociomeunegocio.formatadoresDeTesto.FormatoPreco
import com.example.meunegociomeunegocio.repositorioRom.ProdutoServico
import com.example.meunegociomeunegocio.viewModel.ViewModelAdicaoDeProdutos
import kotlinx.coroutines.launch

@Composable
fun AdicaoDePRodutos(vm: ViewModelAdicaoDeProdutos,modifier: Modifier= Modifier,acaoDeVoutar:()->Unit){
    val nomePRoduto= rememberTextFieldState()
    val preco =rememberTextFieldState("0,0")
    val descricao = rememberTextFieldState()
    val servico = remember{ mutableStateOf(false) }
    val corotinesScope= rememberCoroutineScope()
    Column(Modifier.fillMaxWidth().padding(5.dp),horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
        OutlinedTextField(
            state = nomePRoduto,
            label = { Text("Nome do Produto") },
            modifier = Modifier.fillMaxWidth(0.8f)
        )
        Row(
            Modifier.padding(start = 5.dp, end = 5.dp, top = 5.dp, bottom = 5.dp)
                .fillMaxWidth(0.8f),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text(text = "Produto", Modifier.padding(end = 5.dp))
            RadioButton(
                selected = !servico.value,
                onClick = { servico.value = false },
                Modifier.padding(end = 5.dp)
            )
            Text(text = "Servico", Modifier.padding(end = 5.dp))
            RadioButton(selected = servico.value, onClick = { servico.value = true })

        }
        OutlinedTextField(
            state = preco,
            label = { Text("Preco") },
            inputTransformation = FormatoPreco(),
            modifier = Modifier.padding(bottom = 5.dp).fillMaxWidth(0.8f)
        )
        OutlinedTextField(
            state = descricao,
            label = { Text("Descricao") },
            modifier = Modifier.padding(bottom = 5.dp).fillMaxWidth(0.8f)
        )
        Row(modifier = Modifier.fillMaxWidth(0.8f),horizontalArrangement = Arrangement.Center) {
        Button(onClick = acaoDeVoutar, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
            Text(text = "Cancelar")
        }
        Spacer(modifier = Modifier.padding(5.dp))
        Button(onClick = {
              corotinesScope.launch {
                  vm.adicionarProduto(ProdutoServico(nome = nomePRoduto.text.toString(),
                                                    servico = servico.value,
                                                    preco =preco.text.toString().replace(",", ".").toFloat(),
                                                    descrisao = descricao.text.toString(),
                                                    ativo = true,
                                                    id = 0),
                                                    callback = {acaoDeVoutar()})
              }
        }, colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)) {
            Text(text = "Salvar  ")
        }
    }
        SnackbarHost(hostState = vm.snackbarHostState)
    }

}