package com.example.meunegociomeunegocio.repositorioRom

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "clientes", indices = [Index("nome"), Index("cpf"), Index("cnpj")])
data class EntidadeClientes(@PrimaryKey(autoGenerate = true)
                            @ColumnInfo(name = "id")
                            val id:Int,
                            @ColumnInfo(name = "nome")
                            val nome:String,
                            @ColumnInfo(name = "cpf")
                            val cpf:String? ,
                            @ColumnInfo(name = "cnpj")
                            val cnpj:String?,)

@Entity(tableName = "telefone",
        foreignKeys = [ForeignKey(entity = EntidadeClientes::class,childColumns =["id_cli"], parentColumns = ["id"],onDelete = ForeignKey.CASCADE  )])
data class EntidadeTelefone(@PrimaryKey(autoGenerate = true)
                            @ColumnInfo("id")
                            val id:Int,
                            @ColumnInfo(name = "id_cli")
                            val idCli:Int,
                            @ColumnInfo(name = "ddd")
                            val ddd:String,
                            @ColumnInfo(name = "numeor")
                            val numero:String)

@Entity(tableName = "endereco",
        foreignKeys = [ForeignKey(entity = EntidadeClientes::class,parentColumns = ["id"],childColumns =["id_cli"], onDelete = CASCADE)],  )
data class EntidadeEndereco(@ColumnInfo("id")
                            @PrimaryKey(autoGenerate = true)
                            val id:Int ,
                            @ColumnInfo("id_cli")
                            val idCli:Int,
                            @ColumnInfo("cidade")
                            val cidade:String,
                            @ColumnInfo(name = "estado")
                            val estado:String,
                            @ColumnInfo(name = "bairro")
                            val bairro:String,
                            @ColumnInfo(name = "complemento")
                            val complemeto:String,
                            @ColumnInfo(name = "rua")
                            val rua:String,
                            @ColumnInfo(name = "numero")
                            val numero:String,
                            @ColumnInfo(name = "cep")
                            val cep:String )


data class JuncaoClineteTelefoneEndereco(
    @Embedded val cliente: EntidadeClientes,
    @Relation(
        parentColumn = "id",
        entityColumn = "id_cli")
    val telefones: List<EntidadeTelefone>,
   @Relation(parentColumn = "id",
             entityColumn = "id_cli")
    val enderecos:List<EntidadeEndereco>
)

@Entity("produto_servico", indices = [Index("nome"), Index("descricao"), Index("preco")])
data class EntidadeProdutoServico(@ColumnInfo("id")
                                  @PrimaryKey(autoGenerate = true)
                                  val id: Int ,
                                  @ColumnInfo("produto_servico")
                                  val servico:Boolean,
                                  @ColumnInfo("nome")
                                  val nome: String,
                                  @ColumnInfo(name = "descricao")
                                  val descrisao:String,
                                  @ColumnInfo("preco")
                                  val preco:Float,
                                  @ColumnInfo("ativo", defaultValue = "1")
                                  val atiovo:Boolean)
@Entity(tableName = "estados")
data class EntidadeEstado(@ColumnInfo("id")
                          @PrimaryKey(autoGenerate = true)
                          val  id:Int,
                          @ColumnInfo("descricao")
                          val descricao:String,)

@Entity(tableName = "requisicao",
        foreignKeys = [ForeignKey(entity =EntidadeEstado::class,parentColumns = ["id"],childColumns = ["id_est"]),
                      ForeignKey(entity = EntidadeClientes::class,parentColumns = ["id"],childColumns = ["id_cli"] ) ])

data class EntidadeRequisicao(@ColumnInfo("id")
                              @PrimaryKey(autoGenerate = true)
                              val id: Int,
                              @ColumnInfo("id_cli")
                              val idCli:Int,
                              @ColumnInfo("id_est")
                              val idEs:Int,
                              @ColumnInfo("descricao")
                              val desc:String,
                              @ColumnInfo("observacoes")
                              val obs:String)


data class JuncaoRequesicaoEstado(
    @Embedded val requisicao: EntidadeRequisicao,
    @Relation( parentColumn = "id_est",entityColumn = "id")
    val estado: EntidadeEstado
)

data class juncaoRequesicaoEstadoClinete(@Embedded val requisicao: EntidadeRequisicao,
                                        @Relation(parentColumn = "id_est",entityColumn = "id") val estado: EntidadeEstado,
                                        @Relation(parentColumn = "id_cli",entityColumn = "id")  val cliente: EntidadeClientes)

