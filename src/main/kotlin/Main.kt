import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.singleWindowApplication

fun main() = singleWindowApplication {
  var text by remember { mutableStateOf("Hello, World!") }

  MaterialTheme {
    Column {
      Button(onClick = {
        text = "Hello, Desktop!"
      }) {
        Text(text)
      }
      TextField(text, onValueChange = { text = it })
    }

    TouchBar {
      TextField(text)
      Button("Click me!", onClick = { println("OMG I was clicked!") })

      Group {
        Button("Button1", onClick = {})
        Button("Button2", onClick = {})
      }
    }
  }
}
