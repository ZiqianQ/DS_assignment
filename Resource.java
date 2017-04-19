package Client;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Resource {
	private JSONObject resource;
	private String name;
	private String description;
	private JSONArray tags;
	private String uri;
	private String channel;
	private String owner;
	private String ezserver;
	
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


	public void setDescription(String description) {
		this.description = description;
	}


	public void setTags(String tags) {
		this.tags.add(tags);
	}


	public void setUri(String uri) {
		this.uri = uri;
	}


	public void setOwner(String owner) {
		this.owner = owner;
	}


	public void setEzserver(String ezserver) {
		this.ezserver = ezserver;
	}


	public void setChannel(String channel){
		this.channel = channel;
	}

}
