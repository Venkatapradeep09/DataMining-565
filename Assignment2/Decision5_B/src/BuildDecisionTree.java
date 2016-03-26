import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.main.InputData;
import com.sort.stringval.DataKeyValue;
import com.sort.stringval.DataKeyValueComparator;


import com.tree.core.DataRecord;
import com.tree.core.DecisionNode;


public class BuildDecisionTree {

	private static Integer FlagFilterGiniEntropy=0;
	private DecisionNode root;
	private static Integer totalTrainingRecordSize = 1;
	public static void main(String[] args) {
		long date = System.currentTimeMillis();
		Scanner in = new Scanner(System.in);
		System.out.println("Please enter 0 for gini filter 1 for entropy filter");
		FlagFilterGiniEntropy = in.nextInt();
		
		InputData inputData = new InputData();
		inputData.LoadData("./InputData/vet_c_Data.txt");
		ArrayList<Integer> hitsAllFlods = new ArrayList<Integer>();
		Integer testRecordCount = 0;
		for( int i =1; i<12 ; i++){
			
			inputData.placeTestTrainFoldData(i);
			List<DataRecord> trainingDataRecords = inputData.getTraindDataRecords();
			totalTrainingRecordSize = trainingDataRecords.size();
	//			System.out.println(trainingDataRecords.size());
			BuildDecisionTree decisionTree = new BuildDecisionTree();
//			System.out.println("\n--------------\n");
//			decisionTree.printDataRecords(inputData.testDataRecords);
			
//			System.out.println(decisionTree.getTheBestAttributeForSpilliting(dataRecords));
			decisionTree.buildTree(trainingDataRecords ,  inputData);
			testRecordCount += inputData.testDataRecords.size();
//			System.out.println("\n-----------------------------------");
			hitsAllFlods.add(decisionTree.test(inputData.testDataRecords));
		}
		double sum = 0;
		for( int k =0; k<hitsAllFlods.size(); k++){
//			System.out.println(hitsAllFlods.get(k));
			sum += hitsAllFlods.get(k);
		}
		
		System.out.println("Accuracy :" + (sum*100)/testRecordCount);
		long date1 = System.currentTimeMillis();
		System.out.println(date1-date);
	}
	
	private Integer test(List<DataRecord> testDataRecords) {
		
		int numberOfHits = 0;
		for( int i =0; i <testDataRecords.size(); i++){
			
			DataRecord dataRecord = testDataRecords.get(i);
			String classLable = dataRecord.getClassLable();
			DecisionNode temp = root;
			while(temp.getLeft() != null || temp.getRight() != null){
				 String thresoldValue = temp.getAttributeName();
				 double nodeSplit = temp.getThresold();
				 int index = DataRecord.attributeNames.indexOf(thresoldValue);
				 double testDataAttributeVal =  dataRecord.getAttibuteValues().get(index);
				 if(testDataAttributeVal <= nodeSplit){
					 temp = temp.getLeft();
				 }else
					 temp = temp.getRight();
			}
			if(classLable.equalsIgnoreCase(temp.getClassLable())){
				numberOfHits++;
			}
			
			
		}
//		System.out.println(numberOfHits);
		return numberOfHits;
	}

	public void buildTree(List<DataRecord> dataRecords, InputData inputData){
		if(root == null)
			root = new DecisionNode();
		root.setDataRecords(dataRecords);
		this.constructTreeRecursively(root , inputData);
	}
	
