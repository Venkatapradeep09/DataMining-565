package com.sort.stringval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

public class TestClass {

public static void main(String[] args) {
		
		ArrayList<DataKeyValue> dataListMap = new ArrayList<DataKeyValue>();
		

		dataListMap.add(new DataKeyValue("Iris-setosa",1.4));

		Collections.sort(dataListMap , new DataKeyValueComparator());
		for(DataKeyValue node: dataListMap){
			System.out.println(node.key +  " " +node.value  );
		}
		
		SortedSet<String> sortedNames = new TreeSet<String>();
	    sortedNames.add("Java");
	    sortedNames.add("SQL");
	    sortedNames.add("HTML");
	    sortedNames.add("CSS");
	    sortedNames.add("Java");
	    sortedNames.add("SQL");
	    sortedNames.add("HTML");
	    sortedNames.add("CSS");
	    System.out.println(sortedNames);
		
	}
}
