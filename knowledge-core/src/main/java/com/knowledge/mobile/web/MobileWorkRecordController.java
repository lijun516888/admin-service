package com.knowledge.mobile.web;

import com.acooly.core.common.view.ViewResult;
import com.acooly.module.security.dto.UserDto;
import com.gexin.fastjson.JSONArray;
import com.gexin.fastjson.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
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
        JSONArray jsonArray = new JSONArray();
        JSONObject row1 = new JSONObject();
        row1.put("email", "213@qq.com");
        row1.put("groupId", 1);
        row1.put("id", 111);
        row1.put("img", "");
        row1.put("isEnabled", 1);
        row1.put("loginIp", "0");
        row1.put("loginTime", 0);
        row1.put("password", "227af9354aef4e7d4050b1043d5be6b7");
        row1.put("phone", "13456789015");
        row1.put("realName", "");
        row1.put("regIp", "101.204.253.206");
        row1.put("regTime", "1559037709");
        row1.put("title", "超级管理员");
        row1.put("updateTime", 1559037803);
        row1.put("userName", "abcdefg");
        JSONObject data = new JSONObject();
        jsonArray.add(row1);
        data.put("data", jsonArray);
        data.put("total", 1);

        result.put("code", 10000);
        result.put("status", 1);
        result.put("msg", "");
        result.put("data", data);
        return result;
    }

    @RequestMapping(value = "/mobile/role.data")
    public JSONObject doLoadRole(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject row1 = new JSONObject();
        row1.put("id", 1);
        row1.put("rules", "44,45,41,43,42,39,40,1,38,7,2");
        row1.put("status", 1);
        row1.put("title", "超级管理员");
        row1.put("updateTime", 1552300913);

        JSONObject row2 = new JSONObject();
        row2.put("id", 1);
        row2.put("rules", "1,2");
        row2.put("status", 1);
        row2.put("title", "普通管理员");
        row2.put("updateTime", 1542787522);

        JSONObject data = new JSONObject();
        jsonArray.add(row1);
        jsonArray.add(row2);
        data.put("data", jsonArray);
        data.put("total", 2);

        result.put("code", 10000);
        result.put("status", 1);
        result.put("msg", "");
        result.put("data", data);
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
        jsonObject1.put("name", "manageAdmin");
        jsonObject1.put("path", "admin");
        jsonObjectMeta1.put("icon", "user");
        jsonObjectMeta1.put("noCache", false);
        jsonObjectMeta1.put("title", "管理员列表");
        jsonObject1.put("meta", jsonObjectMeta1);

        JSONObject jsonObject2 = new JSONObject();
        JSONObject jsonObjectMeta2 = new JSONObject();
        jsonObject2.put("alwaysShow", false);
        jsonObject2.put("component", "manage/roles");
        jsonObject2.put("hidden", false);
        jsonObject2.put("alwaysShow", false);
        jsonObject2.put("name", "manageRole");
        jsonObject2.put("path", "rules");
        jsonObjectMeta2.put("icon", "lock");
        jsonObjectMeta2.put("noCache", false);
        jsonObjectMeta2.put("title", "角色列表");
        jsonObject2.put("meta", jsonObjectMeta2);

        jsonArray.add(jsonObject1);
        jsonArray.add(jsonObject2);
        return jsonArray;
    }

}
