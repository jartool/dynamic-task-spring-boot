package io.github.jartool.task.util;

import cn.hutool.crypto.SecureUtil;

/**
 * SecurityUtil
 *
 * @author jartool
 * @date 2021/10/18 17:07:03
 */
public class SecurityUtil {

    public static String encrypt(String text) {
        StringBuilder sb = new StringBuilder();
        text.chars().forEach(c -> sb.append(SecureUtil.md5(String.valueOf(c))));
        return SecureUtil.md5(sb.toString());
    }
}
