package com.henvealf.watermelon.common;

/**
 * <p>
 *
 * <p>
 *
 * @author hongliang.yin/Henvealf
 * @since 2019-06-16
 */
public class TimeUse {

    private long start = System.currentTimeMillis();

    public long took() {
        return System.currentTimeMillis() - start;
    }

    @Override
    public String toString() {
        return String.valueOf(took()) + "ms";
    }

    public static TimeUse get(){
        return new TimeUse();
    }



}
