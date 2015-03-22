package com.contacts.frontend.contact.list;

public interface ContactListHandler {
    void init();
    void delete(long contactId);
    void filterChanged();
}
