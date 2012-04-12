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

import nl.surfnet.coin.api.client.domain.PersonEntry;
import nl.surfnet.coin.api.service.MockService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth.provider.ConsumerDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for the person REST interface..
 */
@Controller
public class PersonController {

    private static Logger LOG = LoggerFactory.getLogger(PersonController.class);

    private static final String GROUP_ID_SELF = "@self";

    private MockService mockService = new MockService();

    @RequestMapping(value = "/social/rest/people/{userId}/{groupId}")
    @ResponseBody
    public PersonEntry getPerson(
            @PathVariable("userId") String userId,
            @PathVariable("groupId") String groupId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Got getPerson-request, for userId '{}', groupId '{}', on behalf of '{}'", new Object[] {userId, groupId, getOnBehalfOf()});
        }
        if (GROUP_ID_SELF.equals(groupId)) {
            return mockService.getPerson(userId, getOnBehalfOf());
        } else {
            throw new UnsupportedOperationException("Not supported: person query other than @self.");
        }
    }

  /**
   * Get the username of the (via oauth) authenticated user that performs this request.
   *
   * @return the username in case of an end user authorized request (3 legged oauth1, authorization code grant oauth2) or the consumer key in case of unauthorized requests.
   */
  private String getOnBehalfOf() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null) {
      return null;
    } else {
      if (auth.getPrincipal() instanceof ConsumerDetails) {
        // Two legged, it does not have end user details
        return ((ConsumerDetails) auth.getPrincipal()).getConsumerKey();
      } else if (auth.getPrincipal() instanceof String) {
        return (String) auth.getPrincipal();
      } else {
        return ((UserDetails) auth.getPrincipal()).getUsername();
      }
    }
  }
}
