package com.sort.stringval;

import java.util.Comparator;

public class DataKeyValueComparator implements  Comparator<DataKeyValue>{

	public int compare(DataKeyValue DataKeyValue1, DataKeyValue DataKeyValue2) {
		return Double.compare(DataKeyValue1.value, DataKeyValue2.value);
//		if(DataKeyValue1.value > DataKeyValue2.value)
//			return 1;
//		else if(DataKeyValue1.value == DataKeyValue2.value)
//			return 0;
//		else
//			return -1;
	}
}