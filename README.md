# dynamic-task-spring-boot

### 一、简介

------------

	基于Spring Schedule实现的动态定时任务。
![2.png](https://i.loli.net/2021/10/26/i8BubDLtTFJkwey.png)
### 二、食用方式
- #### maven

------------

```xml
<!-- dynamic-task-spring-boot -->
<dependency>
    <groupId>io.github.jartool</groupId>
    <artifactId>dynamic-task-spring-boot</artifactId>
    <version>1.0.2</version>
</dependency>
<!-- thymeleaf -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```
- #### configuration

------------
```java
//启动类Add: @EnableDynamicTask注解即可
@EnableDynamicTask
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```
```yaml
#yml配置
jartool:
  task:
    view: /task/conf  #动态定时任务配置页面访问地址,默认: /task/conf
    conf-path: /usr/local/task/task.setting #setting配置文件路径,默认用户目录下:$User/task/setting
    pool:
      core-pool-size: 5  #核心线程数,默认:5
      max-pool-size: 10  #最大线程数,默认:10
      queue-capacity: 100  #队列容量,默认:100
      keep-alive-time: 1  #空闲线程等待超时时间(单位:分钟),默认:1分钟
      thread-factory-name-prefix: dynamic-task-executor-  #执行线程前缀,默认:dynamic-task-executor-
    auth:
      enable: true  #是否开启页面授权,默认: true
      url: /task/auth  #授权校验接口url,默认: /task/auth
      key: auth  #授权key,默认: auth
      username: admin  #用户名,默认: admin
      password: admin  #密码,默认: admin
```
```xml
#task.setting配置
DynamicTask: 定时任务name，不可重复
id：定时任务id，不可重复
cron：定时任务表达式
reference：定时任务Service方法全路径，支持传参。
	示例：io.github.jartool.task.DynamicTaskService@exec,param1,param2
	注意：long字符串传参需在首位拼接英文单引号：'
		示例：io.github.jartool.task.DynamicTaskService@updateById,'195254666521234797
isValid：定时任务是否有效，true有效，false失效
```
