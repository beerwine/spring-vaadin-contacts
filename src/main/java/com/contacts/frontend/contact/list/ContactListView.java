package com.contacts.frontend.contact.list;

import java.util.List;

import com.contacts.entities.Contact;
import com.contacts.entities.Country;
import com.contacts.filters.ContactFilter;
import com.vaadin.navigator.View;


public interface ContactListView extends View {
    void setHandler(ContactListHandler handler);
    void init(List<Country> countries);
    void showContacts(List<Contact> contacts);
    void notifyDeleteSuccess();
    ContactFilter getFilter();
}
