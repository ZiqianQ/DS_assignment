package Client;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.List;

public class Resource {
	private JSONObject resource;
	private String name;
	private String description;
	private JSONArray tags;
	private String uri;
	private String channel;
	private String owner;
	private String ezserver;
	private String secret;
 
	
	//private static ArrayList<Resource> resources = new ArrayList<Resource>();
	
	public Resource(){ //initiate; resourceTemplate
		this.resource = new JSONObject();
		this.name = "";
		this.description = "";
		this.tags = new JSONArray();
		this.uri = "";
		this.channel = "";
		this.owner = "";
		this.ezserver = null;	
		this.secret ="";
		 
	}
	
	public JSONObject render(){
		resource.put("name", name);		
		resource.put("tags",tags);
		resource.put("description", description);
		resource.put("uri", uri);
		resource.put("channel", channel);
		resource.put("owner", owner);
		resource.put("ezserver", ezserver);
		
		return resource;
	}
	


	public void setName(String name) {
		this.name = name;
	}
	 
	public String getName() {
		return name;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public void setTags(String gettags) {
		List<String> getTags = Arrays.asList(gettags.split(","));
		for (int i = 0; i < getTags.size(); i++){
			this.tags.add(getTags.get(i));
		}
	}


	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public String getUri(){
		return uri;
	}
	public String getSecret(){
		return secret;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}


	public void setEzserver(String ezserver) {
		this.ezserver = ezserver;
	}


	public void setChannel(String channel){
		this.channel = channel;
	}

}