data class JuncaoRequesicaoCliente(@Embedded val requisicao: EntidadeRequisicao,
                                   @Relation(parentColumn = "id_cli", entityColumn = "id") val cliente:EntidadeClientes)

data class ProdutoSolicitado(@ColumnInfo(name = "id")val id:Int,
                             @ColumnInfo(name = "id_prd") val idPrd:Int,
                             @ColumnInfo(name = "nome") val nomePrd:String,
                             @ColumnInfo(name = "quantidade") val qnt:Int,
                             @ColumnInfo(name = "preco")val preco:Float,
                             @ColumnInfo(name = "total")val total:Float,
                             @ColumnInfo(name = "produto_servico") val servico:Boolean)

@Entity("requisicao_produto_servico",
         foreignKeys = [ForeignKey(entity = EntidadeRequisicao::class,parentColumns = ["id"],childColumns = ["id_req"]),
                        ForeignKey(entity = EntidadeProdutoServico::class,parentColumns = ["id"],childColumns = ["id_prd"] )],

                                   )
data class EntidadeRequesicaoProduto(@ColumnInfo(name = "id")
                                     @PrimaryKey(autoGenerate = true)
                                     val id: Int,
                                     @ColumnInfo("id_req")
                                     val idReq:Int,
                                     @ColumnInfo(name = "id_prd")
                                     val idProd:Int,
                                     @ColumnInfo(name="quantidade")
                                     val qnr:Int )

data class JuncaoRequesicaoProduto(@Embedded val requisicao: EntidadeRequisicao,
                                   @Relation(parentColumn = "id", entityColumn = "id", associateBy = Junction(EntidadeRequesicaoProduto::class)) val produtos:List<EntidadeProdutoServico>)


data class JuncaoRequisicaoCompleta(
    @Embedded val requisicao: EntidadeRequisicao,

    @Relation(
        parentColumn = "id_est",
        entityColumn = "id"
    )
    val estado: EntidadeEstado,

    @Relation(
        parentColumn = "id_cli",
        entityColumn = "id"
    )
    val cliente: EntidadeClientes,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = EntidadeRequesicaoProduto::class,
            parentColumn = "id_req",
            entityColumn = "id_prd"
        )
    )
    val produtos: List<EntidadeProdutoServico>
)

data class  JuncaoRequisicaoClienteEstadoProduto(@Relation(parentColumn ="id_req" , entityColumn = "id") val juncaoRequisicaoClientne: JuncaoRequisicaoClienteEstadoProduto,
                                                 @Relation(parentColumn = "id_prd", entityColumn = "id", associateBy = Junction(EntidadeRequesicaoProduto::class)) val produtos:List<EntidadeProdutoServico>)

@Entity(tableName = "logs_mudancas",
        foreignKeys = [ForeignKey(entity = EntidadeRequisicao::class, parentColumns = ["id"], childColumns = ["id_req"] ),
                       ForeignKey(entity = EntidadeEstado::class, parentColumns = ["id"], childColumns = ["id_est_antigo"]),
                       ForeignKey(entity = EntidadeEstado::class, parentColumns = ["id"], childColumns = ["id_est_novo"])],
        indices = [Index("data_mudanca")])
data class EntidadeLogsDeMudacaas(@ColumnInfo("id")
                                  @PrimaryKey(autoGenerate = true)
                                  val id:Int ,
                                  @ColumnInfo("id_req")
                                  val idReq:Int,
                                  @ColumnInfo("id_est_antigo")
                                  val idEstAntigo:Int?,
                                  @ColumnInfo("id_est_novo")
                                  val idEstNovo:Int,
                                  @ColumnInfo("data_mudanca")
                                  val dataMudanca:String)


data class  JuncaoLogsMudancaEstadoNovo(@Embedded val logs: EntidadeLogsDeMudacaas,
                                    @Relation(parentColumn = "id_est_novo", entityColumn = "id") val estadoAntigo: EntidadeEstado)

data class JuncaoLogsMudancaEstadoNovoEstadoAntigo(
    @Embedded val logs: EntidadeLogsDeMudacaas,
    @Relation(parentColumn = "id_est_novo", entityColumn = "id")
    val estadoNovo: EntidadeEstado,
    @Relation(parentColumn = "id_est_antigo", entityColumn = "id")
    val estadoAntigo: EntidadeEstado?
)