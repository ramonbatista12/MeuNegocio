package com.example.meunegociomeunegocio.hillt

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.meunegociomeunegocio.Application
import com.example.meunegociomeunegocio.repositorioRom.EstadoRequisicao
import com.example.meunegociomeunegocio.repositorioRom.Repositorio
import com.example.meunegociomeunegocio.repositorioRom.RoomBd
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object  ModuloRom{
    @Provides
    @Singleton
     fun providerRoom(@ApplicationContext context: Context): RoomBd{

         return Room.databaseBuilder(context =context ,
                                     klass = RoomBd::class.java,
                                     name = "nome").addCallback(object : RoomDatabase.Callback() {
             override fun onCreate(db: SupportSQLiteDatabase) {
                 super.onCreate(db)
                 db.execSQL("INSERT or ignore INTO estados (id,descricao) VALUES (${EstadoRequisicao.Cancelado.id},'${EstadoRequisicao.Cancelado.descricao}'),(${EstadoRequisicao.Confirmado.id},'${EstadoRequisicao.Confirmado.descricao}'),(${EstadoRequisicao.Pendente.id},'${EstadoRequisicao.Pendente.descricao}'),(${EstadoRequisicao.Entregue.id},'${EstadoRequisicao.Entregue.descricao}')")
                 db.execSQL("""
                      create trigger if not exists inserindo_requisicao after insert on requisicao
                      for each row
                       begin
                       insert into logs_mudancas(id_req,id_est_novo,data_mudanca) values(new.id,new.id_est,datetime('now'));
                       end ;
                 """.trimIndent())
                 db.execSQL("""
                      create trigger if not exists update_requisicao after update on requisicao
                      for each row
                       WHEN OLD.id_est IS NULL OR OLD.id_est != NEW.id_est
                       begin
                       insert into logs_mudancas(id_req,id_est_antigo,id_est_novo,data_mudanca) values(new.id,old.id_est,new.id_est,datetime('now'));
                       end ;
                 """.trimIndent())
             }
                                     }).build()
     }
    @Provides
    @Singleton
    fun providerRepositorio(roomBd: RoomBd): Repositorio{
        return Repositorio(roomBd)
    }
    @Provides
    @Singleton
    fun providerCriadorPdf(@ApplicationContext context: Context): com.example.meunegociomeunegocio.pdf.CriadorDePfd{
        return com.example.meunegociomeunegocio.pdf.CriadorDePfd(context)

    }
}