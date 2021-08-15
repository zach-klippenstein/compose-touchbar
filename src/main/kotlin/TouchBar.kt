import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Updater
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.window.WindowScope
import ca.weblite.objc.Client
import ca.weblite.objc.Proxy
import java.util.UUID

class TouchBarScope internal constructor(internal val client: Client)

@Composable fun WindowScope.TouchBar(content: @Composable TouchBarScope.() -> Unit) {
  val composition = rememberCompositionContext()
  val updatedContent by rememberUpdatedState(content)

  DisposableEffect(window) {
    val client = Client.getInstance()
    val nsWindow = window.getNSWindow(client)
    val scope = TouchBarScope(client)

    val touchBarComposition = scope.createTouchBarComposition(
      parentComposition = composition,
      onTouchBarInvalidated = {
        nsWindow["touchBar"] = client.createNSTouchBar(it)
      }
    ) {
      updatedContent()
    }

    onDispose {
      touchBarComposition.dispose()
      nsWindow["touchBar"] = null
    }
  }
}

@Composable internal inline fun TouchBarItemNode(
  noinline factory: (identifier: String) -> TouchBarItemNode,
  update: @DisallowComposableCalls (Updater<TouchBarItemNode>.() -> Unit)
) {
  // currentCompositeKeyHash is returning the same key for all invocations of a given function
  // within the same lambda. I didn't think that was how it was supposd to work – wouldn't that
  // break rememberSaveable?
  // currentCompositeKeyHash.toString(36)
  val identifier = remember { UUID.randomUUID().toString() }

  ComposeNode<TouchBarItemNode, TouchBarApplier>(
    factory = { factory(identifier) },
    update = update
  )
}

@Composable internal inline fun TouchBarScope.TouchBarViewNode(
  crossinline viewFactory: () -> Proxy,
  update: @DisallowComposableCalls (Updater<TouchBarItemNode>.() -> Unit)
) {
  TouchBarItemNode(
    factory = { id ->
      val item = client.sendProxy("NSCustomTouchBarItem", "alloc")
        .sendProxy("initWithIdentifier:", id)
      val view = viewFactory()
      item["view"] = view
      TouchBarItemNode(id, item, view)
    },
    update = update
  )
}
