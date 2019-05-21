package com.config;

import com.domain.UserVo;
import com.service.RedisService;
import com.util.RedisKeyUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisConfigTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private ValueOperations<String,Object> valueOperations;

    @Autowired
    private HashOperations<String, String, Object> hashOperations;

    @Autowired
    private ListOperations<String, Object> listOperations;

    @Autowired
    private SetOperations<String, Object> setOperations;

    @Autowired
    private ZSetOperations<String, Object> zSetOperations;

    @Resource
    private RedisService redisService;


    /**
     * redisTemplate用法参考：
     * https://blog.csdn.net/weixin_40461281/article/details/82011670
     */
    /**
     * string
     */
    @Test
    public void testString(){
        redisTemplate.opsForValue().set("num","我是redisTemplate输出的string类型");
        System.out.println(redisTemplate.opsForValue().get("num"));
    }

    /**
     * List
     */
    @Test
    public void testList(){
        List<String> list=new ArrayList<String>();
        list.add("张三");
        list.add("李四");
        list.add("王五");
        redisTemplate.opsForList().leftPushAll("NameList",list);
        System.out.println(redisTemplate.opsForList().range("NameList",0,-1));
    }

    /**
     * Map
     */
    @Test
    public void testMap(){
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("苍井空","36D");
        map.put("加藤鹰","18cm");
        map.put("波多野结衣","36E");
        redisTemplate.opsForHash().putAll("mapList",map);
        System.out.println(redisTemplate.opsForHash().entries("mapList"));
    }


    @Test
    public void testObj() throws Exception{
        UserVo userVo = new UserVo();
        userVo.setAddress("上海");
        userVo.setName("测试dfas");
        userVo.setAge(123);
        ValueOperations<String,Object> operations = redisTemplate.opsForValue();
        redisService.expireKey("name",20, TimeUnit.SECONDS);
        String key = RedisKeyUtil.getKey(UserVo.Table,"name",userVo.getName());
        UserVo vo = (UserVo) operations.get(key);
        System.out.println(vo);
    }

    @Test
    public void testValueOption( )throws  Exception{
        UserVo userVo = new UserVo();
        userVo.setAddress("上海");
        userVo.setName("jantent");
        userVo.setAge(23);
        valueOperations.set("test",userVo);

        System.out.println(valueOperations.get("test"));
    }

    @Test
    public void testSetOperation() throws Exception{
        UserVo userVo = new UserVo();
        userVo.setAddress("北京");
        userVo.setName("jantent");
        userVo.setAge(23);
        UserVo auserVo = new UserVo();
        auserVo.setAddress("n柜昂周");
        auserVo.setName("antent");
        auserVo.setAge(23);
        setOperations.add("user:test",userVo,auserVo);
        Set<Object> result = setOperations.members("user:test");
        System.out.println(result);
    }

    @Test
    public void HashOperations() throws Exception{
        UserVo userVo = new UserVo();
        userVo.setAddress("北京");
        userVo.setName("jantent");
        userVo.setAge(23);
        hashOperations.put("hash:user",userVo.hashCode()+"",userVo);
        System.out.println(hashOperations.get("hash:user",userVo.hashCode()+""));
    }

    @Test
    public void  ListOperations() throws Exception{
        UserVo userVo = new UserVo();
        userVo.setAddress("北京");
        userVo.setName("jantent");
        userVo.setAge(23);
//        listOperations.leftPush("list:user",userVo);
//        System.out.println(listOperations.leftPop("list:user"));
        // pop之后 值会消失
        System.out.println(listOperations.leftPop("list:user"));
    }
}