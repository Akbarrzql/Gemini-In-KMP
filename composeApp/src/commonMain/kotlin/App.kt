import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import feature.GeminiMultimodalScreen
import feature.SummarizeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview


@Preview
@Composable
fun App(){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "menu") {
            composable("menu") {
                MenuScreen(onItemClicked = { routeId ->
                    navController.navigate(routeId)
                })
            }
            composable("gemini_multimodal") {
                GeminiMultimodalScreen()
            }
            composable("resume_text") {
                SummarizeScreen()
            }
            composable("chat") {
//                ChatRoute()
            }
        }
    }
}