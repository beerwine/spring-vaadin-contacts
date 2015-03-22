package com.contacts.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.jadira.usertype.dateandtime.joda.PersistentDateTime;
import org.joda.time.DateTime;

/**
 * Contact
 * @author martin.mecera
 *
 */
@Entity
@TypeDefs({ @TypeDef(name = "jodaDateTime", typeClass = PersistentDateTime.class) })
@SequenceGenerator(name = "contact_sequence", sequenceName = "contact_sequence", initialValue = 20, allocationSize = 1)
@Table(name = "contact", schema = "public")
public class Contact extends AbstractEntity {
    private String name;
    private Country country;
    private String phone;
    private Boolean newsletter;
    private DateTime deliverNewsletter;
    
    @Override
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contact_sequence")
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_country")
    public Country getCountry() {
        return country;
    }
    
    public void setCountry(Country country) {
        this.country = country;
    }

    @Column(name = "phone", length=255, nullable = true)
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "newsletter", nullable = true, columnDefinition = "boolean default false")
    public Boolean getNewsletter() {
        return newsletter;
    }
    
    public void setNewsletter(Boolean newsletter) {
        this.newsletter = newsletter;
    }

    @Column(name = "deliver_newsletter")
    @Type(type = "jodaDateTime")
    public DateTime getDeliverNewsletter() {
        return deliverNewsletter;
    }
    
    public void setDeliverNewsletter(DateTime deliverNewsletter) {
        this.deliverNewsletter = deliverNewsletter;
    }
    
}
