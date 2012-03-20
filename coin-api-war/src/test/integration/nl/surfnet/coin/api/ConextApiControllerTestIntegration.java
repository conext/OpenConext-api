/*
 * Copyright 2012 SURFnet bv, The Netherlands
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.surfnet.coin.api;

import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Token;

import nl.surfnet.coin.api.client.InMemoryOAuthRepositoryImpl;
import nl.surfnet.coin.api.client.OAuthEnvironment;
import nl.surfnet.coin.api.client.OAuthRepository;
import nl.surfnet.coin.api.client.OAuthVersion;
import nl.surfnet.coin.api.client.OpenConextOAuthClientImpl;
import nl.surfnet.coin.api.client.domain.Person;

import static org.junit.Assert.assertEquals;

/**
 * Test Person related queries with selenium
 */
public class ConextApiControllerTestIntegration extends IntegrationSupport {

  private final String OAUTH_KEY = "https://testsp.test.surfconext.nl/shibboleth";
  private final String OAUTH_SECRET = "mysecret";

  // private final String OAUTH_CALLBACK_URL = "http://localhost:8083/";

  // private final String SURFCONEXT_BASE_URL = "http://localhost:8095/";

  private final String USER_ID = "foo";

  private OpenConextOAuthClientImpl client;

  @Before
  public void initialize() throws Exception {
    OAuthEnvironment environment = new OAuthEnvironment();
    environment.setEndpointBaseUrl(URL_UNDER_TEST);
    environment.setOauthKey(OAUTH_KEY);
    environment.setOauthSecret(OAUTH_SECRET);
    environment.setCallbackUrl("http://not-used/");
    OAuthRepository repository = new InMemoryOAuthRepositoryImpl();
    repository.storeToken(new Token(OAUTH_KEY, OAUTH_SECRET), USER_ID,
        OAuthVersion.v10a);
    client = new OpenConextOAuthClientImpl(environment, repository);
  }

  @Test
  public void completeFlow() throws Exception {
    Person person = client.getPerson(USER_ID, null);
    assertEquals("mFoo", person.getAccounts().iterator().next().getUserId());
  }

}