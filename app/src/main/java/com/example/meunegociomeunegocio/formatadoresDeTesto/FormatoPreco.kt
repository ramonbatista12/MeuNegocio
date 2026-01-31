package com.example.meunegociomeunegocio.formatadoresDeTesto

import android.util.Log
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.insert
import androidx.compose.foundation.text.input.placeCursorAtEnd
import java.math.BigDecimal

import java.text.NumberFormat
import java.util.Locale

class FormatoPreco: InputTransformation {
    private val Tag="Formatacao de Preco"
    override fun TextFieldBuffer.transformInput() {
        val textoLimpo = asCharSequence().replace(Regex("\\D"),"").toString().toInt().toString()
        var double = textoLimpo.toInt()/100.0
        replace(0,length,String.format("%.2f",double))
        placeCursorAtEnd()

    }
}
/*
val int=textoLimpo.toInt()
replace(0,length,int.toString())
Log.d(Tag,"texto limpo $textoLimpo texto setado ${asCharSequence().toString()} tamanho $length" )
if(length>2) insert(textoLimpo.length-2,",")
if(length==2) insert(0,"0,")
if(length==1)insert(0,"0,0")*/