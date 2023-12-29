package com.chinhph.chatsample.utils

import com.chinhph.chatsample.domain.models.Conversation
import com.chinhph.chatsample.domain.models.Message
import com.chinhph.chatsample.domain.models.MessageState
import com.chinhph.chatsample.domain.models.User

val fakeUser1 = User(
    userId = "1",
    name = "Phi Huu Chinh",
    avatarUrl = "https://scontent.fhan1-1.fna.fbcdn.net/v/t39.30808-6/339296831_767251358241405_761136795741730501_n.jpg?_nc_cat=108&ccb=1-7&_nc_sid=efb6e6&_nc_eui2=AeGVxNZL3Rqh7wDtbxKL1aJt7bnEMIp1VWLtucQwinVVYuHnZ9GCXV_G5PZuQX1pN_MYwrT8F98AWzOssdIDIrMv&_nc_ohc=YEjnt50uo-YAX-Uru3U&_nc_ht=scontent.fhan1-1.fna&oh=00_AfAISAD3y43ZEDjQ-mu4AmUIcgldETOUWY5KPhrv7jZgbw&oe=6593DFAC",
    nickName = "Chinh Phi"
)

val fakeUser2 = User(
    userId = "2",
    name = "Vu Kieu My",
    avatarUrl = "https://scontent.fhan1-1.fna.fbcdn.net/v/t39.30808-6/288801206_1121978408382426_2089333640100324182_n.jpg?_nc_cat=105&ccb=1-7&_nc_sid=efb6e6&_nc_eui2=AeFNNnT3FW0w2jh3t9UE1zw3AFsuaTLppUwAWy5pMumlTIUw5yr-zP2WwwArvj8KCGuvN5H4LqSyli1CK42eoooM&_nc_ohc=5mT4GLPO8_kAX8wOn3c&_nc_ht=scontent.fhan1-1.fna&oh=00_AfCR-s0PvbilSTsZBvKmjcAdt5k7OEV4J1dqiycGUrCfrw&oe=6593B06C",
    nickName = "Kieu My Vu"
)

val fakeUser3 = User(
    userId = "3",
    name = "Nham Phuong",
    avatarUrl = "https://scontent.fhan1-1.fna.fbcdn.net/v/t1.6435-9/117283203_151096519961688_6044442702993615553_n.jpg?_nc_cat=110&ccb=1-7&_nc_sid=be3454&_nc_eui2=AeE3dYmx4gYNBGBA94K5F58Yjpi0b3z9I2-OmLRvfP0jbzWC3E2Bu9hiOANVPLpgkrKWZpcrvAEHbnQmG41amGcv&_nc_ohc=voCKl-ej1LkAX-LG5EV&_nc_ht=scontent.fhan1-1.fna&oh=00_AfD6Ud9CFg0EgKSAwrZh6-BmGXrQOM9cZyGRX6ENGs-q5A&oe=65B5BEB4",
    nickName = "Phuong Nham"
)

val fakeUser4 = User(
    userId = "4",
    name = "Nham Duc Tung",
    avatarUrl = "https://scontent.fhan1-1.fna.fbcdn.net/v/t39.30808-6/350820299_1757917008006932_439948491046202908_n.jpg?_nc_cat=111&ccb=1-7&_nc_sid=efb6e6&_nc_eui2=AeF_jvJRrlF8BV9WP3V_x7L4fLxvrgHYAgF8vG-uAdgCAcqPecDKICkHaYbVbYoLGHZ9JAJG5H2zySkplKRcqh9v&_nc_ohc=0b-Lo14vk2cAX_7lZfq&_nc_ht=scontent.fhan1-1.fna&oh=00_AfC6b3HJFGKd0oeEWTbUHTR8SSYlwu5ENpcXcYrhnbECfw&oe=6592B751",
    nickName = "Tung Nham"
)

val fakeMessage = Message(
    id = "123",
    message = "hello fkashfjks fhajkfhjkasfhk safhjkashfjksahf kashfkjasfhjkas fka fksajfhkjafhak",
    sendAt = "178236812",
    sendBy = fakeUser1,
    state = MessageState.SEEN,
    updatedAt = "182736183",
    replyMessageId = "1"
)

val fakeConversations = listOf(
    Conversation(
        id = "1",
        userSend = fakeUser1,
        userReceive = fakeUser2,
        lastMessage = fakeMessage
    ),
    Conversation(
        id = "2",
        userSend = fakeUser1,
        userReceive = fakeUser3,
        lastMessage = fakeMessage
    ),
    Conversation(
        id = "3",
        userSend = fakeUser1,
        userReceive = fakeUser4,
        lastMessage = fakeMessage
    )
)