package com.knowledge.owner;

import com.gexin.fastjson.JSON;
import com.gexin.fastjson.JSONArray;
import com.gexin.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.knowledge.utils.HttpUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

public class HttpPostTest {

    /**
     * 事前质检测试
     */
    @Test
    public void preCheck() {
        String url = "http://xbreport.peiban.ink/seatreport/api/report/restrict.jhtml";
        Map<String, String> params = Maps.newHashMap();
        params.put("customerIdCard", "110104196804253024");
        params.put("seatIdCard","142622199211181521");
        params.put("workplaceCode", "1000");
        try {
            String s = HttpUtils.doPost(url, params, 10000, 10000);
            JSONObject obj = JSONObject.parseObject(s);
            String code = obj.getString("code");
            if("1000".equals(code)) {
                JSONObject data = obj.getJSONObject("data");
                JSONArray cusList = data.getJSONArray("customerCountList");
                JSONArray seatList = data.getJSONArray("seatList");
                String isSeatPass = data.getString("isSeatPass"); // 1：要弹框， 0：不要弹框
                System.out.println(isSeatPass);
            }
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
