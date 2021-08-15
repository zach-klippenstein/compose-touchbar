import ca.weblite.objc.Proxy

class TouchBarImage internal constructor(internal val nsImage: Proxy) {
  override fun equals(other: Any?): Boolean = (other as? TouchBarImage)?.nsImage == nsImage
  override fun hashCode(): Int = nsImage.hashCode()
}
