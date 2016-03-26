import java.io.*;
import java.util.*;

import com.tree.core.DataRecord;


public class InputData {


	List<DataRecord> testDataRecords;
	List<DataRecord> traindDataRecords;
	List<DataRecord> dataRecords;
	ArrayList<Integer> randomList;
	
	public static void main(String[] args) {
		InputData inputdata = new InputData();
		inputdata.LoadData("./InputData/iris.data.txt");
		List<DataRecord> dataRecords = inputdata.getDataRecords();
//		inputdata.printDataRecords(dataRecords);
		inputdata.generatedRandomValues();
		inputdata.placeTestTrainFoldData(1);
		/*for( int i =0; i<DataRecord.attributeNames.size(); i++)
			System.out.print(DataRecord.attributeNames.get(i)+ ",");
		System.out.println();
		
		for( int i = 0; i<dataRecords.size(); i++){
			
			for(Double s: dataRecords.get(i).getAttibuteValues()){
				System.out.print(s+",");
			}
			System.out.println(dataRecords.get(i).getClassLable());
		}*/
		
	}
	
/*	private void printDataRecords(List<DataRecord> dataRecords) {
		// TODO Auto-generated method stub
		for( int i=0; i<dataRecords.size(); i++){
			System.out.print( "ClassLable : " +dataRecords.get(i).getClassLable() + " ");
			for( int j2 =0; j2< DataRecord.attributeNames.size(); j2++){
				System.out.print(" " + DataRecord.attributeNames.get(j2) + " :" + dataRecords.get(i).getAttibuteValues().get(j2));
			}
			System.out.println();
		}	
	}*/
	public void generatedRandomValues(){
		randomList = new ArrayList<Integer>();
		Random rand = new Random();
		while(randomList.size() < dataRecords.size()){
			int value = rand.nextInt(dataRecords.size());
			if(!randomList.contains(value)){
				randomList.add(value);
			}
		}//System.out.println(randomList);
		TreeSet<Integer> treeset = new TreeSet<Integer>();
	}
	public void placeTestTrainFoldData(int i){
		
		this.generatedRandomValues();
		int randomSplit = (dataRecords.size() +1)/10;
		int testDataStart = randomSplit*(i-1);
		int testDataEnd = randomSplit*i;
		testDataRecords = new ArrayList<DataRecord>();
		traindDataRecords = new ArrayList<DataRecord>();
		for( int k =0; k< randomList.size(); k++){
			
			if( k >= testDataStart && k <testDataEnd  )
				testDataRecords.add(dataRecords.get(randomList.get(k)));
			else
				traindDataRecords.add(dataRecords.get(randomList.get(k)));
		}
		
//		System.out.println("---------------------testDataRecords---------------------------------");
//		printDataRecords(testDataRecords);
//		System.out.println("---------------------traindDataRecords---------------------------------");
//		printDataRecords(traindDataRecords);
	}
	public List<DataRecord> getData(String fileName){
		
		List<DataRecord> dataDecords = new ArrayList<DataRecord>();
		String line = null;
		try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            int count = 0;
            while((line = bufferedReader.readLine()) != null) {
            	String[] rawDataRecord = line.split(",");
                if(count == 0){
                	DataRecord.attributeNames = new ArrayList<String>();
                	for(String value : rawDataRecord){
                		DataRecord.attributeNames.add(value.trim());
                	}count++;continue;
                }
                DataRecord dataRecord = new DataRecord();
                int classIndex = DataRecord.attributeNames.indexOf("class");
                
                for( int i = 0; i<rawDataRecord.length; i++){
                	if( i == classIndex){
                		dataRecord.setClassLable(rawDataRecord[i]);
                	}else
                		dataRecord.getAttibuteValues().add(Double.parseDouble(rawDataRecord[i].trim()));
                }
                dataDecords.add(dataRecord);
            }
            DataRecord.attributeNames.remove("class");
            
            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        
		
		
        }
		return dataDecords;
		
}
	
	public List<DataRecord> getTestDataRecords() {
		return testDataRecords;
	}

	public void setTestDataRecords(List<DataRecord> testDataRecords) {
		this.testDataRecords = testDataRecords;
	}

	public List<DataRecord> getTraindDataRecords() {
		return traindDataRecords;
	}

	public void setTraindDataRecords(List<DataRecord> traindDataRecords) {
		this.traindDataRecords = traindDataRecords;
	}

	public List<DataRecord> getDataRecords() {
		return dataRecords;
	}

	public void setDataRecords(List<DataRecord> dataRecords) {
		this.dataRecords = dataRecords;
	}

	public void LoadData(String fileName) {
		// TODO Auto-generated method stub
		dataRecords = getData(fileName);
		
	}
}
