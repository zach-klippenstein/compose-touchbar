// import ca.weblite.objc.Client
//
// object TouchBarImages {
//   val TouchBarAddDetail by lazy { loadImageNamed("NSImageNameTouchBarAddDetailTemplate") }
//   val TouchBarAdd by lazy { loadImageNamed("NSImageNameTouchBarAddTemplate") }
//   val TouchBarAlarm by lazy { loadImageNamed("NSImageNameTouchBarAlarmTemplate") }
//   val TouchBarAudioInputMute by lazy { loadImageNamed("NSImageNameTouchBarAudioInputMuteTemplate") }
//   val TouchBarAudioInput by lazy { loadImageNamed("NSImageNameTouchBarAudioInputTemplate") }
//   val TouchBarAudioOutputMute by lazy { loadImageNamed("NSImageNameTouchBarAudioOutputMuteTemplate") }
//   val TouchBarAudioOutputVolumeHigh by lazy { loadImageNamed("NSImageNameTouchBarAudioOutputVolumeHighTemplate") }
//   val TouchBarAudioOutputVolumeLow by lazy { loadImageNamed("NSImageNameTouchBarAudioOutputVolumeLowTemplate") }
//   val TouchBarAudioOutputVolumeMedium by lazy { loadImageNamed("NSImageNameTouchBarAudioOutputVolumeMediumTemplate") }
//   val TouchBarAudioOutputVolumeOff by lazy { loadImageNamed("NSImageNameTouchBarAudioOutputVolumeOffTemplate") }
//   val TouchBarBookmarks by lazy { loadImageNamed("NSImageNameTouchBarBookmarksTemplate") }
//   val TouchBarColorPickerFill by lazy { loadImageNamed("NSImageNameTouchBarColorPickerFill") }
//   val TouchBarColorPickerFont by lazy { loadImageNamed("NSImageNameTouchBarColorPickerFont") }
//   val TouchBarColorPickerStroke by lazy { loadImageNamed("NSImageNameTouchBarColorPickerStroke") }
//   val TouchBarCommunicationAudio by lazy { loadImageNamed("NSImageNameTouchBarCommunicationAudioTemplate") }
//   val TouchBarCommunicationVideo by lazy { loadImageNamed("NSImageNameTouchBarCommunicationVideoTemplate") }
//   val TouchBarCompose by lazy { loadImageNamed("NSImageNameTouchBarComposeTemplate") }
//   val TouchBarDelete by lazy { loadImageNamed("NSImageNameTouchBarDeleteTemplate") }
//   val TouchBarDownload by lazy { loadImageNamed("NSImageNameTouchBarDownloadTemplate") }
//   val TouchBarEnterFullScreen by lazy { loadImageNamed("NSImageNameTouchBarEnterFullScreenTemplate") }
//   val TouchBarExitFullScreen by lazy { loadImageNamed("NSImageNameTouchBarExitFullScreenTemplate") }
//   val TouchBarFastForward by lazy { loadImageNamed("NSImageNameTouchBarFastForwardTemplate") }
//   val TouchBarFolder by lazy { loadImageNamed("NSImageNameTouchBarFolderTemplate") }
//   val TouchBarFolderCopyTo by lazy { loadImageNamed("NSImageNameTouchBarFolderCopyToTemplate") }
//   val TouchBarFolderMoveTo by lazy { loadImageNamed("NSImageNameTouchBarFolderMoveToTemplate") }
//   val TouchBarGetInfo by lazy { loadImageNamed("NSImageNameTouchBarGetInfoTemplate") }
//   val TouchBarGoBack by lazy { loadImageNamed("NSImageNameTouchBarGoBackTemplate") }
//   val TouchBarGoDown by lazy { loadImageNamed("NSImageNameTouchBarGoDownTemplate") }
//   val TouchBarGoForward by lazy { loadImageNamed("NSImageNameTouchBarGoForwardTemplate") }
//   val TouchBarGoUp by lazy { loadImageNamed("NSImageNameTouchBarGoUpTemplate") }
//   val TouchBarHistory by lazy { loadImageNamed("NSImageNameTouchBarHistoryTemplate") }
//   val TouchBarIconView by lazy { loadImageNamed("NSImageNameTouchBarIconViewTemplate") }
//   val TouchBarListView by lazy { loadImageNamed("NSImageNameTouchBarListViewTemplate") }
//   val TouchBarMail by lazy { loadImageNamed("NSImageNameTouchBarMailTemplate") }
//   val TouchBarNewFolder by lazy { loadImageNamed("NSImageNameTouchBarNewFolderTemplate") }
//   val TouchBarNewMessage by lazy { loadImageNamed("NSImageNameTouchBarNewMessageTemplate") }
//   val TouchBarOpenInBrowser by lazy { loadImageNamed("NSImageNameTouchBarOpenInBrowserTemplate") }
//   val TouchBarPause by lazy { loadImageNamed("NSImageNameTouchBarPauseTemplate") }
//   val TouchBarPlay by lazy { loadImageNamed("NSImageNameTouchBarPlayTemplate") }
//   val TouchBarPlayPause by lazy { loadImageNamed("NSImageNameTouchBarPlayPauseTemplate") }
//   val TouchBarPlayhead by lazy { loadImageNamed("NSImageNameTouchBarPlayheadTemplate") }
//   val TouchBarQuickLook by lazy { loadImageNamed("NSImageNameTouchBarQuickLookTemplate") }
//   val TouchBarRecordStart by lazy { loadImageNamed("NSImageNameTouchBarRecordStartTemplate") }
//   val TouchBarRecordStop by lazy { loadImageNamed("NSImageNameTouchBarRecordStopTemplate") }
//   val TouchBarRefresh by lazy { loadImageNamed("NSImageNameTouchBarRefreshTemplate") }
//   val TouchBarRewind by lazy { loadImageNamed("NSImageNameTouchBarRewindTemplate") }
//   val TouchBarRotateLeft by lazy { loadImageNamed("NSImageNameTouchBarRotateLeftTemplate") }
//   val TouchBarRotateRight by lazy { loadImageNamed("NSImageNameTouchBarRotateRightTemplate") }
//   val TouchBarSearch by lazy { loadImageNamed("NSImageNameTouchBarSearchTemplate") }
//   val TouchBarShare by lazy { loadImageNamed("NSImageNameTouchBarShareTemplate") }
//   val TouchBarSidebar by lazy { loadImageNamed("NSImageNameTouchBarSidebarTemplate") }
//   val TouchBarSkipBack by lazy { loadImageNamed("NSImageNameTouchBarSkipBackTemplate") }
//   val TouchBarSkipToStart by lazy { loadImageNamed("NSImageNameTouchBarSkipToStartTemplate") }
//   val TouchBarSkipBack30Seconds by lazy { loadImageNamed("NSImageNameTouchBarSkipBack30SecondsTemplate") }
//   val TouchBarSkipBack15Seconds by lazy { loadImageNamed("NSImageNameTouchBarSkipBack15SecondsTemplate") }
//   val TouchBarSkipAhead15Seconds by lazy { loadImageNamed("NSImageNameTouchBarSkipAhead15SecondsTemplate") }
//   val TouchBarSkipAhead30Seconds by lazy { loadImageNamed("NSImageNameTouchBarSkipAhead30SecondsTemplate") }
//   val TouchBarSkipToEnd by lazy { loadImageNamed("NSImageNameTouchBarSkipToEndTemplate") }
//   val TouchBarSkipAhead by lazy { loadImageNamed("NSImageNameTouchBarSkipAheadTemplate") }
//   val TouchBarSlideshow by lazy { loadImageNamed("NSImageNameTouchBarSlideshowTemplate") }
//   val TouchBarTagIcon by lazy { loadImageNamed("NSImageNameTouchBarTagIconTemplate") }
//   val TouchBarTextBox by lazy { loadImageNamed("NSImageNameTouchBarTextBoxTemplate") }
//   val TouchBarTextList by lazy { loadImageNamed("NSImageNameTouchBarTextListTemplate") }
//   val TouchBarTextBold by lazy { loadImageNamed("NSImageNameTouchBarTextBoldTemplate") }
//   val TouchBarTextItalic by lazy { loadImageNamed("NSImageNameTouchBarTextItalicTemplate") }
//   val TouchBarTextUnderline by lazy { loadImageNamed("NSImageNameTouchBarTextUnderlineTemplate") }
//   val TouchBarTextStrikethrough by lazy { loadImageNamed("NSImageNameTouchBarTextStrikethroughTemplate") }
//   val TouchBarTextJustifiedAlign by lazy { loadImageNamed("NSImageNameTouchBarTextJustifiedAlignTemplate") }
//   val TouchBarTextLeftAlign by lazy { loadImageNamed("NSImageNameTouchBarTextLeftAlignTemplate") }
//   val TouchBarTextCenterAlign by lazy { loadImageNamed("NSImageNameTouchBarTextCenterAlignTemplate") }
//   val TouchBarTextRightAlign by lazy { loadImageNamed("NSImageNameTouchBarTextRightAlignTemplate") }
//   val TouchBarUser by lazy { loadImageNamed("NSImageNameTouchBarUserTemplate") }
//   val TouchBarUserAdd by lazy { loadImageNamed("NSImageNameTouchBarUserAddTemplate") }
//   val TouchBarUserGroup by lazy { loadImageNamed("NSImageNameTouchBarUserGroupTemplate") }
//   val TouchBarVolumeUp by lazy { loadImageNamed("NSImageNameTouchBarVolumeUpTemplate") }
//   val TouchBarVolumeDown by lazy { loadImageNamed("NSImageNameTouchBarVolumeDownTemplate") }
//
//   private val client = Client.getInstance()
//   private fun loadImageNamed(name: String): TouchBarImage {
//     val imageName = client.sendProxy("NSTouchBarItem", name)
//     return client.sendProxy("NSImage", "imageNamed:", imageName)
//       .let(::TouchBarImage)
//   }
// }
