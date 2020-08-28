package com.oleksii.simplechat.data.converters;

import androidx.room.TypeConverter;

import java.sql.Timestamp;

public class TimestampConverter {

    @TypeConverter
    public Long timestampToLong(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        } else {
            return timestamp.getTime();
        }
    }

    @TypeConverter
    public Timestamp longToTimestamp(Long date) {
        if (date == null) {
            return null;
        } else {
            return new Timestamp(date);
        }
    }
}