	private void constructTreeRecursively(
			DecisionNode rootNode, InputData inputData) {
		
		List<DecisionNode> decisionNodes = new ArrayList<DecisionNode>();
		decisionNodes.add(rootNode);
		while(decisionNodes.size() > 0){
			DecisionNode root2 =  decisionNodes.get(0);
			decisionNodes.remove(0);
			List<DataRecord> dataRecords = root2.getDataRecords();
			
			if(this.stopping_build_tree(dataRecords) == true){
				
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
				root2.setClassLable(max_count_key);
	
				
			}else{
				
				String minimumGini_minimumGiniAttribute_bestSplit = getTheBestAttributeForSpilliting(dataRecords);
				String[] arr = minimumGini_minimumGiniAttribute_bestSplit.split("_");
				Double minGini = Double.parseDouble(arr[0]);
				String minimumGiniAttribute = arr[1];
				Double bestSplit = Double.parseDouble(arr[2]); 
	//			System.out.println( " minGini :" + minGini +" minimumGiniAttribute :" + minimumGiniAttribute + " bestSplit :" + bestSplit);
				root2.setImpurity(minGini);
				root2.setThresold(bestSplit);
				root2.setAttributeName(minimumGiniAttribute);
				List<DataRecord> dataRecordsLeft = new ArrayList<DataRecord>();
				List<DataRecord> dataRecordsRight = new ArrayList<DataRecord>();
				splitDataRecord( dataRecords , dataRecordsLeft , dataRecordsRight ,minimumGiniAttribute , bestSplit );
				
				
				Integer leafNodes = this.getTotalNumberOfLeafNodes(root);
				Integer noOfInternalNodes = getTotalNumberOfInternalNodes(root);
				Integer unclassifiedRecordCount = this.getTotalUnclassifiedRecordCount();
				Integer totalNumberOfAttributes = DataRecord.attributeNames.size();
				
				
				Integer accuracyPrev = this.test(inputData.getTraindDataRecords());
				
				
				
				Double MDCvaluePrev = noOfInternalNodes* Math.log(totalNumberOfAttributes)/Math.log(2) + (leafNodes)*Math.log(totalNumberOfAttributes)/Math.log(2) +
									unclassifiedRecordCount*Math.log(totalTrainingRecordSize)/Math.log(2);
				
//				System.out.println(unclassifiedRecordCount + getUnclassifiedCount(dataRecordsLeft) +  getUnclassifiedCount(dataRecordsRight) - root2.getNumberOfUnclassifiedRecords());
				
				Double MDCvalue = (noOfInternalNodes +1)* Math.log(totalNumberOfAttributes)/Math.log(2) + (leafNodes+1)*Math.log(totalNumberOfAttributes)/Math.log(2) +
				(unclassifiedRecordCount + getUnclassifiedCount(dataRecordsLeft) +  getUnclassifiedCount(dataRecordsRight) - root2.getNumberOfUnclassifiedRecords())*Math.log(totalTrainingRecordSize)/Math.log(2);

				
//				if(MDCvalue > MDCvaluePrev){
//					return;
//				}
				
//				System.out.println(unclassifiedRecordCount + getUnclassifiedCount(dataRecordsLeft) +  getUnclassifiedCount(dataRecordsRight) - root2.getNumberOfUnclassifiedRecords());
//				double pessmisticError = (unclassifiedRecordCount + getUnclassifiedCount(dataRecordsLeft) +  getUnclassifiedCount(dataRecordsRight) - root2.getNumberOfUnclassifiedRecords() + 0.5*(leafNodes + 1))/totalTrainingRecordSize;
//				double pessmisticErrorPrev = (unclassifiedRecordCount + (0.5 * leafNodes) )/totalTrainingRecordSize;
//				if(pessmisticError > pessmisticErrorPrev)
//					continue;
				
				
//				System.out.println("pessmisticError :" +pessmisticError);
//				System.out.println("pessmisticErrorPrev : " +pessmisticErrorPrev);
//				System.out.println("leafNodes :" + leafNodes);
//				System.out.println("unclassifiedRecordCount :" + unclassifiedRecordCount);
				
				
				
				
	//			System.out.println("dataRecordsLeft :\n\n" + dataRecordsLeft.size());
	//			System.out.println("dataRecordsRight :\n\n" + dataRecordsRight.size());
				root2.setLeft( new DecisionNode());
				root2.getLeft().setDataRecords(dataRecordsLeft);
//				System.out.println(root2.getLeft().getNumberOfUnclassifiedRecords());
				root2.setRight(new DecisionNode());
				root2.getRight().setDataRecords(dataRecordsRight);
				Integer accuracy = this.test(inputData.getTraindDataRecords());
				if(accuracyPrev > accuracy){
					break;
				}
//				System.out.println(root2.getRight().getNumberOfUnclassifiedRecords());
				decisionNodes.add(root2.getLeft());
				decisionNodes.add(root2.getRight());
//				constructTreeRecursively(dataRecordsLeft , root2.getLeft());
//				constructTreeRecursively(dataRecordsRight , root2.getRight());
			}
		}
			
			
		
	}

