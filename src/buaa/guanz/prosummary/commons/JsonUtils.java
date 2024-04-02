package buaa.guanz.prosummary.commons;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonUtils {
	private static Gson gson = new Gson();
	
//	public static void main(String[] args) {
//		String jsonDataString = "{\"code\": 200, \"msg\": \"\", \"res\": [{\"location\": \"/root/PycharmProjects/flaskVulDetectProject/parsed/root/runtime-EclipseApplication/bugdet/src/bugdet/1722_1.c\", \"function\": \"vp9_compute_qdelta\", \"pred\": 1}, {\"location\": \"/root/PycharmProjects/flaskVulDetectProject/parsed/root/runtime-EclipseApplication/bugdet/src/bugdet/1723_1.c\", \"function\": \"estimate_bits_at_q\", \"pred\": 0}]}";
//		Gson gson = new Gson();
//		java.lang.reflect.Type type = new TypeToken<BaseResult>() {}.getType();
//        BaseResult res = gson.fromJson(jsonDataString, type);
//        System.out.println("OK");
//	}
	
	public static BaseResult getJsonResult(String jsonDataString) {
		java.lang.reflect.Type type = new TypeToken<BaseResult>() {}.getType();
		BaseResult res = null;
		try {
			res = gson.fromJson(jsonDataString, type);
		} catch (Exception e) {
			// TODO: handle exception
//			e.printStackTrace();
			System.err.println("Parse json data error.");
		}
        return res;
	}
}
