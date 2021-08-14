import androidx.compose.runtime.AbstractApplier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.window.WindowScope
import com.thizzer.jtouchbar.JTouchBar
import com.thizzer.jtouchbar.item.GroupTouchBarItem
import com.thizzer.jtouchbar.item.PopoverTouchBarItem
import com.thizzer.jtouchbar.item.TouchBarItem
import com.thizzer.jtouchbar.item.view.TouchBarButton
import com.thizzer.jtouchbar.item.view.TouchBarSlider
import com.thizzer.jtouchbar.item.view.TouchBarTextField
import com.thizzer.jtouchbar.item.view.action.TouchBarViewAction
import com.thizzer.jtouchbar.common.Color as TouchBarColor

class TouchBarScope(private val touchBar: JTouchBar) {

  @Composable fun TextField(text: String) {
    val id = currentCompositeKeyHash.toString()
    val view = remember { TouchBarTextField() }

    ComposeNode<TouchBarItem, TouchBarApplier>(
      factory = { TouchBarItem(id, view) },
      update = {
        set(text) { view.stringValue = it }
      }
    )
  }

  @Composable fun Button(
    title: String,
    onClick: () -> Unit,
    color: Color = Color.Unspecified,
    isEnabled: Boolean = true,
  ) {
    val id = currentCompositeKeyHash.toString()
    val view = remember { TouchBarButton() }
    val action: TouchBarViewAction? = if (isEnabled) {
      TouchBarViewAction { onClick() }
    } else {
      null
    }

    ComposeNode<TouchBarItem, TouchBarApplier>(
      factory = { TouchBarItem(id, view) },
      update = {
        set(title) { view.title = it }
        set(color) {
          if (it != Color.Unspecified) {
            view.bezelColor = it.toTouchBarColor()
          }
        }
        set(action) { view.action = it }
      }
    )
  }

  @Composable fun Slider(
    min: Float,
    max: Float,
    onValueChanged: (Float) -> Unit
  ) {
    val id = currentCompositeKeyHash.toString()
    val updatedOnValueChanged by rememberUpdatedState(onValueChanged)
    val view = remember {
      TouchBarSlider().apply {
        setActionListener { _, value -> updatedOnValueChanged(value.toFloat()) }
      }
    }

    ComposeNode<TouchBarItem, TouchBarApplier>(
      factory = { TouchBarItem(id, view) },
      update = {
        set(min) { view.minValue = it.toDouble() }
        set(max) { view.maxValue = it.toDouble() }
      }
    )
  }

  @Composable fun Group(
    preferredItemWidth: PreferredItemWidth? = null,
    content: @Composable TouchBarScope.() -> Unit
  ) {
    val id = currentCompositeKeyHash.toString()
    val groupTouchBar = remember { JTouchBar() }
    val updatedContent by rememberUpdatedState(content)
    val composition = rememberCompositionContext()

    DisposableEffect(Unit) {
      val touchBarComposition = groupTouchBar.setContent(composition) {
        updatedContent()
      }
      onDispose {
        touchBarComposition.dispose()
      }
    }

    ComposeNode<GroupTouchBarItem, TouchBarApplier>(
      factory = {
        GroupTouchBarItem(id).apply {
          this.groupTouchBar = groupTouchBar
        }
      },
      update = {
        set(preferredItemWidth) {
          if (it == null) {
            this.isPrefersEqualWidths = false
          } else {
            this.isPrefersEqualWidths = true
            this.preferredItemWidth = it.width
          }
        }
      }
    )
  }

  @Composable fun Popover(
    collapsedLabel: String,
    showCloseButton: Boolean = true,
    popoverContent: @Composable (TouchBarScope.() -> Unit)?,
    pressAndHoldContent: @Composable (TouchBarScope.() -> Unit)?,
  ) {
    val id = currentCompositeKeyHash.toString()

    ComposeNode<PopoverTouchBarItem, TouchBarApplier>(
      factory = {
        PopoverTouchBarItem(id).apply {
          this.popoverTouchBar = TODO()
          this.pressAndHoldTouchBar = TODO()
        }
      },
      update = {
        set(showCloseButton) { this.isShowsCloseButton = it }
      }
    )
  }
}

data class PreferredItemWidth(val width: Float)

private fun Color.toTouchBarColor(): TouchBarColor {
  val rgbColor = this.convert(ColorSpaces.Srgb)
  return TouchBarColor(rgbColor.red, rgbColor.green, rgbColor.blue, rgbColor.alpha)
}

