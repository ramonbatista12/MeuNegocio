package com.example.meunegociomeunegocio.utilitario

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.util.TableInfo
import com.example.meunegociomeunegocio.viewModel.IDialogoDeExclusao

@Composable
fun  DialogoExclusao(mensagem: String,mensagemErro: String,mensagemSucesso: String,interfaceDialogoDeExclusao:IDialogoDeExclusao ){
    val abriarDialogo =interfaceDialogoDeExclusao.abrirDialogoDeExclusao.collectAsStateWithLifecycle()
    val estados=interfaceDialogoDeExclusao.estadoDeExclusao.collectAsStateWithLifecycle()
    if(abriarDialogo.value)
        Dialog(onDismissRequest = { interfaceDialogoDeExclusao.fecharDialogo() }) {
            OutlinedCard(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background), modifier = Modifier.width(250.dp).height(250.dp))  {
                Box(modifier = Modifier.fillMaxSize().padding(5.dp)){
                   Column(modifier = Modifier.align(Alignment.TopStart)) {
                       Text(text = "Obss!!", fontSize = 20.sp, modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 5.dp))
                       HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))
                       when(estados.value){
                           is EstadoLoadAcoes.Erro -> {
                               Text(text = mensagemErro, fontSize = 15.sp,modifier = Modifier.padding(start = 5.dp))
                           }
                           is EstadoLoadAcoes.Iniciando -> {
                               Text(text = mensagem, fontSize = 15.sp,modifier = Modifier.padding(start = 5.dp))
                           }
                           is EstadoLoadAcoes.Sucesso -> {
                               Text(text = mensagemSucesso, fontSize = 15.sp,modifier = Modifier.padding(start = 5.dp))
                           }
                           is EstadoLoadAcoes.Criando -> {
                               CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally).size(50.dp))
                           }

                       }

                   }
                BotoesDoDialogoExclusao(estados.value,{interfaceDialogoDeExclusao.excluir()},{interfaceDialogoDeExclusao.fecharDialogo()},modifier = Modifier.align(Alignment.BottomEnd))
                }
            }
        }


}
@Composable
private fun BotoesDoDialogoExclusao(estado: EstadoLoadAcoes,acaoDeExclusao:()->Unit,acaoDeCancelar:()->Unit,modifier: Modifier=Modifier){
    Box(modifier.fillMaxWidth()){
        if(estado !is EstadoLoadAcoes.Criando){
        if(estado !is EstadoLoadAcoes.Sucesso)
        Button({

                acaoDeExclusao()
        }, modifier = Modifier.align(Alignment.CenterStart).padding(start = 10.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)) {
            if (estado is EstadoLoadAcoes.Iniciando)
               Text("Ok", color = Color.White)
            if (estado is EstadoLoadAcoes.Erro)
                Text("Tentar novamente", color = Color.White)
        }
        Button({

            acaoDeCancelar()
        }, modifier = Modifier.align(Alignment.CenterEnd).padding(start = 10.dp), colors = ButtonDefaults.buttonColors(containerColor = if(estado is EstadoLoadAcoes.Sucesso)Color.Blue else Color.Red)) {
            if (estado is EstadoLoadAcoes.Iniciando)
                Text("Cancelar", color = Color.White)
            if (estado is EstadoLoadAcoes.Erro)
                Text("Cancelar", color = Color.White)
            if (estado is EstadoLoadAcoes.Sucesso){
                Text("Ok", color = Color.White)
            }
        }
        }
    }
}