package com.contacts.managers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.contacts.dao.CountryDao;
import com.contacts.entities.Country;


@Service
@Transactional
public class CountryManager {
    @Autowired
    private CountryDao countryDao;
    
    public List<Country> getAll() {
        return countryDao.getAll();
    }
}
