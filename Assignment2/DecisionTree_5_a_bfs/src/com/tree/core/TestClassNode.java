package com.tree.core;

public class TestClassNode {

	public DecisionNode root = null;
	
	public DecisionNode getRoot() {
		return root;
	}
	public void setRoot(DecisionNode root) {
		this.root = root;
	}
	public void createNode(DecisionNode d){
		d = new DecisionNode();
	}
	public static void main(String[] args) {
		TestClassNode t = new TestClassNode();
		t.createNode(t.root);
		if(t.getRoot() ==null)
			System.out.println("root");
	}
}
