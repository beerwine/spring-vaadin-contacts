package com.contacts.frontend.contact.list;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import ru.xpoft.vaadin.VaadinView;

import com.contacts.entities.Contact;
import com.contacts.entities.Country;
import com.contacts.filters.ContactFilter;
import com.contacts.frontend.ViewNames;
import com.contacts.managers.ContactManager;
import com.contacts.managers.CountryManager;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author martin.mecera
 *
 */
@Component
@Scope("prototype")
@VaadinView(ViewNames.CONTACT_LIST)
@SuppressWarnings("serial")
public class ContactListViewImpl extends VerticalLayout implements ContactListView {
    @Autowired
    private ContactManager contactManager;
    
    @Autowired
    private CountryManager countryManager;
    
    private ContactListHandler handler;
    private Table tblContacts;
    private final TableFilterData filterData = new TableFilterData();
    
    @Override
    public void enter(ViewChangeEvent event) {
        ContactListPresenter presenter = new ContactListPresenter(this, contactManager, countryManager);
        presenter.init();
    }

    @Override
    public void setHandler(ContactListHandler handler) {
        this.handler = handler;
    }

    @Override
    public void init(List<Country> countries) {
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setMargin(true);
        addComponent(buttons);
        
        // Redirect to new contact form
        Button btnNewContact = new Button("New Contact");
        buttons.addComponent(btnNewContact);
        btnNewContact.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                Page.getCurrent().setUriFragment("!" + ViewNames.CONTACT_EDIT);
            }
        });
        
        // Filtering
        BeanFieldGroup<TableFilterData> filterForm = new BeanFieldGroup<TableFilterData>(TableFilterData.class);
        filterForm.setBuffered(false);
        filterForm.setItemDataSource(filterData);
        
        FormLayout filters = new FormLayout();
        addComponent(filters);
        filters.setMargin(true);
        
        // name
        TextField txtName = new TextField("Name");
        txtName.setImmediate(true);
        txtName.setNullRepresentation("");
        filters.addComponent(txtName);
        filterForm.bind(txtName, "name");
        txtName.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(TextChangeEvent event) {
                filterData.setName(event.getText());
                handler.filterChanged();
            }
        });
        txtName.setTextChangeEventMode(TextChangeEventMode.EAGER);
        
        // country
        BeanItemContainer<Country> countryContainer = new BeanItemContainer<Country>(Country.class, countries);
        ComboBox cmbCountry = new ComboBox("Country", countryContainer);
        cmbCountry.setItemCaptionPropertyId("name");
        cmbCountry.setNullSelectionAllowed(true);
        cmbCountry.setImmediate(true);
        filterForm.bind(cmbCountry, "country");
        filters.addComponent(cmbCountry);
        
        // newsletter
        CheckBox chkNewsletter = new CheckBox("Receives newsletter", false);
        chkNewsletter.setImmediate(true);
        filterForm.bind(chkNewsletter, "newsletter");
        filters.addComponent(chkNewsletter);
        
        Property.ValueChangeListener filterListener = new Property.ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                handler.filterChanged();
            }
        };
        cmbCountry.addValueChangeListener(filterListener);
        chkNewsletter.addValueChangeListener(filterListener);
        
        // Table
        tblContacts = new Table();
        addComponent(tblContacts);
        
        tblContacts.setColumnHeader("country.name", "Country");
        tblContacts.setColumnHeader("name_edit", "Name");
        tblContacts.setColumnHeader("updated", "Updated");
        tblContacts.setColumnHeader("created", "Created");
        tblContacts.setColumnHeader("delete", "");
        tblContacts.setColumnHeader("phone", "Phone");
        tblContacts.setColumnHeader("newsletter_chk", "Receives newsletter");
        tblContacts.setColumnHeader("deliver", "Deliver newsletter on");
        
        // edit
        tblContacts.addGeneratedColumn("name_edit", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                long contactId = (Long)source.getItem(itemId).getItemProperty("id").getValue();
                String name = (String)source.getItem(itemId).getItemProperty("name").getValue();
                return new Link(name, new ExternalResource("#!" + ViewNames.CONTACT_EDIT + "/" + contactId));
            }
        });
        
        // newsletter
        tblContacts.addGeneratedColumn("newsletter_chk", new Table.ColumnGenerator() {
            
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                boolean newsletter = (Boolean)source.getItem(itemId).getItemProperty("newsletter").getValue();
                CheckBox chk = new CheckBox("", newsletter);
                chk.setEnabled(false);
                return chk;
            }
        });
        
        // deliver newsletter on
        tblContacts.addGeneratedColumn("deliver", new Table.ColumnGenerator() {
            
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                DateTime deliverNewsletter = (DateTime)source.getItem(itemId).getItemProperty("deliverNewsletter").getValue();
                Label lblDeliver = new Label("");
                if(deliverNewsletter != null) {
                    lblDeliver.setValue(deliverNewsletter.toString("dd.MM.yyyy"));
                }
                return lblDeliver;
            }
        });
        
        // delete
        tblContacts.addGeneratedColumn("delete", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                final long contactId = (Long)source.getItem(itemId).getItemProperty("id").getValue();
                final String name = (String)source.getItem(itemId).getItemProperty("name").getValue();
                Button btnDelete = new Button("Delete", new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        ConfirmDialog.show(getUI(), "Delete " + name, "Really delete " + name + "?", 
                            "Delete", "Cancel", new ConfirmDialog.Listener() {
                            
                            @Override
                            public void onClose(ConfirmDialog dialog) {
                                if(dialog.isConfirmed()) {
                                    handler.delete(contactId);
                                }
                            }
                        });
                        
                    }
                });
                return btnDelete;
            }
        });
        
    }

    @Override
    public void showContacts(List<Contact> contacts) {
        BeanItemContainer<Contact> personContainer = new BeanItemContainer<Contact>(Contact.class, contacts);
        personContainer.addNestedContainerProperty("country.name");
        tblContacts.setContainerDataSource(personContainer);
        tblContacts.setVisibleColumns("id", "name_edit", "phone", "country.name", "newsletter_chk", "deliver", "delete");
    }

    @Override
    public void notifyDeleteSuccess() {
        Notification.show("Contact deleted.");
    }

    /**
     * Data in filter
     * @author martin.mecera
     *
     */
    public class TableFilterData implements ContactFilter {
        private String name = null;
        private Country country = null;
        private Boolean newsletter = null;
        
        @Override
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        @Override
        public Country getCountry() {
            return country;
        }
        
        public void setCountry(Country country) {
            this.country = country;
        }
        
        @Override
        public Boolean getNewsletter() {
            return newsletter;
        }
        
        public void setNewsletter(Boolean newsletter) {
            this.newsletter = newsletter;
        }
    }

    @Override
    public ContactFilter getFilter() {
        return filterData;
    }
}
