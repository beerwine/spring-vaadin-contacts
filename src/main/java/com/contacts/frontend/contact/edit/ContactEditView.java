package com.contacts.frontend.contact.edit;

import java.util.List;

import com.contacts.entities.Contact;
import com.contacts.entities.Country;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.navigator.View;

/**
 * Defines methods of the view layer.
 * @author martin.mecera
 *
 */
public interface ContactEditView extends View{
    void setHandler(ContactEditHandler handler);
    void init(Contact contact, List<Country> countries);
    void commit() throws CommitException;
    Contact getContact();
    
    void onContactDoesNotExist(long contactId);
    void onBadParam();
    void onSaveSuccess();
    void onSaveFailed(String message);
}
