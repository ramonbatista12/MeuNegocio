package com.example.meunegociomeunegocio.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meunegociomeunegocio.cadstroDeRequisicao.SelecaoDeClientes
import com.example.meunegociomeunegocio.cadstroDeRequisicao.SelecaoDeProdutos
import com.example.meunegociomeunegocio.repositorioRom.Repositorio
import com.example.meunegociomeunegocio.utilitario.EstadosDeLoad
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class ViewModelCriarRequisicoes @Inject constructor(private val repositorio: Repositorio): ViewModel() {
    private val gerenciador = GengereciadorDeTelas()
    private  val idCliente = MutableStateFlow(0)
    private val corotineContext=viewModelScope
    private val _dialogoAdicionarCliente =MutableStateFlow(false)
    private val _dialogoAdicinarPRoduto=MutableStateFlow(false)
    private val _estadosDeSelecaoDeProdutos= MutableStateFlow<SelecaoDeProdutos>(SelecaoDeProdutos.ProdutosSelecionados)
    private val _estadosDeSelecaoDeClientes= MutableStateFlow<SelecaoDeClientes>(SelecaoDeClientes.Nome)
    private val pesquisa= MutableStateFlow<Pesquisa>(Pesquisa(""))
    private val _observacoes =MutableStateFlow<Pair<String,String>?>(null)
    //Par<Int,Int> vai cepresentar o id do produto e a quantidade
    private val listIdProdutos= MutableStateFlow<List<ProdutoSelecionado>>(emptyList())
    val observacoes=_observacoes
    val  produtosSelecionado=listIdProdutos
    val clienteSelecionado= MutableStateFlow<Pair<Int,String>?>(null)
    val pesquisaDeClientes=pesquisa.flatMapLatest {
         repositorio.pesquisaClientes(it.pesquisa)
    }
    val pesquisaProdutos =pesquisa.flatMapLatest {
        repositorio.fluxoDePesquisaDeProdutos (it.pesquisa)
    }
    val selecaoDeClientes=_estadosDeSelecaoDeClientes
    val estadosDeSelecaoDeProdutos=_estadosDeSelecaoDeProdutos
    val fluxoDeClientes= repositorio.fluxoDeClientes().map {
        if(it.isEmpty())
            EstadosDeLoad.Empty
        else
           EstadosDeLoad.Caregado(it)
    }

    val telaAtual = gerenciador.fluxoDeTelas
     val fluxoDeProdutos =repositorio.fluxoProdutoServico().map {
         if(it.isEmpty())
             EstadosDeLoad.Empty
         else
             EstadosDeLoad.Caregado(it)
     }

    suspend fun mudarPesquisa(pesquisa: Pesquisa){
       this.pesquisa.emit(pesquisa)
    }


    suspend fun irParaTelaDeSelecaoDeClientes(){
        _estadosDeSelecaoDeClientes.emit(SelecaoDeClientes.ListaDeSelecao)
    }
    suspend fun irparaTelaDeVisualizacaoDeNomeDeCliente(){
        _estadosDeSelecaoDeClientes.emit(SelecaoDeClientes.Nome)

    }
    suspend fun irParaSelecaoDosProdutos(){
        _estadosDeSelecaoDeProdutos.emit(SelecaoDeProdutos.ListaDeProdutos)
    }
    suspend fun selecionarCliente(cliente: Pair<Int, String>){
        clienteSelecionado.emit(cliente)
    }
    suspend fun irParaSelecaoDeProdutos(){
        _estadosDeSelecaoDeProdutos.emit(SelecaoDeProdutos.ListaDeProdutos)
    }
    suspend fun irParaListaDeProdutosSelecionados(){
        _estadosDeSelecaoDeProdutos.emit(SelecaoDeProdutos.ProdutosSelecionados)
    }
    suspend fun prosimoEstadio()=gerenciador.proximo()
    suspend fun anteriorEstadio()=gerenciador.anterior()
    suspend fun selecionarProduto(id:Int,nome:String){
        val lista =listIdProdutos.value
        val novoLista=lista.toMutableList()
        novoLista.add(ProdutoSelecionado(id,nome,1))
        listIdProdutos.emit(novoLista)
    }
    suspend fun diminuirQuantidade(index: Int){
        val lista =listIdProdutos.value
        val novoLista=lista.toMutableList()
        novoLista[index]=novoLista[index].copy(id = novoLista[index].id,nome = novoLista[index].nome,quantidade = novoLista[index].quantidade-1)
        listIdProdutos.emit(novoLista)
    }
    suspend fun almentarQuantidade(index: Int){
        val lista =listIdProdutos.value
        val novoLista=lista.toMutableList()
        novoLista[index]=novoLista[index].copy(id = novoLista[index].id,nome = novoLista[index].nome,quantidade = novoLista[index].quantidade+1)
        listIdProdutos.emit(novoLista)
    }
    suspend fun removereProduto(p: ProdutoSelecionado){
        val lista =listIdProdutos.value
        val novoLista=lista.toMutableList()
        novoLista.remove(p)
        listIdProdutos.emit(novoLista)
    }
    suspend fun salvarRequisicao(descricao: String,observacoes: String){
        _observacoes.emit(Pair(descricao,observacoes))
    }

}

data class ProdutoSelecionado(val id:Int,val nome:String,var quantidade:Int)

class GengereciadorDeTelas{
    val fluxoDeTelas= MutableStateFlow<TelasInternas>(TelasInternas.SelecaoDeClientes)
    suspend fun proximo(){
        val proximo =fluxoDeTelas.value.proximo()
        if(proximo!=null){
            fluxoDeTelas.emit(proximo as TelasInternas)

        }
    }
    suspend fun anterior(){
        val anterior =fluxoDeTelas.value.anterio()
        if(anterior!=null){
            fluxoDeTelas.emit(anterior as TelasInternas)

        }
    }
    suspend fun irAoDestino(tela: TelasInternas){
        fluxoDeTelas.emit(tela)
    }
}

sealed class TelasInternas: Estagio{
    object SelecaoDeClientes : TelasInternas(){
        override fun anterio(): Estagio?= null
        override fun proximo(): Estagio? = SelecaoDeProdutos
    }
    object SelecaoDeProdutos : TelasInternas(){
        override fun anterio(): Estagio? = SelecaoDeClientes
        override fun proximo(): Estagio? = Observacoes
    }
    object Observacoes: TelasInternas(){
        override fun anterio(): Estagio? = SelecaoDeProdutos
        override fun proximo(): Estagio? =Confirmacao
    }
    object Confirmacao: TelasInternas(){
        override fun anterio(): Estagio? = Observacoes
        override fun proximo(): Estagio? = null
    }



}

