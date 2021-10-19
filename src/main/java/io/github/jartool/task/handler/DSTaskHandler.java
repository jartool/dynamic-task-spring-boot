package io.github.jartool.task.handler;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.github.jartool.task.common.Constants;
import io.github.jartool.task.config.DynamicTaskConfig;
import io.github.jartool.task.core.AbstractDynamicScheduleHandler;
import io.github.jartool.task.entity.DSTask;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * DSTaskHandler
 *
 * @author jartool
 * @date 2021/10/19 10:07:26
 */
@Component
public class DSTaskHandler extends AbstractDynamicScheduleHandler<DSTask> implements ApplicationContextAware {

    private static final Log log = LogFactory.get();

    private ApplicationContext applicationContext;

    @Resource
    private DynamicTaskConfig dynamicTaskConfig;
    @Resource
    private ExecutorService executor;

    @Override
    protected ExecutorService executor() {
        return executor;
    }

    @Override
    protected List<DSTask> taskList() {
        return dynamicTaskConfig.dsTaskList();
    }

    @Override
    protected void doProcess(DSTask task) {
        log.info(Constants.Log.TASK_PARSE_START_LOG, LocalDateTimeUtil.format(LocalDateTimeUtil.now(), Constants.DateFormatter.DATA_YMD_H24MS));
        final String reference = task.getReference();
        final String[] classDefineArray = reference.split(StrPool.AT);
        if(classDefineArray.length != 2) {
            log.warn(Constants.Log.TASK_UNSUPPORTED);
            return;
        }
        String className = "";
        String methodName = "";
        String parameterStr = "[]";
        try {
            className = classDefineArray[0];
            final Class<?> clazz = Class.forName(className);
            final String[] methodDefineArray = classDefineArray[1].split(StrPool.COMMA);

            Object[] parameterArray = null;
            Class<?>[] parameterTypeArray = null;
            Method method = null;
            int len = methodDefineArray.length;
            if (len > 1) {
                parameterArray = new Object[len - 1];
                parameterTypeArray = new Class<?>[len - 1];
                for (int i = 1; i < len; i++) {
                    parameterArray[i - 1] = convertVal(methodDefineArray[i]);
                    parameterTypeArray[i - 1] = convertClass(methodDefineArray[i]);
                }
                method = ReflectUtil.getMethod(clazz, methodDefineArray[0], parameterTypeArray);
            } else {
                method = ReflectUtil.getMethod(clazz, methodDefineArray[0]);
            }
            Object obj = null;
            try {
                obj = applicationContext.getBean(clazz);
            } catch (Exception ex) {
                log.info(Constants.Log.TASK_NOT_SPRING_BEAN, className);
            }

            if (obj == null) {
                obj = ReflectUtil.newInstance(clazz);
            }
            methodName = method.getName();
            if (parameterArray != null) {
                parameterStr = Convert.toStr(parameterArray);
                ReflectUtil.invoke(obj, method, parameterArray);
            } else {
                ReflectUtil.invoke(obj, method);
            }
        } catch (Exception e) {
            log.error(Constants.Error.TASK_REFLECT, e);
        }
        log.info(Constants.Log.TASK_PARSE_END_LOG,
                LocalDateTimeUtil.format(LocalDateTimeUtil.now(), Constants.DateFormatter.DATA_YMD_H24MS)
                ,className, methodName, parameterStr);
    }

    public Object convertVal(String val) {
        if (val == null || "null".equals(val) || "NULL".equals(val)) {
            return null;
        } else if (NumberUtil.isInteger(val)) {
            return Integer.parseInt(val);
        } else if (NumberUtil.isDouble(val)) {
            return Double.parseDouble(val);
        } else if (NumberUtil.isLong(val)) {
            return Long.parseLong(val);
        }
        if (val.indexOf("'") == 0) {
            val = val.substring(1);
        }
        return val;
    }

    public Class<?> convertClass(String val) {
        if (NumberUtil.isInteger(val)) {
            return Integer.class;
        } else if (NumberUtil.isDouble(val)) {
            return Double.class;
        } else if (NumberUtil.isLong(val)) {
            return Long.class;
        }
        return String.class;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
