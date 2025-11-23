package com.example.meunegociomeunegocio.repositorioRom

import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class Repositorio @Inject constructor(private val roomBd: RoomBd) {
    private val dao = roomBd.assesoAosDados()

    // fluxos
    fun fluxoDeClientes(): Flow<List<Cliente>> =dao.fluxoDeClientes().map { it.map { it.toCliente() } }
    fun fluxoDadosDoCliente(id: Int)=dao.ClientesPorId(id).map {if(it==null)null else it.toDadosDeClientes()}
    fun pesquisaClientes(cliente: String)=dao.pesquisaCliente(cliente).map {it.map {DadosDeClientes(cliente = it.cliente.toCliente(),enderecos =  it.enderecos.map {it.toEndereco()},telefones = it.telefones.map {it.toTelefone()})}}
    fun fluxoProdutoServico(): Flow<List<ProdutoServico>> = dao.fluxoProdutoServico().map { it.map { it.toProdutoServico()  } }
    fun fluxoDePesquisaDeProdutos(produto: String)=dao.fluxoPesquisaProdutoNome(produto).map { it.map { it.toProdutoServico()  } }
    fun fluxoRequisicao(): Flow<List<DadosDaRequisicao>> = dao.fluxoRequisicao().map {it.map{it.toDadosDaRequisicao()}}
    fun fluxoHistoricoDeMudancas(id:Int)=dao.HistoricoDeMudancaPorRequisicao(id).map { it.map { it.toMudancas() }}
    fun custoTotalPorRequisicao(id:Int)=dao.valorTotalRequisitado(id)
    fun requisicaoPorId(id:Int)=dao.requisicaoPorId(id).map {if(it==null) null  else it.toDadosDaRequisicao()}
    fun produtoRequisitado(idReq:Int)=dao.requisicaoProdutoPorId(idReq).map {it.map {it.toProdutoRequisitado()}}
    fun fluxoPodutoPorID(id:Int)=dao.produtoPorId(id).map {if(it==null)null else it.toProdutoServico()}
    fun fluxoDeEstados()=dao.fluxoDeEstados()
    fun fluxoDeRequisicoesPorCliente(query:String)=dao.RequisicaoPorCliente(query).map {it.map {it.toDadosDaRequisicao()}}
    fun fluxoDeRequisicoesPorDataDeMudanca(query: String)=dao.RequisicaoPorData(query).map {it.map {it.toDadosDaRequisicao()}}
    fun fluxoDeRequisicaoPorEstado(query: String)=dao.RequisicaoPorEstado(query).map {it.map {it.toDadosDaRequisicao() }}
    // acoes em clientes
    suspend fun inserirCliente(cliente: Cliente)= withContext(Dispatchers.IO) {   dao.inserirClientes(cliente.toEntidadeCliente())}
    suspend fun apagarCliente(clientes: Cliente)=withContext(Dispatchers.IO){ dao.deletarCliente(clientes.toEntidadeCliente()) }
    suspend fun atuAlizarCliente(clientes: Cliente)=withContext(Dispatchers.IO) { dao.atualizarCliente( clientes.toEntidadeCliente()) }
    //acoes em endereco
    suspend fun inserirEndereco(endereco: Endereco)=withContext(Dispatchers.IO) { dao.insertEndereco(endereco.toEntidadeEndereco()) }
    suspend fun apagarEndereco(endereco: Endereco)=withContext(Dispatchers.IO) { dao.apagarEndereco(endereco.toEntidadeEndereco()) }
    suspend fun atualizarEndereco(endereco: Endereco)=withContext(Dispatchers.IO) { dao.updateEndereco(endereco.toEntidadeEndereco()) }
    //acoes em Telefone
    suspend fun atulizarTelefone(telefone: Telefone)=withContext(Dispatchers.IO) { dao.updateTelefone(telefone.toEntidadeTelefone())}
    suspend fun inserirTelefone(telefone: Telefone)=withContext(Dispatchers.IO) { dao.insertTeleforn(telefone.toEntidadeTelefone())}
    suspend fun apagarTelefone(telefone: Telefone)=withContext(Dispatchers.IO) { dao.apagarTelefone(telefone.toEntidadeTelefone()) }
    //acoes em produtos
    suspend fun inserirProdutoServico(produto: ProdutoServico)=withContext(Dispatchers.IO) { dao.inserirProdutoServico(produto.toEntidadeProdutoServico()) }
    suspend fun apagarProdutoServico(produto: ProdutoServico)=withContext(Dispatchers.IO) { dao.deletarProdutoServico(produto.toEntidadeProdutoServico())  }
    suspend fun atualizarProdutoServico(produto: ProdutoServico)=withContext(Dispatchers.IO) { dao.atualizarProdutoServico(produto.toEntidadeProdutoServico())}
    //acoes em requisicoes
    suspend fun inserirRequisicao(requisicao: EntidadeRequisicao)=withContext(Dispatchers.IO) { dao.inserirRequisicao(requisicao) }
    suspend fun atulizarRequisicao(requisicao: EntidadeRequisicao)=withContext(Dispatchers.IO) { dao.atualizarRequisicao(requisicao) }
    suspend fun apagarRequisicao(requisicao: EntidadeRequisicao)=withContext(Dispatchers.IO) { dao.apagarRequisicao(requisicao)}
    // acoes em estados


}