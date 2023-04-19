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
    private RedisTemplate<String, Integer> redisTemplate;

    public void limit(HttpServletRequest httpServletRequest, String op, Integer limit, Duration timeWindow){
        String key = "RateLimit" + ":" + op + ":" + IpUtils.getIp(httpServletRequest);
        redisTemplate.opsForValue().setIfAbsent(key, limit - 1, timeWindow);
        Integer count = redisTemplate.opsForValue().get(key);
        if(count != null && count < 0){
            throw ApiException.error("Too many request");
        }
    }
}
