package ru.ifmo.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.ifmo.mobile.network.HelloApiService
import ru.ifmo.mobile.network.HelloResponse

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MessageCard()
        }
    }
}

@Composable
fun MessageCard() {
    val helloApiService = remember { HelloApiService() }
    var message by remember { mutableStateOf("Нажмите кнопку") }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(R.drawable.profile),
            contentDescription = "Profile picture",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(text = "Hello, Android!")
            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        loadHelloMessage(helloApiService, { message = it }, { isLoading = it })
                    }
                },
                enabled = !isLoading
            ) {
                Text(text = if (isLoading) "Загрузка..." else "Получить сообщение")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = message, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

suspend fun loadHelloMessage(
    apiService: HelloApiService,
    onResult: (String) -> Unit,
    onLoading: (Boolean) -> Unit
) {
    onLoading(true)
    try {
        val response: HelloResponse = apiService.fetchHello()
        onResult(response.message)
    } catch (e: Exception) {
        //e.printStackTrace()
        onResult("Ошибка загрузки: ${e.message}")
    }
    onLoading(false)
}

@Preview
@Composable
fun PreviewMessageCard() {
    MessageCard()
}
