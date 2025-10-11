package com.example.meunegociomeunegocio.repositorioRom

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [EntidadeClientes::class,
                      EntidadeEndereco::class,
                      EntidadeEstado::class,
                      EntidadeLogsDeMudacaas::class,
                      EntidadeProdutoServico::class,
                      EntidadeRequisicao::class,
                      EntidadeTelefone::class,
                      EntidadeRequesicaoProduto::class],version=1)
abstract class RoomBd :RoomDatabase(){
    abstract fun assesoAosDados(): Daos

}