@Composable fun WindowScope.TouchBar(content: @Composable TouchBarScope.() -> Unit) {
  val touchBar = remember { JTouchBar() }
  val composition = rememberCompositionContext()
  val updatedContent by rememberUpdatedState(content)

  DisposableEffect(window) {
    println("OMG window: $window")

    val touchBarComposition = touchBar.setContent(composition) {
      updatedContent()
    }

    touchBar.show(window)

    onDispose {
      touchBarComposition.dispose()
      touchBar.hide(window)
    }
  }
}

private fun JTouchBar.setContent(
  parentComposition: CompositionContext,
  content: @Composable TouchBarScope.() -> Unit
): Composition {
  val applier = TouchBarApplier(this)
  val composition = Composition(applier, parentComposition)
  val scope = TouchBarScope(this)
  composition.setContent {
    scope.content()
  }
  return composition
}

private class TouchBarApplier(
  private val touchBar: JTouchBar
) : AbstractApplier<TouchBarItem?>(null) {

  override fun insertTopDown(index: Int, instance: TouchBarItem?) {
    touchBar.items.add(index, instance!!)
  }

  override fun insertBottomUp(index: Int, instance: TouchBarItem?) {
    // Ignore
  }

  override fun move(from: Int, to: Int, count: Int) {
    touchBar.items.move(from, to, count)
  }

  override fun remove(index: Int, count: Int) {
    for (i in index + count - 1 downTo index) {
      touchBar.items.removeAt(i)
    }
  }

  override fun onClear() {
    touchBar.items.clear()
  }
}

// @Composable private fun TouchBarPreview() {
//   TouchBar {
//     TextField("Hello!")
//   }
// }

// fun main() {
//   val frame = JFrame()
//
//   val touchBar = JTouchBar()
//   touchBar.addItem(PopoverTouchBarItem("pop").apply {
//     collapsedRepresentationLabel = "click me!"
//     popoverTouchBar = JTouchBar().apply {
//       addItem(TouchBarItem("child", TouchBarTextField().apply {
//         stringValue = "Surprise!"
//       }))
//     }
//     pressAndHoldTouchBar = JTouchBar().apply {
//       addItem(TouchBarItem("child", TouchBarSlider().apply {
//         minValue = 5.0
//         maxValue = 10.0
//       }))
//     }
//   })
//   touchBar.addItem(TouchBarItem("item1", TouchBarTextField().apply {
//     stringValue = "Hello!"
//   }).apply {
//     customizationLabel = "customization"
//     isCustomizationAllowed = true
//   })
//   touchBar.addItem(TouchBarItem("item2", TouchBarButton().apply {
//     title = "I'm a button"
//     setAlternatTitle("Alternate title")
//     type = RADIO
//     allowsMixedState = false
//     setAction {
//       println("OMG button was clicked!")
//       val index = touchBar.items.indexOfFirst { it.identifier == "conditional" }
//       if (index == -1) {
//         touchBar.addItem(TouchBarItem("conditional", TouchBarTextField().apply {
//           stringValue = "peekaboo!"
//         }))
//       } else {
//         touchBar.items.removeAt(index)
//       }
//       touchBar.show(frame)
//     }
//     bezelColor = Color.BLUE
//   }))
//   touchBar.addItem(TouchBarItem("item3", TouchBarSlider().apply {
//     minValue = 0.0
//     maxValue = 10.0
//     this.setActionListener { slider, value ->
//       println("OMG slider $slider moved to $value")
//     }
//   }))
//   touchBar.addItem(TouchBarItem("item4", TouchBarScrubber().apply {
//     this.dataSource = object : ScrubberDataSource {
//       override fun getNumberOfItems(scrubber: TouchBarScrubber?): Int = 5
//
//       override fun getViewForIndex(scrubber: TouchBarScrubber?, index: Long): ScrubberView {
//         return ScrubberTextItemView().apply {
//           this.stringValue = "$index"
//         }
//       }
//     }
//     this.showsArrowButtons = true
//     this.backgroundColor = Color.BROWN
//     this.mode = ScrubberMode.FREE
//     this.selectionOverlayStyle = ScrubberSelectionStyle.OUTLINE_OVERLAY_STYLE
//     this.setActionListener { scrubber, value ->
//       println("OMG scrubber $scrubber moved to $value")
//     }
//   }))
//
//   frame.isVisible = true
//   touchBar.show(frame)
// }
