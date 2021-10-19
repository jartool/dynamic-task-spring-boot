package io.github.jartool.task.annotation;

import io.github.jartool.task.config.DynamicTaskConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * EnableDynamicTask
 *
 * @author jartool
 * @date 2021/10/19 10:36:15
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(DynamicTaskConfig.class)
@Documented
public @interface EnableDynamicTask {

}
