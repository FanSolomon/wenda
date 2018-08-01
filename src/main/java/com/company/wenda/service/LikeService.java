package com.company.wenda.service;

import com.company.wenda.util.JedisAdapter;
import com.company.wenda.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;

    public long getLikeCount(int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        return jedisAdapter.scard(likeKey);
    }

    public int getLikeStatus(int userId, int entityType, int entityId) {    //喜欢的状态
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        String dislikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        if (jedisAdapter.sismember(likeKey, String.valueOf(userId))) {
            return 1;
        }
        return jedisAdapter.sismember(dislikeKey, String.valueOf(userId)) ? -1 : 0; //[-1,不喜欢][0,null][1,喜欢]
    }

    public long like(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);    //key命名保证不重复，key分清晰
        jedisAdapter.sadd(likeKey, String.valueOf(userId));                 //增加点赞

        String dislikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);   //删除user的点踩
        jedisAdapter.srem(dislikeKey, String.valueOf(userId));

        return jedisAdapter.scard(likeKey);     //返回喜欢的总人数
    }

    public long disLike(int userId, int entityType, int entityId) {
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        jedisAdapter.sadd(disLikeKey, String.valueOf(userId));                 //增加点踩

        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);   //删除user的点赞
        jedisAdapter.srem(likeKey, String.valueOf(userId));

        return jedisAdapter.scard(likeKey);     //返回喜欢的总人数
    }
}
