package com.suebosoft.work.helloworld2.service;

import java.util.List;
import java.util.logging.Logger;

import org.slim3.datastore.Datastore;

import com.suebosoft.work.helloworld2.model.Slim3Model;
import com.suebosoft.work.helloworld2.meta.Slim3ModelMeta;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.AuthLevel;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.CollectionResponse.Builder;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

@Api(
	name = "slim3Service",
	version = "v1",
	description = "sample service."
	)
public class Slim3V1Service {

	private static final Logger log = Logger.getLogger(Slim3V1Service.class.getName());
	
	@ApiMethod(
		name = "newAndPut",
		httpMethod = HttpMethod.GET,
		authLevel = AuthLevel.NONE
		)
	public Slim3Model newAndPut(@Named("prop1") String prop1) {
		log.info("prop1=" + prop1);
		
		Transaction tx = Datastore.beginTransaction();
		
		Slim3Model model = new Slim3Model();
		model.setProp1(prop1);
		Key key = Datastore.put(model);
		
		tx.commit();
		
		model.setKey(key);
		log.info("key=" + key);
		return model;
	}

	@ApiMethod(
		name = "queryAll",
		httpMethod = HttpMethod.GET,
		authLevel = AuthLevel.NONE
		)
	public CollectionResponse<Slim3Model> queryAll() {
		List<Slim3Model> list = Datastore.query(Slim3ModelMeta.get()).asList();
		Builder<Slim3Model> builder = CollectionResponse.<Slim3Model> builder();
		builder.setItems(list);
		return builder.build();
	}
}
