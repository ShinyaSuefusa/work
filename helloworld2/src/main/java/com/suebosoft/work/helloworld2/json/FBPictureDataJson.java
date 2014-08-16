package com.suebosoft.work.helloworld2.json;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class FBPictureDataJson extends GenericJson {

	@Key
	private Boolean is_silhouette;
	
	@Key
	private String url;

	public Boolean isSilhouette() {
		return is_silhouette;
	}

	public void setSilhouette(Boolean is_silhouette) {
		this.is_silhouette = is_silhouette;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
