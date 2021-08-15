import ColorPickerButtonType.Standard
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.singleWindowApplication
import kotlin.math.roundToInt

fun main() = singleWindowApplication {
  var text by remember { mutableStateOf("Hello, World!") }
  var showTouchBar by remember { mutableStateOf(true) }
  var sliderValue by remember { mutableStateOf(5f) }
  var showCloseButton by remember { mutableStateOf(true) }
  val popoverController = remember { PopoverController() }
  var colorPickerType: ColorPickerButtonType by remember { mutableStateOf(Standard) }
  var color by remember { mutableStateOf(Color.Blue) }

  MaterialTheme {
    Column {
      Button(onClick = {
        text = "Hello, Desktop!"
      }) {
        Text(text)
      }
      TextField(text, onValueChange = { text = it })
      Text("Slider: $sliderValue")
      Row(verticalAlignment = CenterVertically) {
        Text("Show TouchBar: ")
        Checkbox(showTouchBar, onCheckedChange = { showTouchBar = it })
      }
      Row(verticalAlignment = CenterVertically) {
        Text("Show popover close button: ")
        Checkbox(showCloseButton, onCheckedChange = { showCloseButton = it })
        Button(onClick = popoverController::show) { Text("Open") }
        Button(onClick = popoverController::dismiss) { Text("Close") }
      }
      Row(verticalAlignment = CenterVertically, modifier = Modifier.height(IntrinsicSize.Min)) {
        Box(
          Modifier
            .fillMaxHeight()
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
            .background(color)
        )
        Text("Color picker type: ")
        RadioButton(
          selected = colorPickerType == Standard,
          onClick = { colorPickerType = Standard }
        )
        Text("Standard")
        RadioButton(
          selected = colorPickerType == ColorPickerButtonType.Text,
          onClick = { colorPickerType = ColorPickerButtonType.Text }
        )
        Text("Text")
        RadioButton(
          selected = colorPickerType == ColorPickerButtonType.Stroke,
          onClick = { colorPickerType = ColorPickerButtonType.Stroke }
        )
        Text("Stroke")
      }
    }

    if (showTouchBar) {
      TouchBar {
        TextItem(text)
        ButtonItem("Click me!", onClick = { sliderValue = 1f })

        GroupItem {
          TextItem("child!")
          ButtonItem("child button", onClick = { sliderValue = 9f })
        }

        PopoverItem(
          collapsedLabel = "Open Slider",
          popoverController = popoverController,
          showCloseButton = showCloseButton
        ) {
          SliderItem(
            value = sliderValue,
            minValue = 0f,
            maxValue = 10f,
            onValueChanged = { sliderValue = it }
          )
          TextItem(sliderValue.roundToInt().toString())
        }
        ColorPickerItem(color, onColorChange = { color = it })
      }
    }
  }
}
