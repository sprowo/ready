package com.prowo.ydnamic.mapper;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.Map.Entry;

/**
 * 主要是XML字符串和Map之间的转换
 */
public class XMLMapper {
    public static <T> void marshall(T t, OutputStream stream) throws Exception {
        JAXBContext context = JAXBContext.newInstance(t.getClass());
        Marshaller ms = context.createMarshaller();
        ms.marshal(t, stream);
    }

    public static <T> String marshall(T t) throws Exception {
        String result = StringUtils.EMPTY;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        marshall(t, stream);
        result = stream.toString();
        stream.reset();
        stream.close();
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T unmarshall(InputStream stream, Class<T> clazz) throws Exception {
        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller shaller = context.createUnmarshaller();
        T t = (T) shaller.unmarshal(stream);
        return t;
    }

    public static <T> T unmarshall(String xmlData, Class<T> clazz) throws Exception {
        return unmarshall(new ByteArrayInputStream(xmlData.getBytes("utf-8")), clazz);
    }

//	public static void map2Xml(Map<String, Object> map, String root, OutputStream out) throws Exception {
//		Document document = DocumentHelper.createDocument();
//		Element nodeElement = document.addElement(root);
//		convert(map, nodeElement);
//		out = doc2Stream(document);
//	}

    public static String map2Xml(Map<String, Object> map, String root) throws Exception {
        Document document = DocumentHelper.createDocument();
        Element nodeElement = document.addElement(root);
        convert(map, nodeElement);
        return doc2String(document);
    }

    @SuppressWarnings("rawtypes")
    public static Map<String, Object> xml2Map(InputStream is) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        Document doc = null;
        SAXReader reader = new SAXReader();
        doc = reader.read(is);
        Element root = doc.getRootElement();
        for (Iterator iterator = root.elementIterator(); iterator.hasNext(); ) {
            Element e = (Element) iterator.next();
            List list = e.elements();
            if (list.size() > 0) {
                map.put(e.getName(), xml2Map(e));
            } else {
                // 空节点没必要生成map
                if (StringUtils.isNotBlank(e.getText())) {
                    map.put(e.getName(), e.getText());
                }
            }
        }
        return map;
    }

    public static Map<String, Object> xml2Map(String xml) throws Exception {
        return xml2Map(new ByteArrayInputStream(xml.getBytes("utf-8")));

    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> xml2Map(Element e) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Element> list = e.elements();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Element iter = (Element) list.get(i);
                List<Object> mapList = new ArrayList<Object>();

                if (iter.elements().size() > 0) {
                    Map<String, Object> m = xml2Map(iter);
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = new ArrayList<Object>();
                            mapList.add(obj);
                            mapList.add(m);
                        }
                        if (obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = (List<Object>) obj;
                            mapList.add(m);
                        }
                        map.put(iter.getName(), mapList);
                    } else {
                        map.put(iter.getName(), m);
                    }
                } else {
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = new ArrayList<Object>();
                            mapList.add(obj);
                            mapList.add(iter.getText());
                        }
                        if (obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = (List<Object>) obj;
                            mapList.add(iter.getText());
                        }
                        map.put(iter.getName(), mapList);
                    } else {
                        map.put(iter.getName(), iter.getText());
                    }
                }
            }
        } else {
            map.put(e.getName(), e.getText());
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private static Element convert(Map<String, Object> map, Element parentElement) throws Exception {
        Set<Entry<String, Object>> set = map.entrySet();
        Iterator<Entry<String, Object>> records = set.iterator();
        while (records.hasNext()) {
            Entry<String, Object> entry = (Entry<String, Object>) records.next();
            Element element = parentElement.element(String.valueOf(entry.getKey()));
            if (entry.getValue() != null && HashMap.class.equals(entry.getValue().getClass())) {// 子目录
                if (element == null) {
                    element = parentElement.addElement(String.valueOf(entry.getKey()));
                }
                convert((Map<String, Object>) entry.getValue(), element);
            } else if (entry.getValue() != null && ArrayList.class.equals(entry.getValue().getClass())) {// 子目录
                for (Map<String, Object> varMap : (List<Map<String, Object>>) entry.getValue()) {
                    element = parentElement.addElement(String.valueOf(entry.getKey()));
                    convert(varMap, element);
                }
            } else {// 到达顶点
                if (element == null) {
                    element = parentElement.addElement(String.valueOf(entry.getKey()));
                }
                element.setText(entry.getValue() == null ? StringUtils.EMPTY : entry.getValue().toString());
            }
        }
        return parentElement;
    }

//	private static OutputStream doc2Stream(Document document) throws Exception {
//		// 使用输出流来进行转化
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		// 使用UTF-8编码
//		OutputFormat format = new OutputFormat("   ", true, "UTF-8");
//		XMLWriter writer;
//		writer = new XMLWriter(out, format);
//		writer.write(document);
//		return out;
//	}

    private static String doc2String(Document document) {
        document.setXMLEncoding("UTF-8");
        return document.asXML();
    }

}
