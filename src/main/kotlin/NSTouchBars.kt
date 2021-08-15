@file:Suppress("JAVA_MODULE_DOES_NOT_EXPORT_PACKAGE")

import ca.weblite.objc.Client
import ca.weblite.objc.NSObject
import ca.weblite.objc.Proxy
import ca.weblite.objc.RuntimeUtils.cls
import ca.weblite.objc.annotations.Msg
import com.sun.jna.Pointer
import sun.lwawt.LWWindowPeer
import sun.lwawt.macosx.CPlatformWindow
import java.awt.Component

internal data class KTouchBarSpec(
  val defaultIdentifiers: List<String> = emptyList(),
  val customizableIdentifiers: List<String> = emptyList(),
  val principleItemIdentifier: String? = null,
  val customizationIdentifier: String? = null,
  val itemProvider: (identifier: String) -> Proxy
)

private val EmptyKTouchBarSpec = KTouchBarSpec { error("Invalid identifier: $it") }

private val componentClass = Component::class.java
private val peerField = componentClass.getDeclaredField("peer").apply {
  isAccessible = true
}

internal fun Component.getNSWindow(client: Client): Proxy {
  // See https://github.com/Thizzer/jtouchbar/blob/e55d6e1f3e8b9e84bb668371de2eb92a5117ed1c/src/main/objective-c%2B%2B/Bridged/JNI/JTouchBarBridge.mm#L36-L49

  // First dig through the Swing classes to find the native pointer to the view that Swing is
  // using.
  val peer = peerField.get(this) as? LWWindowPeer ?: error("Invalid peer")
  val platformWindow = peer.platformWindow as? CPlatformWindow ?: error("Invalid platform window")
  val awtViewPointer = Pointer(platformWindow.contentView.awtView)

  // Next try to figure out what that actually points to.
  val isView = client.send(awtViewPointer, "isKindOfClass:", cls("NSView")) == 1.toByte()
  val isWindow = client.send(awtViewPointer, "isKindOfClass:", cls("NSWindow")) == 1.toByte()

  return when {
    isView -> {
      client.sendProxy(awtViewPointer, "window")
    }
    isWindow -> {
      TODO()
    }
    else -> {
      error("Invalid view type")
    }
  }
}

internal fun Client.createEmptyNSTouchBar(): Proxy = createNSTouchBar(EmptyKTouchBarSpec)

internal fun Client.createNSTouchBar(spec: KTouchBarSpec): Proxy {
  // See https://github.com/Thizzer/jtouchbar/blob/e55d6e1f3e8b9e84bb668371de2eb92a5117ed1c/src/main/objective-c%2B%2B/Bridged/JavaTouchBar.mm#L129
  val nsTouchBar = sendProxy("NSTouchBar", "alloc")
    .sendProxy("init")

  val defaultIdentifiers = sendProxy("NSMutableArray", "alloc")
    .sendProxy("init")
  // TODO customizable

  spec.defaultIdentifiers.forEach { identifier ->
    defaultIdentifiers.send("addObject:", identifier)
  }

  nsTouchBar["defaultItemIdentifiers"] = defaultIdentifiers
  nsTouchBar["delegate"] = TouchBarDelegate(spec.itemProvider)

  return nsTouchBar
}

private class TouchBarDelegate(
  private val itemProvider: (identifier: String) -> Proxy
) : NSObject("NSObject") {

  /*
  -(NSTouchBarItem *) touchBar:(NSTouchBar *)touchBar makeItemForIdentifier:(NSTouchBarItemIdentifier)identifier {
    return [JTouchBarUtils touchBar:touchBar makeItemForIdentifier:identifier usingJavaTouchBar:_jTouchBar];
   */
  @Suppress("UNUSED_PARAMETER")
  @Msg(selector = "touchBar:makeItemForIdentifier:", signature = "@@:@@")
  fun makeItemForIdentifier(touchBar: Proxy, identifier: String): Proxy {
    return itemProvider(identifier)
  }
}
