package com.henvealf.watermelon.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

/**
 * 得到任务的运行情况，如果任务失败，就重试 N 次。
 * @author hongliang.yin/Henvealf
 * @date 2018/2/8
 */
public class WorkRetryer {

    private static Logger logger = LoggerFactory.getLogger(WorkRetryer.class);
    private int willRetryTimes = 3;
    private int waitIntervalSecond = 3;

    public WorkRetryer(int retryTimes, int waitIntervalSecond) {
        this.willRetryTimes = retryTimes;
        this.waitIntervalSecond = waitIntervalSecond;
    }

    public WorkRetryer() {}

    /**
     * 根据任务返回的 boolean 来判断是否重试
     * 如果任务没有返回值，只抛出异常，needRetryWork 固定返回 true 就可以
     * @param needRetryWork 具体要被重复的工作
     * @param wordName 工作的名称
     */
    public boolean process(Supplier<Boolean> needRetryWork, String wordName) {
        int retryTimes = 0;
        boolean isSuccess;
        try {
            isSuccess = needRetryWork.get();
        } catch (Exception e) {
            isSuccess = false;
            logger.info("Exception throw when process work.", e);
        }
        while (!isSuccess) {
            if (retryTimes >= this.willRetryTimes) {
                logger.info("Work {} still failed after retry {} times. end!", wordName, retryTimes);
                return false;
            }
            logger.info("Process work {} failed, retry after {} seconds.", wordName, this.waitIntervalSecond);
            try {
                Thread.sleep(this.waitIntervalSecond);
            } catch (InterruptedException e) {
                logger.error("Error happened when retry work " + wordName, e);
            }
            retryTimes ++;
            logger.warn("Begin retry work {} on times {}", wordName, retryTimes);
            try {
                isSuccess = needRetryWork.get();
            } catch (Exception e) {
                logger.error(String.format("Exception throw when retry work %s on times %s.", wordName, retryTimes), e);
                isSuccess = false;
            }
        }
        return true;
    }

    public boolean process(Supplier<Boolean> needRetryWork) {
        return this.process(needRetryWork, "unknown" );
    }
}
