package com.tree.core;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.InputMap;

import com.main.InputData;

public class DecisionNode {
	
	private String attributeName;
	private double thresold;
	private DecisionNode left = null;
	private DecisionNode right = null;
	private double impurity;
	private String classLable;
	private Integer numberOfUnclassifiedRecords;
	private List<DataRecord> DataRecords;
	
	public Integer getStopClassification() {
		return stopClassification;
	}
	public void setStopClassification(Integer stopClassification) {
		this.stopClassification = stopClassification;
	}
	private Integer stopClassification = 1;
	
	
	public Integer getNumberOfUnclassifiedRecords() {
		return numberOfUnclassifiedRecords;
	}
	public void setNumberOfUnclassifiedRecords(Integer numberOfUnclassifiedRecords) {
		this.numberOfUnclassifiedRecords = numberOfUnclassifiedRecords;
		
	}
	public List<DataRecord> getDataRecords() {
		return DataRecords;
	}
	public void setDataRecords(List<DataRecord> dataRecords) {
		DataRecords = dataRecords;
		Map<String ,Integer> classLableCountValue = new HashMap<String ,Integer>();
		int max_count=0; String max_count_key ="";
		for( int i=0; i<dataRecords.size(); i++){
			if(classLableCountValue.containsKey(dataRecords.get(i).getClassLable()) == true){
				Integer countValue =classLableCountValue.get(dataRecords.get(i).getClassLable());
				classLableCountValue.put(dataRecords.get(i).getClassLable(), countValue+1);
				if(max_count < countValue+1){
					max_count = countValue+1;
					max_count_key = dataRecords.get(i).getClassLable();
				}
			}else{
				classLableCountValue.put(dataRecords.get(i).getClassLable(), 1);
				if(max_count < 1){
					max_count = 1;
					max_count_key = dataRecords.get(i).getClassLable();
				}
			}
				
		}
		this.setClassLable(max_count_key);
		
		this.numberOfUnclassifiedRecords = dataRecords.size() - max_count;
//		System.out.println(max_count_key + " _ " + max_count + "_" + this.numberOfUnclassifiedRecords);
	}
	
	
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public double getThresold() {
		return thresold;
	}
	public void setThresold(double thresold) {
		this.thresold = thresold;
	}
	public DecisionNode getLeft() {
		return left;
	}
	public void setLeft(DecisionNode left) {
		this.left = left;
	}
	public DecisionNode getRight() {
		return right;
	}
	public void setRight(DecisionNode right) {
		this.right = right;
	}
	public double getImpurity() {
		return impurity;
	}
	public void setImpurity(double impurity) {
		this.impurity = impurity;
	}
	public String getClassLable() {
		return classLable;
	}
	public void setClassLable(String classLable) {
		this.classLable = classLable;
	}
	
	
	public static void main(String[] args) {
		InputData inputData = new InputData();
		inputData.LoadData("./InputData/iris.data.tweek.txt");
		new DecisionNode().setDataRecords(inputData.getDataRecords());
		
	}
	
}
