package buaa.guanz.prosummary.commons;

import java.util.*;

public class BaseResult {
	int code;
	String msg;
	List<Predict> res;
	
//	public BaseResult(int code, String msg, List<Predict> predicts) {
//		this.code = code;
//		this.msgString = msg;
//		this.predicts = predicts;
//	}
	
	public List<Predict>  getRes() {
		return this.res;
	}
	
	public static class Predict {
		private String location;
		private String function;
		private int pred;
		
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public String getFunction() {
			return function;
		}
		public void setFunction(String function) {
			this.function = function;
		}
		public int getPred() {
			return pred;
		}
		public void setPred(int pred) {
			this.pred = pred;
		}
	}
}
