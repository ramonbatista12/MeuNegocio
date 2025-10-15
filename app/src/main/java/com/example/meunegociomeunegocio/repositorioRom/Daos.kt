package com.example.meunegociomeunegocio.repositorioRom

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface Daos{
    //querys de clientes
    @Query("SELECT * FROM clientes order by nome asc")
    fun fluxoDeClientes(): Flow<List<EntidadeClientes>>
    @Transaction
    @Query("SELECT * FROM clientes WHERE id = :id")
    fun ClientesPorId(id:Int):Flow<JuncaoClineteTelefoneEndereco?>
    @Transaction
    @Query("SELECT * FROM clientes WHERE nome  like '%'||:nome||'%' order by nome asc ")
    fun pesquisaCliente(nome:String):Flow<List<JuncaoClineteTelefoneEndereco>>
    @Update
    suspend fun atualizarCliente(cliente: EntidadeClientes)
    @Insert
    suspend fun inserirClientes(clientes: EntidadeClientes)
    @Delete
    suspend fun deletarCliente(cliente: EntidadeClientes)
    @Insert
    suspend fun insertTeleforn(telefone: EntidadeTelefone)
    @Insert
    suspend fun insertEndereco(endereco: EntidadeEndereco)
    @Update
    suspend fun updateTelefone(telefone: EntidadeTelefone)
    @Update
    suspend fun updateEndereco(endereco: EntidadeEndereco)
    @Delete
    suspend fun apagarEndereco(endereco: EntidadeEndereco)
    @Delete
    suspend fun apagarTelefone(telefone: EntidadeTelefone)
    //querys Produto Servico
    @Query("Select* from produto_servico")
    fun fluxoProdutoServico():Flow<List<EntidadeProdutoServico>>
    @Query("Select* from produto_servico where nome like '%'||:produto||'%' order by nome asc")
    fun fluxoPesquisaProdutoNome(produto: String):Flow<List<EntidadeProdutoServico>>
    @Query("Select* from produto_servico where descricao like '%'||:produto||'%' order by nome asc")
    fun fluxoPesquisaProdutoDescricao(produto: String):Flow<List<EntidadeProdutoServico>>
    @Query("Select* from produto_servico where preco=:produto order by nome asc")
    fun fluxoPesquisaProdutoPreco(produto: Float):Flow<List<EntidadeProdutoServico>>
    @Query("Select * from produto_servico where id = :id")
    fun produtoPorId(id:Int):Flow<EntidadeProdutoServico>
    @Update
    suspend fun atualizarProdutoServico(produto: EntidadeProdutoServico)
    @Insert
    suspend fun inserirProdutoServico(produto: EntidadeProdutoServico)
    @Delete
    suspend fun deletarProdutoServico(produto: EntidadeProdutoServico)
    // querys Requisicao
    @Query("Select * from requisicao")
    fun fluxoRequisicao():Flow<List<EntidadeRequisicao>>
    @Transaction
   @Query("select * from requisicao where id = :id")
   fun requisicaoPorId(id:Int):Flow<juncaoRequesicaoEstadoClinete?>
   @Query("select * from requisicao where id_est = :id")
   fun requisicaoPorEstado(id:Int):Flow<JuncaoRequesicaoProduto?>
   @Update
   suspend fun atualizarRequisicao(requisicao: EntidadeRequisicao)
   @Insert
   suspend fun inserirRequisicao(requisicao: EntidadeRequisicao)
   @Delete
   suspend fun apagarRequisicao(requisicao: EntidadeRequisicao)
   //querys logs de mudanca
   @Transaction
   @Query("select*from logs_mudancas")
   fun fluxoLogsMudanca():Flow<JuncaoLogsMudancaEstadoNovoEstadoAntigo>
   @Transaction
   @Query("select*from logs_mudancas where id_est_novo=:idEstado")
   fun logsMudancaPorEstado(idEstado:Int):Flow<JuncaoLogsMudancaEstadoNovo?>
    @Transaction
    @Query("select*from logs_mudancas where id_req=:idRequesicao")
    fun HistoricoDeMudancaPorRequisicao(idRequesicao:Int):Flow<List<JuncaoLogsMudancaEstadoNovoEstadoAntigo>>

    // queryes estados
   @Query("select*from estados")
   fun fluxoDeEstados():Flow<List<EntidadeEstado>>
   @Insert
   suspend fun inserirEstado(estado: EntidadeEstado)
   @Update
   suspend fun atualizarEstado(estado: EntidadeEstado)
   @Delete
   suspend fun apagarEstado(estado: EntidadeEstado)





}