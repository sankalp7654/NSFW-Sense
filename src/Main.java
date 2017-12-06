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
	     * 
	     * ABOUT THE API RESPONSE
	     * The API returns two tags SFW (SAFE FOR VIEWING) and NSFW (NOT SAFE FOR VIEWING) after processing the image sent by our application
	     * The first "name" tag which is returned depends on the nudity of the image 
	     * if the image seems to be nude then first tag will be NSFW else it will be SFW
             * 
	     */
		
	     /*
	     * THINGS TO BE DONE
	     * (See the JSON file for more clarification)
	     * 1 Store the "name" key present under the "concepts" object. 
             *   This "name" will contain either SFW or NSFW
             * 2 Store the "value" key present under the "concept" object.
   	     *   This "value" will contain the predicted value for "corresponding" name tag. 
	     */
		
	    final List<ClarifaiOutput<Concept>> predictionResults =
			    client.getDefaultModels().nsfwModel() 
			        .predict()
			        .withInputs(
			            ClarifaiInput.forImage("https://developer.clarifai.com/static/images/model-samples/nsfw-003.jpg"))
			        .executeSync()
			        .get();
	    
	    /*
	     * The returned List of Objects from the Clarifai API will look something like this 
	     * [{"id":"f03865711f1b485ba6b8c600b98dae66","createdAt":"Dec 6, 2017 11:53:09 AM","model":{"id":"e9576d86d2004ed1a38ba0cf39ecb4b1","name":"nsfw-v1.0","app_id":"main","created_at":"Sep 18, 2016 3:48:59 AM","model_version":{}},"input":{"id":"feec7001e1fa4682968b9129f6c30a19","data":{"concepts":[],"metadata":{},"image":{"url":"https://samples.clarifai.com/nsfw.jpg"}}},"data":[{"id":"ai_RT20lm2Q","name":"sfw","appID":"main","value":0.90312743},{"id":"ai_KxzHKtPl","name":"nsfw","appID":"main","value":0.096872576}],"status":{}}]
	     */
	    
	    Gson gson = new Gson();
	    String json = gson.toJson(predictionResults); // Converts List of Concepts to JSON output 
   //       System.out.println(json);   Uncommenting this will print the entire response sent by the Clarifai API

     	    int getName = json.indexOf("concepts"); // Returns the position of first character i.e. 'c' of the object "concepts" to getName  
    
       	    /*
       	     * Now we need to find the position of "name" tag present under the object "concepts"
     	     */
     	
   	    getName = json.indexOf("name", getName); // Returns the position of the first character i.e. 'n' present inside the object "concepts" to getName      	
      	
     	    getName = getName + 7; // Inorder to skip   ame":"sfw"  (Returns the position of the first character present under "name" tag i.e. SFW or NSFW, In this case it returns the position of character 's') 
      	    while(json.charAt(getName) != '"')
      	    {
            	System.out.print(json.charAt(getName));
          	getName++;
      	    }
     	    System.out.print(" : ");
   	
          
            int getValue = json.indexOf("value"); // Getting the value sent by the API corresponding to the "name" tag under the object "concepts"
	   /* 
	    * getValue will contain the index of 'v'
	    * we will add 7 to the variable to skip the string    alue":0.90312743
	    * so the getValue will point to first digit present in the string value i.e here 0
	    */
	    getValue = getValue + 7;   
	    
	    while((json.charAt(getValue)) != '}') // Iterate till we get '}' as the end-point lies here for the value of SFW
	    {
	 	   System.out.print(json.charAt(getValue));     
	 	   getValue ++;
	    }
   	    
      }
}















