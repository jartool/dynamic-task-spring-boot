package io.github.jartool.task.common;

/**
 * Constants
 *
 * @author jartool
 * @date 2021/10/19 09:53:34
 */
public class Constants {

    public interface View {
        public static final String REDIRECT = "redirect:";
        public static final String VIEW_TASK = "task/task.html";
    }

    public interface DateFormatter {
        public static final String DATA_YMD_H24MS = "yyyy-MM-dd HH:mm:ss";
        public static final String DATA_YMD_HH24MISS = "yyyy-mm-dd hh24:mi:ss";
        public static final String DATA_YMD_HH24MS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    }

    public interface Log {
        public static final String TASK_SETTING_INIT = "DSTaskSetting init";
        public static final String TASK_EXECUTOR_INIT = "DSTaskExecutor init";
        public static final String TASK_SETTING_DESTROY = "DSTaskSetting destroy";
        public static final String TASK_EXECUTOR_DESTROY = "DSTaskExecutor destroy";
        public static final String TASK_PARSE_START_LOG = "Task Start: {}";
        public static final String TASK_PARSE_END_LOG = "Task End: {} -> ClassName[{}] Method[{}] Parameters{}";
        public static final String TASK_UNSUPPORTED = "unsupported task configuration";
        public static final String TASK_TOO_MANY_EXECUTOR = "{} too many executor, taskId: {}";
        public static final String TASK_REGISTER = "Register task taskId: {}";
        public static final String TASK_RE_REGISTER = "Re-register task taskId: {}";
        public static final String TASK_CANCEL = "Cancel task, taskId: {}";
        public static final String TASK_SAVE_SUCCESS = "save task config success";
        public static final String TASK_NOT_SPRING_BEAN = "[{}] not spring bean";
    }

    public interface Rep {
        public static final String SUCCESS = "success";
        public static final String ERROR = "error";
        public static final String CODE = "code";
        public static final String MSG = "message";

        public static final String KEY = "key";
        public static final String TEXT = "text";
        public static final String MODE = "mode";
        public static final String SAVE_URL = "saveUrl";
        public static final String AUTH_ENABLE = "authEnable";
        public static final String AUTH_URL = "authUrl";
        public static final String AUTH_KEY = "authKey";
        public static final String SECRET = "secret";
    }

    public interface Code {
        public static final int YES = 1;
        public static final int NO = 0;
    }

    public interface Error {
        public static final String TASK_SAVE_ERROR = "error-save-task-setting";
        public static final String TASK_NOT_SUPPORTED = "not supported task";
        public static final String TASK_REFLECT = "error-task-reflect";
        public static final String TASK_SEMAPHORE_NULL = "{} semaphore is null, taskId: {}";
        public static final String TASK_EXECUTE_ERROR = "{} execute error, taskId: {}";
        public static final String TASK_INTERRUPTED_EXCEPTION = "{} interruptedException error, taskId: {}";
    }
}
