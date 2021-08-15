import androidx.compose.runtime.Composable
import ca.weblite.objc.RuntimeUtils.str

@Composable fun TouchBarScope.TextItem(text: String) {
  TouchBarViewNode(
    viewFactory = {
      client.sendProxy("NSTextField", "labelWithString:", text)
    },
    update = {
      set(text) {
        viewImpl!!.send("setStringValue:", str(it))
      }
    }
  )
}
