package com.Udemy.YeoGiDa;

import com.Udemy.YeoGiDa.domain.follow.entity.Follow;
import com.Udemy.YeoGiDa.domain.follow.repository.FollowRepository;
import com.google.api.client.testing.util.SecurityTestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Arrays;
import java.util.Set;


@SpringBootTest
class YeoGiDaApplicationTests {

//    @Autowired
//    StringRedisTemplate redisTemplate;
//    @Test
//    public void testStrings(){
//        final String key = "newKey";
//
//        final ValueOperations<String, String> stringStringValueOperations = redisTemplate.opsForValue();
//
//        stringStringValueOperations.set(key,"1");
//        final String result_1 = stringStringValueOperations.get(key);
//
//        System.out.println("result_1 = " + result_1);
//
//        stringStringValueOperations.increment(key);
//        final String result_2 = stringStringValueOperations.get(key);
//
//        System.out.println("result_2 = " + result_2);
//
//    }
//
//    @Test
//    public void testSortedSet(){
//        String key = "yeogida";
//
//        ZSetOperations<String,String> stringStringZSetOperations = redisTemplate.opsForZSet();
//
//        stringStringZSetOperations.add(key,"H",1);
//        stringStringZSetOperations.add(key,"e",5);
//        stringStringZSetOperations.add(key,"l",10);
//        stringStringZSetOperations.add(key,"l",15);
//        stringStringZSetOperations.add(key,"o",20);



//        Double score = stringStringZSetOperations.score(key, "l");


//        Double score = 0.0;
//
//        try {
//            // 검색을하면 해당검색어를 value에 저장하고, score를 1 준다
//            redisTemplate.opsForZSet().incrementScore(key, "l",1);
//            System.out.println(stringStringZSetOperations.score(key,"l"));
//            redisTemplate.opsForZSet().incrementScore(key, "asda", 1);
//            System.out.println(stringStringZSetOperations.score(key,"asda"));
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//        //score를 1씩 올려준다.
//        redisTemplate.opsForZSet().incrementScore(key, "l", score);
//        redisTemplate.opsForZSet().incrementScore(key, "l", score);
//        System.out.println(stringStringZSetOperations.score(key,"l"));
//        redisTemplate.opsForZSet().incrementScore(key, "asda", score);
//        System.out.println(stringStringZSetOperations.score(key,"asda"));
//
//
//
//        Long size = stringStringZSetOperations.size(key);
//
//        System.out.println("size = " + size);
//
//        Set<String> scoreRange = stringStringZSetOperations.rangeByScore(key, 0, 13);
//
//        System.out.println("scoreRange = " + Arrays.toString(scoreRange.toArray()));
//
//
//    }




}
