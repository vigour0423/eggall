package com.ddl.egg.jaxb;


import org.springframework.util.StringUtils;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mark.huang on 2016-07-18.
 */
public class DateTimeAdapter extends XmlAdapter<String, Date> {
    private static final String DATE_FORMAT_FULL_DATETIME = "EEE MMM dd HH:mm:ss Z yyyy";
    private static final String DATE_FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT_DATE = "yyyy-MM-dd";

    @Override
    public Date unmarshal(String date) throws Exception {
        if (!StringUtils.hasText(date) || "null".equalsIgnoreCase(date)) return null;

        String[] formats = new String[]{DATE_FORMAT_DATETIME, DATE_FORMAT_DATE, DATE_FORMAT_FULL_DATETIME};
        return convert(date, formats);
    }

    @Override
    public String marshal(Date date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DATETIME);
        return sdf.format(date);
    }

    private static Date convert(String value, String[] formats) {
        SimpleDateFormat sdf;
        for (String format : formats) {
            try {
                sdf = new SimpleDateFormat(format);
                return sdf.parse(value);
            } catch (Exception e) {
            }
        }
        throw new RuntimeException("can not convert [" + value + "] to Date");
    }
}
