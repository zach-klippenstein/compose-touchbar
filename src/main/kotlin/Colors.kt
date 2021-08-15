import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import ca.weblite.objc.Client
import ca.weblite.objc.Proxy

private val SharedColorSpaceNS = Client.getInstance()
  .sendProxy("NSColorSpace", "extendedSRGBColorSpace")

internal fun Color.toNSColor(): Proxy {
  val rgbColor = this.convert(ColorSpaces.ExtendedSrgb)
  return Client.getInstance().sendProxy(
    // TODO create the NSColor in the extendedSrgb space as well.
    "NSColor", "colorWithSRGBRed:green:blue:alpha:",
    rgbColor.red,
    rgbColor.green,
    rgbColor.blue,
    rgbColor.alpha
  )
}

internal fun Proxy.nsColorToComposeColor(): Color {
  val convertedColor = this.sendProxy("colorUsingColorSpace:", SharedColorSpaceNS)
  return Color(
    red = (convertedColor.sendRaw("redComponent") as Double).toFloat(),
    green = (convertedColor.sendRaw("greenComponent") as Double).toFloat(),
    blue = (convertedColor.sendRaw("blueComponent") as Double).toFloat(),
    alpha = (convertedColor.sendRaw("alphaComponent") as Double).toFloat(),
    colorSpace = ColorSpaces.ExtendedSrgb
  )
}
