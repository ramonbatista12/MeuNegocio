package com.example.meunegociomeunegocio.pdf

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.util.Log
import androidx.compose.ui.graphics.Canvas
import com.example.meunegociomeunegocio.repositorioRom.DadosDaRequisicao
import com.example.meunegociomeunegocio.repositorioRom.ProdutoRequisitado
import com.example.meunegociomeunegocio.utilitario.Resposta
import java.io.BufferedReader
import java.io.File

class CriadorDePfd(private val contexto: Context) {
    fun create(uri: Uri,dadosDaRequisicao: DadosDaRequisicao,listaDeProdutos: List<ProdutoRequisitado>): Resposta{
        Log.d("CriadorDePdf","pdf criando pdf")
        val contenteResolver=contexto.contentResolver
        val pdf =pdf(dadosDaRequisicao,listaDeProdutos)
        try {
            contenteResolver.openOutputStream(uri).use {
                if(it!=null){
                    pdf.writeTo(it)

                    Log.d("CriadorDePdf","pdf criado com sucesso")
                }
            }
            pdf.close()
            return  Resposta.OK(uri)
        }catch (e: Exception){
            pdf.close()
            return Resposta.Erro(e)
            Log.d("CriadorDePdf","erro ao criar pdf")
        }

    }

    private fun pdf(dadosDaRequisicao: DadosDaRequisicao,listaDeProdutos: List<ProdutoRequisitado>): PdfDocument{

        val pdf=PdfDocument()
        var paginaInf= PdfDocument.PageInfo.Builder(595,842,1).create()
        var pagina = pdf.startPage(paginaInf)
        var offset: OffsetDesenho
        val paint = Paint()
        paint.color= Color.BLACK
        paint.textSize=20f
        offset=desenharCabesalhoRequisicao(pagina,paint,dadosDaRequisicao)
        val largura =(pagina.info.pageWidth-100)/2
        paint.textSize=20f
        desenharTextoNoCentro("Produtos/Serviços",pagina,paint,OffsetDesenho(largura,offset.y+20))


        pdf.finishPage(pagina)
        return pdf
    }

    private fun desenharCabesalhoRequisicao(pagina: PdfDocument.Page,paint: Paint,dadosDaRequisicao: DadosDaRequisicao): OffsetDesenho{
         var offset: OffsetDesenho
         offset =desenheTexto(pagina,paint,"Requisicao numero: ${dadosDaRequisicao.requisicao.id}",OffsetDesenho(50,50))
         paint.textSize=12f
         offset=desenheTexto(pagina,paint,"Cliente: ${dadosDaRequisicao.cliente.nome}",OffsetDesenho(50,offset.y+15))
         if(dadosDaRequisicao.cliente.cpf!=null)
         offset=desenheTexto(pagina,paint,"CPF: ${dadosDaRequisicao.cliente.cpf}",OffsetDesenho(50,offset.y+15))
         if(dadosDaRequisicao.cliente.cnpj!=null)
         offset=desenheTexto(pagina,paint,"CNPJ: ${dadosDaRequisicao.cliente.cnpj}",OffsetDesenho(50,offset.y+15))
         pagina.canvas.drawLine(offset.x.toFloat(),offset.y.toFloat()+5f,pagina.info.pageWidth-50f,offset.y.toFloat()+5f,paint)
         return OffsetDesenho(offset.x,offset.y+15)


    }

    private fun desenharTextoNoCentro(string: String,pagina: PdfDocument.Page,paint: Paint,offset: OffsetDesenho){
     val retangulo = Rect()
     val bauds =paint.getTextBounds(string,0,string.length,retangulo)
     val x = retangulo.width()/4
     pagina.canvas.drawText(string,offset.x.toFloat()-x,offset.y.toFloat(),paint)

    }

    private fun desenheTexto(pagina: PdfDocument.Page,paint: Paint,string: String,offsetDesenho: OffsetDesenho): OffsetDesenho{
        var x=0
        var y=0

        val medidaDoTexto= paint.measureText(string)
        if(medidaDoTexto.toInt()<=pagina.info.pageWidth.toInt()){
           pagina.canvas.drawText(string,offsetDesenho.x.toFloat(),offsetDesenho.y.toFloat(),paint)
           val balds = Rect()
           paint.getTextBounds(string,0,string.length,balds)
           x=balds.width()
           y=balds.height()
           Log.d("CriadorPdf","medidas do offset $x $y")
        }
        else{
            var texto =string
            var offset = OffsetDesenho(offsetDesenho.x,offsetDesenho.y)
            while (!texto.isBlank()){
                val medidaDoTexto= paint.measureText(texto)
                if(medidaDoTexto.toInt()<=pagina.info.pageWidth.toInt()){
                    val quebraDotexto = paint.breakText(texto,true,(pagina.info.pageWidth-50f),null)
                    val linha=texto.substring(quebraDotexto)
                    pagina.canvas.drawText(linha,offset.x.toFloat(),offset.y.toFloat(),paint)
                    val balds = Rect()
                    paint.getTextBounds(linha,0,linha.length,balds)
                    offset= OffsetDesenho(x=offsetDesenho.x,y=balds.height()+4)
                    if(quebraDotexto- texto.length!=0)
                    texto=texto.substring(quebraDotexto,texto.length).toString()
                    else texto=""
                    Log.d("criardor pdf","fim do loop")
                }
            }
            return offset
        }

        return OffsetDesenho(offsetDesenho.x,offsetDesenho.y+y)
    }
    private fun desenharCabesalhoProdutos(pagina: PdfDocument.Page,paint: Paint,dadosDaRequisicao: DadosDaRequisicao,offset: OffsetDesenho): OffsetDesenho{
        var _offset: OffsetDesenho
        _offset=desenheTexto(pagina,paint,"Descricao",OffsetDesenho(50,offset.x))

        return _offset
    }

}

data class OffsetDesenho(val x: Int, val y: Int)