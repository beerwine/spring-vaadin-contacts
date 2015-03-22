package com.contacts.filters;

import com.contacts.entities.Country;

/**
 * Filter contacts
 * @author martin.mecera
 *
 */
public interface ContactFilter {
    String getName();
    Country getCountry();
    Boolean getNewsletter();
}
