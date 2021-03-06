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

package nl.surfnet.coin.api.client.domain;

import org.springframework.util.StringUtils;

/**
 * 
 *
 */
public class Name {
  private String formatted;
  private String familyName;
  private String givenName;

  public Name() {
  }

  public Name(String formatted, String familyName, String givenName) {
    this.formatted = formatted;
    this.familyName = familyName;
    this.givenName = givenName;
  }

  /**
   * @return the formatted
   */
  public String getFormatted() {
    return formatted;
  }

  /**
   * @param formatted
   *          the formatted to set
   */
  public void setFormatted(String formatted) {
    this.formatted = formatted;
  }

  /**
   * @return the familyName
   */
  public String getFamilyName() {
    return familyName;
  }

  /**
   * @param familyName
   *          the familyName to set
   */
  public void setFamilyName(String familyName) {
    this.familyName = familyName;
  }

  /**
   * @return the givenName
   */
  public String getGivenName() {
    return givenName;
  }

  /**
   * @param givenName
   *          the givenName to set
   */
  public void setGivenName(String givenName) {
    this.givenName = givenName;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    /*
     * "name":{"formatted":"Winny Smits","familyName":"Smits","givenName":"Winny"
     */
    if (StringUtils.hasText(formatted)) {
      return formatted;
    }
    boolean givenNameText = StringUtils.hasText(givenName);
    boolean familyNameText = StringUtils.hasText(familyName);
    if (givenNameText && familyNameText) {
      return givenName + " " + familyName;
    }
    if (familyNameText) {
      return familyName;
    }
    if (givenNameText) {
      return givenName;
    }
    return "";
  }
}
