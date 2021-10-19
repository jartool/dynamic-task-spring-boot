package io.github.jartool.task.core;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.github.jartool.task.common.Constants;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * DynamicScheduleContainer
 *
 * @author jartool
 * @date 2021/10/19 17:04:18
 */
public class DynamicScheduleContainer<T extends DynamicScheduleTask> {

    private static final Log log = LogFactory.get();

    private final Map<Long, Pair<T, Pair<ScheduledTask, Semaphore>>> scheduleMap = new ConcurrentHashMap<>();
    private final ScheduledTaskRegistrar taskRegistrar;

    public DynamicScheduleContainer(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        this.taskRegistrar = scheduledTaskRegistrar;
    }

    public void checkTask(final T task, final TriggerTask triggerTask) {
        final long taskId = task.getId();
        if (scheduleMap.containsKey(taskId)) {
            if (task.isValid()) {
                final T oldTask = scheduleMap.get(taskId).getLeft();
                if(oldTask.isChange(task)) {
                    log.info(Constants.Log.TASK_RE_REGISTER, taskId);
                    cancelTask(taskId);
                    registerTask(task, triggerTask);
                }
            } else {
                log.info(Constants.Log.TASK_CANCEL, taskId);
                cancelTask(taskId);
            }
        } else {
            if (task.isValid()) {
                log.info(Constants.Log.TASK_REGISTER, taskId);
                registerTask(task, triggerTask);
            }
        }
    }

    public Semaphore getSemaphore(final long taskId) {
        return this.scheduleMap.get(taskId).getRight().getRight();
    }

    private void registerTask(final T task, final TriggerTask triggerTask) {
        final ScheduledTask latestTask = taskRegistrar.scheduleTriggerTask(triggerTask);
        this.scheduleMap.put(task.getId(), Pair.of(task, Pair.of(latestTask, new Semaphore(1))));
    }

    private void cancelTask(final long taskId) {
        final Pair<T, Pair<ScheduledTask, Semaphore>> pair = this.scheduleMap.remove(taskId);
        if (pair != null) {
            pair.getRight().getLeft().cancel();
        }
    }
}
