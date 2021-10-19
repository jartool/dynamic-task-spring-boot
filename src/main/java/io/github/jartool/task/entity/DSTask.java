package io.github.jartool.task.entity;

import io.github.jartool.task.common.Constants;
import io.github.jartool.task.core.DynamicScheduleTask;

/**
 * DSTask
 *
 * @author jie.li
 * @date 2021/10/19 17:05:08
 */
public class DSTask implements DynamicScheduleTask {

    /**
     * name
     */
    private String name;

    /**
     * id
     */
    private long id;

    /**
     * cron
     */
    private String cron;

    /**
     * reference
     */
    private String reference;

    /**
     * isValid
     */
    private boolean isValid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    @Override
    public boolean isChange(DynamicScheduleTask task) {
        if(task instanceof DSTask) {
            final DSTask currentTask = (DSTask) task;
            return !this.cron.equals(currentTask.cron) || this.isValid != currentTask.isValid || !this.reference.equals(currentTask.getReference());
        } else {
            throw new IllegalArgumentException(Constants.Error.TASK_NOT_SUPPORTED);
        }
    }
}
