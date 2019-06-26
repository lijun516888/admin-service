package com.knowledge.utils;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {
	
	private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    private static final String DEFAULT_CHARSET = "UTF-8";

    private static final String METHOD_POST = "POST";

    private static final String METHOD_GET = "GET";

    private static SSLContext ctx = null;

    private static HostnameVerifier verifier;

    private static SSLSocketFactory socketFactory = null;

    private static class DefaultTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    static {
        try {
            ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() }, new SecureRandom());
            ctx.getClientSessionContext().setSessionTimeout(15);
            ctx.getClientSessionContext().setSessionCacheSize(1000);
            socketFactory = ctx.getSocketFactory();
        } catch (Exception e) {

        }
        verifier = (s, sslSession) -> true;
    }

    public static String doGet(String url, Map<String, String> params, int connectTimeout, int readTimeout) throws IOException {
        return doGet(url, params, DEFAULT_CHARSET, connectTimeout, readTimeout);
    }

    public static String doGet(String url, Map<String, String> params, String charset, int connectTimeout, int
            readTimeout) throws IOException {
        HttpURLConnection conn = null;
        String rsp;
        try {
            String cType = "application/x-www-form-urlencoded;charset=" + charset;
            String query = buildQuery(params, charset);
            try {
                conn = getConnection(buildGetUrl(url, query), METHOD_GET, cType);
                conn.setConnectTimeout(connectTimeout);
                conn.setReadTimeout(readTimeout);
            } catch (IOException e) {
                log.error("请求服务失败,访问地址为:" + url);
                throw e;
            }
            try {
                rsp = getResponseAsString(conn);
            } catch (IOException e) {
                log.error("请求服务失败,访问地址为:" + url);
                throw e;
            }
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }
        return rsp;
    }

    public static String doPost(String url, Map<String, String> params, int connectTimeout, int readTimeout) throws
            IOException {
        return doPost(url, params, DEFAULT_CHARSET, connectTimeout, readTimeout);
    }

    public static String doPost(String url, Map<String, String> params, String charset, int connectTimeout, int
            readTimeout) throws IOException {
        String cType = "application/x-www-form-urlencoded;charset=" + charset;
        String query = buildQuery(params, charset);
        byte[] content = {};
        if(StringUtils.isNotEmpty(query)) {
            content = query.getBytes(charset);
        }
        return doPost(url, cType, content, connectTimeout, readTimeout);
    }

    public static String doPost(String url, String cType, byte[] content, int connectTimeout, int readTimeout) throws
            IOException {
        HttpURLConnection conn = null;
        OutputStream out = null;
        String rsp;
        try {
            try {
                conn = getConnection(new URL(url), METHOD_POST, cType);
                conn.setConnectTimeout(connectTimeout);
                conn.setReadTimeout(readTimeout);
            } catch (IOException e) {
                log.error("请求服务失败,访问地址为:" + url);
                throw e;
            }
            try {
                out = conn.getOutputStream();
                out.write(content);
                rsp = getResponseAsString(conn);
            } catch (IOException e) {
                log.error("请求服务失败,访问地址为:" + url);
                throw e;
            }
        } finally {
            if (out != null) {
                out.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rsp;
    }

    public static String doPost(String url, Map<String, String> params, Map<String, FileItem> fileParams,
                                int connectTimout, int readTimeout) throws IOException {
        if(fileParams == null || fileParams.size() == 0) {
            return doPost(url, params, DEFAULT_CHARSET, connectTimout, readTimeout);
        } else {
            return doPost(url, params, DEFAULT_CHARSET, fileParams, connectTimout, readTimeout);
        }
    }

    public static String doPost(String url, Map<String, String> params, String charset, Map<String, FileItem>
            fileParams, int connectTimeout, int readTimeout) throws IOException {
        if(fileParams == null || fileParams.isEmpty()) {
            return doPost(url, params, charset, connectTimeout, readTimeout);
        }
        String boundary = System.currentTimeMillis() + "";
        HttpURLConnection conn = null;
        OutputStream out = null;
        String rsp;
        try {
            try {
                String cType = "multipart/form-data;boundary=" + boundary + ";charset=" + charset;
                conn = getConnection(new URL(url), METHOD_POST, cType);
                conn.setConnectTimeout(connectTimeout);
                conn.setReadTimeout(readTimeout);
            } catch (IOException e) {
                log.error("请求服务失败,访问地址为:" + url);
                throw e;
            }
            try {
                out = conn.getOutputStream();
                byte[] entryBoundaryBytes = ("\r\n--" + boundary + "\r\n").getBytes();
                for(Map.Entry<String, String> textEntry : params.entrySet()) {
                    byte[] textBytes = getTextEntry(textEntry.getKey(), textEntry.getValue(), charset);
                    out.write(entryBoundaryBytes);
                    out.write(textBytes);
                }
                for(Map.Entry<String, FileItem> fileEntry : fileParams.entrySet()) {
                    FileItem fileItem = fileEntry.getValue();
                    byte[] fileBytes = getFileEntry(fileEntry.getKey(),
                            fileItem.getFileName(),
                            fileItem.getMimeType(),
                            charset);
                    out.write(entryBoundaryBytes);
                    out.write(fileBytes);
                    out.write(fileItem.getContent());
                }
                byte[] endBoundaryBytes = ("\r\n--" + boundary + "--\r\n").getBytes(charset);
                out.write(endBoundaryBytes);
                rsp = getResponseAsString(conn);
            } catch (IOException e) {
                log.error("请求服务失败,访问地址为:" + url);
                throw e;
            }
        } finally {
            if(out != null) {
                out.close();
            }
            if(conn != null) {
                conn.disconnect();
            }
        }
        return rsp;
    }

    public static String buildQuery(Map<String, String> params, String charset) throws IOException {
        if(params == null || params.isEmpty()) {
            return null;
        }
        StringBuilder query = new StringBuilder();
        boolean hasParam = false;
        for(Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if(StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
                if(hasParam) {
                    query.append("&");
                } else {
                    hasParam = true;
                }
                query.append(key).append("=").append(URLEncoder.encode(value, charset));
            }
        }
        return query.toString();
    }

    private static HttpURLConnection getConnection(URL url, String method, String cType) throws IOException {
        HttpURLConnection conn;
        if("https".equals(url.getProtocol())) {
            HttpsURLConnection connHttps = (HttpsURLConnection) url.openConnection();
            connHttps.setSSLSocketFactory(socketFactory);
            connHttps.setHostnameVerifier(verifier);
            conn = connHttps;
        } else {
            conn = (HttpURLConnection) url.openConnection();
        }
        conn.setRequestMethod(method);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Accept", "text/xml,text/javascript,text/html");
        conn.setRequestProperty("User-Agent", "aop-sdk-java");
        conn.setRequestProperty("Content-Type", cType);
        return conn;
    }

    @SuppressWarnings("unused")
	private static Map<String, String> getParamsFromUrl(String url) {
        Map<String, String> map = null;
        if(url != null && url.indexOf('?') != -1) {
            map = splitUrlQuery(url.substring(url.indexOf('?') + 1));
        }
        if(map == null) {
            map = new HashMap<>();
        }
        return map;
    }

    public static Map<String, String> splitUrlQuery(String query) {
        Map<String, String> result = Maps.newHashMap();
        String[] pairs = query.split("&");
        if(pairs != null && pairs.length > 0) {
            for(String pair : pairs) {
                String[] param = pair.split("=", 2);
                if(param != null && param.length == 2) {
                    result.put(param[0], param[1]);
                }
            }
        }
        return result;
    }

    protected static String getResponseAsString(HttpURLConnection conn) throws IOException {
        String charset = getResponseCharset(conn.getContentType());
        InputStream es = conn.getErrorStream();
        if(es == null) {
            return getStreamAsString(conn.getInputStream(), charset);
        } else {
            String msg = getStreamAsString(es, charset);
            if(StringUtils.isEmpty(msg)) {
                throw new IOException(conn.getResponseCode() + ":" + conn.getResponseMessage());
            } else {
                throw new IOException(msg);
            }
        }
    }

    private static String getStreamAsString(InputStream stream, String charset) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
            StringWriter writer = new StringWriter();
            char[] chars = new char[256];
            int count;
            while((count = reader.read(chars)) > 0) {
                writer.write(chars, 0, count);
            }
            return writer.toString();
        } finally {
            if(stream != null) {
                stream.close();
            }
        }
    }

    private static String getResponseCharset(String ctype) {
        String charset = DEFAULT_CHARSET;
        if (!StringUtils.isEmpty(ctype)) {
            String[] params = ctype.split(";");
            for (String param : params) {
                param = param.trim();
                if (param.startsWith("charset")) {
                    String[] pair = param.split("=", 2);
                    if (pair.length == 2) {
                        if (!StringUtils.isEmpty(pair[1])) {
                            charset = pair[1].trim();
                        }
                    }
                    break;
                }
            }
        }
        return charset;
    }

    private static URL buildGetUrl(String strUrl, String query) throws IOException {
        URL url = new URL(strUrl);
        if (StringUtils.isEmpty(query)) {
            return url;
        }
        if (StringUtils.isEmpty(url.getQuery())) {
            if (strUrl.endsWith("?")) {
                strUrl = strUrl + query;
            } else {
                strUrl = strUrl + "?" + query;
            }
        } else {
            if (strUrl.endsWith("&")) {
                strUrl = strUrl + query;
            } else {
                strUrl = strUrl + "&" + query;
            }
        }
        return new URL(strUrl);
    }

    private static byte[] getTextEntry(String fieldName, String fieldValue, String charset) throws IOException {
        StringBuilder entry = new StringBuilder();
        entry.append("Content-Disposition:form-data;name=\"");
        entry.append(fieldName);
        entry.append("\"\r\nContent-Type:text/plain\r\n\r\n");
        entry.append(fieldValue);
        return entry.toString().getBytes(charset);
    }

    @SuppressWarnings("unused")
    private static byte[] getFileEntry(String fieldName, String fileName, String mimeType, String charset) throws
            IOException {
        StringBuilder entry = new StringBuilder();
        entry.append("Content-Disposition:form-data;name=\"");
        entry.append(fieldName);
        entry.append("\";filename=\"");
        entry.append(fileName);
        entry.append("\"\r\nContent-Type:");
        entry.append(mimeType);
        entry.append("\r\n\r\n");
        return entry.toString().getBytes(charset);
    }

    public static void main(String[] args) {
        try {
            /*Map<String, String> params = Maps.newHashMap();
            params.put("userName", "abc");
            File file = new File("C:/Users/Administrator/Desktop/picture/123.png");
            InputStream inputStream = new FileInputStream(file);
            Map<String, FileItem> fileParams = Maps.newHashMap();
            FileItem fileItem1 = new FileItem();
            fileItem1.setFileName(file.getName());
            fileItem1.setContent(ByteStreams.toByteArray(inputStream));
            fileParams.put("file", fileItem1);
            String rsp = HttpUtils.doPost("http://113.140.71.46:10005/dataCenter/api/sizeField/img.jhtml",
                    params, fileParams, 2000, 5000);
            System.out.println(rsp);
            JSONObject jsonObj = JSONObject.parseObject(rsp);
            if("1000".equals(jsonObj.getString("code"))) {
            	JSONArray array = jsonObj.getJSONArray("data");
            	JSONObject obj = (JSONObject) array.get(0);
            	System.out.println(obj.getString("url"));
			} else {
				
			}*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
