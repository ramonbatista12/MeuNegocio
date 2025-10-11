package com.example.meunegociomeunegocio.navegacao

import android.graphics.drawable.Drawable
import com.example.meunegociomeunegocio.R
import java.util.Vector

sealed class IconesDeDestino(val idIcone: Int,val drawable: Drawable?=null){
    object Clientes: IconesDeDestino(idIcone = R.drawable.cliente_2_24)
    object Produtos: IconesDeDestino(idIcone =R.drawable.shoppingmode_53dp_000000_fill0_wght400_grad0_opsz48 ,null)
    object Requisicoes: IconesDeDestino(idIcone = R.drawable.round_inventory_2_24)
}

