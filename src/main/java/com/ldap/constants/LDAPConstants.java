package com.ldap.constants;


/**
 * Class that contains constants values for accessing the LDAP Server.
 * 
 * @author jon
 */
public abstract class LDAPConstants
{
    // This constant is used when searching through all properties within an LDAP entry.
    public static final String[] ALL_LDAP_OBJECT_PROPERTIES = new String[] { "*", "+" };

    public static final String LDAP_SEARCH_WILDCARD = "*";

    // LDAP Attributes
    public static final String LDAP_ATTR_FULLNAME = "cn";
    public static final String LDAP_ATTR_LASTNAME = "sn";
    public static final String LDAP_ATTR_FIRSTNAME = "givenName";
    public static final String LDAP_ATTR_USERNAME = "uid";
    public static final String LDAP_ATTR_PASSWORD = "userPassword";
}
