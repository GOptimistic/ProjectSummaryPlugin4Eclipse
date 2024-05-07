package buaa.guanz.prosummary.utils;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import buaa.guanz.prosummary.commons.BaseResult;
import buaa.guanz.prosummary.commons.JsonUtils;

public class ProjectSummaryGenerator {
	public String getProjectSummary(String projectName, Map<String, String> fileSummaryMap) {
//		try {
//            Thread.sleep(5000); // 睡眠 1000 毫秒，即 1 秒
//        } catch (InterruptedException e) {
//            // 处理中断异常
//        	e.printStackTrace();
//        }
//		return "Babudb is a distributed, key-value store with a data structure based on LSM-tree.";
//		return "BabuDB is a distributed, fault-tolerant, highly scalable, and high-performance key-value store built on top of HBase.";
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("repoName", projectName);
		Gson gson = new Gson();
        String summaryJson = gson.toJson(fileSummaryMap).toString();
		paramMap.put("summaries", summaryJson);
		String res = HttpTools.postformRequest(HttpVars.PROJECTSUMMARYURL, paramMap);
		System.out.println(res);
		BaseResult jsonObj = JsonUtils.getJsonResult(res);
		if (jsonObj != null) {
			System.out.println("Parse json data ok.");
			System.out.println("###### Project Summary " + jsonObj.getCode() + " " + jsonObj.getRes());
			return jsonObj.getRes();
		} else {
			return "Generate project summary failed";
		}
	}

}
