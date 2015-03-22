package com.contacts.managers;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.contacts.dao.ContactDao;
import com.contacts.entities.Contact;
import com.contacts.filters.ContactFilter;

/**
 * Contacts business logic
 * @author martin.mecera
 *
 */
@Service
@Transactional
public class ContactManager {
    @Autowired
    private ContactDao contactDao;
    
    /**
     * Returns all contacts that satisfy condition
     */
    public List<Contact> getByFilter(ContactFilter filter) {
        return contactDao.getByFilter(filter);
    }
    
    /**
     * Saves or updates contact.
     */
    public void save(Contact contact) {
        if(contact.getId() != null) {
            contact.setUpdated(new DateTime());
        }
        contactDao.save(contact);
    }
    
    /**
     * Returns the contact
     */
    public Contact getByID(long id) {
        return contactDao.getByID(id);
    }
    
    /**
     * Deletes the contact
     */
    public void delete(long id) {
        Contact contact = contactDao.getByID(id);
        if(contact != null) {
            contactDao.remove(contact);
        }
    }
}
