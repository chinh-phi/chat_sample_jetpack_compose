package com.chinhph.chatsample.ui.screens.conversation.composables

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Games
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.chinhph.chatsample.R
import com.chinhph.chatsample.utils.FunctionalityNotAvailablePopup
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

enum class InputSelector {
    NONE,
    MAP_AND_GAME,
    CAMERA,
    PICTURE
}

enum class MapAndGameSelector {
    MAP,
    GAME
}

@Preview
@Composable
fun UserInputPreview() {
    UserInput(onMessageSent = {})
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserInput(
    onMessageSent: (String) -> Unit,
    modifier: Modifier = Modifier,
    resetScroll: () -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }
    var isFirstTimeFocus by rememberSaveable {
        mutableStateOf(true)
    }
    var currentInputSelector by rememberSaveable { mutableStateOf(InputSelector.NONE) }
    val dismissKeyboard = { currentInputSelector = InputSelector.NONE }
    val focusManager = LocalFocusManager.current

    // Intercept back navigation if there's a InputSelector visible
    if (currentInputSelector != InputSelector.NONE) {
        BackHandler(onBack = dismissKeyboard)
    }

    var textState by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    var textFieldFocusState by remember { mutableStateOf(false) }

    var isTextFieldExpand by remember { mutableStateOf(false) }

    var composableHeightPx by remember { mutableFloatStateOf(500f) }
    var isExpanded by remember {
        mutableStateOf(false)
    }
    val composableHeight = with(LocalDensity.current) { composableHeightPx.toDp() }
    val draggableState = rememberDraggableState(onDelta = {
        composableHeightPx = if (it > 0) {
            2000f
        } else {
            500f
        }
        isExpanded = it > 0
    })

    var isSendEnable by remember {
        mutableStateOf(false)
    }

    Surface(tonalElevation = 2.dp, contentColor = MaterialTheme.colorScheme.secondary) {
        Column {
            AnimatedVisibility(visible = !isExpanded) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedVisibility(
                        visible = !isTextFieldExpand,
                    ) {
                        UserInputSelector(
                            onSelectorChange = {
                                currentInputSelector = it
                                if (currentInputSelector != InputSelector.NONE) {
                                    focusManager.clearFocus()
                                } else {
                                    focusRequester.requestFocus()
                                }
                            },
                            sendMessageEnabled = textState.text.isNotBlank(),
                            onMessageSent = {
                                onMessageSent(textState.text)
                                textState = TextFieldValue()
                                resetScroll()
                                dismissKeyboard()
                            },
                            currentInputSelector = currentInputSelector
                        )
                    }
                    AnimatedVisibility(
                        visible = isTextFieldExpand,
                    ) {
                        SelectorButton(
                            onClick = {
                                isTextFieldExpand = !isTextFieldExpand
                            },
                            icon = Icons.Rounded.ArrowForwardIos,
                            description = stringResource(id = R.string.emoji_selector_bt_desc)
                        )
                    }
                    UserInputText(
                        textFieldValue = textState,
                        onTextChanged = { textState = it },
                        // Only show the keyboard if there's no input selector and text field has focus
                        keyboardShown = currentInputSelector == InputSelector.NONE && textFieldFocusState,
                        // Close extended selector if text field receives focus
                        onTextFieldFocused = { focused ->
                            if (focused) {
                                currentInputSelector = InputSelector.NONE
                                resetScroll()
                            }
                            textFieldFocusState = focused
                        },
                        onMessageSent = {
                            onMessageSent(textState.text)
                            textState = TextFieldValue()
                        },
                        onTextFieldExpanded = { isExpanded ->
                            isTextFieldExpand = isExpanded
                        },
                        isFirstTimeFocus = isFirstTimeFocus,
                        isFirstTimeFocusUpdate = { value ->
                            isFirstTimeFocus = value
                        },
                        isTextFieldExpand = isTextFieldExpand,
                        modifier = modifier
                            .weight(1f),
                        focusRequester = focusRequester,
                        onSendEnable = { isEnable ->
                            isSendEnable = isEnable
                        }
                    )
                    UserFastEmoji(
                        isSendEnable = isSendEnable,
                        onFastEmojiSent = {

                        },
                        onMessageSent = {
                            onMessageSent(textState.text)
                            textState = TextFieldValue()
                        }
                    )
                }
            }
            SelectorExpanded(
                isExpanded = isExpanded,
                height = composableHeight,
                state = draggableState,
                onCloseRequested = dismissKeyboard,
                onTextAdded = { textState = textState.addText(it) },
                currentSelector = currentInputSelector
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun UserInputText(
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    onTextChanged: (TextFieldValue) -> Unit,
    textFieldValue: TextFieldValue,
    onMessageSent: (String) -> Unit,
    onSendEnable: (Boolean) -> Unit,
    keyboardShown: Boolean,
    onTextFieldFocused: (Boolean) -> Unit,
    onTextFieldExpanded: (Boolean) -> Unit,
    isTextFieldExpand: Boolean,
    isFirstTimeFocus: Boolean,
    isFirstTimeFocusUpdate: (Boolean) -> Unit,
    focusRequester: FocusRequester
) {
    val a11ylabel = stringResource(id = R.string.textfield_desc)
    Box(modifier = modifier.fillMaxWidth()) {
        UserInputTextField(
            textFieldValue,
            onTextChanged,
            onTextFieldFocused,
            onTextFieldExpanded,
            onSendEnable,
            onMessageSent,
            isTextFieldExpand,
            keyboardType,
            isFirstTimeFocus,
            isFirstTimeFocusUpdate,
            Modifier.semantics {
                contentDescription = a11ylabel
                keyboardShownProperty = keyboardShown
            },
            focusRequester = focusRequester
        )
    }
}

@Composable
private fun UserInputTextField(
    textFieldValue: TextFieldValue,
    onTextChanged: (TextFieldValue) -> Unit,
    onTextFieldFocused: (Boolean) -> Unit,
    onTextFieldExpanded: (Boolean) -> Unit,
    onSendEnable: (Boolean) -> Unit,
    onMessageSent: (String) -> Unit,
    isTextFieldExpand: Boolean,
    keyboardType: KeyboardType,
    isFirstTimeFocus: Boolean,
    onFirstTimeFocusUpdate: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester
) {
    var lastFocusState by remember { mutableStateOf(false) }
    var textFieldLoaded by remember { mutableStateOf(false) }

    if (textFieldValue.text.isEmpty() || textFieldValue.text.isBlank()) onSendEnable(false)

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .height(40.dp)
    ) {
        Box(contentAlignment = Alignment.CenterStart, modifier = modifier.weight(1f)) {
            BasicTextField(
                value = textFieldValue,
                onValueChange = {
                    onTextChanged(it)
                    onTextFieldExpanded(true)
                    onSendEnable(it.text.isNotEmpty() && it.text.isNotBlank())
                },
                modifier = modifier
                    .padding(start = 12.dp, end = 8.dp)
                    .onFocusChanged { state ->
                        if (lastFocusState != state.isFocused) {
                            onTextFieldFocused(state.isFocused)
                        }
                        lastFocusState = state.isFocused
                    }
                    .focusRequester(focusRequester)
                    .onGloballyPositioned {
                        if (isFirstTimeFocus) {
                            if (!textFieldLoaded) {
                                focusRequester.requestFocus()
                                textFieldLoaded = true
                            }
                            onFirstTimeFocusUpdate(false)
                        }
                    },
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = ImeAction.Send,
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        onMessageSent(textFieldValue.text)
                    }
                ),
                maxLines = 1,
                textStyle = TextStyle(color = Color.Black)
            )
            Row(modifier = modifier.fillMaxSize()) {
                AnimatedVisibility(visible = !isTextFieldExpand) {
                    Box(modifier = modifier
                        .fillMaxSize()
                        .clickable {
                            onTextFieldExpanded(true)
                            focusRequester.requestFocus()
                        }
                    )
                }
            }
            if (textFieldValue.text.isEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(start = 12.dp),
                    text = "Aa",
                )
            }
        }
        IconButton(
            onClick = {},
        ) {
            Icon(
                imageVector = Icons.Filled.EmojiEmotions,
                tint = Color.Blue,
                modifier = Modifier
                    .padding(top = 8.dp, end = 8.dp, bottom = 8.dp, start = 4.dp)
                    .size(56.dp),
                contentDescription = "icon emoji"
            )
        }
    }
}

