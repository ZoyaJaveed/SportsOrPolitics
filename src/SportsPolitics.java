import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.Set;
import java.io.IOException; 




class DataSet{
	
	public String[] append(int n, String[] arr, String x) {
		int i; 		 
        String newarr[] = new String[n + 1];   
        for (i = 0; i < n; i++) 
            newarr[i] = arr[i];   
        newarr[n] = x; 
  
        return newarr; 
	}
	
	public 	ArrayList<ArrayList<String>> appendExtractedWords(String trainingID, String trainingLabel, ArrayList<String> extractedWordsList) {
		//Appending extracted words with the ID and Label
		
		ArrayList<String> singleListTrainingID = new ArrayList<String>();
		singleListTrainingID.add(trainingID);
		ArrayList<String> singleListTrainingLabel = new ArrayList<String>();
		singleListTrainingLabel.add(trainingLabel);
		ArrayList<ArrayList<String>> listOLists = new ArrayList<ArrayList<String>>();
		listOLists.add(singleListTrainingID);
		listOLists.add(singleListTrainingLabel);
		listOLists.add(extractedWordsList);

		return listOLists;
	}
	
	public String[] extractWords(String[] trainingText) {
		//Removing everything except numbers and letters
		String[] words= {};

		String lowerAlpha = "abcdefghijklmnopqrstuvwxyz";
		String upperAlpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String numbers = "01234567890";
		
		for(String word:trainingText) {
			String currentWord = "";
			for(char c:word.toCharArray()) {
				String cToString = Character.toString(c);
				if((!lowerAlpha.contains(cToString)) && (!upperAlpha.contains(cToString)) && (!numbers.contains(cToString))) {
					if(currentWord.length()>=2) words=append(words.length,words,currentWord.toLowerCase());
					
					currentWord ="";
					continue;
				}
			currentWord+=c;
			}
		//Adding remaining words from currentWord to Words.
			if(currentWord.length()>=2) words=append(words.length,words,currentWord.toLowerCase());
				
		}
	
		return words; 
	}
	
	public ArrayList<ArrayList<ArrayList<String>>> getTrainingData() throws FileNotFoundException {
		ArrayList<ArrayList<String>> trainingData = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<ArrayList<String>>> returntrainingData = new ArrayList<ArrayList<ArrayList<String>>>();
		
		String[] trainingDetails = {};
		File myObj = new File("src//training.txt");
	    Scanner myReader = new Scanner(myObj);
	    while (myReader.hasNextLine()) {
	        String data = myReader.nextLine();
	        data.strip();
	        trainingDetails = data.split(" ");
	        String trainingID = trainingDetails[0];
	        String trainingLabel = trainingDetails[1];
	        String[] trainingText = Arrays.copyOfRange(trainingDetails, 2,trainingDetails.length);
	        String[] extractedWords = extractWords(trainingText);
	        ArrayList<String> extractedWordsList = new ArrayList<String>(Arrays.asList(extractedWords));
	        trainingData = appendExtractedWords(trainingID,trainingLabel,extractedWordsList);
	        returntrainingData.add(trainingData)  ;     
	      		        
	      }
	    myReader.close();	

	  return returntrainingData;
	}
	public ArrayList<ArrayList<ArrayList<String>>> getTestingData() throws FileNotFoundException{
		ArrayList<ArrayList<String>> testingData = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<ArrayList<String>>> returntestingData = new ArrayList<ArrayList<ArrayList<String>>>();
		
		String[] testingDetails = {};
		File myObj = new File("src//test.txt");
	    Scanner myReader = new Scanner(myObj);
	    while (myReader.hasNextLine()) {
	        String data = myReader.nextLine();
	        data.strip();
	        testingDetails = data.split(" ");
	        String testingID = testingDetails[0];
	        String[] testingText = Arrays.copyOfRange(testingDetails, 1,testingDetails.length);
	        String[] extractedWords = extractWords(testingText);
	        ArrayList<String> extractedWordsList = new ArrayList<String>(Arrays.asList(extractedWords));
	        testingData = appendExtractedWords(testingID, "",extractedWordsList);
	         returntestingData.add(testingData);        
	      		        
	      }	 
	    myReader.close(); 
	  return returntestingData;
	}
}

