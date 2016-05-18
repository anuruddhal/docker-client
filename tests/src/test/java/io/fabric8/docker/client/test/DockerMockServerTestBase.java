/*
 * Copyright (C) 2016 Original Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.fabric8.docker.client.test;

import io.fabric8.docker.client.DockerClient;
import io.fabric8.docker.server.mock.DockerMockServer;
import io.fabric8.mockwebserver.dsl.MockServerExpectation;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;

public class DockerMockServerTestBase {

  private DockerMockServer mock = new DockerMockServer();
  private DockerClient client;

  @Before
  public void setUp() throws Exception {
    mock.init();
    client = mock.createClient();
  }

  @After
  public void tearDown() throws IOException {
    mock.destroy();
    client.close();
  }

  public DockerClient getClient() {
    return client;
  }

  public MockServerExpectation expect() {
    return mock.expect();
  }

  @Deprecated
  public <T> void expectAndReturnAsJson(String path, int code, T body) {
    expect().withPath(path).andReturn(code, body).always();
  }
  @Deprecated
  public void expectAndReturnAsString(String path, int code, String body) {
    expect().withPath(path).andReturn(code, body).always();
  }
  @Deprecated
  public <T> void expectAndReturnAsJson(String method, String path, int code, T body) {
    expect().withPath(path).andReturn(code, body).always();
  }
  @Deprecated
  public void expectAndReturnAsString(String method, String path, int code, String body) {
    expect().withPath(path).andReturn(code, body).always();
  }
}
