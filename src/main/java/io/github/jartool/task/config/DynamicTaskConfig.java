package io.github.jartool.task.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.setting.GroupedMap;
import cn.hutool.setting.Setting;
import cn.hutool.system.SystemUtil;
import io.github.jartool.task.common.Constants;
import io.github.jartool.task.entity.DSTask;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * DSTaskConfig
 *
 * @author jartool
 * @date 2021/10/19 10:14:21
 */
@ComponentScan("io.github.jartool.task.**")
@EnableScheduling
@Configuration
public class DynamicTaskConfig implements DisposableBean {

    private static final Log log = LogFactory.get();

    /**
     * setting
     */
    private Setting setting;
    /**
     * executorService
     */
    private ExecutorService executorService;

    @Value("${jartool.task.conf-path:}")
    private String taskSettingPath;
    @Value("${jartool.task.pool.core-pool-size:5}")
    private int corePoolSize;
    @Value("${jartool.task.pool.max-pool-size:10}")
    private int maxPoolSize;
    @Value("${jartool.task.pool.queue-capacity:100}")
    private int queueCapacity;
    @Value("${jartool.task.pool.keep-alive-time:1}")
    private long keepAliveTime;
    @Value("${jartool.task.pool.thread-factory-name-prefix:dynamic-task-executor-}")
    private String threadFactoryNamePrefix;

    @Bean
    public Setting dynamicTaskSetting() {
        String settingPath = taskSettingPath();
        File file = FileUtil.touch(settingPath);
        this.setting = new Setting(file, CharsetUtil.CHARSET_UTF_8, false);
        if (FileUtil.isEmpty(FileUtil.file(settingPath))) {
            Setting defSetting = new Setting("task/task.setting");
            this.setting.addSetting(defSetting);
            this.setting.store();
        }
        this.setting.autoLoad(true);
        log.info(Constants.Log.TASK_SETTING_INIT);
        return this.setting;
    }

    @Bean
    public ExecutorService executor() {
        this.executorService = ExecutorBuilder.create()
                .setCorePoolSize(corePoolSize)
                .setMaxPoolSize(maxPoolSize)
                .setWorkQueue(new LinkedBlockingDeque<>(queueCapacity))
                .setKeepAliveTime(keepAliveTime, TimeUnit.MINUTES)
                .setHandler(new ThreadPoolExecutor.AbortPolicy())
                .setThreadFactory(ThreadFactoryBuilder.create().setNamePrefix(threadFactoryNamePrefix).build())
                .build();
        log.info(Constants.Log.TASK_EXECUTOR_INIT);
        return this.executorService;
    }

    @Override
    public void destroy() throws Exception {
        if (setting != null) {
            setting.autoLoad(false);
            log.info(Constants.Log.TASK_SETTING_DESTROY);
        }
        if (executorService != null) {
            executorService.shutdown();
            log.info(Constants.Log.TASK_EXECUTOR_DESTROY);
        }
    }

    public List<DSTask> dsTaskList() {
        List<DSTask> taskList = new ArrayList<>();
        GroupedMap groupedMap = setting.getGroupedMap();
        Iterator<Map.Entry<String, LinkedHashMap<String, String>>> iterator = groupedMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, LinkedHashMap<String, String>> entry = iterator.next();
            LinkedHashMap<String, String> map = entry.getValue();
            DSTask dsTask = new DSTask();
            dsTask.setName(entry.getKey());
            dsTask.setId(Long.parseLong(map.get("id")));
            dsTask.setCron(map.get("cron"));
            dsTask.setReference(map.get("reference"));
            dsTask.setValid(Boolean.parseBoolean(map.get("isValid")));
            taskList.add(dsTask);
        }
        return taskList;
    }

    public String taskSettingPath() {
        if (CharSequenceUtil.isBlank(taskSettingPath)) {
            taskSettingPath = SystemUtil.getUserInfo().getHomeDir() + "task" + File.separator + "task.setting";
        }
        return taskSettingPath;
    }
}