val KeyboardShownKey = SemanticsPropertyKey<Boolean>("KeyboardShownKey")
var SemanticsPropertyReceiver.keyboardShownProperty by KeyboardShownKey

@Composable
fun UserInputSelector(
    onSelectorChange: (InputSelector) -> Unit,
    sendMessageEnabled: Boolean,
    onMessageSent: () -> Unit,
    currentInputSelector: InputSelector,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SelectorButton(
            onClick = {
                if (currentInputSelector != InputSelector.MAP_AND_GAME)
                    onSelectorChange(InputSelector.MAP_AND_GAME)
                else
                    onSelectorChange(InputSelector.NONE)
            },
            icon = if (currentInputSelector != InputSelector.MAP_AND_GAME) Icons.Rounded.AddCircle else Icons.Rounded.Cancel,
            description = stringResource(id = R.string.emoji_selector_bt_desc)
        )
        SelectorButton(
            onClick = { onSelectorChange(InputSelector.MAP_AND_GAME) },
            icon = Icons.Filled.CameraAlt,
            description = stringResource(id = R.string.emoji_selector_bt_desc)
        )
        SelectorButton(
            onClick = { onSelectorChange(InputSelector.CAMERA) },
            icon = Icons.Filled.Image,
            description = stringResource(id = R.string.emoji_selector_bt_desc)
        )
        SelectorButton(
            onClick = { onSelectorChange(InputSelector.PICTURE) },
            icon = Icons.Filled.Mic,
            description = stringResource(id = R.string.emoji_selector_bt_desc)
        )
    }
}

