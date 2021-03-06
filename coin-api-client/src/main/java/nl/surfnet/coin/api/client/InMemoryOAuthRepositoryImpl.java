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

package nl.surfnet.coin.api.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import nl.surfnet.coin.api.client.internal.OAuthToken;

import org.scribe.model.Token;
import org.springframework.util.Assert;

/**
 * InMemory Repository for Tokens
 * 
 */
public class InMemoryOAuthRepositoryImpl implements OAuthRepository {

  private Map<String, OAuthToken> tokens = new ConcurrentHashMap<String, OAuthToken>();

  @Override
  public OAuthToken getToken(String userId) {
    return tokens.get(userId);
  }

  @Override
  public void storeToken(Token accessToken, String userId, OAuthVersion version) {
    tokens.put(userId, new OAuthToken(accessToken, version));
  }

  @Override
  public void removeToken(String onBehalfOf) {
    Assert.notNull(onBehalfOf, "Token to be removed cannot have key null");
    tokens.remove(onBehalfOf);
  }

}
