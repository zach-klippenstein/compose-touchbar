# compose-touchbar

Proof-of-concept of a [Compose for Desktop](https://www.jetbrains.com/lp/compose/) API for Macbook Touchbars.

## Usage

```kotlin
fun main() = singleWindowApplication {
  TouchBar {
    var sliderValue by remember { mutableStateOf(0f) }
    
    TextField("Hello, world!")
    
    Button("Click me!", onClick = { println("I was clicked!") })

    Group {
      Button("Button1", onClick = {})
      Button("Button2", onClick = {})
    }
    
    Popover(
      collapsedLabel = "Expandable",
      popoverContent = {
        Slider(
          min = 0f,
          max = 10f,
          onValueChanged = { sliderValue = it }
        )
      }
    )
  }
}
```
