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

package nl.surfnet.coin.janus;

import java.util.List;

import nl.surfnet.coin.janus.domain.EntityMetadata;

/**
 * Interface to Janus.
 */
public interface Janus {



  public enum Metadata {

    ENTITY_ID("__entityId"),
    OAUTH_SECRET("coin:oauth:secret"),
    OAUTH_CONSUMERKEY("coin:gadgetbaseurl"),
    OAUTH_CALLBACKURL("coin:oauth:callback_url"),
    OAUTH_TWOLEGGEDALLOWED("coin:oauth:two_legged_allowed"),
    OAUTH_APPTITLE("coin:oauth:app_title"),
    OAUTH_APPDESCRIPTION("coin:oauth:app_description"),
    OAUTH_APPTHUMBNAIL("coin:oauth:app_thumbnail"), 
    OAUTH_APPICON("coin:oauth:app_icon"),
    ORGANIZATION_URL("OrganizationURL:en"),
    ORGANIZATION_NAME("OrganizationName:en"),
    LOGO_URL("logo:0:url"),
    NAMEIDFORMAT("NameIDFormat"),
    DISPLAYNAME("displayName:en"),
    NAME("name:en"),
    DESCRIPTION("description:en"),
    CONTACTS_0_TYPE("contacts:0:contactType"),
    CONTACTS_0_EMAIL("contacts:0:emailAddress"),
    CONTACTS_0_GIVENNAME("contacts:0:givenName"),
    CONTACTS_0_SURNAME("contacts:0:surName"),
    CONTACTS_1_TYPE("contacts:1:contactType"),
    CONTACTS_1_EMAIL("contacts:1:emailAddress"),
    CONTACTS_1_GIVENNAME("contacts:1:givenName"),
    CONTACTS_1_SURNAME("contacts:1:surName")
    ;

    private String val;

    public String val() {
      return val;
    }

    Metadata(String val) {
      this.val = val;
    }
  }
  /**
   * Get a client's metadata by his entityId.
   * @param entityId the entityId
   */
  EntityMetadata getMetadataByEntityId(String entityId, Metadata... attributes);

  /**
   *
   * Get a list of entity ids that match the given metadata key/value pair.
   *
   * @param key the metadata key
   *            @param value the value the give metadata key should have
   * @return the entity id
   */
  List<String> getEntityIdsByMetaData(Metadata key, String value);

  /**
   * Refer to {@link Janus#getAllowedSps(String, String)} but without the revision parameter.
   */
  List<String> getAllowedSps(String idpentityid);

  /**
   * Get a list of SPs that are allowed for this IdP.
   *
   * @param idpentityid the IdPs entity id.
   * @param revision the revision.
   * @return TODO
   */
  List<String> getAllowedSps(String idpentityid, String revision);

  /**
   * Retrieves a list of all Service Providers.
   * Note that if you do not pass along any attributes very
   * little will be returned. You must specify the metadata you
   * want to see.
   *
   * @param attributes the attributes to fetch
   * @return for each SP (by entity id), a map of attributes and its values.
   */
  List<EntityMetadata> getSpList(Metadata... attributes);

}
