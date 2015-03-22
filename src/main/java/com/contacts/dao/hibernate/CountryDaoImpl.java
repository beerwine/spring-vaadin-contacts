package com.contacts.dao.hibernate;

import com.contacts.dao.CountryDao;
import com.contacts.entities.Country;

/**
 * Hibernate implementation of database manipulations with Country.
 * @author martin.mecera
 *
 */
public class CountryDaoImpl extends GenericDaoImpl<Country, Long> implements CountryDao{
    public CountryDaoImpl() {
        super(Country.class);
    }
}
