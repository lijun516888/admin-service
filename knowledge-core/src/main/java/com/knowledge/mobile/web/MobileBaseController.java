package com.knowledge.mobile.web;

import com.acooly.module.security.dto.UserDto;
import com.knowledge.common.BaseConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletRequest;

public abstract class MobileBaseController implements ApplicationContextAware {

    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.redisTemplate = (RedisTemplate) applicationContext.getBean("redisTemplate");
    }

    public UserDto getSessionUser(String token) {
        // JsonMapper.nonEmptyMapper().fromJson(redisTemplate.opsForValue().get(token).toString(), User.class);
        return (UserDto) redisTemplate.opsForHash().get(BaseConstants.SESSION_TOKEN_USER, token);
    }

    public UserDto getSessionUser(HttpServletRequest request) {
        String token = request.getHeader("x-access-token");
        token = StringUtils.isEmpty(token) ? request.getParameter("token") : token;
        return this.getSessionUser(token);
    }

}
