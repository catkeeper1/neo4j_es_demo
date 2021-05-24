//package com.cat.code.neo4j.utils;
//
//import java.net.MalformedURLException;
//import java.util.HashMap;
//import java.util.Map;
//
//import com.internetitem.logback.elasticsearch.ElasticsearchAppender;
//import com.internetitem.logback.elasticsearch.config.ElasticsearchProperties;
//import com.internetitem.logback.elasticsearch.config.HttpRequestHeader;
//import com.internetitem.logback.elasticsearch.config.HttpRequestHeaders;
//import com.internetitem.logback.elasticsearch.config.Property;
//
//import ch.qos.logback.classic.Level;
//import ch.qos.logback.classic.Logger;
//import ch.qos.logback.classic.LoggerContext;
//
///**
// *
// * 获取logback的日志系统，将日志数据写入es数据库
// *
// * @author mgguan
// *
// */
//public class LogUtil {
//
//    /**
//     *
//     * @param clazz     当前记录日志的class，不可以为空
//     * @param esAddress ip:port，不可以为空
//     * @return logback的Logger，内含es的appender
//     */
//    public static Logger getEsLogger(Class<?> clazz, String esAddress) {
//        return getEsLogger(clazz, esAddress, "logs-%date{yyyy-MM-dd}");
//    }
//
//    /**
//     *
//     * @param clazz     当前记录日志的class，不可以为空
//     * @param esAddress ip:port，不可以为空
//     * @param index     写入es的索引，默认是logs-%date{yyyy-MM-dd}, 不可以为空
//     * @return logback的Logger，内含es的appender
//     */
//    public static Logger getEsLogger(Class<?> clazz, String esAddress, String index) {
//        Map<String, Logger> map = new HashMap<String, Logger>();
//        if (map.get(clazz.getName()) != null) {
//            return map.get(clazz.getName());
//        }
//
//        LoggerContext logCtx = new LoggerContext();
//        ElasticsearchAppender esAppender = new ElasticsearchAppender();
//        esAppender.setContext(logCtx);
//        esAppender.setName(clazz.getName());
//        esAppender.setIndex(index);
//
//        esAppender.setType("doc");
//        esAppender.setLoggerName("es-logger");
//        esAppender.setErrorLoggerName("errorLoggerName");
//        esAppender.setMaxMessageSize(100);
//
//        ElasticsearchProperties elasticsearchProperties = new ElasticsearchProperties();
//        Property p1 = new Property();
//        p1.setName("level");
//        p1.setValue("%level");
//        Property p2 = new Property();
//        p2.setName("logger");
//        p2.setValue("%logger");
//        Property p3 = new Property();
//        p3.setName("thread");
//        p3.setValue("%thread");
//        Property p4 = new Property();
//        p4.setName("stacktrace");
//        p4.setValue("%ex");
//        elasticsearchProperties.addProperty(p1);
//        elasticsearchProperties.addProperty(p2);
//        elasticsearchProperties.addProperty(p3);
//        elasticsearchProperties.addProperty(p4);
//        esAppender.setProperties(elasticsearchProperties);
//
//        try {
//            esAppender.setUrl(String.format("http://%s/_bulk", esAddress));
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        HttpRequestHeaders headers = new HttpRequestHeaders();
//        HttpRequestHeader header = new HttpRequestHeader();
//        header.setName("Content-Type");
//        header.setValue("application/json");
//        headers.addHeader(header);
//        esAppender.setHeaders(headers);
//        esAppender.start();
//
//        Logger log = logCtx.getLogger(clazz);
//        log.setAdditive(false);
//        log.setLevel(Level.ALL);
//        log.addAppender(esAppender);
//        map.put(clazz.getName(), log);
//        return log;
//    }
//
//
//}