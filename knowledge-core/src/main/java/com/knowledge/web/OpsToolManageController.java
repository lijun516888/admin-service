package com.knowledge.web;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.openapi.tool.util.StringUtils;
import com.gexin.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping({"/manage/tool"})
public class OpsToolManageController {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @RequestMapping({"index"})
    public String index(HttpServletRequest request, HttpServletResponse response) {


        return "/manage/tool/ops";
    }

    /**
     * 加载所有表和所有表中的字段信息
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping({"loadDbMeta"})
    public JSONObject loadDbMeta(HttpServletRequest request, HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        try {
            Connection conn = jdbcTemplate.getDataSource().getConnection();
            ResultSet rs = null;
            ResultSet rs1 = null;
            conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            DatabaseMetaData meta = conn.getMetaData();
            rs = meta.getTables(null, null, null, new String[]{"TABLE", "VIEW"});
            while(rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                System.out.println(tableName);
                rs1 = meta.getColumns(null, null, tableName.trim(), null);
                List<String> columns = Lists.newArrayList();
                while (rs1.next()) {
                    columns.add(rs1.getString("COLUMN_NAME"));
                }
                obj.put(tableName, columns.toArray(new String[] {}));
            }
        } catch (SQLException e) {
            log.error("DB操作失败{}", e);
        }
        return obj;
    }

    /**
     * 加载所有表和所有表中的字段信息
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping({"execute"})
    public JsonListResult<Map<String, Object>> execute(HttpServletRequest request, HttpServletResponse response) {
        JsonListResult<Map<String, Object>> result = new JsonListResult<>();

        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>();
        pageInfo.setCountOfCurrentPage(10);
        String page = request.getParameter("page");
        if (StringUtils.isNotBlank(page)) {
            pageInfo.setCurrentPage(Integer.parseInt(page));
        }
        String rows = request.getParameter("rows");
        if (StringUtils.isNotBlank(rows)) {
            pageInfo.setCountOfCurrentPage(Integer.parseInt(rows));
        }
        String sql = request.getParameter("sql");
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        String s1 = sql.toUpperCase();
        String queryCount = "SELECT COUNT(1) FROM " + s1.substring(s1.indexOf("FROM") + 4, s1.length());
        long totalCount = jdbcTemplate.queryForObject(queryCount, Long.class);

        int startNum = pageInfo.getCountOfCurrentPage() * (pageInfo.getCurrentPage() - 1);
        long totalPage = (totalCount % pageInfo.getCountOfCurrentPage() == 0 ? totalCount / pageInfo.getCountOfCurrentPage() : totalCount / pageInfo.getCountOfCurrentPage() + 1);
        String pageSql = sql + " limit " + startNum + "," + pageInfo.getCountOfCurrentPage();
        List<Map<String, Object>> r = jdbcTemplate.queryForList(pageSql);
        pageInfo.setPageResults(r);
        pageInfo.setTotalCount(totalCount);
        pageInfo.setTotalPage(totalPage);
        result.setRows(maps);
        result.setTotal(pageInfo.getTotalCount());
        Map<Object, Object> map = Maps.newLinkedHashMap();
        List<Map<String, String>> lm = Lists.newArrayList();
        if(r.size() > 0) {
            Iterator<Map.Entry<String, Object>> iterator = r.get(0).entrySet().iterator();
            while(iterator.hasNext()) {
                Map<String, String> f = Maps.newHashMap();
                String key = iterator.next().getKey();
                f.put("field", key);
                f.put("title", key);
                lm.add(f);
            }
        }
        map.put("fields", lm);
        result.setData(map);
        return result;
    }

}
