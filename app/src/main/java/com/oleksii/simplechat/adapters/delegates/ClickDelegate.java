package com.oleksii.simplechat.adapters.delegates;

import com.oleksii.simplechat.models.Message;

public interface ClickDelegate {
    void onClickEvent(Message message);
    void onLongClickEvent(Message message);
}
