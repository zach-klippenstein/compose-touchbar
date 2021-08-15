import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color

@Composable fun TouchBarScope.ButtonItem(
  title: String,
  alternateTitle: String = "",
  image: TouchBarImage? = null,
  alternateImage: TouchBarImage? = null,
  bezelColor: Color? = null,
  onClick: () -> Unit,
) {
  val updatedOnClick by rememberUpdatedState(onClick)

  TouchBarViewNode(
    viewFactory = {
      val actionTarget = object : GenericActionTarget() {
        override fun triggerNullary() {
          updatedOnClick()
        }
      }

      client.sendProxy(
        "NSButton", "buttonWithTitle:target:action:",
        /* title */ title,
        /* target */ actionTarget,
        /* action(selector) */ "triggerNullary"
      )
    },
    update = {
      set(title) {
        viewImpl!!.send("setStringValue:", it)
      }
      set(alternateTitle) {
        viewImpl!!["alternateTitle"] = it
      }
      set(image) {
        viewImpl!!["image"] = it
      }
      set(alternateImage) {
        viewImpl!!["alternateImage"] = it
      }
      set(bezelColor) {
        it?.let { color ->
          viewImpl!!["bezelColor"] = color.toNSColor()
        }
      }
    }
  )
}
