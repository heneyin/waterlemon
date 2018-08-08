package com.henvealf.watermelon.common.test;

import com.henvealf.watermelon.commom.DateTimeWm;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * @author hongliang.yin/Henvealf on 2018/7/22
 */
public class DateTimeWmTest {

    @Test
    public void testWeek(){
        for (int i = 0; i < 500000; i ++) {
            DateTimeWm.getCurrentWeekMondayZeroDateString();
        }
        System.out.println(DateTimeWm.getCurrentWeekMondayZeroDateSecondString());
    }

    @Test
    public void testMouth(){
        for (int i = 0; i < 500000; i ++) {
            DateTimeWm.getCurrentMonthFirstDayZeroDateSecondString();
        }
        System.out.println(DateTimeWm.getCurrentMonthFirstDayZeroDateSecondString());
    }

    @Test
    public void testQuarter(){
        for (int i = 0; i < 500000; i ++) {
            DateTimeWm.getCurrentQuarterFirstDayZeroDateSecondString();
        }
        System.out.println(DateTimeWm.getCurrentQuarterFirstDayZeroDateSecondString());
    }

    @Test
    public void testYear(){
        for (int i = 0; i < 500000; i ++) {
            DateTimeWm.getCurrentYearFirstDayZeroDateSecondString();
        }
        System.out.println(DateTimeWm.getCurrentYearFirstDayZeroDateSecondString());
    }

    @Test
    public void testGetYesterday(){
        for (int i = 0; i < 500000; i ++) {
            DateTimeWm.getYesterdayZeroDateSecondString();
        }
        System.out.println(DateTimeWm.getYesterdayZeroDateSecondString());
    }

    @Test
    public void betweenStringAndDate() {
        for (int i = 0; i < 5; i ++) {
            Date date = DateTimeWm.parseDateFromSecondString("2018-06-06 12:33:33");
            System.out.println(DateTimeWm.formatDateToSecondString(date));
        }
    }

    @Test
    public void testCheckFormatt() {
        String errorDate = "2018-08-aadc";
        String trueDate = "2018-08-04";
        assert !DateTimeWm.isFormattedString(errorDate);
        assert DateTimeWm.isFormattedString(trueDate);
    }

    @Test
    public void testGetPrevDay() {
        String nowDate = "2018-08-08 10:11:11";
        Date prevDate = DateTimeWm.addDays(DateTimeWm.parseDateFromSecondString(nowDate), -1);
        System.out.println(DateTimeWm.formatDateToSecondString(prevDate));
    }

    @Test
    public void testAddDateString() {
        System.out.println(DateTimeWm.addDateString("2018-09-01",DateTimeWm.DATE_FORMAT_STRING, Calendar.DATE, 10));
    }

    @Test
    public void testDateDiff() {
        System.out.println(DateTimeWm.dateDiff(DateTimeWm.DATE_UNIT_DAY, "2018-08-14", "2018-09-15"));
        System.out.println(DateTimeWm.dateDiff(DateTimeWm.DATE_UNIT_DAY, "2018-08-14", "2018-09-15"));
    }

}
