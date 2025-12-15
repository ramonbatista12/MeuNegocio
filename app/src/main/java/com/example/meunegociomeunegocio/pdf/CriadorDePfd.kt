package com.example.meunegociomeunegocio.pdf

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.util.Log
import androidx.compose.ui.graphics.Canvas
import com.example.meunegociomeunegocio.apresentacaoDeProdutos.formatarPreco
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
     /**
     * essa obra de arte var pagina = arrayOf( pdf.startPage(paginaInf)) e algo que eu vi no rust aonde nao se podia crar a estrutura de
     * dados arvore pelas limitacoes de ponteiros do rus t sem fazer algo muito complexo
     * eu usei amesma tecnica par a criar um ponteiroa para pagina
     * aonde a pagina pode ser modificada pela funcao
     * */
    private fun pdf(dadosDaRequisicao: DadosDaRequisicao,listaDeProdutos: List<ProdutoRequisitado>): PdfDocument{

        val pdf=PdfDocument()
        var paginaInf= PdfDocument.PageInfo.Builder(595,842,1).create()
        var pagina = arrayOf( pdf.startPage(paginaInf))
        var offset: OffsetDesenho
        val paint = Paint()
        paint.color= Color.BLACK
        paint.textSize=20f
        offset=desenharCabesalhoRequisicao(pagina[0],paint,dadosDaRequisicao)
        val largura =(pagina[0].info.pageWidth-100)/2
        paint.textSize=20f
        desenharTextoNoCentro("Produtos/Serviços",pagina[0],paint,OffsetDesenho(largura,offset.y+20))
        paint.textSize=15f
        val posicoesIniciaisCabesalho =desenharCabesalhoProdutos(pagina[0],paint,dadosDaRequisicao,offset.copy(x=offset.x,y=offset.y+70))
        offset = desenharProdutos(pagina,pdf,paint,listaDeProdutos,posicoesIniciaisCabesalho)
        pagina[0].canvas.drawLine(50f,offset.y.toFloat(),pagina[0].info.pageWidth-50f,offset.y.toFloat(),paint)
        val listaDePrecos =listaDeProdutos.map { it.total }.sum()
        offset=desenheTexto(pagina[0],paint,"Total: ${listaDePrecos.toString().formatarPreco()}",OffsetDesenho(50,offset.y+30))
        if(offset.y>pagina[0].info.pageHeight-200){
            val numero =pagina[0].info.pageNumber
            pdf.finishPage(pagina[0])
            paginaInf= PdfDocument.PageInfo.Builder(595,842,numero+1).create()
            pagina[0]= pdf.startPage(paginaInf)
            offset= OffsetDesenho(x = 50,y= 50)
        }

        desenheTexto(pagina[0],paint,"Obs : "+dadosDaRequisicao.requisicao.obs,OffsetDesenho(offset.x,offset.y+30),pagina[0].info.pageWidth-100)
        pdf.finishPage(pagina[0])
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
    private fun desenheTexto(pagina: PdfDocument.Page,paint: Paint,string: String,offsetDesenho: OffsetDesenho,limite: Int): OffsetDesenho{
        var x=0
        var y=0
        val tamanhoDisponivel =(offsetDesenho.x-(limite))
        val medidaDoTexto= paint.measureText(string)
        if(medidaDoTexto.toInt()<=tamanhoDisponivel){
            pagina.canvas.drawText(string,offsetDesenho.x.toFloat(),offsetDesenho.y.toFloat(),paint)
            val balds = Rect()
            paint.getTextBounds(string,0,string.length,balds)
            x=balds.width()
            y=balds.height()
            Log.d("CriadorPdf","medidas do offset $x $y   limite $limite")
        }
        else{
            var texto =string
            var offset = OffsetDesenho(offsetDesenho.x,offsetDesenho.y)
            while (!texto.isBlank()){
                Log.d("CriadorPdf","medidas do offset no wile $x $y")
                Log.d("criardor pdf","texto ${texto}")
                val medidaDoTexto= paint.measureText(texto)
                if(medidaDoTexto.toInt()>=tamanhoDisponivel){
                    val quebraDotexto = paint.breakText(texto,true,(pagina.info.pageWidth-50f),null)
                    val linha=texto.substring(0,quebraDotexto)
                    pagina.canvas.drawText(linha,offset.x.toFloat(),offset.y.toFloat(),paint)
                    val balds = Rect()
                    paint.getTextBounds(linha,0,linha.length,balds)
                    offset= OffsetDesenho(x=offsetDesenho.x,y=(balds.height()+3)+offset.y)
                    if(quebraDotexto- texto.length!=0)
                        texto=texto.substring(quebraDotexto,texto.length).toString()
                    else texto=""
                    Log.d("criardor pdf","fim do loop")
                }
                else{
                    Log.d("criardor pdf","fim do loop ${texto}")
                    pagina.canvas.drawText(texto,offset.x.toFloat(),offset.y.toFloat(),paint)
                    val balds = Rect()
                    paint.getTextBounds(texto,0,texto.length,balds)
                    offset= OffsetDesenho(x=offsetDesenho.x,y=(balds.height()+3)+offset.y)
                    texto=""
                }

                Log.d("CriadorPdf","medidas do offset no wile $x $y")
            }

            return offset.copy(x=offsetDesenho.x,y=offsetDesenho.y+offsetDesenho.y)
        }

        return OffsetDesenho(offsetDesenho.x,offsetDesenho.y+y)
    }
    private fun desenharCabesalhoProdutos(pagina: PdfDocument.Page,paint: Paint,dadosDaRequisicao: DadosDaRequisicao,offset: OffsetDesenho): Array<OffsetDesenho>{
        var _offset=mutableListOf<OffsetDesenho>()
        _offset.add(desenheTexto(pagina,paint,"Prod/Serv",OffsetDesenho(50,offset.y)))
        _offset.add(desenheTexto(pagina,paint,"Quantidade",OffsetDesenho(250,offset.y)))
        _offset.add(desenheTexto(pagina,paint,"Preco",OffsetDesenho(380,offset.y)))
        _offset.add(desenheTexto(pagina,paint,"Total",OffsetDesenho(480,offset.y)))

        return _offset.toTypedArray()
    }
    private fun desenharProdutos(pagina: Array< PdfDocument.Page>,pdf: PdfDocument,paint: Paint,listaDeProdutos: List<ProdutoRequisitado>,posicoes: Array<OffsetDesenho>): OffsetDesenho{
        var offset=posicoes[0]
        listaDeProdutos.forEach {
         Log.d("CriadorPdf","desenhando produto offsetAtual x=${offset.x} y=${offset.y}")
        val  offsetAux=desenheTexto(pagina[0],paint,it.nomePrd,offset.copy(x=offset.x,y=offset.y+20),(posicoes[1].x))
                   desenheTexto(pagina[0],paint,it.qnt.toString(),OffsetDesenho(x=posicoes[1].x,y=offset.y+20),posicoes[2].x)
                   desenheTexto(pagina[0],paint,it.preco.toString().formatarPreco(),offset.copy(x=posicoes[2].x,y=offset.y+20),posicoes[3].x)
                   desenheTexto(pagina[0],paint,it.total.toString().formatarPreco(),offset.copy(x=posicoes[3].x,y=offset.y+20),pagina[0].info.pageWidth-50)
            offset=offsetAux.copy(x=posicoes[0].x,y=offsetAux.y+5)
            Log.d("CriadorPdf","desenhando produto offsetFinaloop x=${offset.x} y=${offset.y}")
            if(offset.y>pagina[0].info.pageHeight-100){
                val numero =pagina[0].info.pageNumber
                pdf.finishPage(pagina[0])
                pagina[0]=pdf.startPage(PdfDocument.PageInfo.Builder(595,842,numero+1).create())
                val _paint = Paint()

                offset= OffsetDesenho(x = 50,y= 70)
            }
        }
        return offset
    }
}

data class OffsetDesenho(val x: Int, val y: Int)