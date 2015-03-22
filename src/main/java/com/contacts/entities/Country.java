package com.contacts.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Conuntry
 * @author martin.mecera
 *
 */
@Entity
@SequenceGenerator(name = "country_sequence", sequenceName = "country_sequence", initialValue = 20, allocationSize = 1)
@Table(name = "country", schema = "public")
public class Country extends AbstractEntity {
    private String name;

    @Override
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "country_sequence")
    public Long getId() {
        return super.getId();
    }
    
    @Column(name = "name", length=255, nullable = false)
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
