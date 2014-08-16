package com.suebosoft.work.helloworld2.json;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class FBPictureJson extends GenericJson {

	@Key
	private FBPictureDataJson data;

	public FBPictureDataJson getData() {
		return data;
	}

	public void setData(FBPictureDataJson data) {
		this.data = data;
	}

}

