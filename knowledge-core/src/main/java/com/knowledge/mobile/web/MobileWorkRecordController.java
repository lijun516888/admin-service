package com.knowledge.mobile.web;

import com.acooly.core.common.view.ViewResult;
import com.acooly.module.security.dto.UserDto;
import com.gexin.fastjson.JSONArray;
import com.gexin.fastjson.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 移动端接口
 */
@RestController
public class MobileWorkRecordController extends MobileBaseController {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/mobile/work/doList.data")
    public ViewResult doList(HttpServletRequest request) {
        ViewResult result = new ViewResult();
        UserDto user = getSessionUser(request);
        result.setData(user);
        return result;
    }

    @RequestMapping(value = "/mobile/doLoadUser.data")
    public JSONObject doLoadUser(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        UserDto user = getSessionUser(request);
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("email", "123@163.com");
        jsonObj.put("group", "超级管理员");
        jsonObj.put("groupId", "1");
        jsonObj.put("id", "1");
        jsonObj.put("img", "http://en.hardphp.com/uploads/images/20190108/21b4a8aa748142cd506976ef0f7e1bbb.png");
        jsonObj.put("isEnabled", "1");
        jsonObj.put("loginIp", "192.168.1.190");
        jsonObj.put("loginTime", 1559024432);
        jsonObj.put("password", "123456");
        jsonObj.put("phone", "13132354113");
        jsonObj.put("realName", "三江哥");
        jsonObj.put("regIp", "127.0.0.1");
        jsonObj.put("regTime", 1498276451);
        jsonObj.put("updateTime", 1546934906);
        jsonObj.put("userName", "admin");
        JSONArray jsonArray = new JSONArray();

        JSONObject menu1 = new JSONObject();
        JSONObject menuMeta = new JSONObject();
        menu1.put("alwaysShow", true);
        menu1.put("component", "layout/Layout");
        menu1.put("hidden", false);
        menu1.put("name", "manage");
        menu1.put("path", "/manage");
        menuMeta.put("icon", "component");
        menuMeta.put("noCache", "false");
        menuMeta.put("title", "权限管理");
        menu1.put("meta", menuMeta);
        menu1.put("children", this.bindChildren());
        jsonArray.add(menu1);
        jsonObj.put("access", jsonArray);

        result.put("code", 10000);
        result.put("data", jsonObj);
        result.put("status", 1);
        return result;
    }


    @RequestMapping(value = "/mobile/admin.data")
    public JSONObject doLoadAdmin(HttpServletRequest request) {
        JSONObject result = new JSONObject();

        return result;
    }

    @RequestMapping(value = "/mobile/role.data")
    public JSONObject doLoadRole(HttpServletRequest request) {
        JSONObject result = new JSONObject();

        return result;
    }

    private JSONArray bindChildren() {
        JSONArray jsonArray = new JSONArray();

        JSONObject jsonObject1 = new JSONObject();
        JSONObject jsonObjectMeta1 = new JSONObject();
        jsonObject1.put("alwaysShow", false);
        jsonObject1.put("component", "manage/admin");
        jsonObject1.put("hidden", false);
        jsonObject1.put("alwaysShow", false);
        jsonObject1.put("name", "manage/admin");
        jsonObject1.put("path", "admin");
        jsonObjectMeta1.put("icon", "user");
        jsonObjectMeta1.put("noCache", false);
        jsonObjectMeta1.put("title", "管理员列表");
        jsonObject1.put("meta", jsonObjectMeta1);

        JSONObject jsonObject2 = new JSONObject();
        JSONObject jsonObjectMeta2 = new JSONObject();
        jsonObject2.put("alwaysShow", false);
        jsonObject2.put("component", "manage/rules");
        jsonObject2.put("hidden", false);
        jsonObject2.put("alwaysShow", false);
        jsonObject2.put("name", "manage/rules");
        jsonObject2.put("path", "rules");
        jsonObjectMeta2.put("icon", "lock");
        jsonObjectMeta2.put("noCache", false);
        jsonObjectMeta2.put("title", "权限列表");
        jsonObject2.put("meta", jsonObjectMeta2);

        jsonArray.add(jsonObject1);
        jsonArray.add(jsonObject2);
        return jsonArray;
    }

}