@Composable
fun SelectorButton(
    onClick: () -> Unit,
    icon: ImageVector,
    description: String,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            icon,
            tint = LocalContentColor.current,
            modifier = modifier
                .padding(8.dp)
                .size(56.dp),
            contentDescription = description
        )
    }
}

@Composable
fun UserFastEmoji(
    modifier: Modifier = Modifier,
    isSendEnable: Boolean = false,
    onMessageSent: () -> Unit,
    onFastEmojiSent: () -> Unit,
) {
    if (isSendEnable) {
        IconButton(
            onClick = onMessageSent
        ) {
            Icon(
                imageVector = Icons.Filled.Send,
                tint = Color.Blue,
                modifier = Modifier
                    .padding(8.dp)
                    .size(56.dp),
                contentDescription = "fast emoji"
            )
        }
    } else {
        IconButton(
            onClick = onFastEmojiSent
        ) {
            Icon(
                imageVector = Icons.Filled.Accessibility,
                tint = Color.Blue,
                modifier = Modifier
                    .padding(8.dp)
                    .size(56.dp),
                contentDescription = "fast emoji"
            )
        }
    }
}

@Composable
private fun SelectorExpanded(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    height: Dp,
    state: DraggableState,
    currentSelector: InputSelector,
    onCloseRequested: () -> Unit,
    onTextAdded: (String) -> Unit
) {
    if (currentSelector == InputSelector.NONE) return

    // Request focus to force the TextField to lose it
    val focusRequester = FocusRequester()

    Surface(tonalElevation = 8.dp) {
        when (currentSelector) {
            InputSelector.MAP_AND_GAME -> MapAndGameSelector(isExpanded, height, state)
            InputSelector.PICTURE -> FunctionalityNotAvailablePanel()
            InputSelector.CAMERA -> FunctionalityNotAvailablePanel()
            else -> {
                throw NotImplementedError()
            }
        }
    }
}

