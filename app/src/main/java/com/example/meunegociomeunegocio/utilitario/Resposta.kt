package com.example.meunegociomeunegocio.utilitario
/**
 * classe respsonsavel por representar uma respostas de qualquer operacao
 * aqui ela sera usda para repesentar a respsota da criacao do pdf
 * retornando a uri para a chamada da intente que vai permitir compartilhar o pdf
 * caso o pdf seja criado com sucesso
 * **/
sealed class Resposta{
     class OK<T>(val respsosta:T): Resposta()
     class Erro<T>(val erro:T): Resposta()
     object _Erro: Resposta()

}