package com.example.meunegociomeunegocio.navegacao

import android.graphics.drawable.Drawable
import com.example.meunegociomeunegocio.R
import java.util.Vector

sealed class IconesDeDestino(val idIcone: Int,val drawable: Drawable?=null,val rota: DestinosDeNavegacao){
    object Clientes: IconesDeDestino(idIcone = R.drawable.cliente_2_24, rota = DestinosDeNavegacao.Clientes)
    object Produtos: IconesDeDestino(idIcone =R.drawable.ic_shoping ,null,rota=DestinosDeNavegacao.Produtos)
    object Requisicoes: IconesDeDestino(idIcone = R.drawable.round_inventory_2_24, rota = DestinosDeNavegacao.Requisicoes)

    companion object{
        val listaDeIcones=listOf(Clientes,Requisicoes,Produtos)
    }

}