	private Integer getUnclassifiedCount(List<DataRecord> dataRecords){
		
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
		return dataRecords.size() - max_count;

	}
	private Integer getTotalUnclassifiedRecordCount() {
		// TODO Auto-generated method stub
		DecisionNode temp = root;
		Integer unclassifiedRecords = getTotalUnclassifiedRecordCountRec(temp);
		return unclassifiedRecords;
		
		
	}
	
	private Integer getTotalNumberOfLeafNodes(DecisionNode temp) {
		
		if(temp.getLeft() == null & temp.getRight() == null){
			return 1;
		}else{
				return getTotalNumberOfLeafNodes(temp.getLeft()) + getTotalNumberOfLeafNodes(temp.getRight()); 
		}
	}
	private Integer getTotalNumberOfInternalNodes(DecisionNode temp) {
		
		if(temp.getLeft() == null & temp.getRight() == null){
			return 0;
		}else{
				return getTotalNumberOfLeafNodes(temp.getLeft()) + getTotalNumberOfLeafNodes(temp.getRight()) + 1; 
		}
	}
	
	private Integer getTotalUnclassifiedRecordCountRec(DecisionNode temp) {
		// TODO Auto-generated method stub
		if(temp.getLeft() == null & temp.getRight() == null){
			return temp.getNumberOfUnclassifiedRecords();
		}else{
				return getTotalUnclassifiedRecordCountRec(temp.getLeft()) + getTotalUnclassifiedRecordCountRec(temp.getRight()); 
		}
	}

	private void splitDataRecord(List<DataRecord> dataRecords,	List<DataRecord> dataRecordsLeft, 
			List<DataRecord> dataRecordsRight, String minimumGiniAttribute, Double bestSplit) {
		
		int indexOfAttribute = DataRecord.attributeNames.indexOf(minimumGiniAttribute);
		for( int i=0; i < dataRecords.size(); i++ ){
		try{
			if(dataRecords.get(i).getAttibuteValues().get(indexOfAttribute) <= bestSplit){
				dataRecordsLeft.add(dataRecords.get(i));
			}else
				dataRecordsRight.add(dataRecords.get(i));
		}catch(Exception e){
			System.out.println();
		}
		}
	}

	private String getTheBestAttributeForSpilliting(List<DataRecord> dataRecords) {
		// TODO Auto-generated method stub
		
		double minimumGini = 100; String minimumGiniAttribute = ""; double bestSplit = 0;
		
		for( int i=0; i< DataRecord.attributeNames.size(); i++){
			ArrayList<DataKeyValue> dataListMap = new ArrayList<DataKeyValue>();
			for( int j =0; j < dataRecords.size(); j++ ){
				dataListMap.add(new DataKeyValue(dataRecords.get(j).getClassLable() , dataRecords.get(j).getAttibuteValues().get(i)));
			}
			String bestSplit_bestGini = getBestSplitGiniValueThresold(dataListMap);
			String[] splitGiniStr =  bestSplit_bestGini.split("_");
			String giniStr = splitGiniStr[1];
			
			if(minimumGini > Double.parseDouble(giniStr)){
				minimumGini = Double.parseDouble(giniStr);
				bestSplit = Double.parseDouble(splitGiniStr[0]);
				minimumGiniAttribute = DataRecord.attributeNames.get(i); 
//				System.out.println( "minimumGini :" + minimumGini + " bestSplit :" +bestSplit + " minimumGiniAttribute : " + minimumGiniAttribute );
			}
		}
		return minimumGini+ "_" + minimumGiniAttribute + "_" + bestSplit;
		
	}

