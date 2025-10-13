package com.example.meunegociomeunegocio.apresentacaoDeClientes

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meunegociomeunegocio.repositorioRom.Cliente
import com.example.meunegociomeunegocio.viewModel.ViewModelCliente

@Composable
fun ListaDeClientes(modifier: Modifier= Modifier,vm:ViewModelCliente){
    LaunchedEffect(Unit) {
        Log.d("ListaDeClientes","LaunchedEffect")
    }
    val fluxoDeClientes=vm.fluxoDeCliente.collectAsState(initial = emptyList())
    LazyColumn(modifier=modifier) {
        items(fluxoDeClientes.value) {
            ItemsDeClientes(it)
        }
    }
}

@Composable
private fun ItemsDeClientes(cli: Cliente){
    Column(modifier = Modifier.fillMaxWidth().padding(top = 3.dp, start = 5.dp, end = 5.dp, bottom = 3.dp)) {
     Row(modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp), verticalAlignment = Alignment.CenterVertically) {
        CirculoDeInicias(nome = cli.nome,cli.id)
        Spacer(Modifier.padding(10.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = cli.nome, fontSize = 20.sp)
            Text(text = when{
                cli.cpf!=null->cli.cpf
                cli.cnpj!=null->cli.cnpj
                else->""
            }, fontSize = 9.sp
            )}
        }

    }

    }



@Composable
private fun CirculoDeInicias(nome: String,id:Int=0){
    val iniciais= remember{mutableStateOf("")}
    val color= remember{ mutableStateOf(Cores.lisColors[0])}
    LaunchedEffect(nome) {
        val split =nome.split(Regex("\\s"))
        if (split.size==2){
            iniciais.value=split[0].first().toString()+split[1].first().toString()

        }
        else
            iniciais.value=split[0].first().toString()

    }
    LaunchedEffect(iniciais) {
        val char =iniciais.value.subSequence(0,1)
        val indice = char[0].code % Cores.lisColors.size
        Log.d("CirculoDeInicias","LaunchedEffect $id w$indice ")
        color.value=Cores.lisColors[indice]
    }
    Box(modifier = Modifier.size(40.dp)
                           .clip(CircleShape)
                           .border(1.dp, color = androidx.compose.ui.graphics.Color.Black, shape = CircleShape)
                           .background(color.value)){
        Text(text = iniciais.value, modifier = Modifier.align(Alignment.Center), fontSize = 20.sp)
    }

}

object Cores{
    val lisColors =listOf<Color>(Color.Cyan,Color.Red,Color.Green,Color.Yellow,Color.Magenta)
}