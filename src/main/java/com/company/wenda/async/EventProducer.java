package com.company.wenda.async;

import com.alibaba.fastjson.JSONObject;
import com.company.wenda.util.JedisAdapter;
import com.company.wenda.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel) {
        try {
//            BlockingQueue<EventModel> q = new ArrayBlockingQueue<EventModel>();
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
