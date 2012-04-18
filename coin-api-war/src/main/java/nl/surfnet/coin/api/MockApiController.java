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

import static nl.surfnet.coin.api.PersonController.getOnBehalfOf;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import nl.surfnet.coin.api.client.domain.AbstractEntry;
import nl.surfnet.coin.api.client.domain.Group20;
import nl.surfnet.coin.api.client.domain.Group20Entry;
import nl.surfnet.coin.api.client.domain.GroupMembersEntry;
import nl.surfnet.coin.api.client.domain.Person;
import nl.surfnet.coin.api.client.domain.PersonEntry;
import nl.surfnet.coin.api.service.GroupService;
import nl.surfnet.coin.api.service.PersonService;

import org.apache.commons.beanutils.BeanComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for the mock REST interface.
 */
@Controller
@RequestMapping(value = "mock10/social/rest")
@SuppressWarnings("unchecked")
public class MockApiController {

  private static Logger LOG = LoggerFactory.getLogger(MockApiController.class);

  @Resource(name = "mockService")
  private PersonService personService;

  @Resource(name = "mockService")
  private GroupService groupService;

  @RequestMapping(value = "/people/{userId}/@self")
  @ResponseBody
  public PersonEntry getPersonAtSelf(@PathVariable("userId")
  String userId) {
    return getPerson(userId);
  }

  @RequestMapping(value = "/people/{userId}")
  @ResponseBody
  public PersonEntry getPerson(@PathVariable("userId")
  String userId) {

    LOG.info("Got getPerson-request, for userId '{}' on behalf of '{}'",
        new Object[] { userId, PersonController.getOnBehalfOf() });
    if (PersonController.PERSON_ID_SELF.equals(userId)) {
      userId = getOnBehalfOf();
    }
    return personService.getPerson(userId, PersonController.getOnBehalfOf());
  }

  @RequestMapping(value = "/people/{userId}/{groupId}")
  @ResponseBody
  public GroupMembersEntry getGroupMembers(@PathVariable("userId")
  String userId, @PathVariable("groupId")
  String groupId, @RequestParam(value = "count", required = false)
  Integer count, @RequestParam(value = "startIndex", required = false)
  Integer startIndex, @RequestParam(value = "sortBy", required = false)
  String sortBy) {
    if (PersonController.PERSON_ID_SELF.equals(userId)) {
      userId = getOnBehalfOf();
    }
    LOG.info("Got getGroupMembers-request, for userId '{}', groupId '{}', on behalf of '{}'", new Object[] { userId,
        groupId, PersonController.getOnBehalfOf() });
    GroupMembersEntry groupMembers = personService.getGroupMembers(groupId, PersonController.getOnBehalfOf());
    List<Person> entry = groupMembers.getEntry();
    entry = (List<Person>) processQueryOptions(groupMembers, count, startIndex, sortBy, entry);
    groupMembers.setEntry(entry);
    return groupMembers;
  }

  @RequestMapping(value = "/groups/{userId}")
  @ResponseBody
  public Group20Entry getGroups(@PathVariable("userId")
  String userId, @RequestParam(value = "count", required = false)
  Integer count, @RequestParam(value = "startIndex", required = false)
  Integer startIndex, @RequestParam(value = "sortBy", required = false)
  String sortBy) {
    if (PersonController.PERSON_ID_SELF.equals(userId)) {
      userId = getOnBehalfOf();
    }
    LOG.info("Got getGroups-request, for userId '{}',  on behalf of '{}'",
        new Object[] { userId, PersonController.getOnBehalfOf() });
    Group20Entry groups = groupService.getGroups20(userId, PersonController.getOnBehalfOf());
    List<Group20> entry = groups.getEntry();
    entry = (List<Group20>) processQueryOptions(groups, count, startIndex, sortBy, entry);
    return groups;
  }

  private List<? extends Object> processQueryOptions(AbstractEntry parent, Integer count, Integer startIndex,
      String sortBy, List<? extends Object> entry) {
    parent.setTotalResults(entry.size());
    if (StringUtils.hasText(sortBy)) {
      BeanComparator comparator = new BeanComparator(sortBy);
      Collections.sort(entry, comparator);
      parent.setSorted(true);
    }
    if (startIndex != null) {
      entry = entry.subList(startIndex, entry.size());
      parent.setStartIndex(startIndex);
    }
    if (count != null) {
      entry = entry.subList(0, count);
      parent.setItemsPerPage(count);
    }

    return entry;
  }

}
