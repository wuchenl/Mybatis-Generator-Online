package com.letters7.wuchen.demo.generator;

import com.alibaba.fastjson.JSON;
import com.letters7.wuchen.demo.generator.model.DBType;
import com.letters7.wuchen.demo.generator.model.GeneratorConfig;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.security.SSLProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.junit.Test;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.namespace.QName;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Objects;


/**
 * @author wuchen
 * @version 0.1
 * @date 2018/12/27 10:10
 * @desc
 */
public class TestClass {

    /**
     * 获取https的webService接口数据
     *
     * @param wsdl      地址
     * @param namespace 命名空间，为namespace
     * @param method    具体调用的方法模块
     * @param str       参数值
     * @return 对应的结果值
     */
    public static String getRainbowData(String wsdl, String namespace, String method, String str) {
        String result = "";
        try {
            RPCServiceClient serviceClient = new RPCServiceClient();
            Options options = serviceClient.getOptions();
            // 超时时间 180S
            options.setTimeOutInMilliSeconds(180000);
            // 指定调用WebService的URL
            EndpointReference targetEPR = new EndpointReference(wsdl);
            options.setTo(targetEPR);
            // 重点。忽略https证书  (ProtocolSocketFactory) 可以去除。会提示过时。但实际内部也是这样的
//            options.setProperty(HTTPConstants.CUSTOM_PROTOCOL_HANDLER, new Protocol("https", (ProtocolSocketFactory) new SSLProtocolSocketFactory(getSSLContext()), 443));
            // 指定方法的参数值，可以为空，但为空也必须在调用时传入是new Object[]的形式
            Object[] requestParam = new Object[]{str};
            // 指定方法返回值的数据类型的Class对象 同上
            Class<?>[] responseParam = new Class[]{String.class};
            // 指定要调用的getGreeting方法及WSDL文件的命名空间
            QName requestMethod = new QName(namespace, method);
            // 调用方法并输出该方法的返回值
            Object[] objects = serviceClient.invokeBlocking(requestMethod, requestParam,
                    responseParam);
            if (objects.length > 0) {
                result = String.valueOf(objects[0]);
            } else {
                System.out.println("没有获取到值！");
            }
        } catch (AxisFault e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("取到的值为：" + result);
        return result;
    }

    public static SSLContext getSSLContext() {
        TrustManager[] customTrustManager = new TrustManager[]{new X509TrustManager() {

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};
        SSLContext sslCtx = null;
        try {
            sslCtx = SSLContext.getInstance("SSL");
            sslCtx.init(null, customTrustManager, null);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return sslCtx;
    }

    @Test
    public void testWebService() {
//        String wsdlUrl = "https://doris.darewayhealth.com:9020/doris/PackDataService?wsdl";
        String wsdlUrl = "http://2v190m5974.imwork.net:34036/doris/PackDataService?wsdl";
        String namespace = "http://client.currency.com/";
        String method = "QueryMedicalInfo";
        String str = "{\"encodeRules\":\"djFpx4QiKEklvu8UF5FMO1RS1rQn3P0ZCCb4PeDDlPFMckhLLzgwEmvUNygpwnQCg89XRUT2VJAucJZeFOBW/wVylvou8Xpl/Awaayl02j9oacn64caHAmNZwKLj00hMgvQo31Lf7TYY5r5TpObjSRl2TmP6ErMxtuNhdE4Wh50=\",\"sign\":\"rL+DEbV5+XJU0/4fV/pSj/MfOL8Jk2boPTba31BywXhwc39IQ7BeKRb8I38KpwIAsjSDE6YTASpYFjUfKjLYqDJonrpZULxWpC85uI8+JUh9rgmAH7W9UV5TrNPUZ/17Pu5c08HqGU/lZ6b2pp4wkI+VJgZUdTZXi4JGey8Ynjo=\",\"content\":\"YjsQ8r1mwrsafMtsx8GJjNSKs/EEPKkWKq+A0x7VepdD/fk//9AVYb3ANSsEUot5bRPz/g6PTdubCkvzonQEiNbOfUfff6Om2ByUMmhbkKIPMC/z2zWV6b4Aklfn/Io+SrakgC1Uzng+QZUA693l3XCHgQ4cwQO/4itUhlQyJA4=\",\"timestamp\":1551064931292}tb37005";
        System.out.println(str);
        String result = getRainbowData(wsdlUrl, namespace, method, str);
        System.out.println(result);

    }

    @Test
    public void testClassFind() throws ClassNotFoundException, IOException {

        String path = Class.forName(DBType.MySQL5.getDbClass()).getProtectionDomain().getCodeSource().getLocation().getPath();
        File file = new File(path);
        String libPath = URLDecoder.decode(file.getCanonicalPath(), Charset.forName("UTF-8").displayName());
        System.out.println(libPath);
    }

    @Test
    public void testNull() {
        String json = "{\"annotation\":false,\"annotationDAO\":false,\"comment\":false,\"jsr310Support\":false,\"needForUpdate\":false,\"needToStringHashcodeEquals\":false,\"offsetLimit\":false,\"overrideXML\":false,\"useActualColumnNames\":false,\"useDAOExtendStyle\":false,\"useExample\":false,\"useSchemaPrefix\":false,\"useTableNameAlias\":false}\n";
        GeneratorConfig generatorConfig2 = GeneratorConfig.builder().build();
        GeneratorConfig generatorConfig = JSON.parseObject(json, GeneratorConfig.class);
        generatorConfig2.setProjectFolder(generatorConfig.getProjectFolder());
        System.out.println(generatorConfig2);
    }

}
