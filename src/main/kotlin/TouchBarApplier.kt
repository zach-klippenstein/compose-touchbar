import androidx.compose.runtime.AbstractApplier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.rememberUpdatedState
import ca.weblite.objc.Client
import ca.weblite.objc.Proxy

internal data class TouchBarItemNode(
  val identifier: String,
  val itemImpl: Proxy,
  val viewImpl: Proxy? = null
)

private val RootTouchBarItemNode = TouchBarItemNode("", Proxy(), null)

internal class TouchBarApplier(
  private val onTouchBarInvalidated: (List<TouchBarItemNode>) -> Unit
) : AbstractApplier<TouchBarItemNode>(RootTouchBarItemNode) {

  private var previousIdentifiers = emptyList<String>()
  private val items = mutableListOf<TouchBarItemNode>()

  override fun insertTopDown(index: Int, instance: TouchBarItemNode) {
    items.add(index, instance)
  }

  override fun insertBottomUp(index: Int, instance: TouchBarItemNode) {
    // Ignore.
  }

  override fun move(from: Int, to: Int, count: Int) {
    items.move(from, to, count)
  }

  override fun remove(index: Int, count: Int) {
    items.remove(index, count)
  }

  override fun onClear() {
    items.clear()
  }

  override fun onEndChanges() {
    val newIdentifiers = items.map { it.identifier }
    if (newIdentifiers != previousIdentifiers) {
      // Need to set a new NSTouchBar on the NSWindow.
      onTouchBarInvalidated(items.toList())
    }
    previousIdentifiers = newIdentifiers
  }
}

@Composable internal fun TouchBarScope.TouchBarComposition(
  onTouchBarInvalidated: (KTouchBarSpec) -> Unit,
  onDispose: () -> Unit = {},
  content: @Composable TouchBarScope.() -> Unit
) {
  val composition = rememberCompositionContext()
  val updatedContent by rememberUpdatedState(content)
  val updatedOnDispose by rememberUpdatedState(onDispose)

  DisposableEffect(this) {
    val touchBarComposition = createTouchBarComposition(
      parentComposition = composition,
      onTouchBarInvalidated = onTouchBarInvalidated,
    ) {
      updatedContent()
    }

    onDispose {
      touchBarComposition.dispose()
      updatedOnDispose()
    }
  }
}

internal fun TouchBarScope.createTouchBarComposition(
  parentComposition: CompositionContext,
  onTouchBarInvalidated: (KTouchBarSpec) -> Unit,
  content: @Composable TouchBarScope.() -> Unit
): Composition {
  val applier = TouchBarApplier { items ->
    val spec = KTouchBarSpec(
      defaultIdentifiers = items.map { it.identifier },
      itemProvider = { identifier ->
        items.first { it.identifier == identifier }.itemImpl
      }
    )
    onTouchBarInvalidated(spec)
  }

  return Composition(applier, parentComposition).apply {
    setContent {
      content()
    }
  }
}
