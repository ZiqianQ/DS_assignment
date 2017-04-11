package Client;

import java.util.ArrayList;

import org.json.simple.JSONArray;

public class Resource {
	private String name;
	private String description;
	private JSONArray tags;
	private String uri;
	private String channel;
	private String owner;
	private String ezserver;
	
	private static ArrayList<Resource> resources = new ArrayList<Resource>();
	
	public Resource(){ //initiate; resourceTemplate
		this.name = "";
		this.description = "";
		this.tags = new JSONArray();
		this.uri = "";
		this.channel = "";
		this.owner = "";
		this.ezserver = null;	
	}
	
	//public String toString(){
	//	return 
	//}
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public JSONArray getTags() {
		return tags;
	}


	public void setTags(JSONArray tags) {
		this.tags = tags;
	}


	public String getUri() {
		return uri;
	}


	public void setUri(String uri) {
		this.uri = uri;
	}


	public String getOwner() {
		return owner;
	}


	public void setOwner(String owner) {
		this.owner = owner;
	}


	public String getEzserver() {
		return ezserver;
	}


	public void setEzserver(String ezserver) {
		this.ezserver = ezserver;
	}


	public String getChannel() {
		return channel;
	}


	public void setChannel(String channel){
		this.channel = channel;
	}

}
