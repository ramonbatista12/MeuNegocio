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
    suspend fun inserirClientes(clientes: EntidadeClientes): Long
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
    fun produtoPorId(id:Int):Flow<EntidadeProdutoServico?>
    @Update
    suspend fun atualizarProdutoServico(produto: EntidadeProdutoServico)
    @Insert
    suspend fun inserirProdutoServico(produto: EntidadeProdutoServico)
    @Delete
    suspend fun deletarProdutoServico(produto: EntidadeProdutoServico)

    // querys Requisicao
    @Query("Select * from requisicao")
    fun fluxoRequisicao():Flow<List<juncaoRequesicaoEstadoClinete>>
   @Transaction
   @Query("select * from requisicao where id = :id")
   fun requisicaoPorId(id:Int):Flow<juncaoRequesicaoEstadoClinete?>
   @Transaction
   @Query("select rps.id as id,rps.id_prd,ps.nome,rps.quantidade,ps.produto_servico,ps.preco," +
           " ps.preco*rps.quantidade as total  from " +
          "requisicao_produto_servico as rps join produto_servico as ps on rps.id_prd=ps.id " +
          " where rps.id_req=:id")
   fun requisicaoProdutoPorId(id:Int):Flow<List<ProdutoSolicitado>>
   @Query("select * from requisicao where id_est = :id")
   fun requisicaoPorEstado(id:Int):Flow<JuncaoRequesicaoProduto?>
   @Transaction
   @Query("select*from requisicao where id_cli in (select id from clientes where nome like '%'||:query||'%' or cpf like '%'||:query||'%' or cnpj like '%'||:query||'%')")
   fun RequisicaoPorCliente(query:String ):Flow<List<juncaoRequesicaoEstadoClinete>>
   @Transaction
   @Query("select*from requisicao where id_est in (select id from estados where descricao like '%'||:query||'%')")
   fun RequisicaoPorEstado(query:String ):Flow<List<juncaoRequesicaoEstadoClinete>>
   @Transaction
   @Query("select*from requisicao where id in(select id_req from logs_mudancas where data_mudanca like '%'||:query||'%') ")
   fun RequisicaoPorData(query:String ):Flow<List<juncaoRequesicaoEstadoClinete>>
   @Update
   suspend fun atualizarRequisicao(requisicao: EntidadeRequisicao)
   @Insert
   suspend fun inserirRequisicao(requisicao: EntidadeRequisicao): Long
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
    @Query("select*from logs_mudancas where id_req=:idRequesicao order by data_mudanca desc")
    fun HistoricoDeMudancaPorRequisicao(idRequesicao:Int):Flow<List<JuncaoLogsMudancaEstadoNovoEstadoAntigo>>
    @Transaction
    @Query("select sum(total ) from (select rps.quantidade*ps.preco as total from requisicao_produto_servico rps join produto_servico ps on rps.id_prd=ps.id where id_req=:idRequesicao )")
    fun  valorTotalRequisitado(idRequesicao: Int):Flow<Double?>
    // queryes estados
   @Query("select*from estados")
   fun fluxoDeEstados():Flow<List<EntidadeEstado>>
   @Insert
   suspend fun inserirEstado(estado: EntidadeEstado)
   @Update
   suspend fun atualizarEstado(estado: EntidadeEstado)
   @Delete
   suspend fun apagarEstado(estado: EntidadeEstado)
   //querys na tabela associativa requisicao produto
   @Insert
   suspend fun insertProdutosRequisitados(list: List<EntidadeRequesicaoProduto>)
   @Query("delete from requisicao_produto_servico where id_req=:id")
   suspend fun apagarProdutoPorRequisicao(id:Int)
   @Update
   suspend fun atualizarProdutoRequisicao(produto: EntidadeRequesicaoProduto)
   @Delete
   suspend fun excluitProdutoRequisitado(produto: EntidadeRequesicaoProduto)



}