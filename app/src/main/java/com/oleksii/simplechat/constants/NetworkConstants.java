package com.oleksii.simplechat.constants;

public class NetworkConstants {
    public static final String CONNECT_EVENT_ID = "connect";
    public static final String RECONNECT_EVENT_ID = "reconnect";
    public static final String DISCONNECT_EVENT_ID = "disconnect";
    public static final String SYNC_EVENT_ID = "sync";
    public static final String SYNCED_EVENT_ID = "synced";
    public static final String NEW_GROUP_EVENT_ID = "onNewGroupAdded";
    public static final String NEW_MESSAGE_SENT_EVENT_ID = "onNewMessageSent";
    public static final String NEW_MESSAGE_RECEIVED_EVENT_ID = "onNewMessageReceived";
    public static final String LEAVE_GROUP_EVENT_ID = "onLeaveGroup";
    public static final String SOMEONE_LEFT_GROUP_EVENT_ID = "onSomeoneLeftGroup";
    public static final String YOU_LEFT_GROUP_EVENT_ID = "onYouLeftGroup";

//    public static final String CHAT_SERVER_URL = "http://10.0.2.2:3000/";
    public static final String CHAT_SERVER_URL = "https://simple-chat-oleksii.herokuapp.com/";

    public static final String NEW_MESSAGES_CHANNEL_ID = "new_messages_channel_id";
}
