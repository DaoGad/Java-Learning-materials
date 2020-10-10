package com.redistest;

import com.alibaba.fastjson.JSONObject;
import netscape.javascript.JSUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * @author gcq
 * @Create 2020-10-10
 */
public class TestTx {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.186.133",6379);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hello","world");
        jsonObject.put("name","lubenwei");

        // 开启事务
        Transaction multi = jedis.multi();
        String result = jsonObject.toJSONString();

        try {
            multi.set("user1",result);
            multi.set("user2",result);

            int i = 1/0;
            // 执行事务
            multi.exec();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println(jedis.get("user1"));
            System.out.println(jedis.get("user2"));
            jedis.close();
        }

    }
}