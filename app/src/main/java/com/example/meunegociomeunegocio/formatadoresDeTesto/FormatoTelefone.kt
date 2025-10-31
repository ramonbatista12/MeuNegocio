package com.example.meunegociomeunegocio.formatadoresDeTesto

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.insert
import androidx.compose.foundation.text.input.placeCursorAtEnd

class FormatoTelefone: InputTransformation {
    override fun TextFieldBuffer.transformInput() {
       val textoLimpo =asCharSequence().replace(Regex("\\D"),"")
       replace(0,length,textoLimpo)
       if(textoLimpo.length>2)insert(2," ")
       if(textoLimpo.length>7)insert(8,"-")
       if(textoLimpo.length>0)placeCursorAtEnd()
       if(textoLimpo.length>11)revertAllChanges()
    }
}