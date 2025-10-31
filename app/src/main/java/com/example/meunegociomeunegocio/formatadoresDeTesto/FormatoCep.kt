package com.example.meunegociomeunegocio.formatadoresDeTesto

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.insert
import androidx.compose.foundation.text.input.placeCursorAtEnd

class FormatoCep: InputTransformation {
    override fun TextFieldBuffer.transformInput() {
        val textoLimpo=asCharSequence().replace(Regex("\\D"),"")
        replace(0,length,textoLimpo)
        if(textoLimpo.length>5)insert(5,"-")
        if(textoLimpo.length>8)revertAllChanges()
        if(textoLimpo.length>0)this.placeCursorAtEnd()
    }
}