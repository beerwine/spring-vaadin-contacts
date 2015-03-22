package com.contacts.frontend.contact.list;

import java.util.List;

import com.contacts.entities.Contact;
import com.contacts.filters.ContactFilter;
import com.contacts.managers.ContactManager;
import com.contacts.managers.CountryManager;

/**
 * 
 * @author martin.mecera
 *
 */
public class ContactListPresenter implements ContactListHandler {
    private final ContactListView view;
    private final ContactManager contactManager;
    private final CountryManager countryManager;
    
    public ContactListPresenter(ContactListView view, ContactManager contactManager, CountryManager countryManager) {
        this.view = view;
        this.contactManager = contactManager;
        this.countryManager = countryManager;
    }

    @Override
    public void init() {
        view.setHandler(this);
        view.init(countryManager.getAll());
        
        refreshContacts();
    }

    @Override
    public void delete(long contactId) {
        contactManager.delete(contactId);
        view.notifyDeleteSuccess();
        refreshContacts();
    }
    
    private void refreshContacts() {
        ContactFilter filter = view.getFilter();
        List<Contact> contacts = contactManager.getByFilter(filter);
        view.showContacts(contacts);
    }

    @Override
    public void filterChanged() {
        refreshContacts();
    }
    
}
