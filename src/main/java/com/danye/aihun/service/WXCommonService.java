package com.danye.aihun.service;

import com.danye.aihun.utils.MyX509TrustManager;
import com.danye.aihun.utils.WXConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.util.Random;

/**
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/25 13:16
 */
@Service
@PropertySource("classpath:environment.properties")
public class WXCommonService implements InitializingBean {

    private static Logger log = LogManager.getLogger(WXCommonService.class);

    @Autowired
    private Environment environment;

    /**
     * 发送 https 请求
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return JsonNode（通过 JsonNode.get(key) 的方式获取 JSON 对象的属性值）
     */
    public JsonNode httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        JsonNode jsonNode = null;
        try {
            // 创建 SSLContext 对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述 SSLContext 对象中得到 SSLSocketFactory 对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            // 当 outputStr 不为 null 时，向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            conn.disconnect();
            jsonNode = new ObjectMapper().readTree(buffer.toString());
        } catch (ConnectException ce) {
            log.error("连接超时:{}", ce);
        } catch (Exception e) {
            log.error("https请求异常:{}", e);
        }
        return jsonNode;
    }

    /**
     * 创建随机字符串
     *
     * @param length 创建字符串的长度
     */
    public String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 获取当前时间戳
     *
     * @return 字符串
     */
    public String getCurrentTimeStamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }


    /**
     * 微信接口信息处理(错误信息，无返回值)
     *
     * @param errInfo  传入错误信息
     * @param log      Logger 对象
     * @param jsonNode JsonNode 对象
     */
    public void portExceptionInfoLog(String errInfo, Logger log, JsonNode jsonNode) {
        int errorCode = jsonNode.get("errcode").asInt();
        String errorMsg = jsonNode.get("errmsg").asText();
        log.error(errInfo + " errcode: " + errorCode + " errmsg: " + errorMsg);
    }

    /**
     * 微信接口信息处理(boolean 型返回值)
     *
     * @param info     传入信息
     * @param log      Logger 对象
     * @param jsonNode JsonNode 对象
     */
    public boolean portDealInfoLog(String info, Logger log, JsonNode jsonNode) {
        boolean result;
        int errorCode = jsonNode.get("errcode").asInt();
        String errorMsg = jsonNode.get("errmsg").asText();
        if (0 == errorCode) {
            result = true;
            log.info(info + "成功 errcode: " + errorCode + " errmsg: " + errorMsg);
        } else {
            result = false;
            log.error(info + "失败 errcode: " + errorCode + " errmsg: " + errorMsg);
        }
        return result;
    }

    /**
     * 微信授权登录地址
     */
    public String wxOauth2Url(String redirectUrl, String scope, String state) {
        String requestUrl = WXConstants.OAUTH2_AUTHORIZE_URL.replace("APPID", environment.getProperty("APPID"));
        requestUrl = requestUrl.replace("REDIRECT_URI", redirectUrl);
        requestUrl = requestUrl.replace("SCOPE", scope);
        requestUrl = requestUrl.replace("STATE", state);
        return requestUrl;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
    }
}
