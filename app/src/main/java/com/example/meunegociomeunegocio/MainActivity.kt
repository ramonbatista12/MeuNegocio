package com.example.meunegociomeunegocio

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.meunegociomeunegocio.navegacao.Navigraf
import com.example.meunegociomeunegocio.repositorioRom.EntidadeClientes
import com.example.meunegociomeunegocio.repositorioRom.Repositorio
import com.example.meunegociomeunegocio.ui.theme.MeuNegocioMeunegocioTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var  repositorio: Repositorio

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MeuNegocioMeunegocioTheme {
                    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
                    val navHostController = rememberNavController()
                    Scaffold(modifier = Modifier.fillMaxSize()
                                                .safeDrawingPadding()
                                                .safeContentPadding()
                                                .safeGesturesPadding(),
                             bottomBar = {}){
                        Navigraf(navController = navHostController,
                                 windowSize=windowSizeClass,
                                 modifier = Modifier.fillMaxSize().padding(it))

                    }

                    }
              }
       }
}


