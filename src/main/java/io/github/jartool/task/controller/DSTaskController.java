package io.github.jartool.task.controller;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.github.jartool.task.common.Constants;
import io.github.jartool.task.entity.AuthEntity;
import io.github.jartool.task.entity.DataEntity;
import io.github.jartool.task.service.DSTaskService;
import io.github.jartool.task.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * DSTaskController
 * @author jartool
 * @version 1.0
 * @date 2021/8/24 15:05
 */
@Controller
public class DSTaskController {

    private static final Log log = LogFactory.get();
    
    @Resource
    private DSTaskService dsTaskService;

    @Value("${jartool.task.save-url:/task/save}")
    private String taskSaveUrl;
    @Value("${jartool.task.auth.enable:true}")
    private boolean authEnable;
    @Value("${jartool.task.auth.key:auth}")
    private String authKey;
    @Value("${jartool.task.auth.url:/task/auth}")
    private String authUrl;
    @Value("${jartool.task.auth.username:admin}")
    private String username;
    @Value("${jartool.task.auth.password:admin}")
    private String password;

    /**
     * readTaskConf
     */
    @GetMapping("${jartool.task.conf-url:/task/conf}")
    public String readTaskConf(Model model) {
        model.addAttribute(Constants.Rep.KEY, "task.setting");
        model.addAttribute(Constants.Rep.TEXT, dsTaskService.readTaskConf());
        model.addAttribute(Constants.Rep.MODE, "ini");
        model.addAttribute(Constants.Rep.SAVE_URL, taskSaveUrl);
        model.addAttribute(Constants.Rep.AUTH_KEY, authKey);
        model.addAttribute(Constants.Rep.AUTH_URL, authUrl);
        model.addAttribute(Constants.Rep.AUTH_ENABLE, authEnable);
        return Constants.View.VIEW_TASK;
    }

    /**
     * saveTaskConf
     * @param data data
     */
    @PostMapping("${jartool.task.save-url:/task/save}")
    @ResponseBody
    public JSONObject saveTaskConf(@RequestBody DataEntity data) {
        if (data == null) throw new IllegalArgumentException(Constants.Rep.ERROR);
        JSONObject json = new JSONObject();
        try {
            dsTaskService.saveTaskConf(data);
            json.set(Constants.Rep.CODE, Constants.Code.YES);
            json.set(Constants.Rep.MSG, Constants.Rep.SUCCESS);
        } catch (Exception e) {
            log.error(Constants.Error.TASK_SAVE_ERROR, e);
            json.set(Constants.Rep.CODE, Constants.Code.NO);
            json.set(Constants.Rep.MSG, Constants.Rep.ERROR);
        }
        return json;
    }

    @PostMapping("${jartool.task.auth.url:/task/auth}")
    @ResponseBody
    public JSONObject auth(@RequestBody AuthEntity authEntity) {
        JSONObject json = new JSONObject();
        json.set(Constants.Rep.CODE, -1);
        String secretKey = SecurityUtil.encrypt(username + password);
        if (CharSequenceUtil.isNotBlank(authEntity.getKey()) && authEntity.getKey().equals(secretKey)) {
            json.set(Constants.Rep.CODE, 0);
        } else if (username.equals(authEntity.getUsername()) && password.equals(authEntity.getPassword())){
            json.set(Constants.Rep.CODE, 0);
            json.set(Constants.Rep.SECRET, secretKey);
        }
        return json;
    }
}
