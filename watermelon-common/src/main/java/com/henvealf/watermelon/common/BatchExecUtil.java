package com.henvealf.watermelon.common;

import java.util.List;
import java.util.function.Consumer;

/**
 *
 * 分批执行的工具。
 *
 * @author hongliang.yin/Henvealf
 * @date 2020/3/9
 */
public class BatchExecUtil {

    public static <T> int exec(List<T> data, int batchSize, Consumer<List<T>> exec) {
        if (data == null || data.isEmpty() || exec == null) {
            return 0;
        }
        int dataSize = data.size();
        int start = 0;
        int end;
        int totalTimes = 0;
        do {
            end = Math.min(start + batchSize, dataSize);
            List<T> subBatch = data.subList(start, end);
            exec.accept(subBatch);
            totalTimes ++;
            start = end;
        } while (end < dataSize);
        return totalTimes;
    }

}
