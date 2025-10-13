package com.example.meunegociomeunegocio.repositorioRom

import com.example.meunegociomeunegocio.navegacao.IconesDeDestino
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
    fun fluxoDadosDoCliente(id: Int)=dao.ClientesPorId(id).map {
        if(it==null)null
        else
            DadosDeClientes(Cliente(it.cliente.id,it.cliente.nome,it.cliente.cpf,it.cliente.cnpj),
                            enderecos =  it.enderecos.map {
                                Endereco(id=it.id,
                                         cidade = it.cidade,
                                         idCli = it.idCli ,
                                         estado = it.estado,
                                         bairro = it.bairro,
                                         rua = it.rua,
                                         complemento = it.complemeto,
                                         numero = it.numero)
                            },
                            telefones = it.telefones.map {
                                 Telefone(it.id, idCli = it.idCli ,numero = it.numero,ddd=it.ddd)
                            })
    }
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
    suspend fun inserirCliente(cliente: Cliente)= coroutinesScope.launch {   dao.inserirClientes(EntidadeClientes(cliente.id,cliente.nome,cliente.cpf,cliente.cnpj))}
    suspend fun apagarCliente(clientes: Cliente)=coroutinesScope.launch { dao.deletarCliente(EntidadeClientes(clientes.id,clientes.nome,clientes.cpf,clientes.cnpj)) }
    suspend fun atuAlizarCliente(clientes: Cliente)=coroutinesScope.launch { dao.atualizarCliente( EntidadeClientes(clientes.id,clientes.nome,clientes.cpf,clientes.cnpj)) }
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