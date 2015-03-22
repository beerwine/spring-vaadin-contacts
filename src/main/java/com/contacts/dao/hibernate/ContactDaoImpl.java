package com.contacts.dao.hibernate;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;

import com.contacts.dao.ContactDao;
import com.contacts.entities.Contact;
import com.contacts.filters.ContactFilter;

/**
 * Hibernate implementation of database manipulations with Contacts.
 * @author martin.mecera
 *
 */
public class ContactDaoImpl extends GenericDaoImpl<Contact, Long> implements ContactDao{
    public ContactDaoImpl() {
        super(Contact.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Contact> getByFilter(ContactFilter filter) {
        StringBuilder sb = new StringBuilder();
        sb.append("FROM ").append(Contact.class.getName()).append(" c");
        
        Map<String, Object> properties = new HashMap<String, Object>();
        List<String> conditions = new LinkedList<String>();
        if(filter != null) {
            if(StringUtils.isNotBlank(filter.getName())) {
                conditions.add("c.name LIKE :name");
                properties.put("name", "%" + filter.getName() + "%");
            }
            if(filter.getCountry() != null) {
                conditions.add("c.country.id = :countryId");
                properties.put("countryId", filter.getCountry().getId());
            }
            if(BooleanUtils.isTrue(filter.getNewsletter())) {
                conditions.add("c.newsletter = TRUE");
            }
        }
        
        if(!conditions.isEmpty()) {
            sb.append(" WHERE ").append(StringUtils.join(conditions, " AND "));
        }
        
        Query query = getSessionFactory().getCurrentSession().createQuery(sb.toString());
        if(!properties.isEmpty()) {
            query.setProperties(properties);
        }
        
        return query.list();
    }
}
