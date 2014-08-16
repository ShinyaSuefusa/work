package com.suebosoft.work.helloworld2.json;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class FBMeJson extends GenericJson {

	@Key
	private String id;
	
	@Key
	private String name;
	
	@Key
	private FBPictureJson picture;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FBPictureJson getPicture() {
		return picture;
	}

	public void setPicture(FBPictureJson picture) {
		this.picture = picture;
	}

	
}
