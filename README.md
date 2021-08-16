# compose-touchbar

Proof-of-concept of a [Compose for Desktop](https://www.jetbrains.com/lp/compose/) API for Macbook Touchbars.

## Usage

```kotlin
fun main() = singleWindowApplication {
  TouchBar {
    var sliderValue by remember { mutableStateOf(0f) }
    
    TextField("Hello, world!")
    
    Button("Click me!", onClick = { println("I was clicked!") })

    Group {
      Button("Button1", onClick = {})
      Button("Button2", onClick = {})
    }
    
    Popover(
      collapsedLabel = "Expandable",
      popoverContent = {
        Slider(
          min = 0f,
          max = 10f,
          onValueChanged = { sliderValue = it }
        )
      }
    )
  }
}
```

## TODO

This is just a proof-of-concept, and there are some major issues that would need to be addressed before this could be a real thing:

- [ ] Still getting crashes in native code now and then, e.g. on button clicks.
- [ ] ObjC interop library Java-Objective-C-Bridge requires manual build. Either package a fat JAR, or wait for the library to be published to a public repo (https://github.com/shannah/Java-Objective-C-Bridge/issues/17).
- [ ] Build more item composables. `NSScrubber` and maybe a picker seem like particularly good candidates. See https://developer.apple.com/documentation/appkit/nstouchbaritem?language=objc.
- [ ] Check for memory leaks. I'm not sure how Java-Objective-C-Bridge handles memory management. I would be surprised if we aren't leaking native `NSObject`s all over the place.
- [ ] Check thread usage. I've seen lots of warnings about how interactions with UI must happen on the AppKit main thread, but there have only been two places where performing such operations on the AWT thread instead actually caused crashes. But there are lots of warnings being spat out about operating on the constraint engine on other threads, I think particularly with `NSSlider`, that should be addressed, and I wonder if this isn't also why things like button clicks sometimes randomly crash.
- [ ] Figure out an API to support customization. macOS automatically provides the ability for users to add/remove/reorder items in a touch bar, but the developer must specify which items are customizable/required, and provide additional labels.
- [ ] Improve generation of item identifiers. I am not sure what the exact requirements for identifiers are, aside from Apple recommending they be UUIDs combined with domain-style names. However, I'm guessing that in order to support customizations taking effect across multiple process instances, the IDs must be stable between processes. The current approach of generating random UUIDs is not. I think an approach that would work better is to derive the ID from the [`currentCompositeKeyHash`](https://developer.android.com/reference/kotlin/androidx/compose/runtime/package-summary?authuser=1#currentCompositeKeyHash()), the same thing that `rememberSaveable` does to derive string keys for saved state. However that key is not guaranteed to be unique, and it is very easy to make that happen – two calls to the same composable function from the same lambda will have the same composite key. `rememberSaveable` handles this by remembering insertion order, and so we could do the same by making `TouchBarScope` hash a monotomically-increasing counter with the key hash. This would generate IDs that would be stable across different instances of the same process, although not necessarily across program versions (new versions might have different composables and thus different keys), so it might also be necessary to allow passing explicit ID strings.
