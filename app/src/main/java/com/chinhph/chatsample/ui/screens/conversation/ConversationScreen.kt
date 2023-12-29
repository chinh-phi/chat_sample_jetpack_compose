package com.chinhph.chatsample.ui.screens.conversation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.chinhph.chatsample.R
import com.chinhph.chatsample.domain.models.Message
import com.chinhph.chatsample.ui.composables.CircleAvatar
import com.chinhph.chatsample.ui.screens.conversation.composables.SymbolAnnotationType
import com.chinhph.chatsample.ui.screens.conversation.composables.UserInput
import com.chinhph.chatsample.ui.screens.conversation.composables.messageFormatter
import com.chinhph.chatsample.ui.screens.home.composables.JetchatAppBar
import com.chinhph.chatsample.utils.fakeMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {

    val scrollState = rememberLazyListState()
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)

    Scaffold(
        topBar = {
            ConversationAppBar(
                title = "Phi Huu Chinh hjfahf ashfhashfasjkf ashfkjshafkjhsafkjhas fhkjashfkasdfasjkf ",
                onNavIconPressed = {
                    navController.popBackStack()
                },
                scrollBehavior = scrollBehavior,
            )
        },
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.ime),
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ConversationMessages(
                messages = listOf(
                    fakeMessage,
                    fakeMessage,
                    fakeMessage,
                    fakeMessage,
                    fakeMessage,
                    fakeMessage,
                    fakeMessage,
                    fakeMessage,
                    fakeMessage,
                    fakeMessage,
                    fakeMessage,
                    fakeMessage
                ),
                scrollState = scrollState,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = modifier.height(8.dp))
            UserInput(onMessageSent = {})
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationAppBar(
    title: String,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavIconPressed: () -> Unit = { }
) {
    JetchatAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        onNavIconPressed = onNavIconPressed,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.clickable {
                    println("click title")
                }
            ) {
                CircleAvatar(
                    size = 32.dp
                )
                Spacer(modifier = modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.Outlined.ArrowBack,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .clickable(onClick = onNavIconPressed)
                    .padding(horizontal = 4.dp, vertical = 16.dp)
                    .height(24.dp),
                contentDescription = stringResource(id = R.string.search)
            )
        },
        actions = {
            // New conversation icon
            Icon(
                imageVector = Icons.Outlined.Phone,
                tint = Color.Blue,
                modifier = Modifier
                    .clickable(onClick = {

                    })
                    .padding(start = 8.dp, end = 8.dp)
                    .height(24.dp),
                contentDescription = stringResource(id = R.string.search),
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_video),
                tint = Color.Blue,
                modifier = Modifier
                    .clickable(onClick = {

                    })
                    .padding(end = 16.dp)
                    .height(24.dp),
                contentDescription = stringResource(id = R.string.search)
            )
        }
    )
}

@Composable
fun ConversationMessages(
    messages: List<Message>,
    scrollState: LazyListState,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.padding(horizontal = 8.dp)) {
        LazyColumn(
            reverseLayout = true,
            state = scrollState,
            modifier = Modifier
                .testTag(ConversationTestTag)
                .fillMaxSize()
        ) {
            for (index in messages.indices) {
                item {
                    ConversationMessage(
                        onAuthorClick = {},
                        msg = fakeMessage,
                        onMessageClick = {},
                        onMessageLongHold = {},
                        onMessageSlider = {}
                    )
                }
            }
        }
    }
}

@Composable
fun ConversationMessage(
    modifier: Modifier = Modifier,
    onAuthorClick: (String) -> Unit,
    msg: Message,
    isFromMe: Boolean = false,
    onMessageClick: (String) -> Unit,
    onMessageLongHold: (String) -> Unit,
    onMessageSlider: (String) -> Unit
) {
    Row {
        CircleAvatar(
            size = 28.dp
        )
        Spacer(modifier = modifier.width(8.dp))
        ChatItemBumble(message = msg, isFromMe = isFromMe)
    }
}

private val ChatBubbleShape = RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)

@Composable
fun ChatItemBumble(
    message: Message,
    isFromMe: Boolean
) {
    val backgroundBubbleColor = if (isFromMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    Column {
        Surface(
            color = backgroundBubbleColor,
            shape = ChatBubbleShape
        ) {
            ClickableMessage(
                message = message,
                isFromMe = isFromMe,
                authorClicked = {}
            )
        }
    }
}

@Composable
fun ClickableMessage(
    message: Message,
    isFromMe: Boolean,
    authorClicked: (String) -> Unit
) {
    val uriHandler = LocalUriHandler.current

    val styledMessage = messageFormatter(
        text = message.message,
        primary = isFromMe
    )

    ClickableText(
        text = styledMessage,
        style = MaterialTheme.typography.bodyLarge.copy(color = LocalContentColor.current),
        modifier = Modifier.padding(16.dp),
        onClick = {
            styledMessage
                .getStringAnnotations(start = it, end = it)
                .firstOrNull()
                ?.let { annotation ->
                    when (annotation.tag) {
                        SymbolAnnotationType.LINK.name -> uriHandler.openUri(annotation.item)
                        SymbolAnnotationType.PERSON.name -> authorClicked(annotation.item)
                        else -> Unit
                    }
                }
        }
    )
}

const val ConversationTestTag = "ConversationTestTag"

@Composable
@Preview
fun ConversationScreenPreview() {
    ConversationScreen(navController = rememberNavController())
}