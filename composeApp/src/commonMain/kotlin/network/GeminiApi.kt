package network
import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import dev.shreyaspatil.ai.client.generativeai.type.GenerateContentResponse
import dev.shreyaspatil.ai.client.generativeai.type.PlatformImage
import dev.shreyaspatil.ai.client.generativeai.type.content
import dev.shreyaspatil.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.flow.Flow
import kotlin.io.encoding.ExperimentalEncodingApi

class GeminiApi {
    private val config = generationConfig {
        temperature = 0.7f
    }

    private val apiKey = "YOUR_API_KEY"


    private val generativeVisionModel = GenerativeModel(
        //modelName = "gemini-pro-vision"  for response be better
        modelName = "models/gemini-1.5-pro-latest",
        apiKey = apiKey,
        generationConfig = config
    )

    private val generativeModel = GenerativeModel(
        //modelName = "gemini-pro"  for response be better
        modelName = "gemini-1.5-flash-latest",
        apiKey = apiKey,
        generationConfig = config
    )

    fun generateContent(prompt: String): Flow<GenerateContentResponse> {
        return generativeModel.generateContentStream(prompt)
    }


    fun generateContent(prompt: String, imageData: ByteArray): Flow<GenerateContentResponse> {
        val content = content {
            image(PlatformImage(imageData))
            text(prompt)
        }
        return generativeVisionModel.generateContentStream(content)
    }
}