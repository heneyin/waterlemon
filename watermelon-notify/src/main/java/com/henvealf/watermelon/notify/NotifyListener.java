package com.henvealf.watermelon.notify;

import org.apache.curator.framework.recipes.cache.ChildData;

/**
 * @author hongliang.yin/Henvealf on 2018/12/17
 */
//@FunctionalInterface
public interface NotifyListener {
    public void process(ChildData currentData);
}
