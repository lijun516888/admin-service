package com.knowledge.mobile.web;

import com.acooly.core.common.view.ViewResult;
import com.acooly.module.security.domain.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端接口
 */
@RestController
public class MobileWorkRecordController extends MobileBaseController {

    @RequestMapping(value = "/mobile/work/doList.data")
    public ViewResult doList(@RequestParam("token") String token) {
        ViewResult result = new ViewResult();
        User user = getSessionUser(token);
        result.setData(user);
        return result;
    }

}
