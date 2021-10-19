package io.github.jartool.task.core;

/**
 * DynamicScheduleTask
 *
 * @author jartool
 * @date 2021/10/19 10:04:27
 */
public interface DynamicScheduleTask {

    /**
     * 任务ID
     */
    long getId();

    /**
     * 任务cron表达式
     */
    String getCron();

    /**
     * 任务是否有效
     */
    boolean isValid();

    /**
     * 任务是否改变
     */
    boolean isChange(DynamicScheduleTask task);
}