class WordCount{
	
	public ArrayList<String> getWordList(ArrayList<ArrayList<ArrayList<String>>> trainingData) {
		ArrayList<String> eachWordList = new ArrayList<String>();
		ArrayList<String> wordList = new ArrayList<String>();
		
		for(int i=0;i<trainingData.size();i++) {
			eachWordList = trainingData.get(i).get(2);
			wordList.addAll(eachWordList);
		}
		return wordList;
		
	}
	
	public HashMap<String,Double> getWordPrabability(ArrayList<ArrayList<ArrayList<String>>> trainingData,String label) {
		ArrayList<String> wordList = new ArrayList<String>();
		HashMap<String,Integer> wordFrequency = new HashMap<String,Integer>();  
		HashMap<String,Double> wordProbability = new HashMap<String,Double>();  
		int totalCount = 0;
		
		//Getting the word list of training Data
		wordList = getWordList(trainingData);	
		
		
		//Assigning frequency for each word as 1 initially.
		for(String word :wordList) {
			wordFrequency.put(word, 1);
		}
		
		//Calculating the count of each word in training data without label, also if label is "Politics" and "Sports" in training data
		for(int i=0;i<trainingData.size();i++) {			
			if((trainingData.get(i).get(1).contains(label))|| (label.isEmpty())) {
				totalCount+=trainingData.get(i).get(2).size();
				//System.out.println(totalCount);
				for(String word :trainingData.get(i).get(2)) {
					wordFrequency.put(word, wordFrequency.get(word) + 1);
				}			
			}			
		}
				
		for(String key:wordFrequency.keySet()) {
			wordProbability.put(key,(wordFrequency.get(key) * 1.0) / totalCount);
		}
	return 	wordProbability;
	}
	
	public float getLabelProbability(ArrayList<ArrayList<ArrayList<String>>> trainingData,String label) {
		float count = 0;
		float totalCount = trainingData.size();
		float labelProbability = 0;
		
		for(int i=0;i<trainingData.size();i++) {
			if(trainingData.get(i).get(1).contains(label)) {
				count+=1;
			}
		}
		
		labelProbability = (float) ((count * 1.0)/totalCount);
		return labelProbability;
	}
	
	
}

class TestData{
	
	
	
	private List<ArrayList<List>> labellingTest(List<ArrayList<List>> labeledDataArg,ArrayList<String> arrayList, String label,
			float probabilitySportsLabel, float probabilityPoliticsLabel) {
		// TODO Auto-generated method stub

		ArrayList<List> eachLabelledTest = new ArrayList<List>();
		ArrayList<Float> probabilitySports = new ArrayList<Float>();
		ArrayList<Float> probabilityPolitics = new ArrayList<Float>();
		ArrayList<String> labelString = new ArrayList<String>();
		
		probabilitySports.add(probabilitySportsLabel);			
		probabilityPolitics.add(probabilityPoliticsLabel);		
		labelString.add(label);
		
		eachLabelledTest.add(arrayList);
		eachLabelledTest.add(labelString);
		eachLabelledTest.add(probabilitySports);
		eachLabelledTest.add(probabilityPolitics);		
		labeledDataArg.add(eachLabelledTest);			
		
		return labeledDataArg;
	}
	
	

