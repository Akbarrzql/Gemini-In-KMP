package feature

import network.GeminiApi
import source.SummarizeUiState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.akbarrzql.geminiinkmp.Res
import app.akbarrzql.geminiinkmp.summarize_hint
import app.akbarrzql.geminiinkmp.summarize_label
import com.mikepenz.markdown.m3.Markdown
import dev.shreyaspatil.ai.client.generativeai.type.GenerateContentResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun SummarizeScreen() {
    var textToSummarize by rememberSaveable { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var uiState by remember { mutableStateOf<SummarizeUiState>(SummarizeUiState.Initial) }
    val api = remember { GeminiApi() }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        ElevatedCard(
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            OutlinedTextField(
                value = textToSummarize,
                label = { Text(stringResource(Res.string.summarize_label)) },
                placeholder = { Text(stringResource(Res.string.summarize_hint)) },
                onValueChange = { textToSummarize = it },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
            TextButton(
                onClick = {
                    if (textToSummarize.isNotBlank()) {
                        coroutineScope.launch {
                            uiState = SummarizeUiState.Loading
                            val promptText = "Summarize the following text for me: $textToSummarize"
                            try {
                                summarizeContentAsFlow(api, promptText)
                                    .collect {
                                        println("response = ${it.text}")
                                        content += it.text
                                        uiState = SummarizeUiState.Success(content)
                                    }
                            } catch (e: Exception) {
                                uiState = SummarizeUiState.Error(e.message ?: "Unknown error")
                            }
                        }
                    }
                },
                modifier = Modifier
                    .padding(end = 16.dp, bottom = 16.dp)
                    .align(Alignment.End)
            ) {
                Text("Summarize")
            }
        }

        Spacer(Modifier.height(16.dp))

        when (uiState) {
            is SummarizeUiState.Initial -> {
                // No UI for initial state
            }
            is SummarizeUiState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }
            is SummarizeUiState.Success -> {
                Card(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    )
                ) {
                    SelectionContainer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Markdown(content)
                    }
                }
            }
            is SummarizeUiState.Error -> {
                val errorState = uiState as SummarizeUiState.Error
                Card(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        .fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = errorState.errorMessage,
                        color = MaterialTheme.colorScheme.onError,
                        modifier = Modifier
                            .padding(all = 16.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}



fun summarizeContentAsFlow(
    api: GeminiApi,
    prompt: String,
): Flow<GenerateContentResponse> = api.generateContent(prompt)