package com.example.desafioalunos2 // Mude para o nome do seu pacote

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson

// Passo 4: Inicializando o App no MainActivity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // O tema do seu app pode ser diferente
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Chama a função que configura toda a navegação
                    AppNavigation()
                }
            }
        }
    }
}

// Modelo de dados que será passado entre as telas.
data class Bicho(val nome: String, val especie: String, val classe: String, val level: Int)

// Passo 1: Configurar o NavHost e NavController
@Composable
fun AppNavigation() {
    // Cria e armazena uma instância do NavController.
    val navController = rememberNavController()

    // NavHost é o contêiner que exibe a tela atual.
    NavHost(
        navController = navController,
        startDestination = "tela1" // Define a rota inicial.
    ) {
        // Define a primeira rota, "tela1".
        composable("tela1") {
            Tela1(navController)
        }

        // Define a segunda rota, "tela2", que aceita um argumento chamado "bichoJson".
        composable("tela2/{bichoJson}") { backStackEntry ->
            // Extrai o argumento "bichoJson" da rota.
            val bichoJson = backStackEntry.arguments?.getString("bichoJson")

            // Usa a biblioteca Gson para deserializar o JSON de volta para um objeto bicho.
            val bicho = Gson().fromJson(bichoJson, Bicho::class.java)

            // Exibe a Tela2, passando o objeto 'bicho' já reconstruído.
            Tela2(navController, bicho)
        }
    }
}

// Passo 2: Tela 1 - Exibindo a Lista de bichos
@Composable
fun Tela1(navController: NavController) {
    // Cria uma lista estática de bichos.
    val bicho = listOf(
        Bicho("João", "Elfo", classe = "Arqueiro", level = 36),
        Bicho("Maria", "Bruxa", classe = "Maga", level = 71),
        Bicho("Carlos", "Rato", classe = "Observador", level = 483)
    )

    // LazyColumn exibe a lista de forma eficiente.
    LazyColumn {
        items(bicho) { bicho ->
            Text(
                text = "${bicho.nome} - ${bicho.especie} - ${bicho.classe} - ${bicho.level}",
                modifier = Modifier
                    .fillParentMaxWidth() // Ocupa a largura toda para facilitar o clique
                    .padding(16.dp)
                    .clickable {
                        // 1. Serializa o objeto 'bicho' para uma string JSON.
                        val bichoJson = Gson().toJson(bicho)
                        // 2. Navega para a rota "tela2", passando o JSON como argumento.
                        navController.navigate("tela2/$bichoJson")
                    }
            )
        }
    }
}

// Passo 3: Tela 2 - Exibindo os Detalhes do Bicho
@Composable
fun Tela2(navController: NavController, bicho: Bicho) {
    // Column para organizar o conteúdo verticalmente e centralizá-lo.
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Nome: ${bicho.nome}")
        Text(text = "Especie: ${bicho.especie}")
        Text(text = "Classe: ${bicho.classe}")
        Text(text = "Level: ${bicho.level}")

        Spacer(modifier = Modifier.height(16.dp))

        // Botão para retornar à tela anterior.
        Button(onClick = {
            // Remove a tela atual da pilha de navegação e volta para a anterior.
            navController.popBackStack()
        }) {
            Text("Voltar")
        }
    }
}

// (Opcional) Adiciona um preview para ver a UI no Android Studio sem rodar o app
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MaterialTheme {
        AppNavigation()
    }
}