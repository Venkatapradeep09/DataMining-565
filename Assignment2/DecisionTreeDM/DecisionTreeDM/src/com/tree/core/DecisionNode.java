package com.tree.core;

public class DecisionNode {
	
	private String attributeName;
	private double thresold;
	private DecisionNode left = null;
	private DecisionNode right = null;
	private double impurity;
	private String classLable;
	
	
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
	
	
	
	
	
}
