package com.contacts;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.DiscoveryNavigator;

import com.vaadin.annotations.Theme;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

/**
 * 
 * @author martin.mecera
 *
 */
@Component
@Scope("prototype")
@Theme("valo")
public class ContactsUI extends UI{
	private static final long serialVersionUID = 8440611785133537972L;

	@SuppressWarnings("unused")
    @Override
	protected void init(VaadinRequest request) {
		setSizeFull();
		Page.getCurrent().setTitle("Contacts");
        DiscoveryNavigator navigator = new DiscoveryNavigator(this, this);
	}

}
