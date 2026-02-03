package com.example.meunegociomeunegocio.adicaoDeProdutos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meunegociomeunegocio.formatadoresDeTesto.FormatoPreco
import com.example.meunegociomeunegocio.loads.DialogoLoad
import com.example.meunegociomeunegocio.loads.TitulosDeLoad
import com.example.meunegociomeunegocio.repositorioRom.ProdutoServico
import com.example.meunegociomeunegocio.viewModel.ViewModelAdicaoDeProdutos
import kotlinx.coroutines.launch

@Composable
fun AdicaoDePRodutos(vm: ViewModelAdicaoDeProdutos,modifier: Modifier= Modifier,acaoDeVoutar:()->Unit){
    val nomePRoduto= vm.textFildStateNomeProduto
    val preco =vm.textFildPReco
    val descricao = vm.textFildStateDescricao
    val servico = vm.servicoProduto.collectAsState()
    val corotinesScope= rememberCoroutineScope()
    val loadProduto =vm.loadProduto.collectAsState()
    Column(Modifier.fillMaxWidth().padding(5.dp),horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
        Text(text = "Adicionar Produto", fontSize = 25.sp, modifier = Modifier.padding(bottom = 10.dp,top=20.dp))
        OutlinedTextField(
            state = nomePRoduto,
            label = { Text("Nome do Produto") },
            modifier = Modifier.fillMaxWidth(0.98f).focusRequester(vm.focoNome)
        )
        Row(
            Modifier.padding(start = 5.dp, end = 5.dp, top = 5.dp, bottom = 5.dp)
                .fillMaxWidth(0.98f).focusRequester(vm.focoPreco),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text(text = "Produto", Modifier.padding(end = 5.dp))
            RadioButton(
                selected = !servico.value,
                onClick = { vm.produtoSelecionado() },
                Modifier.padding(end = 5.dp).focusRequester(vm.focoDescricao)
            )
            Text(text = "Servico", Modifier.padding(end = 5.dp))
            RadioButton(selected = servico.value, onClick = { vm.servicoSelecionado() })

        }
        OutlinedTextField(
            state = preco,
            label = { Text("Preco") },
            inputTransformation = FormatoPreco(),
            modifier = Modifier.padding(bottom = 5.dp).fillMaxWidth(0.98f)
        )
        OutlinedTextField(
            state = descricao,
            label = { Text("Descricao") },
            modifier = Modifier.padding(bottom = 5.dp).fillMaxWidth(0.98f)
        )
        Box(modifier = Modifier.fillMaxWidth(0.98f).align(Alignment.CenterHorizontally).padding(start = 20.dp, end = 20.dp, bottom = 20.dp)) {
        Button(onClick = acaoDeVoutar,modifier= Modifier.align(Alignment.CenterStart), colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
            Text(text = "Cancelar")
        }

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
        }, modifier= Modifier.align(Alignment.CenterEnd),colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)) {
            Text(text = "Salvar  ")
        }
    }
        SnackbarHost(hostState = vm.snackbarHostState)
    }
    DialogoLoad(TitulosDeLoad.ProdutosServicos.titulo,loadProduto.value)
}