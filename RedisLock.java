
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
public class RedisLockUtils implements AutoCloseable{

    private RedisTemplate redisTemplate;
    private String key;
    private String value;
    // 单位：秒
    private int expireTime;

    public RedisLockUtils(RedisTemplate redisTemplate, String key, int expireTime) {
        this.redisTemplate = redisTemplate;
        this.key = key;
        this.value = UUID.randomUUID().toString();
        this.expireTime = expireTime;
    }

    /**
     * 获取分布式锁
     * @return
     */
    public boolean getLock() {
        RedisCallback<Boolean> redisCallback = redisConnection -> {
            // 设置NX
            RedisStringCommands.SetOption setOption = RedisStringCommands.SetOption.ifAbsent();
            // 设置过期时间
            Expiration expiration = Expiration.seconds(expireTime);

            // 序列化key和value
            byte[] redisKey = redisTemplate.getKeySerializer().serialize(key);
            byte[] redisValue = redisTemplate.getValueSerializer().serialize(value);

            // 执行set nx 操作
            Boolean result = redisConnection.set(redisKey, redisValue, expiration, setOption);
            return result;
        };

        // 获取分布式锁
        Boolean lock = (Boolean) redisTemplate.execute(redisCallback);

        return lock;
    }

    /**
     * 释放锁
     * @return
     */
    public boolean unLock() {
        String script = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then\n" +
                "    return redis.call(\"del\",KEYS[1])\n" +
                "else\n" +
                "    return 0\n" +
                "end";
        RedisScript<Boolean> redisScript = RedisScript.of(script, Boolean.class);
        List<String> keys = Arrays.asList(key);

        Boolean result = (Boolean) redisTemplate.execute(redisScript, keys, value);
        log.info("释放锁的结果：" + result);
        return result;
    }

    @Override
    public void close() throws Exception {
        // 释放锁
        unLock();
    }
}





package com.example.distributedemo.controller;

import com.example.distributedemo.lock.RedisLockUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@Slf4j
public class RedisLockController {

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/redisLock")
    public String redisLock() throws InterruptedException {
        log.info("我进入了方法");

        // 获取锁
        try(RedisLockUtils redisLock = new RedisLockUtils(redisTemplate, "redisKey", 30)) {

            boolean lock = redisLock.getLock();

            if (lock) {
                log.info("我进入了锁！");
                Thread.sleep(15000);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        log.info("方法执行完成");
        return "方法执行完成";
    }
}




启动类：



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableScheduling // 开启定时任务
public class DistributeDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributeDemoApplication.class, args);
    }

}


定时任务：


import com.example.distributedemo.lock.RedisLockUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SchedulerService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Scheduled(cron = "0/5 * * * * ?")
    public void sendSms() {
        try(RedisLockUtils redisLock = new RedisLockUtils(redisTemplate, "autoSms", 30)) {
            boolean lock = redisLock.getLock();

            if (lock) {
                log.info("向138xxxxx用户发短信！");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}


