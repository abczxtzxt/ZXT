package com.liebe.base_lib.action;

/**
 * Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 延迟任务执行。如果{@link ActionUnit}验证模型中没有嵌套的验证模型，则可以直接使用{@link DelayAction}
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
public class DelayAction implements ChainCallback {

    private static DelayAction sInstance;
    private ActionUnit mActionUnit;

    public static DelayAction getInstance() {
        if (null == sInstance) {
            sInstance = new DelayAction();
        }
        sInstance.mActionUnit.clear();
        return sInstance;
    }


    private DelayAction() {
        mActionUnit = new ActionUnit();
    }

    private void reset() {
        mActionUnit.clear();
    }

    public DelayAction setAction(Action action) {
        mActionUnit.setAction(action);
        return this;
    }

    public DelayAction addFactor(Factor factor) {
        // 只添加无效的，验证不通过的
        if (!factor.isValid()) {
            mActionUnit.addFactor(factor);
        }
        return this;
    }

    public void execute() {
        Factor lastFactor = mActionUnit.getLastFactor();
        // 如果上一条Factor没有通过，是不允许再发起call的
        if (lastFactor != null && !lastFactor.isValid()) {
            return;
        }

        if (mActionUnit.hasFactor()) {
            // 执行验证
            Factor factor = mActionUnit.pollFactor();
            if (factor.isValid()) {
                execute();
            } else {
                mActionUnit.setLastFactor(factor);
                factor.doAction(this);
            }
        } else {
            Action action = mActionUnit.getAction();
            if (action != null) {
                // 执行action
                action.execute();
                reset();
            }
        }
    }

    @Override
    public void onContinue() {
        execute();
    }

    @Override
    public void onInterrupt() {
        // 中断时清理数据，避免内存泄漏
        reset();
    }
}
