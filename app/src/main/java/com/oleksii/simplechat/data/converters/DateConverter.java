package com.oleksii.simplechat.data.converters;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {

    @TypeConverter
    public Long dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
    }

    @TypeConverter
    public Date timestampToDate(Long date) {
        if (date == null) {
            return null;
        } else {
            return new Date(date);
        }
    }
}
