package com.liebe.base_lib.action;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 一个执行单元。包括一个执行目标体和一个检验队列。检验队列用来暂存所有的前置条件，当所有的前置条件都通过后，才能进行执行单元。
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
public class ActionUnit {
    // 目标行为体
    private Action mAction;
    // 前置条件队列
    private Queue<Factor> mFactorQueue = new ArrayDeque<>();
    // 上一个执行的因子
    private Factor mLastFactor;

    public ActionUnit() {

    }

    public void addFactor(Factor factor) {
        mFactorQueue.add(factor);
    }

    public Action getAction() {
        return mAction;
    }

    public void setAction(Action action) {
        mAction = action;
    }

    public Factor getLastFactor() {
        return mLastFactor;
    }

    public void setLastFactor(Factor lastFactor) {
        mLastFactor = lastFactor;
    }

    public boolean hasFactor() {
        return !mFactorQueue.isEmpty();
    }

    public Factor pollFactor() {
        if (hasFactor()) {
            return mFactorQueue.poll();
        } else {
            return null;
        }
    }

    public void clear() {
        mFactorQueue.clear();
        mLastFactor = null;
        mAction = null;
    }

    public void check() {
        for (Factor factor : mFactorQueue) {
            if (factor.isValid()) {
                mFactorQueue.remove(factor);
            }
        }
    }
}
