package com.gemo.core.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.gemo.core.constant.SystemConstant;

public class HttpUtils {

	private static final PoolingHttpClientConnectionManager connectionManager;
	private static final CloseableHttpClient httpClient;
    private static final String CHARSET = "UTF-8";
    
    static {
    	
    	Properties properties = CommonUtils.getPropertiesByResource("/config/system.properties");
    	
    	String connectTimeout = properties.getProperty("httpclient.connect.timeout", "1000");
    	String connectionRequestTimeout = properties.getProperty("httpclient.request.timeout", "1000");
    	String socketTimeout = properties.getProperty("httpclient.socket.timeout", "3600000");
    	
    	String maxTotal = properties.getProperty("httpclient.pool.total", "1200");
    	String defaultMaxPerRoute = properties.getProperty("httpclient.pool.per.route", "200");
    	
    	final String idleConnectionTimeout = properties.getProperty("httpclient.idle.timeout", "20000");
    	
    	System.out.println("httpclient.connect.timeout：" + connectTimeout);
    	System.out.println("httpclient.request.timeout：" + connectionRequestTimeout);
    	System.out.println("httpclient.socket.timeout：" + socketTimeout);
    	System.out.println("httpclient.pool.total：" + maxTotal);
    	System.out.println("httpclient.pool.per.route：" + defaultMaxPerRoute);
    	System.out.println("httpclient.idle.timeout：" + idleConnectionTimeout);
    	
        RequestConfig config = RequestConfig.custom().
        		//设置连接超时时间，单位毫秒
        		setConnectTimeout(new Integer(connectTimeout)).
        		//设置从connect Manager获取Connection 超时时间，单位毫秒。
        		setConnectionRequestTimeout(new Integer(connectionRequestTimeout)).
        		//请求获取数据的超时时间，单位毫秒。
        		setSocketTimeout(new Integer(socketTimeout)).build();
        

        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(new Integer(maxTotal));
        connectionManager.setDefaultMaxPerRoute(new Integer(defaultMaxPerRoute));
        
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).setConnectionManager(connectionManager).build();
        
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(new Runnable() {
			public void run() {
				while(true){
					CommonUtils.sleep(60000);
					System.out.println("关闭闲置" + idleConnectionTimeout + "毫秒及过期的Http连接池链接！");
					connectionManager.closeExpiredConnections();
					connectionManager.closeIdleConnections(new Integer(idleConnectionTimeout), TimeUnit.MILLISECONDS);
				}
			}
		});
    }
    
    public static String post(String url,Map<String,Object> params,Boolean isMultipart) throws Exception{
    	
    	HttpEntity reqEntity = null;
    	if(params!=null && params.size()>0){
    		if(isMultipart!=null && isMultipart){
    			MultipartEntityBuilder meb = MultipartEntityBuilder.create();
        		for(Map.Entry<String,Object> entry : params.entrySet()){
        			Object obj = entry.getValue();
        			ContentBody contentBody = null;
        			if(obj instanceof File){
        				contentBody = new FileBody((File)obj);
        				meb.addPart(entry.getKey(), contentBody);
        			}else if(obj instanceof File[]){
        				for(File file : (File[])obj){
        					contentBody = new FileBody(file);
            				meb.addPart(entry.getKey(), contentBody);
        				}
        			}else{
        				if(obj instanceof Date){
            				obj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date)obj);
            			}
        				contentBody = new StringBody(obj.toString(),ContentType.create("text/plain", Charset.forName(CHARSET)));
        				meb.addPart(entry.getKey(), contentBody);
        			}
        			
        		}
        		reqEntity = meb.build();
        		
        	}else{
        		List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
        		for(Map.Entry<String,Object> entry : params.entrySet()){
        			Object obj = entry.getValue();
        			if(obj instanceof Date){
        				obj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date)obj);
        			}else if(obj instanceof File){
        				throw new Exception("please set isMultipart true");
        			}
        			pairs.add(new BasicNameValuePair(entry.getKey(),entry.getValue().toString()));
        		}
                reqEntity = new UrlEncodedFormEntity(pairs,CHARSET);
        	}
    	}
    	
    	HttpPost httpPost = new HttpPost(url);
    	if(reqEntity!=null){
    		httpPost.setEntity(reqEntity);
    	}
    	
    	CloseableHttpResponse response = httpClient.execute(httpPost);
    	
    	int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200 && statusCode != 201) {
            httpPost.abort();
            throw new RuntimeException("HttpClient,error status code :" + statusCode);
        }
        
        HttpEntity entity = response.getEntity();
        
        String result = null;
        if (entity != null){
            result = EntityUtils.toString(entity, "utf-8");
        }
        EntityUtils.consume(entity);
        response.close();
    	
        return result;
    }
    
    public static void postFile(String url,Map<String,Object> params,String filePath) throws Exception{
    	
    	HttpEntity reqEntity = null;
    	if(params!=null && params.size()>0){
    		List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
    		for(Map.Entry<String,Object> entry : params.entrySet()){
    			Object obj = entry.getValue();
    			if(obj instanceof Date){
    				obj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date)obj);
    			}else if(obj instanceof File){
    				throw new Exception("please set isMultipart true");
    			}
    			pairs.add(new BasicNameValuePair(entry.getKey(),entry.getValue().toString()));
    		}
            reqEntity = new UrlEncodedFormEntity(pairs,CHARSET);
    	}
    	
    	HttpPost httpPost = new HttpPost(url);
    	if(reqEntity!=null){
    		httpPost.setEntity(reqEntity);
    	}
    	
    	CloseableHttpResponse response = httpClient.execute(httpPost);
    	
    	int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200 && statusCode != 201) {
            httpPost.abort();
            throw new RuntimeException("HttpClient,error status code :" + statusCode);
        }
        
        HttpEntity entity = response.getEntity();
        
        InputStream in = entity.getContent();
        
        File file = new File(filePath);
        if(!file.exists()){
        	file.createNewFile();
        }
        
        OutputStream out = new FileOutputStream(file);
        
        byte[] buffer = new byte[4096];
        int readLength = 0;
        while ((readLength=in.read(buffer)) > 0) {
	        byte[] bytes = new byte[readLength];
	        System.arraycopy(buffer, 0, bytes, 0, readLength);
	        out.write(bytes);
        }
        
        out.flush();
        out.close();
        
        EntityUtils.consume(entity);
        response.close();
    }
    
    public static String get(String url,Map<String,String> params) throws Exception {
        if(params != null && !params.isEmpty()){
            List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
            for(Map.Entry<String,String> entry : params.entrySet()){
                String value = entry.getValue();
                if(value != null){
                    pairs.add(new BasicNameValuePair(entry.getKey(),value));
                }
            }
            url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, CHARSET));
        }
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200 && statusCode != 201) {
            httpGet.abort();
            throw new RuntimeException("HttpClient,error status code :" + statusCode);
        }
        HttpEntity entity = response.getEntity();
        String result = null;
        if (entity != null){
            result = EntityUtils.toString(entity, "utf-8");
        }
        EntityUtils.consume(entity);
        response.close();
        return result;
    }
    
    public static void main(String[] args){
    	//上传文件列表
    	List<Map<String,String>> configList = new ArrayList<>();
    	for(int i=1;i<3;i++){
    		Map<String,String> fileConfigMap = new LinkedHashMap<>();
    		fileConfigMap.put("fileName","测试文件" + i);
    		fileConfigMap.put("fileSuffix","java");
    		fileConfigMap.put("subdirectory","/temp");
    		configList.add(fileConfigMap);
    	}
    	String filePath1 = "C:/Users/p50/Desktop/SoHoInspectionWorkList.java";
    	File file1 = new File(filePath1);
    	System.out.println(file1.exists());
    	String filePath2 = "C:/Users/p50/Desktop/SoHoWorkOrderListCount.java";
    	File file2 = new File(filePath2);
    	System.out.println(file2.exists());
    	
    	Map<String,Object> params = new LinkedHashMap<>();
    	try {
			params.put("jsonString", SystemConstant.jsonMapper.writeValueAsString(configList));
			params.put("file", new File[]{file1,file2});
			HttpUtils.post("http://localhost:8888/EMS_iSagy/Spring/MVC/entrance/unifier/upload", params, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
