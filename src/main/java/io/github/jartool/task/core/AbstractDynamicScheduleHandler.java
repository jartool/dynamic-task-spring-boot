package io.github.jartool.task.core;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.github.jartool.task.common.Constants;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * AbstractDynamicScheduleHandler
 *
 * @author jartool
 * @date 2021/10/19 10:02:10
 */
public abstract class AbstractDynamicScheduleHandler<T extends DynamicScheduleTask> implements SchedulingConfigurer {

    private static final Log log = LogFactory.get();

    /**
     * dsContainer
     */
    private DynamicScheduleContainer<T> dsContainer;

    /**
     * className
     */
    private final String CLASS_NAME = getClass().getSimpleName();

    /**
     * ExecutorService
     *
     * @return {@code ExecutorService}
     */
    protected abstract ExecutorService executor();

    /**
     * taskList
     *
     * @return {@code List<T>}
     */
    protected abstract List<T> taskList();

    /**
     * doProcess
     *
     * @param task task
     */
    protected abstract void doProcess(T task);

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        dsContainer = new DynamicScheduleContainer<>(taskRegistrar);
        taskRegistrar.addFixedDelayTask(this::scheduleTask, 1000);
    }

    /**
     * scheduleTask
     */
    private void scheduleTask() {
        CollUtil.emptyIfNull(taskList()).forEach(task ->
            dsContainer.checkTask(task, new TriggerTask(() ->
                    this.execute(task), triggerContext -> new CronTrigger(task.getCron()).nextExecutionTime(triggerContext)
            ))
        );
    }

    /**
     * execute
     *
     * @param task 任务
     */
    private void execute(final T task) {
        final long taskId = task.getId();
        try {
            Semaphore semaphore = dsContainer.getSemaphore(taskId);
            if (Objects.isNull(semaphore)) {
                log.error(Constants.Error.TASK_SEMAPHORE_NULL, CLASS_NAME, taskId);
                return;
            }
            if (semaphore.tryAcquire(3, TimeUnit.SECONDS)) {
                try {
                    executor().execute(() -> doProcess(task));
                } finally {
                    semaphore.release();
                }
            } else {
                log.warn(Constants.Log.TASK_TOO_MANY_EXECUTOR, CLASS_NAME, taskId);
            }
        } catch (InterruptedException e) {
            log.warn(Constants.Error.TASK_INTERRUPTED_EXCEPTION, CLASS_NAME, taskId);
        } catch (Exception e) {
            log.error(Constants.Error.TASK_EXECUTE_ERROR, CLASS_NAME, taskId, e);
        }
    }

}
