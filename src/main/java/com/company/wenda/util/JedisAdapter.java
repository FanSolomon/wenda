package com.company.wenda.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.wenda.model.User;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;

import java.util.List;

@Service
public class JedisAdapter implements InitializingBean{
    private JedisPool pool;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
    public static void print(int index, Object obj) {
        System.out.println(String.format("%d, %s", index, String.valueOf(obj)));

    }

    public static void main(String[] argv) {
        Jedis jedis = new Jedis("redis://localhost:6379/9");    //端口号：6379，选择数据库9
        jedis.flushDB();        //把当前数据库删掉

        //get set
        jedis.set("hello", "world");
        print(1, jedis.get("hello"));
        jedis.rename("hello", "newhello");
        print(1, jedis.get("newhello"));
        jedis.setex("hello2", 20, "world"); //带过期时间

        //数值操作
        jedis.set("pv", "100");
        jedis.incr("pv");   //加1
        jedis.incrBy("pv", 5);  //加5
        print(2, jedis.get("pv"));
        jedis.decrBy("pv", 3);  //减3
        print(2, jedis.get("pv"));
        jedis.incrBy("pv", 7);

        print(3, jedis.keys("*"));

        //list      <l>
        String listName = "list";
        jedis.del(listName);
        for (int i = 0; i < 10; ++i) {
            jedis.lpush(listName, "a" + String.valueOf(i));     //插入列表
        }
        print(4, jedis.lrange(listName, 0, 12));        //取出
        print(4, jedis.lrange(listName, 0, 3));
        print(5, jedis.llen(listName)); //长度
        print(6, jedis.lpop(listName)); //pop出来
        print(7, jedis.llen(listName));
        print(8, jedis.lrange(listName, 2, 4));
        print(9, jedis.lindex(listName, 3));
        print(10, jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER, "a4", "xxx"));
        print(10, jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE, "a4", "vvv"));
        print(11, jedis.lrange(listName, 0, 12));

        //hash      <h>
        String userKey = "userxx";
        jedis.hset(userKey, "name", "Jim");
        jedis.hset(userKey, "age", "12");
        jedis.hset(userKey, "phone", "1818181818");
        print(12, jedis.hget(userKey, "name"));
        print(13, jedis.hgetAll(userKey));
        jedis.hdel(userKey, "phone");
        print(14, jedis.hgetAll(userKey));
        print(15, jedis.hexists(userKey, "email"));
        print(16, jedis.hexists(userKey, "age"));
        print(17, jedis.hkeys(userKey));
        print(18, jedis.hvals(userKey));
        jedis.hsetnx(userKey, "school", "hdu"); //如果不存在，添加进去，原本存在则无效（可防止重复写入）
        jedis.hsetnx(userKey, "name", "zyt");
        print(19, jedis.hgetAll(userKey));

        //set 集合    <s>
        String likeKey1 = "commentLike1";
        String likeKey2 = "commentLike2";
        for (int i = 0; i < 10; ++i) {
            jedis.sadd(likeKey1, String.valueOf(i));
            jedis.sadd(likeKey2, String.valueOf(i*i));
        }
        print(20, jedis.smembers(likeKey1));
        print(21, jedis.smembers(likeKey2));
        print(22, jedis.sunion(likeKey1, likeKey2));    //求并
        print(23, jedis.sdiff(likeKey1, likeKey2));     //求不同
        print(24, jedis.sinter(likeKey1, likeKey2));     //求交
        print(25, jedis.sismember(likeKey1, "12")); //是否存在12
        print(26, jedis.sismember(likeKey2, "16"));
        jedis.srem(likeKey1, "5");  //删除5
        print(27, jedis.smembers(likeKey1));
        jedis.smove(likeKey2, likeKey1, "25");     //移动25likeKey2-->likeKey1
        print(28, jedis.smembers(likeKey1));
        print(28, jedis.smembers(likeKey2));
        print(29, jedis.scard(likeKey1));   //统计总数
        print(30, jedis.srandmember(likeKey1, 2));  //随机取出2个（抽奖）

        //SortedSet优先队列（堆），有优先级      <z>
        String rankKey = "rankKey";
        jedis.zadd(rankKey, 15, "Jim");
        jedis.zadd(rankKey, 66, "Ben");
        jedis.zadd(rankKey, 78, "Lee");
        jedis.zadd(rankKey, 93, "Alice");
        jedis.zadd(rankKey, 44, "Tomato");
        print(31, jedis.zcard(rankKey));
        print(32, jedis.zcount(rankKey, 61, 100));
        print(33, jedis.zscore(rankKey, "Lee"));    //浮点
        jedis.zincrby(rankKey, 2, "Lee");
        print(34, jedis.zscore(rankKey, "Lee"));
        jedis.zincrby(rankKey, 2, "Le");
        print(35, jedis.zscore(rankKey, "Le"));
        print(36, jedis.zrange(rankKey, 0, 100));
        print(37, jedis.zrange(rankKey, 0, 10));
        print(37, jedis.zrange(rankKey, 1, 3));
        print(37, jedis.zrevrange(rankKey, 1, 3));
        //遍历
        for (Tuple tuple : jedis.zrangeByScoreWithScores(rankKey, "60", "100")) {
            print(38, tuple.getElement() + ":" + String.valueOf(tuple.getScore()));
        }

        print(39, jedis.zrank(rankKey, "Ben"));
        print(40, jedis.zrevrank(rankKey, "Ben"));

        //根据字母排序
        String setKey = "zset";
        jedis.zadd(setKey, 1, "a");
        jedis.zadd(setKey, 1, "c");
        jedis.zadd(setKey, 1, "e");
        jedis.zadd(setKey, 1, "b");
        jedis.zadd(setKey, 1, "d");
        jedis.zadd(setKey, 1, "z");

        print(41, jedis.zlexcount(setKey, "-", "+"));
        print(42, jedis.zlexcount(setKey, "(b", "[d"));
        print(43, jedis.zlexcount(setKey, "[b", "[d"));
        jedis.zrem(setKey, "b");    //删除b
        print(44, jedis.zrange(setKey, 0 , 10));
        jedis.zremrangeByLex(setKey, "(c", "+");       //删除c以上
        print(45, jedis.zrange(setKey, 0, 2));

        //连接池
        /*
        JedisPool pool = new JedisPool("redis://localhost:6379/9");
        for (int i = 0; i < 100; ++i) {     //默认连接池8条线程
            Jedis j = pool.getResource();
            print(46, j.get("pv"));
            j.close();  //释放jedis资源
        }
        */

        //通过JSON的序列化实现一个缓存
        //可做对象缓存
        User user = new User();
        user.setName("xx");
        user.setPassword("ppp");
        user.setHeadUrl("a.png");
        user.setSalt("salt");
        user.setId(1);
        print(47, JSONObject.toJSONString(user));
        jedis.set("user1", JSONObject.toJSONString(user));

        String value = jedis.get("user1");
        User user2 = JSON.parseObject(value, User.class);
        print(48, user2);

    }

    //初始化
    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis://localhost:6379/10");
    }

    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }
}
