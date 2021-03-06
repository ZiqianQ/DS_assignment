package Server;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Error {
	
	public JSONObject unknownCmd(){		
		JSONObject message = new JSONObject();
		message.put("response", "error");
		message.put("errorMessage", "invalid command");	
		return message;
	}
	
	public JSONObject missingCmd(){
		JSONObject message = new JSONObject();
		message.put("response", "error");
		message.put("errorMessage", "missing or incorrect type for command");	
		return message;
	}
	
	public JSONObject missingRes(){
		JSONObject message = new JSONObject();
		message.put("response", "error");
		message.put("errorMessage", "missing resource");	
		return message;	
	}
	
	public JSONObject invalidRes(){
		JSONObject message = new JSONObject();
		message.put("response", "error");
		message.put("errorMessage", "invalid resource");	
		return message;
		
	}
	
	public JSONObject missingResT(){
		JSONObject message = new JSONObject();
		message.put("response", "error");
		message.put("errorMessage", "missing resourceTemplate");	
		return message;	
	}
	
	public JSONObject invalidResT(){
		JSONObject message = new JSONObject();
		message.put("response", "error");
		message.put("errorMessage", "invalid resourceTemplate");	
		return message;
		
	}
	
	public JSONObject incorrectSecret(){
		JSONObject message = new JSONObject();
		message.put("response", "error");
		message.put("errorMessage", "incorrect secret");	
		return message;
	}
	
	public JSONObject noSecret(){
		JSONObject message = new JSONObject();
		message.put("response", "error");
		message.put("errorMessage","missing resource and/or secret");	
		return message;
	}

	public JSONObject interval() {
		JSONObject message = new JSONObject();
		message.put("response", "error");
		message.put("errorMessage","try again after interval");	
		return message;
	}
	

}
