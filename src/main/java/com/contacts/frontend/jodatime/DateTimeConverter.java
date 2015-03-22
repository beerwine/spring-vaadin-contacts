package com.contacts.frontend.jodatime;

import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;

import com.vaadin.data.util.converter.Converter;


@SuppressWarnings("serial")
public class DateTimeConverter implements Converter<Date, DateTime> {

    @Override
    public Class<DateTime> getModelType() {
        return DateTime.class;
    }
    @Override
    public Class<Date> getPresentationType() {
        return Date.class;
    }
    @Override
    public DateTime convertToModel(Date value, Class<? extends DateTime> targetType, Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        return new DateTime(value);
    }
    @Override
    public Date convertToPresentation(DateTime value, Class<? extends Date> targetType, Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        if(value == null) {
            return null;
        } else {
            return value.toDate();
        }
    }
}
