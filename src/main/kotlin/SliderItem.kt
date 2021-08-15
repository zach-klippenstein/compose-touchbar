import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import ca.weblite.objc.RuntimeUtils.sel

@Composable fun TouchBarScope.SliderItem(
  value: Float,
  onValueChanged: (Float) -> Unit,
  label: String? = null,
  minValue: Float = 0f,
  maxValue: Float = 1f
) {
  val updatedOnValueChanged by rememberUpdatedState(onValueChanged)

  TouchBarItemNode(
    factory = { id ->
      val item = client.sendProxy("NSSliderTouchBarItem", "alloc")
        .sendProxy("initWithIdentifier:", id)

      val actionTarget = object : GenericActionTarget() {
        override fun triggerNullary() {
          // getDouble returns an NSNumber proxy, not an actual double. Bug?
          val currentValue = item.sendRaw("doubleValue") as Double
          updatedOnValueChanged(currentValue.toFloat())
        }
      }
      item["target"] = actionTarget
      // For some reason we can't set this property using .set(), it has its own set selector.
      item.send("setAction:", sel("triggerNullary"))

      val slider = item.getProxy("slider")
      TouchBarItemNode(id, item, slider)
    },
    update = {
      set(label) {
        itemImpl["label"] = it
      }
      set(minValue) {
        viewImpl!!.sendRaw("setMinValue:", it.toDouble())
      }
      set(maxValue) {
        viewImpl!!.sendRaw("setMaxValue:", it.toDouble())
      }
      // This must be done after setting min and max, in case the new value is outside the previous
      // range.
      set(value) {
        itemImpl.sendRaw("setDoubleValue:", it.toDouble())
      }
    }
  )
}
