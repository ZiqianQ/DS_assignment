package Client;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class ClientCommands {
	
	public JSONObject query(){
		JSONObject newCommand = new JSONObject();
		newCommand.put("command", "QUERY");
		newCommand.put("relay", false);
		JSONObject resourceTemplate = new JSONObject();
		resourceTemplate.put("name", "");
		JSONArray tags = new JSONArray();
		Object put = resourceTemplate.put("tags",tags);
		resourceTemplate.put("description", "");
		resourceTemplate.put("uri", "");
		resourceTemplate.put("channel", "");
		resourceTemplate.put("owner", "");
		resourceTemplate.put("ezserver", null);
		newCommand.put("resourceTemplate",resourceTemplate);
		
		return newCommand;
		//System.out.println(newCommand.toJSONString());
	
	}
	
	public JSONObject fetch(){
		JSONObject newCommand = new JSONObject();
		newCommand.put("command", "FETCH");

		JSONObject resourceTemplate = new JSONObject();
		resourceTemplate.put("name", "bilibili");
		JSONArray tags = new JSONArray();
		Object put = resourceTemplate.put("tags",tags);
		resourceTemplate.put("description", "");
		resourceTemplate.put("uri", "http:\\/\\/www.bilibili.com");
		resourceTemplate.put("channel", "");
		resourceTemplate.put("owner", "");
		resourceTemplate.put("ezserver", "sunrise.cis.unimelb.edu.au:3780");
		newCommand.put("resourceTemplate",resourceTemplate);
		return newCommand;
	}
	
		
	
	

}
