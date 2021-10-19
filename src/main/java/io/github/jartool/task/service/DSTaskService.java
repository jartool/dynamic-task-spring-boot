package io.github.jartool.task.service;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.github.jartool.task.common.Constants;
import io.github.jartool.task.config.DynamicTaskConfig;
import io.github.jartool.task.entity.DataEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * DSTaskService
 *
 * @author jartool
 * @date 2021/10/19 10:16:17
 */
@Service
public class DSTaskService {

    private static final Log log = LogFactory.get();

    @Resource
    private DynamicTaskConfig dynamicTaskConfig;

    public String readTaskConf() {
        FileReader reader = new FileReader(dynamicTaskConfig.taskSettingPath());
        return reader.readString();
    }

    public void saveTaskConf(DataEntity data) {
        FileWriter writer = new FileWriter(dynamicTaskConfig.taskSettingPath());
        writer.write(data.getData());
        log.info(Constants.Log.TASK_SAVE_SUCCESS);
    }
}
