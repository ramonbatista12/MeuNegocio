package com.example.meunegociomeunegocio.repositorioRom

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

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
