package com.example.meunegociomeunegocio
import com.example.meunegociomeunegocio.apresentacaoDeProdutos.formatData
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
class TexteHoraData {
    @Test
    fun textDatahora(){
        val list =listOf<String>("2004-10-11 20:00:30",
                                 "2004-1-11 20:00:30",
                                 "2004-102-11 20:00:30",
                                 "2004-10-1 200:000:030")
        for (s in list)
           try {
              val sf=s.formatData()
               println("string formatada com sucesso $s para $sf")
           }catch (e: Exception){
               println("excesao capturada ${e.message} para $s")
           }
    }
}