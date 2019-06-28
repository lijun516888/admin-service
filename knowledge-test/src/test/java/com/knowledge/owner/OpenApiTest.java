package com.knowledge.owner;

import com.gexin.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.knowledge.common.factory.db.DbFactory;
import com.knowledge.common.factory.db.GenricDbFactory;
import com.knowledge.common.factory.db.MysqlDb;
import com.knowledge.utils.HttpUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class OpenApiTest {

    /**
     * OpenApi datamGround
     * local
     * 开放平台中数据中台的测试方式
     */
    @Test
    public void DataCenterTest() throws Exception {
        DbFactory<MysqlDb> factory = new GenricDbFactory<>();
        MysqlDb db = factory.getDb(MysqlDb.class);
        db.init("root","111111","jdbc:mysql://192.168.1.250:3305/callcenter-kstk");
        JdbcTemplate jdbc = db.loadJdbcTemplate();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("PHONENO1 AS tel, ");
        sb.append("NAME AS name, ");
        sb.append("(CASE WHEN SEX = '1001001' THEN '1' ELSE '0' END) AS sex, ");
        sb.append("BIRTHDAY AS birthday, ");
        sb.append("'1' AS 'cardType', ");
        sb.append("FIELD2 AS cardNo, ");
        sb.append("PROVINCE AS province, ");
        sb.append("CITY AS city, ");
        sb.append("AREA AS county, ");
        sb.append("ADDRESS AS address, ");
        sb.append("FIELD18 AS remark, ");
        sb.append("'3' AS dataType, ");
        sb.append("'北大测试' AS dataSource, ");
        sb.append("'泰康数据库' AS advertiserId ");
        sb.append("FROM crm_customer2000 LIMIT 1000,10 ");
        List<Map<String, Object>> maps = jdbc.queryForList(sb.toString());
        // 生成签名
        String key = "808493665ec3c140d159064a429db787";
        Map<String, String> map = Maps.newHashMap();
        map.put("signType", "MD5");
        map.put("requestNo", System.currentTimeMillis()+""+System.currentTimeMillis());
        map.put("protocol", "HTTP_FORM_JSON");
        map.put("service", "datamGround");
        map.put("partnerId", "com.ksdata.center");
        map.put("version", "1.0");
        map.put("batchId", System.currentTimeMillis()+"");
        map.put("timestamp", System.currentTimeMillis()+"");
        map.put("workplaceCode", "6000");
        map.put("batchMonth", "201906");
        map.put("mdFirstTypeName", "1026013");
        map.put("mdSecondTypeName", "1026013001");
        map.put("mdThirdTypeName", "1026013001001");
        map.put("batchCode", "ks00003");
        map.put("batchRemark", "从泰康转移到北大");
        map.put("batchFullName", "201906-西安北大-ks00003-水果-香蕉-A1-从泰康转移到北大");
        map.put("customers", JSONObject.toJSONString(maps));
        Map<String, Object> sortMap = new TreeMap<>(map);
        StringBuilder stringToSign = new StringBuilder();
        String signature;
        if(sortMap.size() > 0) {
            sortMap.forEach((k, v) -> {
                if(StringUtils.isNotBlank(k) && StringUtils.isNotBlank(v.toString())) {
                    stringToSign.append(k).append("=").append(v.toString()).append("&");
                }
            });
            stringToSign.deleteCharAt(stringToSign.length() - 1);
        }
        stringToSign.append(key);
        signature = DigestUtils.md5Hex(stringToSign.toString().getBytes("UTF-8"));
        System.out.println("生成签名:" + signature);
        map.put("sign", signature);
        HttpUtils.doPost("http://localhost:8889/gateway.html", map,5000, 5000);
    }

}
