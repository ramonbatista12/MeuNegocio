package com.example.meunegociomeunegocio.formatadoresDeTesto

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.insert
import androidx.compose.foundation.text.input.placeCursorAtEnd
import kotlin.text.Regex

class FormatoCpf: InputTransformation {
    override fun TextFieldBuffer.transformInput() {
        val textoAux=asCharSequence()
        val textoLimpo=textoAux.replace(Regex("\\D"),{""})

        this.replace(0,length,textoLimpo.toString())
        if(textoLimpo.length>3)insert(3,".")
        if(textoLimpo.length>6)insert(7,".")
        if(textoLimpo.length>9)insert(11,"-")
        if(textoLimpo.length>0) placeCursorAtEnd()
        if(textoLimpo.length>11) revertAllChanges()


    }
}