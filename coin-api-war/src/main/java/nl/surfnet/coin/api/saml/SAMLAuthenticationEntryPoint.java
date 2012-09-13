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
package nl.surfnet.coin.api.saml;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.surfnet.spring.security.opensaml.AuthnRequestGenerator;
import nl.surfnet.spring.security.opensaml.util.IDService;
import nl.surfnet.spring.security.opensaml.util.TimeService;
import nl.surfnet.spring.security.opensaml.xml.EndpointGenerator;

import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.metadata.Endpoint;
import org.opensaml.saml2.metadata.SingleSignOnService;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * SamlAuthenticationEntryPoint.java
 * 
 */
public class SAMLAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private static final Logger LOG = LoggerFactory.getLogger(SAMLAuthenticationEntryPoint.class);

  private TimeService timeService = new TimeService();
  private IDService idService = new IDService();
  
  @Resource(name="openSAMLContext")
  private OpenSAMLContext openSAMLContext;
  
  private String ssoUrl = "https://engine.dev.surfconext.nl/authentication/idp/single-sign-on";

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.springframework.security.web.AuthenticationEntryPoint#commence(javax
   * .servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   * org.springframework.security.core.AuthenticationException)
   */
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException,
      ServletException {
    sendAuthnRequest(response);
  }
 
  private void sendAuthnRequest(HttpServletResponse response) throws IOException {
    AuthnRequestGenerator authnRequestGenerator = new AuthnRequestGenerator(openSAMLContext.entityId(), timeService,
        idService);
    EndpointGenerator endpointGenerator = new EndpointGenerator();

    final String target = ssoUrl;

    Endpoint endpoint = endpointGenerator.generateEndpoint(
        SingleSignOnService.DEFAULT_ELEMENT_NAME, target, openSAMLContext.assertionConsumerUri());

    AuthnRequest authnRequest = authnRequestGenerator.generateAuthnRequest(target, openSAMLContext.assertionConsumerUri());

    try {
      openSAMLContext.samlMessageHandler().sendSAMLMessage(authnRequest, endpoint, response, "relayState");
    } catch (MessageEncodingException mee) {
      LOG.error("Could not send authnRequest to Identity Provider.", mee);
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}
