package com.danye.aihun.utils;

import com.danye.aihun.model.WXRespTextMessage;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息处理工具类
 *
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/25 10:26
 */
public class WXMessageUtil {

    /**
     * 解析微信发来的请求（XML）
     *
     * @param request 请求
     * @return Map<String, String>
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> parseXml(HttpServletRequest request)
            throws Exception {
        // 将解析结果存储在 HashMap 中
        Map<String, String> map = new HashMap<String, String>();
        // 从 request 中取得输入流
        InputStream inputStream = request.getInputStream();
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到 XML 根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();
        // 遍历所有子节点
        for (Element e : elementList) {
            map.put(e.getName(), e.getText());
        }
        // 释放资源
        inputStream.close();
        return map;
    }

    /**
     * 扩展 xstream 使其支持 CDATA
     */
    private XStream xstream = new XStream(new XppDriver() {
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out) {
                // 对所有 XML 节点的转换都增加 CDATA 标记
                boolean cdata = true;
                @SuppressWarnings("unchecked")
                public void startNode(String name, Class clazz) {
                    super.startNode(name, clazz);
                }
                protected void writeText(QuickWriter writer, String text) {
                    if (cdata) {
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    } else {
                        writer.write(text);
                    }
                }
            };
        }
    });

    /**
     * 文本消息对象转换成 XML
     *
     * @param wxRespTextMessage 文本消息对象
     * @return xml
     */
    public String messageToXml(WXRespTextMessage wxRespTextMessage) {
        xstream.processAnnotations(wxRespTextMessage.getClass());
        return xstream.toXML(wxRespTextMessage);
    }
}