	public List<ArrayList<List>>  labellingTestData(ArrayList<ArrayList<ArrayList<String>>> testingData,
			HashMap<String, Double> wordProbabilitySports, HashMap<String, Double> wordProbabilityPolitics,
			float labelProbabilitySports, float labelProbabilityPolitics) {
		// TODO Auto-generated method stub
		List<ArrayList<List>> labeledData = new ArrayList<ArrayList<List>>();
		
		
		for(int i=0;i<testingData.size();i++) {
			float probabilitySportsLabel = labelProbabilitySports;
			float probabilityPoliticsLabel = labelProbabilityPolitics;
			
			for(String word: testingData.get(i).get(2)) {
				if(wordProbabilitySports.containsKey(word)) {
					probabilitySportsLabel*=wordProbabilitySports.get(word);
					probabilityPoliticsLabel*=wordProbabilityPolitics.get(word);
				}else {
					continue;
				}
			}			
			if(probabilitySportsLabel>=probabilityPoliticsLabel) {
				//append label to text sports
				labeledData = labellingTest(labeledData,testingData.get(i).get(0),"Sports",probabilitySportsLabel,probabilityPoliticsLabel);
			}else {
				//append label to text politics
				labeledData = labellingTest(labeledData,testingData.get(i).get(0),"Politics",probabilitySportsLabel,probabilityPoliticsLabel);
			}
		} 
		
		System.out.println(labeledData);
		return labeledData;
		
	}



	public void printlabelledTestData(List<ArrayList<List>> labeledTestData) {
		// TODO Auto-generated method stub
		//Creating a file.
		try {
		      File myObj = new File("src//LabelledTestFile.txt");
		      if (myObj.createNewFile()) {
		        System.out.println("File created: " + myObj.getName());
		      } else {
		        System.out.println("File already exists.");
		      }
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		
		//Writing the labels with the ID to the file.
		try {
		      FileWriter myWriter = new FileWriter("src//LabelledTestFile.txt");
		      for(int i=0;i<labeledTestData.size();i++) {
		    	  myWriter.write(labeledTestData.get(i).get(0).get(0) + " " + labeledTestData.get(i).get(1).get(0)+"\n");		    		    	  
		      }
		      
		      myWriter.close();
		      System.out.println("Successfully wrote to the file.");
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		
		
	}
	
}

public class SportsPolitics {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		ArrayList<ArrayList<ArrayList<String>>> trainingData = new ArrayList<ArrayList<ArrayList<String>>>();
		ArrayList<ArrayList<ArrayList<String>>> testingData = new ArrayList<ArrayList<ArrayList<String>>>();
		HashMap<String,Double> wordProbabilityTotal = new HashMap<String,Double>();
		HashMap<String,Double> wordProbabilitySports = new HashMap<String,Double>();
		HashMap<String,Double> wordProbabilityPolitics = new HashMap<String,Double>();
		List<ArrayList<List>> labeledTestData = new ArrayList<ArrayList<List>>();
		
		
		float labelProbabilitySports = 0,labelProbabilityPolitics = 0;
		
		//Fetching Training and Testing Dataset.
		DataSet dataset = new DataSet();
		trainingData=dataset.getTrainingData();
	    testingData=dataset.getTestingData();
		
		WordCount wordcount = new WordCount();
		wordProbabilityTotal = wordcount.getWordPrabability(trainingData,"");
		wordProbabilitySports = wordcount.getWordPrabability(trainingData,"Sports");
		wordProbabilityPolitics = wordcount.getWordPrabability(trainingData, "Politics");
		
		
		labelProbabilitySports = wordcount.getLabelProbability(trainingData,"Sports");
		labelProbabilityPolitics = wordcount.getLabelProbability(trainingData, "Politics");
				
		Set<Map.Entry<String, Double>> wordProbabilityTotalEntries = wordProbabilityTotal.entrySet();

		for (Map.Entry<String, Double> entry : wordProbabilityTotalEntries) {
	
			wordProbabilitySports.put(entry.getKey(), (wordProbabilitySports.get(entry.getKey())/entry.getValue()));
			wordProbabilityPolitics.put(entry.getKey(), (wordProbabilityPolitics.get(entry.getKey())/entry.getValue()));
			   
		}
		TestData result = new TestData();
		labeledTestData = result.labellingTestData(testingData,wordProbabilitySports,wordProbabilityPolitics,labelProbabilitySports,labelProbabilityPolitics);
		result.printlabelledTestData(labeledTestData);
		
		
	}

	

}
