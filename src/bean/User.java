package bean;

import java.util.Random;

public class User {
    private final String id;
    private String username;
    private String password;
    private boolean state;
    private String phonenumber;
    private int findPwdFailCount = 3;   //找回密码连续失败次数（防爆破用）

    public int getFindPwdFailCount() {
        return findPwdFailCount;
    }

    public void setFindPwdFailCount(int findPwdFailCount) {
        this.findPwdFailCount = findPwdFailCount;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    private static String generateId() {
        StringBuilder sb = new StringBuilder("itheima");
        Random r = new Random();
        for (int i = 0; i < 5; i++) {
            sb.append(r.nextInt(10));
        }
        return sb.toString();
    }


    public User() {
        id = generateId();
        state = true;
    }
    public User(String username, String password) {
        id = generateId();
        this.username = username;
        this.password = password;
        state = true;
    }


    public String getId() {
        return id;
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

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

}