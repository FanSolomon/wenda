package com.company.wenda.async;

import java.util.List;

public interface EventHandler {
    void doHandle(EventModel model);    //处理Event

    List<EventType> getSupportEventTypes(); //关注的Event

}
