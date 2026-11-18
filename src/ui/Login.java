package ui;

import bean.User;
import util.Input;

import java.util.ArrayList;
import java.util.Random;

public class Login {

    //找回密码允许的最大失败次数（防爆破）
    private static final int MAX_FIND_PASSWORD_TRIES = 3;

    private final ArrayList<User> list = new ArrayList<User>();

    //登录界面
    public void start() {
        while (true) {
            System.out.println("文字格斗游戏开始了~");
            System.out.println("╔════════════════════════════════╗");
            System.out.println("    🎮 欢迎来到文字格斗游戏 🎮   ");
            System.out.println("╚════════════════════════════════╝");
            System.out.println("请选择操作：1登录 2注册 3找回密码 4退出");

            int a = Input.readInt();
            switch (a) {
                case 1 -> login();
                case 2 -> register();
                case 3 -> findPassword();
                case 4 -> {
                    System.out.println("用户选择了退出操作");
                    System.exit(0);
                }
                default -> {
                    System.out.println("请输入 1~4 之间的数字");
                }
            }
        }
    }

    //登录功能
    public void login() {

        //判断用户名是否存在。
        String name = Input.readLine("请输入用户名:");
        if (!contains(list, name)) {
            System.out.println("用户" + name + "不存在，请注册");
            //如果不存在返回注册
            return;
        }

        //判断用户是否被禁用
        int index = findIndex(list, name);
        if (!list.get(index).isState()) {
            System.out.println("用户" + list.get(index).getUsername() + "已经锁定，请联系黑马程序员官方客服：XXX-XXXXX");
            return;
        }

        String rightpassword = list.get(index).getPassword();
        for (int i = 0; i < 3; i++) {
            //判断密码
            String password = Input.readLine("请输入密码:");

            //判断验证码
            while (true) {
                String rightCode = getCode();
                System.out.println("正确的验证码为：" + rightCode);

                String code = Input.readLine("请输入验证码:");

                if (code.equalsIgnoreCase(rightCode)) {
                    System.out.println("验证码正确");
                    break;
                } else {
                    System.out.println("验证码错误，请重新输入");
                    continue;
                }
            }

            //验证密码
            if (password.equals(rightpassword)) {
                System.out.println("登录成功");
                list.get(index).setFindPwdFailCount(0); //正常登录成功，找回密码的失败次数也清零
                FightingGame fightingGame = new FightingGame();
                fightingGame.startGame(name);
                return;
            } else {
                if (i == 2) {
                    list.get(index).setState(false);
                    System.out.println("用户" + list.get(index).getUsername() + "已经锁定，请联系黑马程序员官方客服：XXX-XXXXX");
                    return;
                } else {
                    System.out.println("密码错误请重新输入！！！当前还剩" + (2 - i) + "次机会");
                }
            }
        }
    }

    //注册账号
    public void register() {
        User u = new User();
        //设置用户名
        while (true) {
            String name = Input.readLine("请输入用户名：");
            //判断用户名长度
            if (!checkLen(3, 16, name)) {
                System.out.println("用户名长度必须在3 ~ 16位");
                System.out.println("请重新输入:");
                continue;
            }
            //判断用户名只能由字母、数字组成，不能是纯数字
            if (!checkUserName(name)) {
                System.out.println("用户名只能由字母、数字组成，不能是纯数字");
                System.out.println("请重新输入:");
                continue;
            }
            //判断用户名是否唯一
            if (contains(list, name)) {
                System.out.println("用户名重复，请重新输入");
                continue;
            }
            //用户名符合要求
            u.setUsername(name);
            break;
        }
        //设置密码
        while (true) {
            String password = Input.readLine("请输入密码：");
            //判断密码长度
            if (!checkLen(3, 8, password)) {
                System.out.println("密码长度必须在3 ~ 8位");
                continue;
            }
            //判断密码只能是字母加数字的组合，不能有其他字符
            if (!checkUserPassword(password)) {
                System.out.println("密码只能是字母加数字的组合，不能有其他字符");
                continue;
            }

            //二次确认：不一致就退回第一步，两遍密码一起重输
            String passwordAgain = Input.readLine("请再次输入密码：");
            if (!password.equals(passwordAgain)) {
                System.out.println("两次输入的密码不一致，请重新设置密码！");
                continue;
            }

            u.setPassword(password);
            break;
        }
        //输入手机号
        while (true) {
            String phoneNumber = Input.readLine("请输入手机号，以便您忘记密码时找回：");
            if (!phoneNumber.matches("1[3-9]\\d{9}")) {
                System.out.println("手机号格式不正确，请重新输入");
                continue;
            }
            if (containsPhone(list, phoneNumber)) {
                System.out.println("该手机号已被注册，请重新输入");
                continue;
            }
            u.setPhonenumber(phoneNumber);
            break;
        }
        //将对象添加到 list 当中
        list.add(u);
        //提示成功
        System.out.println("账号" + u.getUsername() + "创建成功！");
    }

