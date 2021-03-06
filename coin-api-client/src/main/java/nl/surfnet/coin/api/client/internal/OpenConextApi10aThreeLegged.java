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

package nl.surfnet.coin.api.client.internal;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;
import org.scribe.model.Verb;

/**
 * Thrre legged Api. 
 * 
 */
public class OpenConextApi10aThreeLegged extends DefaultApi10a {

  private String baseUrl;
  
  public OpenConextApi10aThreeLegged(String baseUrl) {
    super();
    this.baseUrl = withEndingSlash(baseUrl);
  }

  private String withEndingSlash(String path) {
    return path.endsWith("/") ? path : path + "/";
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.scribe.builder.api.DefaultApi10a#getRequestTokenEndpoint()
   */
  @Override
  public String getRequestTokenEndpoint() {
    return baseUrl + "oauth1/requestToken";
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.scribe.builder.api.DefaultApi10a#getAccessTokenEndpoint()
   */
  @Override
  public String getAccessTokenEndpoint() {
    return baseUrl + "oauth1/accessToken";
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.scribe.builder.api.DefaultApi10a#getAuthorizationUrl(org.scribe.model
   * .Token)
   */
  @Override
  public String getAuthorizationUrl(Token requestToken) {
    return baseUrl + "oauth1/confirm_access?oauth_token=" + requestToken.getToken();
  }

  @Override
  public Verb getRequestTokenVerb() {
    return Verb.GET;
  }
}
