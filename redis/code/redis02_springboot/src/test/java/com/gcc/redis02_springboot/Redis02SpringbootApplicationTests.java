package com.gcc.redis02_springboot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcc.redis02_springboot.pojo.User;
import com.gcc.redis02_springboot.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class Redis02SpringbootApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void testUtil(){
        redisUtil.set("k1","k1");
        System.out.println(redisUtil.get("k1"));
    }

    @Test
    void contextLoads() {
        // redisTemplate 操作不同的类型
        // opsForValue 操作字符串 类似 String
        // opsForSet 操作集合
//        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
//        connection.flushDb();
//        connection.flushAll();

        redisTemplate.opsForValue().set("mykey","lubenwei");
        System.out.println(redisTemplate.opsForValue().get("mykey"));
    }
    @Test
    public void test() throws JsonProcessingException {
        User user = new User("张三", 19);
        String jsonUser = new ObjectMapper().writeValueAsString(user);
        redisTemplate.opsForValue().set("user",jsonUser);
        System.out.println(redisTemplate.opsForValue().get("user"));
    }


}
