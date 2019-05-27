package com.knowledge.mobile.web;

import com.acooly.core.utils.mapper.JsonMapper;
import com.acooly.module.security.domain.User;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;

public abstract class MobileBaseController implements ApplicationContextAware {

    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.redisTemplate = (RedisTemplate) applicationContext.getBean("redisTemplate");
    }

    public User getSessionUser(String token) {
        return JsonMapper.nonEmptyMapper().fromJson(redisTemplate.opsForValue().get(token).toString(), User.class);
    }

}
