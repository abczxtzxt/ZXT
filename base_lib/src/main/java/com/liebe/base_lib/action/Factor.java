package com.liebe.base_lib.action;

/**
 * Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description * 前置执行因子，目标任务执行前需要先行执行的条件
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
public interface Factor {
    /**
     * 是否满足条件，不满足的话执行{@link Factor#doAction(ChainCallback)}
     *
     * @return 检验结果
     */
    boolean isValid();

    /**
     * 不满足条件时需要执行的行为
     *
     * @param callback 每个因子执行完成之后应该调用该接口{@link ChainCallback#onContinue()}方法否则执行链不会继续执行，中断执行则调用{@link ChainCallback#onInterrupt()}
     */
    void doAction(ChainCallback callback);
}
