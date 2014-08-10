package com.suebosoft.work.helloworld2.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.suebosoft.work.helloworld2.model.Slim3Model;

public class Slim3ServiceTest extends AppEngineTestCase {

  private static Slim3V1Service service = new Slim3V1Service();

  @Test
  public void slim3test1() {
    int beforeCount = tester.count(Slim3Model.class);

    service.newAndPut("abc");

    assertThat(tester.count(Slim3Model.class), is(equalTo(beforeCount + 1)));
  }

  LocalServiceTestHelper helper;

  @Override
  public void setUp() throws Exception {
    LocalDatastoreServiceTestConfig dsConfig = new LocalDatastoreServiceTestConfig();
    dsConfig.setNoStorage(true);
    dsConfig.setNoIndexAutoGen(true);
    helper = new LocalServiceTestHelper(dsConfig);
    helper.setUp();
    super.setUp();
  }

  @Override
  public void tearDown() throws Exception {
    super.tearDown();
    helper.tearDown();
  }
}
