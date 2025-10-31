package com.example.meunegociomeunegocio.formatadoresDeTesto

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.insert
import androidx.compose.foundation.text.input.placeCursorAtEnd

class FormatoCnpj: InputTransformation {
    override fun TextFieldBuffer.transformInput() {
       val textoFormatado=asCharSequence().replace(Regex("\\D"),"")
       replace(0,length,textoFormatado)
       if(textoFormatado.length>2)insert(2,".")
       if(textoFormatado.length>5)insert(6,".")
       if(textoFormatado.length>8)insert(10,"/")
       if(textoFormatado.length>12)insert(15,"-")
       if(textoFormatado.length>0) placeCursorAtEnd()
       if(textoFormatado.length>14) revertAllChanges()
    }
}