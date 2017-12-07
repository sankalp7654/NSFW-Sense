import java.util.List;
import java.io.IOException;
import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import com.google.gson.Gson;

public class Main {

	public static void main(String [ ] args) throws IOException {
	
		/*
		 * Authorizing our Java application with the API key using the client
		 * client will call the API by making the request
		 * ClarifaiClient is provided by the Clarifai for making request in Java  
		 */
	    
		ClarifaiClient client = new ClarifaiBuilder("a2ecf96cfb4544f3b357d845adecdf8d").buildSync();
		
		/*
		 * ABOUT THE API RESPONSE
		 * (See the JSON file for more clarification)
		 * The API returns two tags in "concepts" object i.e. name and value 
		 * The two possible String in "name" tag could be SFW (SAFE FOR VIEWING) or NSFW (NOT SAFE FOR VIEWING) which is decided by the Clarifai API after processing the image sent by our application
		 * The first "name" tag returned under "concepts" depends on the nudity of the image 
		 * if the image seems to be nude then first "name" tag under "concepts" will be NSFW else it will be SFW
		 * 
		 */
		
		/*
		 * THINGS TO BE DONE
		 * (See the JSON file for more clarification)
		 * 1 Store the "name" key present under the "concepts" object. 
		 *   This "name" will contain either SFW or NSFW
		 * 2 Store the "value" key present under the "concepts	" object.
		 *   This "value" will contain the predicted value for "corresponding" name tag. 
		 */
		
	    final List<ClarifaiOutput<Concept>> predictionResults =
			    client.getDefaultModels().nsfwModel() 
			        .predict()
			        .withInputs(
			            ClarifaiInput.forImage("https://developer.clarifai.com/static/images/model-samples/nsfw-006.jpg"))
			        .executeSync()
			        .get();
	    
	    
	    /*
	     * The returned List of Objects from the Clarifai API will look something like this 
	     * [{"id":"f03865711f1b485ba6b8c600b98dae66","createdAt":"Dec 6, 2017 11:53:09 AM","model":{"id":"e9576d86d2004ed1a38ba0cf39ecb4b1","name":"nsfw-v1.0","app_id":"main","created_at":"Sep 18, 2016 3:48:59 AM","model_version":{}},"input":{"id":"feec7001e1fa4682968b9129f6c30a19","data":{"concepts":[],"metadata":{},"image":{"url":"https://samples.clarifai.com/nsfw.jpg"}}},"data":[{"id":"ai_RT20lm2Q","name":"sfw","appID":"main","value":0.90312743},{"id":"ai_KxzHKtPl","name":"nsfw","appID":"main","value":0.096872576}],"status":{}}]
	     */
	    
	    Gson gson = new Gson();
	    String json = gson.toJson(predictionResults); // Converts List of Concepts to JSON output 
   //   System.out.println(json);   Uncommenting this will print the entire response sent by the Clarifai API in JSON format


     	int getPositionOfName = json.indexOf("concepts"); // Returns the position of first character i.e. 'c' of the object "concepts" to getPositionOfName  
     	
     	/*
     	 * Now we need to find the position of "name" tag present under the object "concepts"
     	 */
     	
     	getPositionOfName = json.indexOf("name", getPositionOfName); // Returns the position of the first character i.e. 'n' present inside the object "concepts" to getPositionOfName      	
        getPositionOfName = getPositionOfName + 7; // Inorder to skip   ame":"sfw"  (Returns the position of the first character present under "name" tag i.e. SFW or NSFW, In this case it returns the position of character 's') 
      	
        char [] getName = new char[10]; // Using to store the result i.e either SFW or NSFW character by character 
        int i=0;
        while(json.charAt(getPositionOfName) != '"')
      	{
          	getName[i++] = json.charAt(getPositionOfName);
          	getPositionOfName++;
      	}
        getName[i] = '\0';
        
        String name = new String(getName); // Converting the character array to string to get name  
     	System.out.print(name + " : "); // Displaying the value of name tag 
     	
     
        int getPositionOfValue = json.indexOf("value"); // Getting the value sent by the API corresponding to the "name" tag under the object "concepts"
	   /* 
	    * getPositionOfValue will contain the index of 'v'
	    * we will add 7 to the variable to skip the string    alue":0.90312743
	    * so the getPositionOfValue will point to first digit present in the string value i.e here 0
	    */
	    getPositionOfValue = getPositionOfValue + 7;   
	    
	    char [] getValue = new char[20]; // Stores the value (Probability)
	    int j=0;
	    while((json.charAt(getPositionOfValue)) != '}') // Iterate till we get '}' as the end-point lies here for the value of SFW
	    {
	 	   getValue[j++] = json.charAt(getPositionOfValue);    
	 	   getPositionOfValue ++;
	    }

//	    String valueInString = new String(getValue); // Converting the character array to string to get number
//   	    System.out.println(valueInString); // Displaying the value
//   	    
   	    StringToInteger obj1 = new StringToInteger();
   	    Double value = obj1.parseInt(getValue);
   	    System.out.println(value);
   	    
   	    if((value > 0.15) && (value < 0.75)) {
   	    		int getPositionOfNextName = json.indexOf("name",getPositionOfName);
   	         getPositionOfNextName = getPositionOfNextName + 7;
   	        
   	      char [] getNextName = new char[10]; // Using to store the result i.e either SFW or NSFW character by character 
          int p=0;
          while(json.charAt(getPositionOfNextName) != '"')
        	{
            	getNextName[p++] = json.charAt(getPositionOfNextName);
            	getPositionOfNextName++;
        	}
          getNextName[p] = '\0';
          
          String nextName = new String(getNextName); // Converting the character array to string to get name  
        	System.out.print(nextName + " : "); // Displaying the value of name tag 
        	
        	   int getPositionOfNextValue = json.indexOf("value",getPositionOfValue); // Getting the value sent by the API corresponding to the "name" tag under the object "concepts"
        	   /* 
        	    * getPositionOfValue will contain the index of 'v'
        	    * we will add 7 to the variable to skip the string    alue":0.90312743
        	    * so the getPositionOfValue will point to first digit present in the string value i.e here 0
        	    */
        	    getPositionOfNextValue = getPositionOfNextValue + 7;   
        	    
        	    char [] getNextValue = new char[20]; // Stores the value (Probability)
        	    int q=0;
        	    while((json.charAt(getPositionOfNextValue)) != '}') // Iterate till we get '}' as the end-point lies here for the value of SFW
        	    {
        	 	   getNextValue[q++] = json.charAt(getPositionOfNextValue);    
        	 	   getPositionOfNextValue ++;
        	    }

//        	    String valueInString = new String(getValue); // Converting the character array to string to get number
//           	    System.out.println(valueInString); // Displaying the value
//           	    
           	    StringToInteger obj2 = new StringToInteger();
           	    Double nextValue = obj2.parseInt(getNextValue);
           	    System.out.println(nextValue);
           	    	
       
	}
   }
}
	

