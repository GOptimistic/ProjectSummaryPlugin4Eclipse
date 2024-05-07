package buaa.guanz.prosummary.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import com.google.gson.Gson;

import buaa.guanz.prosummary.commons.BaseResult;
import buaa.guanz.prosummary.commons.JsonUtils;


public class HttpTools {
	
	public static String postformRequest(String url, Map<String, String> paramMap) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        String result = "";
        // 创建httpClient实例
        httpClient = HttpClients.createDefault();
        // 创建httpPost远程连接实例
        HttpPost httpPost = new HttpPost(url);
        // 配置请求参数实例(不需要可忽略)
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 设置连接主机服务超时时间
                .setConnectionRequestTimeout(30000)// 设置连接请求超时时间
                .setSocketTimeout(120000)// 设置读取数据连接超时时间
                .build();
//         为httpPost实例设置配置(不需要可忽略)
        httpPost.setConfig(requestConfig);
        // 设置请求头，配置form表单
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        // 封装表单参数
        if (null != paramMap && paramMap.size() > 0) {
            // 以下代码使用实现类BasicNameValuePair生成NameValuePair
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            // 通过map集成entrySet方法获取entity
            Set<Entry<String, String>> entrySet = paramMap.entrySet();
            // 循环遍历，获取迭代器
            Iterator<Entry<String, String>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Entry<String, String> mapEntry = iterator.next();
                nvps.add(new BasicNameValuePair(mapEntry.getKey(), mapEntry.getValue().toString()));
            }
 
            // 为httpPost设置封装好的请求参数
            try {
                // post实例塞参数的方法
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        try {
            // 服务器返回的所有信息都在HttpResponse中, httpClient对象执行post请求,并返回响应参数对象
            httpResponse = httpClient.execute(httpPost);
            // 先取出服务器返回的状态码,如果等于200说明success
            int code = httpResponse.getStatusLine().getStatusCode();
            assert code == 200;
            // 从响应对象中获取响应内容
            // EntityUtils.toString()有重载方法
            // 这个静态方法将HttpEntity转换成字符串,防止服务器返回的数据带有中文,所以在转换的时候将字符集指定成utf-8即可
            HttpEntity entity = httpResponse.getEntity();
            result = EntityUtils.toString(entity,"UTF-8");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != httpResponse) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
	
	public static String readFileToString(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line);
            content.append(System.lineSeparator());
        }
        reader.close();
        return content.toString();
    }
	
	public static void main(String[] args) {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("repoName", "babudb");
//		String fileContent = "";
//		try {
//            String filePath = "/Users/guanzheng/cls_work/python_test/github_repo_data/java/xtreemfs_babudb/java/babudb-core/src/main/java/org/xtreemfs/babudb/index/ByteRange.java";
//            fileContent = readFileToString(filePath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//		paramMap.put("context", fileContent);
		Map<String, String> summaryMap = new HashMap<>();
		summaryMap.put("a.b.c.test", "test");
		summaryMap.put("a.b.c.big", "big");
		summaryMap.put("a.b.c.d.small", "small");
		Gson gson = new Gson();
        String summaryJson = gson.toJson(summaryMap).toString();
		paramMap.put("summaries", summaryJson);
		String res = postformRequest(HttpVars.PROJECTSUMMARYURL, paramMap);
		System.out.println(res);
		BaseResult jsonObj = JsonUtils.getJsonResult(res);
		if (jsonObj != null) {
			System.out.println("Parse json data ok.");
			System.out.println(jsonObj.getRes());
		}
	}

}