    //判断长度的 工具
    private static boolean checkLen(int minlen, int maxlen, String str) {
        return str != null && str.length() >= minlen && str.length() <= maxlen;
    }

    //获取信息的 工具
    private static int[] getInfo(String userInfo) {
        int charcount = 0;
        int numcount = 0;
        int othercount = 0;
        for (int i = 0; i < userInfo.length(); i++) {
            char c = userInfo.charAt(i);
            if (c >= '0' && c <= '9') numcount++;
            else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) charcount++;
            else othercount++;
        }
        return new int[]{charcount, numcount, othercount};
    }

    //判断用户名只能由字母、数字组成，不能是纯数字的 工具
    private static boolean checkUserName(String username) {
        int[] arr = getInfo(username);
        return arr[0] > 0 && arr[2] == 0;
    }

    //获取username的索引
    private static int findIndex(ArrayList<User> list, String username) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUsername().equals(username)) {
                return i;
            }
        }
        return -1;
    }

    //判断用户名是否唯一的 工具
    private static boolean contains(ArrayList<User> list, String username) {
        return findIndex(list, username) != -1;
    }

    //判断密码只能是字母加数字的组合，不能有其他字母的 工具
    private static boolean checkUserPassword(String password) {
        int[] arr = getInfo(password);
        return arr[0] > 0 && arr[1] > 0 && arr[2] == 0;
    }

    //获取验证码
    private String getCode() {
        ArrayList<Character> letters = new ArrayList<>();

        //把所有大小写字母放进一个容器
        for (int i = 0; i < 26; i++) {
            letters.add((char) (i + 'a'));
            letters.add((char) (i + 'A'));
        }

        //从容器里随机选出四个字母
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < 4; i++) {
            int index = r.nextInt(letters.size());
            sb.append(letters.get(index));
        }

        //在末尾添加一位数字
        sb.append(r.nextInt(10));

        //数字的位置是任意的
        //将sb变成字符串再用toCharArray()方法变成数组
        char[] arr = sb.toString().toCharArray();

        //将最大索引上的数字和一个索引进行交换
        int index = r.nextInt(arr.length);
        char temp = arr[index];
        arr[index] = arr[arr.length - 1];
        arr[arr.length - 1] = temp;

        //将数组变回字符串
        String code = new String(arr);

        //返回验证码
        return code;
    }

    //查询手机号是否唯一
    private static boolean containsPhone(ArrayList<User> list, String phone) {
        for (User u : list) {
            if (phone.equals(u.getPhonenumber())) {
                return true;
            }
        }
        return false;
    }

    //手机号找回密码
    public void findPassword() {
        String userName = Input.readLine("请输入用户名：");
        int index = findIndex(list, userName);
        if (index == -1) {
            System.out.println("用户" + userName + "不存在，请先注册");
            return;
        }
        User user = list.get(index);

        //防爆破：失败次数累计在账号上，重开菜单/重进流程都不会清零
        if (user.getFindPwdFailCount() >= MAX_FIND_PASSWORD_TRIES) {
            System.out.println("账号" + user.getUsername() + "找回密码失败次数过多，已锁定找回功能，请联系黑马程序员官方客服：XXX-XXXXX");
            return;
        }

        //手机号最多试 MAX_FIND_PASSWORD_TRIES 次，输错一次记一次；直接回车可取消
        while (true) {
            String phoneNumber = Input.readLine("请输入手机号（直接回车取消）：");
            if (phoneNumber.isEmpty()) {
                System.out.println("已取消找回密码");
                return;
            }
            if (phoneNumber.equals(user.getPhonenumber())) {
                break; //验证通过
            }
            int failCount = user.getFindPwdFailCount() + 1;
            user.setFindPwdFailCount(failCount);
            if (failCount >= MAX_FIND_PASSWORD_TRIES) {
                System.out.println("手机号累计输错" + MAX_FIND_PASSWORD_TRIES + "次，找回密码功能已锁定，请联系黑马程序员官方客服：XXX-XXXXX");
                return;
            }
            System.out.println("手机号错误，还可以尝试" + (MAX_FIND_PASSWORD_TRIES - failCount) + "次");
        }

        while (true) {
            String password = Input.readLine("请输入新密码：");
            if (!checkLen(3, 8, password)) {
                System.out.println("密码长度必须在3 ~ 8位");
                continue;
            }
            if (!checkUserPassword(password)) {
                System.out.println("密码只能是字母加数字的组合，不能有其他字符");
                continue;
            }
            String passwordAgain = Input.readLine("请再次输入新密码：");
            if (!password.equals(passwordAgain)) {
                System.out.println("两次输入的密码不一致，请重新设置密码！");
                continue;
            }
            boolean wasLocked = !user.isState();
            user.setPassword(password);
            user.setFindPwdFailCount(0);  //找回成功，失败次数清零
            user.setState(true);          //手机号已验证身份，顺带解除登录锁定
            System.out.println("密码重置成功" + (wasLocked ? "，账号已同时解锁" : "") + "，请使用新密码登录");
            return;
        }
    }
}