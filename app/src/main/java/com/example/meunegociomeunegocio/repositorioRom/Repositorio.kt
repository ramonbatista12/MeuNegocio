package com.example.meunegociomeunegocio.repositorioRom

import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
                                         numero = it.numero,
                                         cep = it.cep)
                            },
                            telefones = it.telefones.map {
                                 Telefone(it.id, idCli = it.idCli ,numero = it.numero,ddd=it.ddd)
                            })
    }
    fun pesquisaClientes(cliente: String)=dao.pesquisaCliente(cliente).map {
        it.map {
            DadosDeClientes(Cliente(it.cliente.id,it.cliente.nome,it.cliente.cpf,it.cliente.cnpj),
                enderecos =  it.enderecos.map {
                    Endereco(id=it.id,
                        cidade = it.cidade,
                        idCli = it.idCli ,
                        estado = it.estado,
                        bairro = it.bairro,
                        rua = it.rua,
                        complemento = it.complemeto,
                        numero = it.numero,
                        cep = it.cep)
                },
                telefones = it.telefones.map {
                    Telefone(it.id, idCli = it.idCli ,numero = it.numero,ddd=it.ddd)
                })}
    }
    fun fluxoProdutoServico(): Flow<List<ProdutoServico>> = dao.fluxoProdutoServico().map { it.map { ProdutoServico(it.id,it.servico,it.nome,it.descrisao,it.preco,it.atiovo)  } }
    fun fluxoDePesquisaDeProdutos(produto: String)=dao.fluxoPesquisaProdutoNome(produto).map { it.map { ProdutoServico(it.id,it.servico,it.nome,it.descrisao,it.preco,it.atiovo)  } }
    fun fluxoRequisicao(): Flow<List<DadosDaRequisicao>> = dao.fluxoRequisicao().map {
        it.map{
            DadosDaRequisicao(requisicao = Requisicao(it.requisicao.id,it.requisicao.idCli,it.requisicao.idEs,it.requisicao.desc ,obs = it.requisicao.obs),
                               estado = Estado(it.estado.id,it.estado.descricao),
                               cliente = Cliente(it.cliente.id,it.cliente.nome,it.cliente.cpf,it.cliente.cnpj))

        }
    }
    fun fluxoHistoricoDeMudancas(id:Int)=dao.HistoricoDeMudancaPorRequisicao(id).map { it.map {  Mudanca(it.logs.id,it.logs.dataMudanca,
                                                                        it.logs.idReq,
                                                                        if(it.estadoAntigo == null) null else Estado(it.estadoAntigo.id, descricao = it.estadoAntigo.descricao),
                                                                        Estado(it.estadoNovo.id, descricao = it.estadoNovo.descricao)) }}
    fun custoTotalPorRequisicao(id:Int)=dao.valorTotalRequisitado(id)
    fun requisicaoPorId(id:Int)=dao.requisicaoPorId(id).map {
        if(it==null) null
        else
         DadosDaRequisicao(requisicao=Requisicao(it.requisicao.id ,it.requisicao.idCli,it.requisicao.idEs,it.requisicao.desc,it.requisicao.obs),
                          estado = Estado(it.estado.id,it.estado.descricao),
                          cliente =  Cliente(it.cliente.id,it.cliente.nome,it.cliente.cpf,it.cliente.cnpj)  )


    }
    fun produtoRequisitado(idReq:Int)=dao.requisicaoProdutoPorId(idReq).map {
        it.map {
            ProdutoRequisitado(id = it.id, idProd = it.idPrd, nomePrd = it.nomePrd, qnt = it.qnt, preco = it.preco, total = it.total, produtoServico = it.servico)
        }
    }
    fun fluxoPodutoPorID(id:Int)=dao.produtoPorId(id).map {
        if(it==null)null
        else
        ProdutoServico(id=it.id, servico = it.servico,nome=it.nome,descrisao = it.descrisao, preco = it.preco,ativo=it.atiovo)}
    fun fluxoDeEstados()=dao.fluxoDeEstados()
    fun fluxoDeRequisicoesPorCliente(query:String)=dao.RequisicaoPorCliente(query).map {
        it.map {
            DadosDaRequisicao(requisicao = Requisicao(it.requisicao.id,it.requisicao.idCli,it.requisicao.idEs,it.requisicao.desc ,obs = it.requisicao.obs),
                estado = Estado(it.estado.id,it.estado.descricao),
                cliente = Cliente(it.cliente.id,it.cliente.nome,it.cliente.cpf,it.cliente.cnpj))
        }


    }
    fun fluxoDeRequisicoesPorDataDeMudanca(query: String)=dao.RequisicaoPorData(query).map {
        it.map {
            DadosDaRequisicao(requisicao = Requisicao(it.requisicao.id,it.requisicao.idCli,it.requisicao.idEs,it.requisicao.desc ,obs = it.requisicao.obs),
                estado = Estado(it.estado.id,it.estado.descricao),
                cliente = Cliente(it.cliente.id,it.cliente.nome,it.cliente.cpf,it.cliente.cnpj))
        }


    }
    fun fluxoDeRequisicaoPorEstado(query: String)=dao.RequisicaoPorEstado(query).map {
        it.map {
            DadosDaRequisicao(requisicao = Requisicao(it.requisicao.id,it.requisicao.idCli,it.requisicao.idEs,it.requisicao.desc ,obs = it.requisicao.obs),
                estado = Estado(it.estado.id,it.estado.descricao),
                cliente = Cliente(it.cliente.id,it.cliente.nome,it.cliente.cpf,it.cliente.cnpj))
        }


    }
    // acoes em clientes
    suspend fun inserirCliente(cliente: Cliente)= coroutinesScope.async {   dao.inserirClientes(EntidadeClientes(cliente.id,cliente.nome,cliente.cpf,cliente.cnpj))}.await()
    suspend fun apagarCliente(clientes: Cliente)=coroutinesScope.launch { dao.deletarCliente(EntidadeClientes(clientes.id,clientes.nome,clientes.cpf,clientes.cnpj)) }
    suspend fun atuAlizarCliente(clientes: Cliente)=coroutinesScope.launch { dao.atualizarCliente( EntidadeClientes(clientes.id,clientes.nome,clientes.cpf,clientes.cnpj)) }
    //acoes em endereco
    suspend fun inserirEndereco(endereco: Endereco)=coroutinesScope.launch { dao.insertEndereco(EntidadeEndereco(endereco.id,endereco.idCli,endereco.cidade,endereco.estado,endereco.bairro,endereco.rua,endereco.complemento,endereco.numero,endereco.cep)) }
    suspend fun apagarEndereco(endereco: Endereco)=coroutinesScope.launch { dao.apagarEndereco(EntidadeEndereco(endereco.id,endereco.idCli,endereco.cidade,endereco.estado,endereco.bairro,endereco.rua,endereco.complemento,endereco.numero,endereco.cep)) }
    suspend fun atualizarEndereco(endereco: Endereco)=coroutinesScope.launch { dao.updateEndereco(EntidadeEndereco(endereco.id,endereco.idCli,endereco.cidade,endereco.estado,endereco.bairro,endereco.rua,endereco.complemento,endereco.numero,endereco.cep)) }
    //acoes em Telefone
    suspend fun atulizarTelefone(telefone: Telefone)=coroutinesScope.launch { dao.updateTelefone(EntidadeTelefone(telefone.id,telefone.idCli,telefone.ddd,telefone.numero))}
    suspend fun inserirTelefone(telefone: Telefone)=coroutinesScope.launch { dao.insertTeleforn(EntidadeTelefone(telefone.id,telefone.idCli,telefone.ddd,telefone.numero))}
    suspend fun apagarTelefone(telefone: Telefone)=coroutinesScope.launch { dao.apagarTelefone(EntidadeTelefone(telefone.id,telefone.idCli,telefone.ddd,telefone.numero)) }
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