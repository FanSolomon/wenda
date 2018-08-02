package com.company.wenda.async;

import java.util.HashMap;
import java.util.Map;

public class EventModel {
    //参数独立出来，以便快速读取
    private EventType type; //事件发生的现场(事件类型)
    private int actorId;    //触发者
    private int entityType; //触发对象类型
    private int entityId;   //触发对象Id
    private int entityOwnerId;  //与触发对象相关的人

    private Map<String, String> exts = new HashMap<String, String>();

    public EventModel() {

    }

    //getExt，setExt方便读取、设置一些字段
    public EventModel setExt(String key, String value) {
        exts.put(key, value);
        return this;
    }

    public String getExt(String key) {
        return exts.get(key);
    }

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public EventModel(EventType type) {
        this.type = type;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public EventModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }
}
