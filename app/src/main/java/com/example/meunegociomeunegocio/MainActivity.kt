package com.example.meunegociomeunegocio

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import com.example.meunegociomeunegocio.componetesMain.BaraLateral
import com.example.meunegociomeunegocio.componetesMain.BarraInferior
import com.example.meunegociomeunegocio.navegacao.Navigraf
import com.example.meunegociomeunegocio.repositorioRom.EntidadeClientes
import com.example.meunegociomeunegocio.repositorioRom.Repositorio
import com.example.meunegociomeunegocio.ui.theme.MeuNegocioMeunegocioTheme
import com.example.meunegociomeunegocio.viewModel.ViewModelMain
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var  repositorio: Repositorio

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val lifecycle =lifecycleScope
        lifecycle.launch {
            repositorio.fluxoDeClientes().collect {
                Log.d("MainActivity",it.toString() +" esiste dados aqui ")
            }
        }
        setContent {
            MeuNegocioMeunegocioTheme {
                    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
                    val navHostController = rememberNavController()
                    val mainCorotineScope = rememberCoroutineScope()
                    val mainViewModel: ViewModelMain= viewModel()
            PermanentNavigationDrawer(drawerContent = {
              BaraLateral(windowSizeClass,{
                  mainCorotineScope.launch { navHostController.navigate(it) }
              })
            }, modifier = Modifier.fillMaxSize().safeDrawingPadding()

                 ) {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    bottomBar = { BarraInferior(windowSizeClass = windowSizeClass,
                                                acaoDeNavegacao = {
                                                    Log.d("MainActivity","acaoDeNavegacao")
                                                    mainCorotineScope.launch { navHostController.navigate(it) }},
                                                vm = mainViewModel
                    ) },
                    floatingActionButton = {if(!windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)&&!windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND))
                                            FloatingActionButton(onClick = {}){} }){
                    Navigraf(navController = navHostController,
                        windowSize=windowSizeClass,
                        modifier = Modifier.fillMaxSize())

                }

            }
            }

              }
       }
}

@Composable
fun PermanentNavigation(modifier:Modifier= Modifier,drawerContent:@Composable ()->Unit,content:@Composable ()->Unit){
    PermanentNavigationDrawer(drawerContent = {drawerContent()}, modifier =modifier ) {
        content()

    }
}
