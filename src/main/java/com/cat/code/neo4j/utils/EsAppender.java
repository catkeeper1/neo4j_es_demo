//package com.cat.code.neo4j.utils;
//
//import com.alibaba.fastjson.JSON;
//import lombok.Data;
//import org.apache.log4j.AppenderSkeleton;
//import org.apache.log4j.spi.ErrorCode;
//import org.apache.log4j.spi.LocationInfo;
//import org.apache.log4j.spi.LoggingEvent;
//import org.apache.log4j.spi.ThrowableInformation;
//import org.elasticsearch.action.bulk.BulkRequestBuilder;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.TransportAddress;
//import org.elasticsearch.common.xcontent.XContentBuilder;
//import org.elasticsearch.transport.client.PreBuiltTransportClient;
//import org.springframework.util.CollectionUtils;
//
//import java.io.IOException;
//import java.net.InetAddress;
//import java.net.NetworkInterface;
//import java.net.UnknownHostException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Enumeration;
//import java.util.List;
//
//import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
//
//@Data
//public class EsAppender extends AppenderSkeleton {
//    private static final String COLON_SEPARATOR = ":";
//    private static final String COMMA_SEPARATOR = ",";
//    private static final int DEFAULT_BUFFER_SIZE = 1;
//
//    /**
//     * 集群名称
//     */
//    private String clusterName;
//    /**
//     * Es集群地址
//     * ip:port,ip2:port2,ip3:port3
//     */
//    private String address;
//    /**
//     * 日志缓冲池大小,如果缓冲池大小为1则日志会被立即同步到ES中</br>
//     * 否则需要等到缓冲池Size达到bufferSize了后才会将日志刷新至ES</br>
//     * bufferSize默认初始化为1
//     */
//    private int bufferSize;
//    /**
//     * 日志缓冲数据
//     */
//    private List<XContentBuilder> buffers;
//    /**
//     * 日志删除数据
//     */
//    private List<XContentBuilder> removes;
//    /**
//     * 操作ES集群的客户端
//     */
//    private TransportClient client;
//    /**
//     * 插入索引
//     */
//    private String index;
//    /**
//     * 插入类型
//     */
//    private String type;
//
//    public EsAppender() {
//        buffers = new ArrayList<>();
//        removes = new ArrayList<>();
//    }
//
//    @Override
//    protected void append(LoggingEvent event) {
//        parseLog(event);
//        if (buffers.size() >= (bufferSize == 0 ? DEFAULT_BUFFER_SIZE : bufferSize)) {
//            flushBuffer();
//        }
//    }
//
//    private void parseLog(LoggingEvent event) {
//        LocationInfo locationInfo = event.getLocationInformation();
//        ThrowableInformation throwableInformation = event.getThrowableInformation();
//        // 判断是否打印堆栈信息
//        if (throwableInformation != null) {
//            Throwable throwable = throwableInformation.getThrowable();
//            try {
//                buffers.add(jsonBuilder()
//                        .startObject()
//                        .field("className", locationInfo.getClassName())
//                        .field("fileName", locationInfo.getFileName())
//                        .field("lineNumber", locationInfo.getLineNumber())
//                        .field("methodName", locationInfo.getMethodName())
//                        .field("serverIp", getIp())
//                        .field("logName", event.getLogger().getName())
//                        .field("logLevel", event.getLevel().toString())
//                        .field("logThread", event.getThreadName())
//                        .field("logMills", new Date(event.getTimeStamp()))
//                        .field("logMessage", JSON.toJSONString(event.getMessage()))
//                        .field("throwMessage", throwable.getMessage())
//                        .field("throwDetailMessage", throwable.toString())
//                        .field("throwStackTrace", JSON.toJSONString(throwable.getStackTrace()))
//                        .endObject());
//            } catch (IOException e) {
//                errorHandler.error("Error parseLog", e, ErrorCode.GENERIC_FAILURE);
//            }
//        } else {
//            // 下面这部分是打印普通日志信息的
//            try {
//                buffers.add(jsonBuilder()
//                        .startObject()
//                        .field("className", locationInfo.getClassName())
//                        .field("fileName", locationInfo.getFileName())
//                        .field("lineNumber", locationInfo.getLineNumber())
//                        .field("methodName", locationInfo.getMethodName())
//                        .field("serverIp", getIp())
//                        .field("logName", event.getLogger().getName())
//                        .field("logLevel", event.getLevel().toString())
//                        .field("logThread", event.getThreadName())
//                        .field("logMills", new Date(event.getTimeStamp()))
//                        .field("logMessage", JSON.toJSONString(event.getMessage()))
//                        .endObject());
//            } catch (IOException e) {
//                errorHandler.error("Error parseLog", e, ErrorCode.GENERIC_FAILURE);
//            }
//        }
//    }
//
//
//    /**
//     * 将数据写入到ES中
//     */
//    private void flushBuffer() {
//        if (!CollectionUtils.isEmpty(buffers)) {
//            BulkRequestBuilder bulkRequestBuilder = getClient().prepareBulk();
//            for (XContentBuilder xContentBuilder : buffers) {
//                bulkRequestBuilder.add(getClient().prepareIndex(index, type).setSource(xContentBuilder));
//                removes.add(xContentBuilder);
//            }
//            bulkRequestBuilder.get();
//            buffers.removeAll(removes);
//            removes.clear();
//        }
//    }
//
//    @Override
//    public void close() {
//        flushBuffer();
//        try {
//            if (client != null) {
//                client.close();
//            }
//        } catch (Exception e) {
//            errorHandler.error("Error closing client", e, ErrorCode.GENERIC_FAILURE);
//        }
//        this.closed = true;
//    }
//
//    @Override
//    public boolean requiresLayout() {
//        return false;
//    }
//
//    private TransportClient getClient() {
//        if (client == null) {
//            try {
//                System.setProperty("es.set.netty.runtime.available.processors", "false");
//                Settings settings = Settings.builder().put("cluster.name", clusterName).build();
//                client = new PreBuiltTransportClient(settings);
//                String[] addressArr = address.split(COMMA_SEPARATOR);
//                for (String address : addressArr) {
//                    String[] arr = address.split(COLON_SEPARATOR);
//                    client.addTransportAddresses(new TransportAddress(InetAddress.getByName(arr[0]), Integer.parseInt(arr[1])));
//                }
//            } catch (Exception e) {
//                errorHandler.error("Error getClient", e, ErrorCode.GENERIC_FAILURE);
//            }
//        }
//        return client;
//    }
//
//    private String getIp() throws UnknownHostException {
//        try {
//            InetAddress candidateAddress = null;
//            // 遍历所有的网络接口
//            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
//                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
//                // 在所有的接口下再遍历IP
//                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
//                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
//                    // 排除loopback类型地址
//                    if (!inetAddr.isLoopbackAddress()) {
//                        if (inetAddr.isSiteLocalAddress()) {
//                            // 如果是site-local地址，就是它了
//                            return inetAddr.getHostAddress();
//                        } else if (candidateAddress == null) {
//                            // site-local类型的地址未被发现，先记录候选地址
//                            candidateAddress = inetAddr;
//                        }
//                    }
//                }
//            }
//            if (candidateAddress != null) {
//                return candidateAddress.getHostAddress();
//            }
//            // 如果没有发现 non-loopback地址.只能用最次选的方案
//            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
//            if (jdkSuppliedAddress == null) {
//                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
//            }
//            return jdkSuppliedAddress.getHostAddress();
//        } catch (Exception e) {
//            UnknownHostException unknownHostException = new UnknownHostException(
//                    "Failed to determine LAN address: " + e);
//            unknownHostException.initCause(e);
//            throw unknownHostException;
//        }
//    }
//}
