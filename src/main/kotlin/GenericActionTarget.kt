import ca.weblite.objc.NSObject
import ca.weblite.objc.annotations.Msg

/**
 * Generic parent class for views that need to take callbacks.
 * It has different optional methods you can override depending the callback parameters you need.
 */
internal abstract class GenericActionTarget : NSObject("NSObject") {

  @Msg(selector = "triggerNullary", signature = "v@:")
  open fun triggerNullary() {
  }

  // @Msg(selector = "triggerNullary:", signature = "v@:d?")
  // open fun triggerNullary() {
  // }
}