package com.tree.core;
import java.util.*;
public class DataRecord {
	
	public DataRecord(){
		attibuteValues = new ArrayList<Double>();
	}
	private String classLable;
	private List<Double> attibuteValues;
	public static List<String> attributeNames;
	
	public String getClassLable() {
		return classLable;
	}
	public void setClassLable(String classLable) {
		this.classLable = classLable;
	}
	public List<Double> getAttibuteValues() {
		return attibuteValues;
	}
	public void setAttibuteValues(List<Double> attibuteValues) {
		this.attibuteValues = attibuteValues;
	}
	
	
	
	
}
