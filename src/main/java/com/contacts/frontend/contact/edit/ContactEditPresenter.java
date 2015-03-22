package com.contacts.frontend.contact.edit;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.contacts.entities.Contact;
import com.contacts.entities.Country;
import com.contacts.managers.ContactManager;
import com.contacts.managers.CountryManager;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;

/**
 * 
 * @author martin.mecera
 *
 */
public class ContactEditPresenter implements ContactEditHandler{
    private static Logger logger = LoggerFactory.getLogger(ContactEditPresenter.class);
    
    private final ContactEditView view; 
    private final ContactManager contactManager;
    private final CountryManager countryManager;
    
    public ContactEditPresenter(ContactEditView view, ContactManager contactManager, CountryManager countryManager) {
        this.view = view;
        this.contactManager = contactManager;
        this.countryManager = countryManager;
    }
    
    /**
     * Loads Contact from service and inits view
     * @param params
     */
    public void init(String params) {
        view.setHandler(this);
        
        List<Country> countries = countryManager.getAll();
        
        if(StringUtils.isEmpty(params)) {
            view.init(new Contact(), countries);
        } else {
            try{
                long contactId = Long.parseLong(params);
                Contact contact = contactManager.getByID(contactId);
                if(contact == null) {
                    view.onContactDoesNotExist(contactId);
                } else {
                    view.init(contact, countries);
                }
            } catch(NumberFormatException e) {
                view.onBadParam();
            }
        }
    }
    
    @Override
    public void save() {
        try{
            view.commit();
            Contact contact = view.getContact();
            contactManager.save(contact);
            view.onSaveSuccess();
        } catch(CommitException e) { // validation failed
            view.onSaveFailed(e.getMessage());
        } catch(Exception e) {
            logger.error("Exception when saving contact", e);
            view.onSaveFailed(e.getMessage());
        }
    }

}