@Composable
fun FunctionalityNotAvailablePanel() {
    AnimatedVisibility(
        visibleState = remember { MutableTransitionState(false).apply { targetState = true } },
        enter = expandHorizontally() + fadeIn(),
        exit = shrinkHorizontally() + fadeOut()
    ) {
        Column(
            modifier = Modifier
                .height(320.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(id = R.string.not_available),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = stringResource(id = R.string.not_available_subtitle),
                modifier = Modifier.paddingFrom(FirstBaseline, before = 32.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun TextFieldValue.addText(newString: String): TextFieldValue {
    val newText = this.text.replaceRange(
        this.selection.start,
        this.selection.end,
        newString
    )
    val newSelection = TextRange(
        start = newText.length,
        end = newText.length
    )

    return this.copy(text = newText, selection = newSelection)
}

@Composable
fun MapAndGameSelector(
    isExpanded: Boolean,
    height: Dp,
    state: DraggableState
) {
    var selected by remember { mutableStateOf(MapAndGameSelector.MAP) }

    val a11yLabel = stringResource(id = R.string.emoji_selector_desc)
    Column {
        AnimatedVisibility(visible = !isExpanded) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                ExtendedSelectorInnerButton(
                    icon = Icons.Filled.Map,
                    onClick = { selected = MapAndGameSelector.MAP },
                    selected = true,
                    modifier = Modifier.weight(1f)
                )
                ExtendedSelectorInnerButton(
                    icon = Icons.Filled.Games,
                    onClick = { selected = MapAndGameSelector.GAME },
                    selected = false,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        MapSelector(state = state, height = height)
    }

    if (selected == MapAndGameSelector.MAP) {
//        RequestLocationPermission()
    }

    if (selected == MapAndGameSelector.GAME) {
        NotAvailablePopup(onDismissed = { selected = MapAndGameSelector.MAP })
    }
}

@Composable
fun ExtendedSelectorInnerButton(
    icon: ImageVector,
    onClick: () -> Unit,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    val colors = IconButtonDefaults.iconButtonColors(
        containerColor = if (selected) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
        else Color.Transparent,
        disabledContainerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSurface,
        disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.74f)
    )
    IconButton(
        onClick = onClick,
        modifier = modifier
            .padding(8.dp)
            .height(36.dp),
        colors = colors
    ) {
        Icon(imageVector = icon, contentDescription = "button extended $icon")
    }
}

@Composable
private fun NotAvailablePopup(onDismissed: () -> Unit) {
    FunctionalityNotAvailablePopup(onDismissed)
}

@Composable
fun MapSelector(
    modifier: Modifier = Modifier,
    state: DraggableState,
    height: Dp
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .draggable(state, Orientation.Vertical, reverseDirection = true)
            .animateContentSize(
                animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
            ),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        val singapore = LatLng(1.35, 103.87)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(singapore, 10f)
        }

        Column {
            SearchLocationInput()
            Spacer(modifier = modifier.height(8.dp))
            GoogleMap(
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = MarkerState(position = singapore),
                    title = "Singapore",
                    snippet = "Marker in Singapore"
                )
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission() {
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )
    SideEffect {
        locationPermissionsState.launchMultiplePermissionRequest()
    }

    if (locationPermissionsState.allPermissionsGranted) {

    } else {

    }
}

@Composable
fun SearchLocationInput(
    modifier: Modifier = Modifier,
    onSearch: (key: String) -> Unit = { },
) {
    var text by remember {
        mutableStateOf("")
    }

    Column(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier
                .padding(vertical = 4.dp)
                .height(8.dp)
                .width(40.dp)
                .background(color = Color.Gray, shape = RoundedCornerShape(12.dp))
        )
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .height(40.dp)
        ) {
            IconButton(
                onClick = {},
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp),
                    contentDescription = "icon emoji"
                )
            }
            Box(contentAlignment = Alignment.CenterStart) {
                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = modifier
                        .padding(horizontal = 8.dp),
                    maxLines = 1,
                    textStyle = TextStyle(color = Color.Black),
                )
                if (text.isEmpty()) {
                    Text(
                        modifier = Modifier
                            .padding(end = 16.dp),
                        text = "Tim kiem dia diem va vi tri",
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun MapPreview() {
    SelectorExpanded(
        onCloseRequested = {},
        onTextAdded = { },
        currentSelector = InputSelector.MAP_AND_GAME,
        height = 200.dp,
        isExpanded = false,
        state = rememberDraggableState(onDelta = {})
    )
}

