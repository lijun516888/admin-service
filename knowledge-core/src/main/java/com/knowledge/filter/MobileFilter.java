package com.knowledge.filter;

import com.acooly.core.common.view.ViewResult;
import com.acooly.core.utils.Encodes;
import com.acooly.core.utils.mapper.JsonMapper;
import com.acooly.module.security.domain.User;
import com.acooly.module.security.service.UserService;
import com.acooly.module.security.utils.Digests;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class MobileFilter extends OncePerRequestFilter {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        String url = request.getServletPath();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        if(url.equals("/mobile/login.data")) {
            this.sysLogin(request, response);
            return;
        }
        String token = request.getParameter("token");
        if(Strings.isNullOrEmpty(token)) {
            response.getWriter().print(printError("token不能为空"));
            return;
        }
        Object o = redisTemplate.opsForValue().get(token);
        if(o == null) {
            response.getWriter().print(printError("无效的token"));
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String printError(String detail) {
        ViewResult viewResult = new ViewResult();
        viewResult.setSuccess(false);
        viewResult.setDetail(detail);
        return JsonMapper.nonEmptyMapper().toJson(viewResult);
    }

    private String printSuccess(String detail, Object data) {
        ViewResult viewResult = new ViewResult();
        viewResult.setSuccess(true);
        viewResult.setDetail(detail);
        viewResult.setData(data);
        return JsonMapper.nonEmptyMapper().toJson(viewResult);
    }

    private void sysLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if(Strings.isNullOrEmpty(username)) {
            response.getWriter().print(printError("用户名不能为空"));
            return;
        }
        if(Strings.isNullOrEmpty(password)) {
            response.getWriter().print(printError("用户密码不能为空"));
            return;
        }
        User user = userService.findUserByUsername(username);
        if(Objects.isNull(user)) {
            response.getWriter().print(printError("用户名不存在"));
            return;
        }
        String salt = user.getSalt();
        String dbPassword = user.getPassword();
        String userPassword = Encodes.encodeHex(Digests.sha1(password.getBytes(), Encodes.decodeHex(salt), 1024));
        if(!dbPassword.equals(userPassword)) {
            response.getWriter().print(printError("用户名或者密码错误"));
            return;
        }
        Object o = redisTemplate.opsForValue().get(username);
        if(o != null) {
            redisTemplate.delete(o.toString());
        }
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(username, token);
        redisTemplate.opsForValue().set(token, JsonMapper.nonEmptyMapper().toJson(user));
        Map<String, Object> data = Maps.newHashMap();
        data.put("token", token);
        response.getWriter().print(printSuccess("登陆成功", data));
    }

}
