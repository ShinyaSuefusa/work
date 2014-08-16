package com.suebosoft.work.helloworld2.service;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import org.slim3.datastore.Datastore;

import com.google.api.client.json.JsonParser;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.AuthLevel;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.suebosoft.work.helloworld2.json.FBMeJson;
import com.suebosoft.work.helloworld2.model.FBUserModel;

@Api(
		name = "facebookService",
		version = "v1",
		description = "facebook service."
		)
public class FacebookV1Service {

	private static final Logger log = Logger.getLogger(FacebookV1Service.class.getName());
	
	private static final String CLIENT_ID = "1449262175351695";
	private static final String CLIENT_SECRET = "3c740fcb64fc7211aad066a78a0c93fb";
	private static final String REDIRECT_URI = "http://localhost/";

	@ApiMethod(
			name = "login",
			httpMethod = HttpMethod.GET,
			authLevel = AuthLevel.NONE
			)
	public FBUserModel login(@Named("code") String code) throws IOException {
		log.info("code=" + code);
		
		String token = getToken(code);

		log.info("token=" + token);

		FBMeJson me = getMe(token);

		Transaction tx = Datastore.beginTransaction();
		
		// モデルを作成する
		FBUserModel model = new FBUserModel();
		model.setId(me.getId());
		model.setName(me.getName());
		if (me.getPicture()!=null && me.getPicture().getData()!=null) {
			model.setPicture(me.getPicture().getData().getUrl());
		}

		Key key = Datastore.put(model);
		
		tx.commit();
		
		model.setKey(key);
		log.info("key=" + key);

		return model;
	}
	
	private static String getToken(String code) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("https://graph.facebook.com/oauth/access_token?");
		sb.append("client_id=").append(CLIENT_ID);
		sb.append("&");
		sb.append("client_secret=").append(CLIENT_SECRET);
		sb.append("&");
		sb.append("code=").append(code);
		sb.append("&");
		sb.append("redirect_uri=").append(REDIRECT_URI);
		
		HTTPResponse response = getHttpResponse(sb.toString());
		int statusCode = response.getResponseCode();
		if (statusCode != 200) {
			throw new IOException("returned error status=" + statusCode + ", content=" + new String(response.getContent()));
		}
		//HTTPエンティティからコンテント（中身）を生成
		String content = new String(response.getContent());
		if (!content.startsWith("access_token=")) {
			throw new IOException("returned error. " + content);
		}
		String[] params = content.split("&");
		String[] kv = params[0].split("=");
		return kv[1];
	}

	private static FBMeJson getMe(String token) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("https://graph.facebook.com/v2.1/me?");
		sb.append("fields=").append("id,name,picture");
		sb.append("&");
		sb.append("access_token=").append(token);
		
		HTTPResponse response = getHttpResponse(sb.toString());
		int statusCode = response.getResponseCode();
		if (statusCode != 200) {
			throw new IOException("returned error status=" + statusCode);
		}
		// レスポンスからJSONオブジェクトを実体化する
		JacksonFactory jf = new JacksonFactory();
		JsonParser jp = jf.createJsonParser(new String(response.getContent()));
		FBMeJson me = jp.parse(FBMeJson.class);
		
		return me;
	}
	
	private static final HTTPResponse getHttpResponse(String url) throws IOException {
		URLFetchService ufs = URLFetchServiceFactory.getURLFetchService();
		HTTPResponse response = ufs.fetch(new URL(url));
		
		return response;

	}
}
