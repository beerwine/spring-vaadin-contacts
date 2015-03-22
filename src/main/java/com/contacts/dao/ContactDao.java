package com.contacts.dao;

import java.util.List;

import com.contacts.entities.Contact;
import com.contacts.filters.ContactFilter;

/**
 * Database manipulation with Contacts.
 * @author martin.mecera
 *
 */
public interface ContactDao extends GenericDao<Contact, Long>{
    /**
     * Returns all contacts that satisfy the filter
     */
    List<Contact> getByFilter(ContactFilter filter);
}
