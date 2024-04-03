package buaa.guanz.prosummary.utils;

import java.util.Map;

public class ProjectSummaryGenerator {
	public String getProjectSummary(String projectName, Map<String, String> fileSummaryMap) {
		try {
            Thread.sleep(5000); // 睡眠 1000 毫秒，即 1 秒
        } catch (InterruptedException e) {
            // 处理中断异常
        	e.printStackTrace();
        }
		return "Babudb is a distributed, key-value store with a data structure based on LSM-tree.";
	}

}
