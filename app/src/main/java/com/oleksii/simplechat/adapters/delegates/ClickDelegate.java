package com.oleksii.simplechat.adapters.delegates;

import com.oleksii.simplechat.models.Message;

public interface ClickDelegate {
    void onShortClickEvent(Message message);
    void onSecondShortClickEvent(Message message);
    void onLongClickEvent(Message message);
}
