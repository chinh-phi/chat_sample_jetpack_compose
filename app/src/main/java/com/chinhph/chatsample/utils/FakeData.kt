package com.chinhph.chatsample.utils

import com.chinhph.chatsample.domain.models.Conversation
import com.chinhph.chatsample.domain.models.Message
import com.chinhph.chatsample.domain.models.MessageState
import com.chinhph.chatsample.domain.models.User

val fakeUser1 = User(
    userId = "zok3kU0AQzgVzqxT8arJAGozspO2",
    displayName = "Phi Huu Chinh",
    photoUrl = "https://lh3.googleusercontent.com/a/ACg8ocIdXgMM8NMYer_8fl2A5bM78T_AAW59gQ6RLe_mscsfrgI=s96-c",
    nickName = "Chinh Phi"
)

val fakeUser2 = User(
    userId = "RFAmADvPTTcXg3uP3EKT9CffP3l2",
    displayName = "QCDept VinID",
    photoUrl = "https://lh3.googleusercontent.com/a/ACg8ocIMUG3XkQZxHsveb5RxkOGB2lt4O_SZbJ_qyt6SgjM0=s96-c",
    nickName = "QCDept VinID"
)

val fakeMessage = Message(
    id = "123",
    message = "hello",
    sendBy = fakeUser1.userId,
    state = MessageState.SEEN,
    updatedAt = "182736183",
    replyMessageId = "1"
)

val fakeConversation = Conversation(
    lastMessage = fakeMessage,
    members = listOf(
        fakeUser1, fakeUser2
    ),
    memberIds = listOf(
        fakeUser1.userId.orEmpty(), fakeUser2.userId.orEmpty()
    )
)