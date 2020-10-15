package com.redistest;

import redis.clients.jedis.Jedis;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author gcq
 * @Create 2020-10-10
 */
public class TestPing {
    public static void main(String[] args) {

        Jedis jedis = new Jedis("192.168.186.133",6379);
        // jedis 所有命令就是我们之前学习的命令
//        System.out.println(jedis.ping());

        System.out.println("清空数据:" + jedis.flushDB());
        System.out.println("判断某个键是否存在:" + jedis.exists("username"));
        System.out.println("新增<username,test> 的键值对:" + jedis.set("username","test"));
        System.out.println("新增<password,password>:" + jedis.set("password","password"));
        System.out.println("系统中所有键如下:");
        Set<String> keys = jedis.keys("*");
        System.out.println(keys);
        System.out.println("删除键password:" + jedis.del("password"));
        System.out.println("判断password键是否存在:" + jedis.exists("password"));
        System.out.println("查看键username所存储的值的类型:" + jedis.type("username"));
        System.out.println("随即返回key空间一个:" + jedis.randomKey());
        System.out.println("重命名key:" + jedis.rename("username","name"));
        System.out.println("取出改后的name:" + jedis.get("name"));
        System.out.println("按索引查询:" + jedis.select(0));
        System.out.println("删除当前选择数据库的所有key:" + jedis.flushDB());
        System.out.println("返回当前数据库中key的数目:" + jedis.dbSize());
        System.out.println("删除所有数据库中的所有key:" + jedis.flushAll());

        jedis.flushDB();

        System.out.println("========新增键值对要防止覆盖原先值");
        System.out.println(jedis.setnx("key1","value1"));
        System.out.println(jedis.setnx("key2","value2"));
        System.out.println(jedis.setnx("key2","value2_new"));
        System.out.println(jedis.get("key1"));
        System.out.println(jedis.get("key2"));

        System.out.println("========新增键值并设置有效时间======");
        System.out.println(jedis.setex("key3",2,"value3"));
        System.out.println(jedis.get("key3"));
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(jedis.get("key3"));

        System.out.println("=====获取原值，更新为新值======");
        System.out.println(jedis.getSet("key2","key2GetSet"));
        System.out.println(jedis.get("key2"));
        System.out.println("获取key2的值的字符串:" + jedis.getrange("key2",2,4));
    }

}