package com.example.meunegociomeunegocio.repositorioRom


fun EntidadeClientes.toCliente(): Cliente= Cliente(this.id,this.nome,this.cpf,this.cnpj)
fun Cliente.toEntidadeCliente(): EntidadeClientes= EntidadeClientes(this.id,this.nome,this.cpf,this.cnpj)
fun EntidadeTelefone.toTelefone(): Telefone= Telefone(this.id,this.idCli,this.ddd,this.numero)
fun Telefone.toEntidadeTelefone(): EntidadeTelefone= EntidadeTelefone(this.id,this.idCli,this.ddd,this.numero)
fun EntidadeEndereco.toEndereco(): Endereco= Endereco(id=this.id, idCli = this.idCli, rua = this.rua, numero = this.numero, bairro = this.bairro,
    cidade = this.cidade, cep = this.cep, estado = this.estado, complemento = this.complemeto)
fun Endereco.toEntidadeEndereco(): EntidadeEndereco= EntidadeEndereco(id=this.id, idCli = this.idCli, rua = this.rua, numero = this.numero, bairro = this.bairro,
    cidade = this.cidade, cep = this.cep, estado = this.estado, complemeto = this.complemento)
fun EntidadeRequisicao.toRequisicao(): Requisicao = Requisicao(id = this.id, idCli = this.idCli, idEs = this.idEs, desc = this.desc, obs = this.obs)
fun Requisicao.toEntidadeRequisicao(): EntidadeRequisicao = EntidadeRequisicao(id = this.id, idCli = this.idCli, idEs = this.idEs, desc = this.desc?:"", obs = this.obs?:"")
fun EntidadeProdutoServico.toProdutoServico(): ProdutoServico=ProdutoServico(id = this.id, servico = this.servico, nome = this.nome, descrisao = this.descrisao, preco = this.preco, ativo = this.atiovo)
fun ProdutoServico.toEntidadeProdutoServico(): EntidadeProdutoServico=EntidadeProdutoServico(id = this.id, servico = this.servico, nome = this.nome, descrisao = this.descrisao, preco = this.preco, atiovo = this.ativo)
fun EntidadeEstado.toEstado(): Estado =Estado(id = this.id, descricao = this.descricao)
fun JuncaoLogsMudancaEstadoNovoEstadoAntigo.toMudancas(): Mudanca= Mudanca(id = this.logs.id, data = this.logs.dataMudanca, idReq = this.logs.idReq, idEstAntigo = this.estadoAntigo?.toEstado() ?: null, idEstNovo = this.estadoNovo.toEstado())
fun juncaoRequesicaoEstadoClinete.toDadosDaRequisicao(): DadosDaRequisicao = DadosDaRequisicao(requisicao = this.requisicao.toRequisicao(), estado = this.estado.toEstado(),cliente = this.cliente.toCliente())
fun JuncaoClineteTelefoneEndereco.toDadosDeClientes(): DadosDeClientes= DadosDeClientes(this.cliente.toCliente(),this.telefones.map { it.toTelefone() },this.enderecos.map { it.toEndereco() })
fun ProdutoSolicitado.toProdutoRequisitado(): ProdutoRequisitado= ProdutoRequisitado(id = this.id, idProd = this.idPrd, nomePrd = this.nomePrd, qnt = this.qnt, preco = this.preco, total = this.total, produtoServico = this.servico)
fun EntidadeRequesicaoProduto.toProdutoRequisicao(): ProdutoRequisicao=ProdutoRequisicao(id = this.id, idReq = this.idReq, idProd = this.idProd, qnt = this.qnr)
fun ProdutoRequisicao.toEntidadeRequesicaoProduto(): EntidadeRequesicaoProduto= EntidadeRequesicaoProduto(id=this.id, idReq = this.idReq, idProd = this.idProd, qnr = this.qnt)
