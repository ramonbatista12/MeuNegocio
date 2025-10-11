package com.example.meunegociomeunegocio.repositorioRom

import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class Repositorio @Inject constructor(private val roomBd: RoomBd) {
    private val dao = roomBd.assesoAosDados()
    private  val coroutinesScope= CoroutineScope(Dispatchers.IO)
    // fluxos
    fun fluxoDeClientes(): Flow<List<Cliente>> =dao.fluxoDeClientes().map { it.map { Cliente(it.id,it.nome,it.cpf,it.cnpj) } }
    fun fluxoProdutoServico(): Flow<List<ProdutoServico>> = dao.fluxoProdutoServico().map { it.map { ProdutoServico(it.id,it.servico,it.nome,it.descrisao,it.preco,it.atiovo)  } }
    fun fluxoRequisicao()=dao.fluxoRequisicao()
    fun fluxoHistoricoDeMudancas(id:Int)=dao.HistoricoDeMudancaPorRequisicao(id).map { it.map {  Mudanca(it.logs.id,
                                                                        it.logs.idReq,
                                                                        if(it.estadoAntigo == null) null else Estado(it.estadoAntigo.id, descricao = it.estadoAntigo.descricao),
                                                                        Estado(it.estadoNovo.id, descricao = it.estadoNovo.descricao)) }}
    fun requisicaoPorId(id:Int)=dao.requisicaoPorId(id).map {  }
    fun fluxoRequisicaoPorEstado(id:Int)=dao.requisicaoPorEstado(id)
    fun fluxoDeEstados()=dao.fluxoDeEstados()
    // acoes em clientes
    suspend fun inserirCliente(cliente: EntidadeClientes)= coroutinesScope.launch {   dao.inserirClientes(cliente)}
    suspend fun apagarCliente(clientes: EntidadeClientes)=coroutinesScope.launch { dao.deletarCliente(clientes) }
    suspend fun atuAlizarCliente(clientes: EntidadeClientes)=coroutinesScope.launch { dao.atualizarCliente(clientes) }
    //acoes em endereco
    suspend fun inserirEndereco(endereco: EntidadeEndereco)=coroutinesScope.launch { dao.insertEndereco(endereco) }
    suspend fun apagarEndereco(endereco: EntidadeEndereco)=coroutinesScope.launch { dao.apagarEndereco(endereco) }
    suspend fun atualizarEndereco(endereco: EntidadeEndereco)=coroutinesScope.launch { dao.updateEndereco(endereco) }
    //acoes em produtos
    suspend fun inserirProdutoServico(produto: ProdutoServico)=coroutinesScope.launch { dao.inserirProdutoServico(EntidadeProdutoServico(produto.id,produto.servico,produto.nome,produto.descrisao,produto.preco,produto.ativo)) }
    suspend fun apagarProdutoServico(produto: ProdutoServico)=coroutinesScope.launch { dao.deletarProdutoServico(EntidadeProdutoServico(produto.id,produto.servico,produto.nome,produto.descrisao,produto.preco,produto.ativo))  }
    suspend fun atualizarProdutoServico(produto: ProdutoServico)=coroutinesScope.launch { dao.atualizarProdutoServico(EntidadeProdutoServico(produto.id,produto.servico,produto.nome,produto.descrisao,produto.preco,produto.ativo))}
    //acoes em requisicoes
    suspend fun inserirRequisicao(requisicao: EntidadeRequisicao)=coroutinesScope.launch { dao.inserirRequisicao(requisicao) }
    suspend fun atulizarRequisicao(requisicao: EntidadeRequisicao)=coroutinesScope.launch { dao.atualizarRequisicao(requisicao) }
    suspend fun apagarRequisicao(requisicao: EntidadeRequisicao)=coroutinesScope.launch { dao.apagarRequisicao(requisicao)}
    // acoes em estados


}