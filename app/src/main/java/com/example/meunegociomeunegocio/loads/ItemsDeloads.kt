package com.example.meunegociomeunegocio.loads

import androidx.compose.animation.core.EaseInCirc
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.EaseInElastic
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.meunegociomeunegocio.utilitario.EstadosDeLoadCaregamento
import kotlinx.coroutines.Delay

@Composable
fun LoadClientes(){
    val valorFinal=with(LocalDensity.current) {
        500.dp.toPx()
    }
    val infiniteTransition = rememberInfiniteTransition("Animacao de load da lista de clientes")
    val cores=listOf(Color.LightGray, MaterialTheme.colorScheme.background,MaterialTheme.colorScheme.background, Color.LightGray)
    val animacao=infiniteTransition.animateFloat(initialValue = 0f,
                                                 targetValue = valorFinal.dp.value,
                                                 animationSpec = InfiniteRepeatableSpec(tween(durationMillis = 2000),
                                                                                        RepeatMode.Restart ))

    val linearGradienteDeCores= Brush.linearGradient(colors =cores, start = Offset(40f,50f), end = Offset(animacao.value+40,animacao.value+50))
    Row(Modifier.fillMaxWidth().padding(top = 3.dp, start = 5.dp, end = 5.dp, bottom = 5.dp)) {
        Box(Modifier.size(40.dp).background(brush =linearGradienteDeCores, shape = CircleShape).padding(end = 20.dp))
        Spacer(Modifier.padding(5.dp))
        Column(verticalArrangement = Arrangement.Center) {
            Spacer(Modifier.padding(2.dp))
            Box(Modifier.size(width = 250.dp,10.dp).background(brush = linearGradienteDeCores).padding(bottom = 20.dp).clip(
                RoundedCornerShape(10.dp)
            ))
            Spacer(Modifier.padding(5.dp))
            Box(Modifier.size(width = 90.dp,5.dp).background(brush = linearGradienteDeCores).clip(
                RoundedCornerShape(10.dp)
            ))

        }

    }
}

@Composable
fun ItemDelLoadTabelas(){
    val valorFinal=with(LocalDensity.current) {
        500.dp.toPx()
    }
    val infiniteTransition = rememberInfiniteTransition("Animacao de load da lista de clientes")
    val cores=listOf(Color.LightGray, MaterialTheme.colorScheme.background,MaterialTheme.colorScheme.background, Color.LightGray)
    val animacao=infiniteTransition.animateFloat(initialValue = 0f,
        targetValue = valorFinal.dp.value,
        animationSpec = InfiniteRepeatableSpec(tween(durationMillis = 2000),
            RepeatMode.Restart ))

    val linearGradienteDeCores= Brush.linearGradient(colors =cores, start = Offset(40f,50f), end = Offset(animacao.value+40,animacao.value+50))
    Row(Modifier.fillMaxWidth().height(50.dp).padding(top = 3.dp, start = 5.dp, end = 5.dp, bottom = 5.dp).background(brush = linearGradienteDeCores)){

    }





}
@Composable
fun DialogoLoad(String: String,estado: EstadosDeLoadCaregamento,cor: Color= Color.Blue){
    when(estado){
        is EstadosDeLoadCaregamento.load -> {

            Dialog(onDismissRequest = {}) {
                Box(modifier = Modifier.width(300.dp).height(150.dp)){
                    Text(text = "Carregando $String", modifier = Modifier.align(Alignment.TopCenter), fontSize = 18.sp, color = Color.White)
                    Row(Modifier.fillMaxWidth().fillMaxHeight().padding(top=20.dp).align(Alignment.Center), horizontalArrangement = Arrangement.Center,verticalAlignment = Alignment.CenterVertically) {
                        Bolinha(cor,0)
                        Spacer(Modifier.padding(5.dp))
                        Bolinha(cor,100)
                        Spacer(Modifier.padding(5.dp))
                        Bolinha(cor,150)
                    }


                }}}
        else -> {}
    }

}

@Composable
fun LoadBox3pontinhos(modifier: Modifier=Modifier,String: String,estado: EstadosDeLoadCaregamento,cor: Color= Color.Blue){

        Box(modifier = modifier.width(300.dp).height(150.dp)){
            Text(text = "Carregando $String", modifier = Modifier.align(Alignment.TopCenter), fontSize = 18.sp, color = Color.Black)
            Row(Modifier.fillMaxWidth().fillMaxHeight().padding(top=20.dp).align(Alignment.Center), horizontalArrangement = Arrangement.Center,verticalAlignment = Alignment.CenterVertically) {
                Bolinha(cor,0)
                Spacer(Modifier.padding(5.dp))
                Bolinha(cor,100)
                Spacer(Modifier.padding(5.dp))
                Bolinha(cor,150)
            }


        }
}
@Composable
private fun Bolinha(cor: Color,delay: Int){
    var animacao = rememberInfiniteTransition()
    var ofsetMasimo=-20f
    var transicao=animacao.animateFloat(initialValue = 0f,
                                        targetValue = ofsetMasimo,
                                        animationSpec = InfiniteRepeatableSpec(tween(durationMillis = 1000, delayMillis = delay, easing = EaseInCubic),
                                            RepeatMode.Reverse, StartOffset(0))
                                         )
    Box(Modifier.size(20.dp).offset(y = transicao.value.dp).drawWithCache({
        onDrawBehind {
            drawCircle(color = cor)
        }
    })){}}


sealed class TitulosDeLoad(val titulo:String){
    object Clientes: TitulosDeLoad("Clientes")
    object ProdutosServicos: TitulosDeLoad("Produtos/Serviços")
    object Requisicoes: TitulosDeLoad("Requisicao")

}

sealed class EstadosDosCirculos{
    object Inicio: EstadosDosCirculos()
    object Meio: EstadosDosCirculos()
    object Fim: EstadosDosCirculos()
}