package com.example.meunegociomeunegocio.loads

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

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