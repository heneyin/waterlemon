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
        testDateDiff(DateTimeWm.DATE_UNIT_DAY, "2018-03-04 12:12:12.111","2018-03-04 12:12:12.000");
        testDateDiff(DateTimeWm.DATE_UNIT_DAY, "2018-03-04 12:12:12.111","2018-03-04 12:12:12.111");
        testDateDiff(DateTimeWm.DATE_UNIT_DAY, "2018-03-04 12:12:12.111","2018-03-04 12:12:12.222");
        System.out.println();
        testDateDiff(DateTimeWm.DATE_UNIT_DAY, "2018-03-04 12:12:12.000","2018-03-04 12:12:11.333");
        testDateDiff(DateTimeWm.DATE_UNIT_DAY, "2018-03-04 12:12:13.000","2018-03-04 12:12:11.333");
        System.out.println();
        testDateDiff(DateTimeWm.DATE_UNIT_DAY, "2018-03-04 01:12:13.000","2018-03-04 02:12:11.333");
        testDateDiff(DateTimeWm.DATE_UNIT_DAY, "2018-03-04 02:12:13.000","2018-03-04 01:17:11.333");
        System.out.println();
        testDateDiff(DateTimeWm.DATE_UNIT_DAY, "2018-03-05 02:12:13.000","2018-03-04 01:17:11.333");
        testDateDiff(DateTimeWm.DATE_UNIT_DAY, "2018-03-04 02:12:13.000","2018-03-05 01:17:11.333");
        testDateDiff(DateTimeWm.DATE_UNIT_DAY, "2018-03-04 02:12:13.000","2018-03-05 02:17:11.333");
        testDateDiff(DateTimeWm.DATE_UNIT_DAY, "2018-03-04 02:12:13.000","2018-03-10 02:17:11.333");
        System.out.println();
        testDateDiff(DateTimeWm.DATE_UNIT_MONTH, "2018-03-04 02:12:13.000","2018-04-03 02:17:11.333");
        testDateDiff(DateTimeWm.DATE_UNIT_MONTH, "2018-03-04 02:12:13.000","2018-04-05 02:17:11.333");
        testDateDiff(DateTimeWm.DATE_UNIT_MONTH, "2018-03-04 02:12:13.000","2018-04-04 02:12:13.000");
        testDateDiff(DateTimeWm.DATE_UNIT_MONTH, "2018-03-04 02:12:13.333","2018-04-04 02:12:13.000");
        testDateDiff(DateTimeWm.DATE_UNIT_MONTH, "2018-03-04 02:12:13.333","2018-04-04 02:12:14.000");
        testDateDiff(DateTimeWm.DATE_UNIT_MONTH, "2018-03-04 02:12:13.333","2018-04-04 02:13:10.000");
        testDateDiff(DateTimeWm.DATE_UNIT_MONTH, "2018-03-04 02:12:13.333","2018-04-04 03:13:10.000");
        testDateDiff(DateTimeWm.DATE_UNIT_MONTH, "2018-03-04 02:12:13.333","2018-04-10 03:13:10.000");
        testDateDiff(DateTimeWm.DATE_UNIT_MONTH, "2018-03-04 02:12:13.333","2018-05-10 03:13:10.000");
        testDateDiff(DateTimeWm.DATE_UNIT_MONTH, "2018-03-04 02:12:13.333","2019-05-10 03:13:10.000");
        testDateDiff(DateTimeWm.DATE_UNIT_MONTH, "2018-03-04 02:12:13.333","2017-05-10 03:13:10.000");
        System.out.println();
        testDateDiff(DateTimeWm.DATE_UNIT_YEAR, "2018-05-10 03:13:10.333","2017-05-10 03:13:10.000");
    }

    private void testDateDiff(String unit, String startDate, String endDate) {
        System.out.println(String.format("unit: %s, startDate: %s, endDate: %s = %s",
                                unit, startDate, endDate, DateTimeWm.dateDiff(unit, startDate, endDate,"yyyy-MM-dd HH:mm:ss.SSS")));
    }

}
