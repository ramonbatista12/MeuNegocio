package com.example.meunegociomeunegocio.utilitario

sealed class EstadosDeLoad{
    object  load: EstadosDeLoad()
    object Erro: EstadosDeLoad()
    object Empty: EstadosDeLoad()
    data class  Caregado<T>(val obj:T): EstadosDeLoad()

}