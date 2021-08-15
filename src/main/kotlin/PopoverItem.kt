import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ca.weblite.objc.NSObject
import ca.weblite.objc.Proxy
import ca.weblite.objc.RuntimeUtils.sel
import ca.weblite.objc.annotations.Msg

@Composable fun TouchBarScope.PopoverItem(
  collapsedLabel: String,
  popoverController: PopoverController = remember { PopoverController() },
  showCloseButton: Boolean = true,
  showOnPressAndHold: Boolean = true,
  content: @Composable TouchBarScope.() -> Unit,
) {
  PopoverItem(
    collapsedLabel = collapsedLabel,
    popoverController = popoverController,
    showCloseButton = showCloseButton,
    popoverContent = content,
    pressAndHoldContent = if (showOnPressAndHold) content else null
  )
}

@Composable fun TouchBarScope.PopoverItem(
  collapsedLabel: String,
  popoverController: PopoverController = remember { PopoverController() },
  showCloseButton: Boolean = true,
  popoverContent: @Composable (TouchBarScope.() -> Unit)?,
  pressAndHoldContent: @Composable (TouchBarScope.() -> Unit)?,
) {
  var popoverItem: Proxy by remember { mutableStateOf(Proxy()) }

  if (popoverContent != null) {
    TouchBarComposition(
      onTouchBarInvalidated = { spec ->
        popoverItem["popoverTouchBar"] = client.createNSTouchBar(spec)
      },
      onDispose = {
        popoverItem["popoverTouchBar"] = client.createEmptyNSTouchBar()
      },
      content = popoverContent
    )
  }

  if (pressAndHoldContent != null) {
    TouchBarComposition(
      onTouchBarInvalidated = { spec ->
        popoverItem["pressAndHoldTouchBar"] = client.createNSTouchBar(spec)
      },
      onDispose = {
        popoverItem["pressAndHoldTouchBar"] = null
      },
      content = pressAndHoldContent
    )
  }

  TouchBarItemNode(
    factory = { id ->
      popoverItem = client.sendProxy("NSPopoverTouchBarItem", "alloc")
        .sendProxy("initWithIdentifier:", id)
      TouchBarItemNode(id, popoverItem)
    },
    update = {
      set(collapsedLabel) {
        itemImpl["collapsedRepresentationLabel"] = it
      }
      set(showCloseButton) {
        itemImpl.send("setShowsCloseButton:", it)
      }
    }
  )

  DisposableEffect(popoverController) {
    // We don't need to key the effect on popoverItem since it will only be set once, and by the
    // time the effect runs it will already have been set.
    popoverController.popoverItems += popoverItem
    onDispose {
      popoverController.popoverItems -= popoverItem
    }
  }
}

class PopoverController {

  internal var popoverItems = listOf<Proxy>()

  /** Helper object to execute show/dismiss methods on the AppKit main thread. */
  private val executor = object : NSObject("NSObject") {

    @Msg(selector = "showAll", signature = "v@:")
    fun showAll() {
      popoverItems.forEach {
        it.send("showPopover:", /* sender */ this)
      }
    }

    @Msg(selector = "dismissAll", signature = "v@:")
    fun dismissAll() {
      popoverItems.forEach {
        it.send("dismissPopover:", /* sender */ this)
      }
    }
  }

  fun show() {
    executor.send(
      "performSelectorOnMainThread:withObject:waitUntilDone:",
      sel("showAll"),
      executor,
      /* waitUntilDone */ false
    )
  }

  fun dismiss() {
    executor.send(
      "performSelectorOnMainThread:withObject:waitUntilDone:",
      sel("dismissAll"),
      executor,
      /* waitUntilDone */ false
    )
  }
}
