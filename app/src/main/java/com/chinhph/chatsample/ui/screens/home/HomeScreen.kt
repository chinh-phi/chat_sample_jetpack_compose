package com.chinhph.chatsample.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.chinhph.chatsample.R
import com.chinhph.chatsample.domain.models.Conversation
import com.chinhph.chatsample.navigation.Screens
import com.chinhph.chatsample.ui.composables.CircleAvatar
import com.chinhph.chatsample.ui.screens.home.composables.JetchatAppBar
import com.chinhph.chatsample.utils.fakeConversations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavIconPressed: () -> Unit = { },
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val scrollState = rememberLazyListState()
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)

    Scaffold(
        topBar = {
            HomeAppBar(
                title = stringResource(id = R.string.home_title),
                onNavIconPressed = onNavIconPressed,
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
            SearchBar()
            ListConversation(
                listConversation = fakeConversations,
                scrollState = scrollState,
                navController = navController
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(
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
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
        },
        actions = {
            // New conversation icon
            Icon(
                imageVector = Icons.Outlined.Create,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .clickable(onClick = {

                    })
                    .padding(horizontal = 12.dp, vertical = 16.dp)
                    .height(24.dp),
                contentDescription = stringResource(id = R.string.search)
            )
        },
        isCenterTitle = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearch: (key: String) -> Unit = { }
) {
    var text by remember {
        mutableStateOf("")
    }
    Box(modifier = Modifier.absolutePadding(left = 16.dp, right = 16.dp)) {
        TextField(
            value = text,
            onValueChange = { value ->
                text = value
                onSearch(value)
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(4.dp),
            shape = RoundedCornerShape(8.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Search, contentDescription = "icon search")
            },
            colors = TextFieldDefaults.textFieldColors(
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            placeholder = {
                Text(text = "Tim kiem")
            }
        )
    }
}

@Composable
fun ListConversation(
    modifier: Modifier = Modifier,
    listConversation: List<Conversation>,
    scrollState: LazyListState,
    navController: NavHostController
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {
        LazyColumn(
            reverseLayout = false,
            state = scrollState,
            modifier = modifier
                .testTag("abc")
                .fillMaxSize()
        ) {
            for (conversation in listConversation) {
                item {
                    ConversationCompose(
                        onConversationClick = { conversationId ->
                            navController.navigate(Screens.ConversationScreen.route)
                        },
                        conversation = conversation
                    )
                }
            }
        }
    }
}

@Composable
fun ConversationCompose(
    modifier: Modifier = Modifier,
    onConversationClick: (String) -> Unit,
    conversation: Conversation
) {
    Row(
        modifier = modifier
            .clickable {
                onConversationClick(conversation.id)
            }
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleAvatar(
            modifier = modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
            size = 56.dp,
            imageUrl = conversation.userReceive.avatarUrl
        )
        Spacer(modifier = modifier.width(8.dp))
        Box(modifier = modifier.weight(1f)) {
            Column {
                Text(
                    text = conversation.userReceive.nickName,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                )
                Spacer(modifier = modifier.height(4.dp))
                Text(
                    text = conversation.lastMessage.message,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 14.sp
                    )
                )
            }
        }
        Spacer(modifier = modifier.width(8.dp))
        Image(
            painter = if (conversation.userReceive.avatarUrl.isEmpty())
                painterResource(R.drawable.avatar_sample)
            else
                rememberAsyncImagePainter(conversation.userReceive.avatarUrl),
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,            // crop the image if it's not a square
            modifier = modifier
                .padding(end = 16.dp)
                .size(16.dp)
                .clip(CircleShape)                       // clip to the circle shape
        )
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}
