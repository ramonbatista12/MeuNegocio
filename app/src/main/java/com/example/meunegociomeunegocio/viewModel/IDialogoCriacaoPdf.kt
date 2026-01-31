package com.example.meunegociomeunegocio.viewModel

import android.net.Uri
import com.example.meunegociomeunegocio.utilitario.EstadoLoadAcoes
import kotlinx.coroutines.flow.MutableStateFlow
/**
 * representa o contratro em as clases que gerenciam o dialogo que cria pdfs
 * e a implementacao da caixa de dialogo que cria pdfs
 * */
interface IDialogoCriacaoPdf {
    val caixaDeDialogoCriarPdf: MutableStateFlow<Boolean>
    val envioDerequisicao: MutableStateFlow<Uri?>
    val estadosDeCriacaoDePdf: MutableStateFlow<EstadoLoadAcoes>
    fun abrirDialogo()
    fun fecharDialogo()
    fun criarPdf(uri:Uri?)
    fun limparEnvio()
}