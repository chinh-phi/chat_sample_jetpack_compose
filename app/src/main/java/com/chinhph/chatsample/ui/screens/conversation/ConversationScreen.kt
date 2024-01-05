package com.chinhph.chatsample.ui.screens.conversation

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.chinhph.chatsample.R
import com.chinhph.chatsample.domain.models.Conversation
import com.chinhph.chatsample.domain.models.Message
import com.chinhph.chatsample.domain.models.User
import com.chinhph.chatsample.ui.composables.CircleAvatar
import com.chinhph.chatsample.ui.screens.conversation.composables.SymbolAnnotationType
import com.chinhph.chatsample.ui.screens.conversation.composables.UserInput
import com.chinhph.chatsample.ui.screens.conversation.composables.messageFormatter
import com.chinhph.chatsample.ui.screens.home.HomeViewModel
import com.chinhph.chatsample.ui.screens.home.composables.JetchatAppBar
import com.chinhph.chatsample.utils.Response
import com.chinhph.chatsample.utils.Reverse
import com.chinhph.chatsample.utils.fakeMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    conversationId: String? = null,
    viewModel: ConversationViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val scrollState = rememberLazyListState()
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)
    val messages = viewModel.messages.collectAsState()

    val conversations = homeViewModel.conversations.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.getListMessage(conversationId.orEmpty())
    }

    val conversation: Conversation? = conversations.value.let {
        if (it is Response.Success) {
            it.data?.first { conversation -> conversation.id == conversationId }
        } else null
    }

    val userPartner = conversation?.members?.firstOrNull { it.userId != homeViewModel.getUserId() }
    Scaffold(
        topBar = {
            ConversationAppBar(
                userPartner = userPartner,
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
            messages.value.let {
                when (it) {
                    is Response.Failure -> {
                        val context = LocalContext.current
                        Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
                    }

                    is Response.Loading -> {
                        CircularProgressIndicator()
                    }

                    is Response.Success -> {
                        ConversationMessages(
                            messages = it.data?.sortedByDescending { message -> message.sendAt }
                                ?: emptyList(),
                            scrollState = scrollState,
                            modifier = Modifier.weight(1f),
                            homeViewModel = homeViewModel
                        )
                    }

                    else -> {}
                }
            }
            Spacer(modifier = modifier.height(8.dp))
            UserInput(onMessageSent = { text ->
                viewModel.sendMessage(
                    fakeMessage.copy(
                        message = text,
                        conversationId = conversationId,
                        sendAt = System.currentTimeMillis() / 1000,
                        sendBy = homeViewModel.getUserId(),
                        previousMessageId = if (messages.value is Response.Success) {
                            if ((messages.value as Response.Success<List<Message>>).data?.lastOrNull()?.sendBy == homeViewModel.getUserId())
                                (messages.value as Response.Success<List<Message>>).data?.lastOrNull()?.id else null
                        } else
                            null
                    )
                )
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationAppBar(
    userPartner: User?,
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
                }
            ) {
                CircleAvatar(
                    size = 32.dp,
                    imageUrl = userPartner?.photoUrl
                )
                Spacer(modifier = modifier.width(8.dp))
                Text(
                    text = userPartner?.nickName.orEmpty(),
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
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    Box(modifier = modifier.padding(horizontal = 8.dp)) {
        LazyColumn(
            reverseLayout = true,
            state = scrollState,
            modifier = Modifier
                .testTag(ConversationTestTag)
                .fillMaxSize()
        ) {
            for (message in messages) {
                item {
                    val isFormMe = message.sendBy == homeViewModel.getUserId()
                    // neu khong co prev va next thi la single
                    val type =
                        if (message.previousMessageId == null && message.nextMessageId == null) {
                            TYPE_BUBBLE_CHAT.SINGLE
                        }
                        // neu khong co prev ma co next thi la top
                        else if (message.previousMessageId == null) {
                            TYPE_BUBBLE_CHAT.TOP
                        }
                        // neu co prev ma khong co next thi la bottom
                        else if (message.nextMessageId == null) {
                            TYPE_BUBBLE_CHAT.BOTTOM
                        }
                        // neu co ca 2 thi la mid
                        else {
                            TYPE_BUBBLE_CHAT.MID
                        }
                    ConversationMessage(
                        onAuthorClick = {},
                        msg = message,
                        isFromMe = isFormMe,
                        typeBubbleChat = type,
                        onMessageClick = {},
                        onMessageLongHold = {},
                        onMessageSlider = {}
                    )
                    Spacer(modifier = Modifier.height(8.dp))
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
    typeBubbleChat: TYPE_BUBBLE_CHAT,
    onMessageClick: (String) -> Unit,
    onMessageLongHold: (String) -> Unit,
    onMessageSlider: (String) -> Unit
) {
    if (!isFromMe) {
        Row {
            CircleAvatar(
                size = 28.dp
            )
            Spacer(modifier = modifier.width(8.dp))
            ChatItemBumble(message = msg, isFromMe = isFromMe, typeBubbleChat)
        }
    } else {
        Row(horizontalArrangement = Arrangement.Reverse, modifier = modifier.fillMaxWidth()) {
            ChatItemBumble(message = msg, isFromMe = isFromMe, typeBubbleChat)
        }
    }
}

private val ChatBubbleShapeTopFromMe = RoundedCornerShape(20.dp, 20.dp, 4.dp, 20.dp)
private val ChatBubbleShapeMidFromMe = RoundedCornerShape(20.dp, 4.dp, 4.dp, 20.dp)
private val ChatBubbleShapeBottomFromMe = RoundedCornerShape(20.dp, 4.dp, 20.dp, 20.dp)
private val ChatBubbleShapeSingleFromMe = RoundedCornerShape(20.dp, 20.dp, 20.dp, 20.dp)

private val ChatBubbleShapeTopNotFromMe = RoundedCornerShape(20.dp, 20.dp, 20.dp, 4.dp)
private val ChatBubbleShapeMidNotFromMe = RoundedCornerShape(4.dp, 20.dp, 20.dp, 4.dp)
private val ChatBubbleShapeBottomNotFromMe = RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)
private val ChatBubbleShapeSingleNotFromMe = RoundedCornerShape(20.dp, 20.dp, 20.dp, 20.dp)

@Composable
fun ChatItemBumble(
    message: Message,
    isFromMe: Boolean,
    typeBubbleChat: TYPE_BUBBLE_CHAT
) {
    val backgroundBubbleColor = if (isFromMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    Column {
        Surface(
            color = backgroundBubbleColor,
            shape = if (isFromMe) {
                when (typeBubbleChat) {
                    TYPE_BUBBLE_CHAT.TOP -> ChatBubbleShapeTopFromMe
                    TYPE_BUBBLE_CHAT.MID -> ChatBubbleShapeMidFromMe
                    TYPE_BUBBLE_CHAT.BOTTOM -> ChatBubbleShapeBottomFromMe
                    TYPE_BUBBLE_CHAT.SINGLE -> ChatBubbleShapeSingleFromMe
                }
            } else {
                when (typeBubbleChat) {
                    TYPE_BUBBLE_CHAT.TOP -> ChatBubbleShapeTopNotFromMe
                    TYPE_BUBBLE_CHAT.MID -> ChatBubbleShapeMidNotFromMe
                    TYPE_BUBBLE_CHAT.BOTTOM -> ChatBubbleShapeBottomNotFromMe
                    TYPE_BUBBLE_CHAT.SINGLE -> ChatBubbleShapeSingleNotFromMe
                }
            }
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
        text = message.message.toString(),
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

enum class TYPE_BUBBLE_CHAT {
    TOP, MID, BOTTOM, SINGLE
}