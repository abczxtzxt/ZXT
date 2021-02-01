package com.liebe.base_lib.action;

/**
 * Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description Factor执行链回调接口
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
public interface ChainCallback {

    /**
     * 继续执行链式因子
     */
    void onContinue();

    /**
     * 中断当前执行链，用于及时清理数据避免内存泄漏
     */
    void onInterrupt();
}
