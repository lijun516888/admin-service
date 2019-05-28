package com.knowledge.filter;

import com.acooly.core.common.view.ViewResult;
import com.acooly.core.utils.Encodes;
import com.acooly.core.utils.StringUtils;
import com.acooly.core.utils.mapper.JsonMapper;
import com.acooly.module.security.domain.User;
import com.acooly.module.security.dto.UserDto;
import com.acooly.module.security.service.UserService;
import com.acooly.module.security.utils.Digests;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.knowledge.common.BaseConstants;
import org.springframework.beans.BeanUtils;
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

        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, X-Requested-With, Content-Type, Accept, " +
                        "x-access-appid, x-access-token");

        String url = request.getServletPath();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        if(url.equals("/mobile/login.data")) {
            this.sysLogin(request, response);
            return;
        }
        String token = request.getHeader("x-access-token");
        token = StringUtils.isEmpty(token) ? request.getParameter("token") : token;
        if(Strings.isNullOrEmpty(token)) {
            response.getWriter().print(printError("token不能为空"));
            return;
        }
        Object o = redisTemplate.opsForHash().get(BaseConstants.SESSION_TOKEN_USER, token);
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
        viewResult.setCode("10303");
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
            // response.getWriter().print(printError("用户名或者密码错误"));
            Map<String, Object> data = Maps.newHashMap();
            data.put("status", "0");
            data.put("msg", "用户密码错误");
            data.put("code", 10303);
            response.getWriter().print(JsonMapper.nonEmptyMapper().toJson(data));
            return;
        }
        Object o = redisTemplate.opsForHash().get(BaseConstants.SESSION_USERNAME_TOKEN, username);
        if(o != null) {
            redisTemplate.opsForHash().delete(BaseConstants.SESSION_USERNAME_TOKEN, username);
            redisTemplate.opsForHash().delete(BaseConstants.SESSION_TOKEN_USER, o.toString());
        }
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForHash().put(BaseConstants.SESSION_USERNAME_TOKEN, username, token);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        redisTemplate.opsForHash().put(BaseConstants.SESSION_TOKEN_USER, token, userDto);

        Map<String, Object> data = Maps.newHashMap();
        data.put("status", "1");
        data.put("msg", "登陆成功");
        data.put("code", 10000);
        Map<String, String> ds = Maps.newHashMap();
        ds.put("userToken", token);
        data.put("data", ds);
        response.getWriter().print(JsonMapper.nonEmptyMapper().toJson(data));

        /*{msg: "用户密码错误", status: 0, code: 10303}
        code: 10303
        msg: "用户密码错误"
        status: 0}*/

        /*"{"status":1,
        "data":{"userToken":"49C6B6EEB7033F1C44D6346BA97B73C3MDAwMDAwMDAwML6Mna-FqrqSib2cgMOHoIC7o36CkY6xfLjQzqGGZIXVuGadr4u5um-LunZ_w4efabGmjmWDo3-gu5SsqIhkp6vHZJ3Ynau-moXTqGG4nZSnyX-ohIONk4a7qbuwiGOJp8mfZpqNt7aHheBxpbCggnayp6CdhY2CsMbOzomZiWuVyHp304271I-Z0IJjuGSUjcqOsJuKgHKKyJa8ioadcKy7e4O3jLShdg"},
        "msg":"登录成功","code":10000}"*/

    }

}
