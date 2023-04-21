package org.javaexplorer.web.service;

import org.javaexplorer.error.ApiException;
import org.javaexplorer.utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;

@Service
public class RateLimitService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private String getKey(String op, HttpServletRequest httpServletRequest){
        return "RateLimit" + ":" + op + ":" + IpUtils.getIp(httpServletRequest);
    }

    public void limit(HttpServletRequest httpServletRequest, String op, Integer limit, Duration timeWindow){
        String key = getKey(op, httpServletRequest);
        redisTemplate.opsForValue().setIfAbsent(key, String.valueOf(limit), timeWindow);
        int amount = Integer.parseInt(redisTemplate.opsForValue().get(key));
        if(amount < 1){
            throw ApiException.error("Too many request");
        }
        redisTemplate.opsForValue().decrement(key);
    }
}
