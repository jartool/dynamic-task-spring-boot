package io.github.jartool.task.entity;

/**
 * AuthEntity
 *
 * @author jartool
 * @date 2021/10/18 17:00:26
 */
public class AuthEntity {

    private String key;
    private String username;
    private String password;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
