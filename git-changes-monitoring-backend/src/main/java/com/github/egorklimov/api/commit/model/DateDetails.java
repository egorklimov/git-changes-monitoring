package com.github.egorklimov.api.commit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateDetails {

    private int dayOfYear;
    private int dayOfMonth;
    private int dayOfWeek;
    private int year;
    private int month;
    private int hour;

    public DateDetails(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        this.dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        this.dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        this.dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.hour = calendar.get(Calendar.HOUR);
    }
}
