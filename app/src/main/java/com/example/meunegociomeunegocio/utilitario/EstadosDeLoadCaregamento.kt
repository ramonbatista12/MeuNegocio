package com.example.meunegociomeunegocio.utilitario

sealed class EstadosDeLoadCaregamento{
    object  load: EstadosDeLoadCaregamento()
    object Erro: EstadosDeLoadCaregamento()
    object Empty: EstadosDeLoadCaregamento()
    data class  Caregado<T>(val obj:T): EstadosDeLoadCaregamento()

}

sealed class EstadoLoadAcoes{
    object  Iniciando: EstadoLoadAcoes()
    object Criando: EstadoLoadAcoes()
    object Erro: EstadoLoadAcoes()
    object Sucesso: EstadoLoadAcoes()
}