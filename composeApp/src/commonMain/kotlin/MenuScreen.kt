import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.akbarrzql.geminiinkmp.Res
import app.akbarrzql.geminiinkmp.action_send
import app.akbarrzql.geminiinkmp.menu_chat_description
import app.akbarrzql.geminiinkmp.menu_chat_title
import app.akbarrzql.geminiinkmp.menu_reason_description
import app.akbarrzql.geminiinkmp.menu_reason_title
import app.akbarrzql.geminiinkmp.menu_summarize_description
import app.akbarrzql.geminiinkmp.menu_summarize_title
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

data class MenuItem(
    val routeId: String,
    val titleResId: StringResource,
    val descriptionResId: StringResource
)

@Composable
fun MenuScreen(
    onItemClicked: (String) -> Unit = { }
) {
    val menuItems = listOf(
        MenuItem("gemini_multimodal", Res.string.menu_summarize_title, Res.string.menu_summarize_description),
        MenuItem("resume_text", Res.string.menu_reason_title, Res.string.menu_reason_description),
        MenuItem("chat", Res.string.menu_chat_title, Res.string.menu_chat_description)
    )
    LazyColumn(
        Modifier
            .padding(top = 16.dp, bottom = 16.dp)
    ) {
        items(menuItems) { menuItem ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(menuItem.titleResId),
                        style = MaterialTheme.typography.h5
                    )
                    Text(
                        text = stringResource(menuItem.descriptionResId),
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    TextButton(
                        onClick = {
                            onItemClicked(menuItem.routeId)
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(text = stringResource(Res.string.action_send))
                    }
                }
            }
        }
    }
}