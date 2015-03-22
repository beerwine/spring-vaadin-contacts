package com.contacts.frontend.contact.edit;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.contacts.entities.Contact;
import com.contacts.entities.Country;
import com.contacts.frontend.ViewNames;
import com.contacts.frontend.jodatime.DateTimeConverter;
import com.contacts.frontend.jodatime.JodaFieldFactory;
import com.contacts.managers.ContactManager;
import com.contacts.managers.CountryManager;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroupFieldFactory;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author martin.mecera
 *
 */
@Component
@Scope("prototype")
@VaadinView(ViewNames.CONTACT_EDIT)
@SuppressWarnings("serial")
public class ContactEditViewImpl extends VerticalLayout implements ContactEditView {
    @Autowired
    private ContactManager contactManager;
    
    @Autowired
    private CountryManager countryManager;

    /**
     * Allows: +420 728 51 98 82, 728 51 98 82, (0042) 728 51 98 82
     */
    private static final Pattern phonePattern = Pattern.compile("^\\s*\\+?[\\p{N}\\s]+$|^\\s*\\([\\p{N}\\s]+\\)[\\p{N}\\s]+$");
    
    private ContactEditHandler handler;
    
    private BeanFieldGroup<Contact> form;
    
    @Override
    public void enter(ViewChangeEvent event) {
        ContactEditPresenter presenter = new ContactEditPresenter(this, contactManager, countryManager);
        presenter.init(event.getParameters());
    }

    @Override
    public void setHandler(ContactEditHandler handler) {
        this.handler = handler;
    }

    @Override
    public void init(Contact contact, List<Country> countries) {
        form = new BeanFieldGroup<Contact>(Contact.class);
        FieldGroupFieldFactory fieldFactory = new JodaFieldFactory();
        form.setFieldFactory(fieldFactory);
        
        form.setItemDataSource(contact);
        
        // Name
        FormLayout layout = new FormLayout();
        layout.setMargin(true);
        
        TextField txtName = new TextField("Name");
        txtName.setNullRepresentation("");
        txtName.addValidator(new StringLengthValidator("Name can not be empty.", 1, 255, false));
        txtName.setImmediate(true);
        form.bind(txtName, "name");
        layout.addComponent(txtName);
        
        // Phone
        TextField txtPhone = new TextField("Phone");
        txtPhone.setNullRepresentation("");
        txtPhone.addValidator(new Validator() {
            @Override
            public void validate(Object value) throws InvalidValueException {
                String phoneNo = (String)value;
                // empty phone number is allowed
                if(StringUtils.isNoneEmpty(phoneNo)) {
                    if(!phonePattern.matcher(phoneNo).matches()) {
                        throw new InvalidValueException("Valid phone numbers are: +420 728 51 98 82, 728 51 98 82, (0042) 728 51 98 82");
                    }
                }
            }
        });
        txtPhone.setImmediate(true);
        form.bind(txtPhone, "phone");
        layout.addComponent(txtPhone);
        
        // Country
        BeanItemContainer<Country> countryContainer = new BeanItemContainer<Country>(Country.class, countries);
        ComboBox cmbCountry = new ComboBox("Country", countryContainer);
        cmbCountry.setItemCaptionPropertyId("name");
        cmbCountry.setNullSelectionAllowed(true);
        form.bind(cmbCountry, "country");
        layout.addComponent(cmbCountry);
        
        final DateField dfDeliver = new DateField("Deliver newsletter on");
        
        // Newsletter
        final CheckBox chkNewsletter = new CheckBox("Receives newsletter", false);
        chkNewsletter.setImmediate(true);
        chkNewsletter.addListener(new Listener() {
            @Override
            public void componentEvent(Event event) {
                dfDeliver.setEnabled(BooleanUtils.isTrue(chkNewsletter.getValue()));
            }
        });
        form.bind(chkNewsletter, "newsletter");
        layout.addComponent(chkNewsletter);
        
        // Deliver newsletter
        dfDeliver.setConverter(new DateTimeConverter());
        dfDeliver.setDateFormat("dd.MM.yyyy");
        form.bind(dfDeliver, "deliverNewsletter");
        dfDeliver.setEnabled(BooleanUtils.isTrue(contact.getNewsletter()));
        layout.addComponent(dfDeliver);
        
        // Form
        addComponent(layout);
        
        HorizontalLayout buttons = new HorizontalLayout();
        addComponent(buttons);
        
        // Save
        Button btnSave = new Button("Save");
        buttons.addComponent(btnSave);
        btnSave.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                handler.save();
            }
        });
        
        // cancel
        Label lblOr = new Label(" or ");
        buttons.addComponent(lblOr);
        
        Link lnkCancel = new Link("cancel", new ExternalResource("#!" + ViewNames.CONTACT_LIST));
        buttons.addComponent(lnkCancel);
    }

    @Override
    public void commit() throws CommitException {
        if(form.isValid()) {
            form.commit();
        } else {
            throw new CommitException("Some values are not correct.");
        }
    }

    @Override
    public Contact getContact() {
        return form.getItemDataSource().getBean();
    }

    @Override
    public void onContactDoesNotExist(long contactId) {
        Page.getCurrent().setUriFragment("!" + ViewNames.CONTACT_LIST);
        Notification.show("Contact " + contactId + " does not exist.", Type.WARNING_MESSAGE);
    }

    @Override
    public void onBadParam() {
        Page.getCurrent().setUriFragment("!" + ViewNames.CONTACT_LIST);
        Notification.show("Bad contact ID in parameter", Type.WARNING_MESSAGE);
    }

    @Override
    public void onSaveSuccess() {
        Page.getCurrent().setUriFragment("!" + ViewNames.CONTACT_LIST);
        Notification.show("Saved");
    }

    @Override
    public void onSaveFailed(String message) {
        Notification.show("Save failed", message, Type.ERROR_MESSAGE);
    }

}