	private String getBestSplitGiniValueThresold(ArrayList<DataKeyValue> dataListMap) {
		
		
		Collections.sort(dataListMap , new DataKeyValueComparator());
		Double range = dataListMap.get(dataListMap.size() - 1).value - dataListMap.get(0).value;
		double bestSplit = dataListMap.get(dataListMap.size()/2 - 1).value ;
		double bestGini =100;
		int numberOfSplits =0;
		if(dataListMap.size() > 100 ){
			numberOfSplits = dataListMap.size()/5;
		}else{
			numberOfSplits = 20;
		}
		Double splitBandWidth = range / numberOfSplits;
		double dataSplitThresold = dataListMap.get(dataListMap.size() - 1).value , minimumGini =1;
		for( int i = 1; i < numberOfSplits+1; i++ ){
			 double splitAtData = dataListMap.get(0).value + i*splitBandWidth;
			 Map<String, Integer> classVariableCountSet1 = new HashMap<String, Integer>();
			 Map<String, Integer> classVariableCountSet2 = new HashMap<String, Integer>();
			 int countOfSet1 =0, countOfSet2 =0;
			 for( int j2 = 0; j2< dataListMap.size(); j2++){
				 if(dataListMap.get(j2).value <= splitAtData){
					 putValueIntoMapWithIncrement(classVariableCountSet1, dataListMap.get(j2).key );
					 countOfSet1++;
				 }else{
					 putValueIntoMapWithIncrement(classVariableCountSet2, dataListMap.get(j2).key );
					 countOfSet2++;
				 }
			 } 
			 double giniSet1;
			 double giniSet2;
			 // Gini Index Of set
			 if(FlagFilterGiniEntropy == 0){
				  giniSet1 = caluclateGiniIndexSet( countOfSet1 , classVariableCountSet1);
				  giniSet2 = caluclateGiniIndexSet( countOfSet2 , classVariableCountSet2);
			 }else{
				 giniSet1 = caluclateEntropyIndexSet( countOfSet1 , classVariableCountSet1);
				 giniSet2 = caluclateEntropyIndexSet( countOfSet2 , classVariableCountSet2);
			 }
			 double comulativeGini = (countOfSet1*giniSet1 + countOfSet2*giniSet2)/(countOfSet1 + countOfSet2);
			 if(bestGini > comulativeGini){
				 bestGini = comulativeGini;
				 bestSplit = splitAtData;
			 }
			 
		}
		return bestSplit + "_" +bestGini;
	}

	private double caluclateGiniIndexSet(int countOfSet,Map<String, Integer> classVariableCountSet) {
		// TODO Auto-generated method stub
		if( countOfSet <= 0){
			return 0;
		}else{
			double gini = 1;
			for(String s : classVariableCountSet.keySet()){
				double countOfClassLable = classVariableCountSet.get(s) + 0.0;
				gini -= Math.pow(countOfClassLable/countOfSet , 2);
			}
			return gini;
			
		}
		
	}
	private double caluclateEntropyIndexSet(int countOfSet,Map<String, Integer> classVariableCountSet) {
		// TODO Auto-generated method stub
		if( countOfSet <= 0){
			return 0;
		}else{
			double entropy = 0;
			for(String s : classVariableCountSet.keySet()){
				double countOfClassLable = classVariableCountSet.get(s) + 0.0;
				try{
					entropy -= countOfClassLable/countOfSet* Math.log(countOfClassLable/countOfSet);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			return entropy;
			
		}
		
	}

	public void putValueIntoMapWithIncrement( Map<String, Integer> classVariableCountSet ,String key){
		if(classVariableCountSet.containsKey(key)){
			 classVariableCountSet.put(key , classVariableCountSet.get(key) +1);
		}else{
			classVariableCountSet.put(key , 1);
		}
	}
	// have to one more condition to check if the data set is same or different.
	public Boolean stopping_build_tree( List<DataRecord> dataRecords){
		
//		System.out.println( "\n In stopping_build_tree funtion \n");
//		printDataRecords( dataRecords);
		//checking if the sub having same class name
		Boolean sameClassName = true;
		for( int i =0; i< dataRecords.size()&& sameClassName != false; i++){
			for( int j =i+1; j < dataRecords.size() && sameClassName != false; j++){
				if(!dataRecords.get(i).getClassLable().equals(dataRecords.get(j).getClassLable())){
					sameClassName = false;
				}
			}
		}
		if(sameClassName == true){
			return true;
		}
		
		return false;
		
	}

	private void printDataRecords(List<DataRecord> dataRecords) {
		// TODO Auto-generated method stub
		for( int i=0; i<dataRecords.size(); i++){
			System.out.print( "ClassLable : " +dataRecords.get(i).getClassLable() + " ");
			for( int j2 =0; j2< DataRecord.attributeNames.size(); j2++){
				System.out.print(" " + DataRecord.attributeNames.get(j2) + " :" + dataRecords.get(i).getAttibuteValues().get(j2));
			}
			System.out.println();
		}	
	}
}
