package ui;

import bean.EnemyCharacter;
import bean.MyCharacter;
import util.Input;

import java.util.ArrayList;
import java.util.Random;

public class FightingGame {

    //启动游戏
    public void startGame(String name) {
        //标题
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("       \uD83C\uDFAE " + name + "欢迎来到文字格斗游戏 \uD83C\uDFAE");
        System.out.println("╚════════════════════════════════════════╝");

        //创建角色并分配属性
        MyCharacter myCharacter = createPlayerCharacter(name);
        System.out.println("角色创建成功!");

        //展示属性与技能
        myCharacter.show();
        myCharacter.showSkill();

        //创建敌人列表
        ArrayList<EnemyCharacter> enemyCharacterArrayList = new ArrayList<>();
        enemyCharacterArrayList.add(new EnemyCharacter("初级战士", 80, 15, 10, "猛击"));
        enemyCharacterArrayList.add(new EnemyCharacter("敏捷刺客", 60, 20, 5, "快速攻击"));
        enemyCharacterArrayList.add(new EnemyCharacter("重装坦克", 120, 10, 20, "防御姿态"));
        enemyCharacterArrayList.add(new EnemyCharacter("神秘法师", 70, 25, 8, "火球术"));


        int count = 1; //记录战斗了几场
        int winCount = 0; //记录胜场数

        //战斗
        while (myCharacter.isAlive()) {

            //重置敌人的属性，敌人属性每场HP+10, ATK+3, DEF+2
            if (winCount != 0) {
                for (int i = 0; i < enemyCharacterArrayList.size(); i++) {
                    EnemyCharacter c = enemyCharacterArrayList.get(i);
                    c.setMaxHp(c.getMaxHp() + 10);
                    c.setHp(c.getMaxHp());
                    c.setAttack(c.getAttack() + 3);
                    c.setDefence(c.getDefence() + 2);
                }
            }

            //随机选择敌人
            Random random = new Random();
            int index = random.nextInt(enemyCharacterArrayList.size());
            EnemyCharacter enemy = enemyCharacterArrayList.get(index);

            //战斗开始
            System.out.println("═══════════════════════════════════════");
            System.out.println("⚔\uFE0F 第" + count + "场战斗开始！对手:" + enemyCharacterArrayList.get(index).getName());

            int round = 1; //记录回合数
            //和单个敌人战斗
            while (true) {
                System.out.println("---------------------------------------");
                System.out.println("⚔\uFE0F 第" + round + "回合开始!");
                System.out.println(getHealthbar(myCharacter.getName(), myCharacter.getHp(), myCharacter.getMaxHp()));
                System.out.println(getManaBar(myCharacter.getName(), myCharacter.getMp(), myCharacter.getMaxMP()));
                System.out.println(getHealthbar(enemy.getName(), enemy.getHp(), enemy.getMaxHp()));

                //玩家回合
                playerTurn(myCharacter, enemy);
                if (!enemy.isAlive()) {
                    System.out.println("\uD83C\uDF89 你击败了 " + enemy.getName() + "!");
                    winCount++;

                    //每场战斗胜利回复20 -40点生命（实际回复量会被最大生命值截断）
                    if (myCharacter.isAlive()) {
                        int hpBeforeHeal = myCharacter.getHp();
                        int healHP = random.nextInt(20, 41);
                        myCharacter.heal(healHP);
                        int actualHeal = myCharacter.getHp() - hpBeforeHeal;
                        System.out.println("\uD83D\uDC9A 战斗结束！你恢复了 " + actualHeal + "点生命值");

                        System.out.println("\uD83C\uDFC6 当前胜场: " + winCount);
                        System.out.println("═══════════════════════════════════════");
                    }

                    //每场战斗胜利回复最大蓝量的百分之三十
                    if (myCharacter.isAlive()) {
                        int mpBeforeRestore = myCharacter.getMp();
                        int restore = (int) (myCharacter.getMaxMP() * 0.3);
                        myCharacter.setMp(mpBeforeRestore + restore);   //setMp 里已经封顶，不用再判断
                        int actualRestore = myCharacter.getMp() - mpBeforeRestore;
                        System.out.println("\uD83D\uDD35 战斗结束！你恢复了 " + actualRestore + "点蓝量");
                    }

                    break;
                }

                //敌人回合
                enemyTurn(enemy, myCharacter);
                if (!myCharacter.isAlive()) {
                    String taunt = EnemyCharacter.randomTaunt();   //列表为空时返回 null
                    if (taunt != null) {
                        System.out.println("来自对面的嘲讽：" + taunt);
                    }
                    System.out.println("💀 你被击败了");
                    break;
                }
                round++;
            }

            //每胜三场提升属性
            if (myCharacter.isAlive() && winCount > 0 && winCount % 3 == 0) {
                System.out.println("\uD83C\uDFC6 恭喜你获得了属性提升！");

                myCharacter.setMaxHp(myCharacter.getMaxHp() + 30);
                myCharacter.setAttack(myCharacter.getAttack() + 5);
                myCharacter.setDefence(myCharacter.getDefence() + 3);

                System.out.println("最大生命值 + 30，攻击力 + 5，防御力 + 3");
                System.out.print("当前属性： ");
                myCharacter.show();
            }

            //询问玩家是否继续
            if (myCharacter.isAlive()) {
                String choose = Input.readLine("继续下一场战斗？(y/n):");
                if (choose.equalsIgnoreCase("y")) {
                    count++;
                    continue;
                } else if (choose.equalsIgnoreCase("n")) {
                    break;
                } else {
                    System.out.println("没有这个选项，默认游戏继续");
                    count++;
                    continue;
                }
            }
        }

        //最终结算
        System.out.println("======================================");
        System.out.println("游戏结束！");
        System.out.println("总胜场：" + winCount);
        System.out.println("感谢游玩！");

        return;
    }

    //玩家回合
    public void playerTurn(MyCharacter myCharacter, EnemyCharacter enemy) {
        ArrayList<String> skills = myCharacter.getSkillList();

        System.out.println("===== 你的回合 =====");
        if (skills.isEmpty()) {
            System.out.println("你没有可用技能，默认使用普通攻击");
            normalAttack(myCharacter, enemy);
            return;
        }
        //菜单按技能列表动态生成，不再硬编码 get(0)/get(1)/get(2)
        for (int i = 0; i < skills.size(); i++) {
            String skill = skills.get(i);
            System.out.println((i + 1) + ". " + skill + getSkillCostHint(skill));
        }
        System.out.print("选择行动 (1-" + skills.size() + "): ");

        //把序号翻译成技能名，非数字或越界都按普通攻击处理
        String skill = chooseSkill(Input.readLine(), skills);
        if (skill == null) {
            System.out.println("没有这个操作，默认选择普通攻击");
            skill = "普通攻击";
        }

        switch (skill) {
            case "强力一击" -> strongAttack(myCharacter, enemy);
            case "生命汲取" -> lifeDrain(myCharacter);
            default -> normalAttack(myCharacter, enemy);
        }
    }

    //把玩家输入的数字翻译成技能名，非法输入返回 null
    private String chooseSkill(String choose, ArrayList<String> skills) {
        try {
            int index = Integer.parseInt(choose);
            if (index >= 1 && index <= skills.size()) {
                return skills.get(index - 1);
            }
        } catch (NumberFormatException ignored) {
            //落到下面统一返回 null
        }
        return null;
    }

    //菜单里显示的技能消耗
    private String getSkillCostHint(String skill) {
        return switch (skill) {
            case "强力一击" -> " (消耗10MP)";
            case "生命汲取" -> " (消耗10MP，恢复生命)";
            default -> "";
        };
    }

    //普通攻击
    private void normalAttack(MyCharacter myCharacter, EnemyCharacter enemy) {
        int damage = calculateDamage(myCharacter.getAttack(), enemy.getDefence());
        System.out.println("⚔\uFE0F 你对 " + enemy.getName() + "使用了普通攻击，造成了" + damage + "点伤害！");
        enemy.takeDamage(damage);
    }

    //强力一击：消耗10MP
    private void strongAttack(MyCharacter myCharacter, EnemyCharacter enemy) {
        if (!myCharacter.consumeMP(10)) {
            System.out.println("蓝量不足十点，无法使用强力一击");
            return;
        }
        int damage = calculateDamage((int) (myCharacter.getAttack() * 1.8), enemy.getDefence());
        System.out.println("\uD83D\uDCA5 消耗10MP, 你对 " + enemy.getName() + "使用了强力一击，造成了" + damage + "点伤害！");
        enemy.takeDamage(damage);
    }

    //生命汲取：消耗10MP，恢复生命
    private void lifeDrain(MyCharacter myCharacter) {
        if (!myCharacter.consumeMP(10)) {
            System.out.println("蓝量不足十点，无法使用生命汲取");
            return;
        }
        int hpBeforeHeal = myCharacter.getHp();
        int health = new Random().nextInt(21);
        myCharacter.heal(health);
        int actualHeal = myCharacter.getHp() - hpBeforeHeal;
        System.out.println("\uD83D\uDC9A 消耗10MP, 你使用了生命汲取，回复了" + actualHeal + "点生命值");
    }

    //敌人回合
    public void enemyTurn(EnemyCharacter enemy, MyCharacter myCharacter) {
        System.out.println("===== " + enemy.getName() + "的回合 =====");
        Random random = new Random();
        int num = random.nextInt(10);
        String action = "普通攻击";

        if (num >= 5) {
            action = enemy.getSkill();
        }

        switch (action) {
            case "普通攻击":
                int damage1 = calculateDamage(enemy.getAttack(), myCharacter.getDefence());
                myCharacter.takeDamage(damage1);
                System.out.println("⚔\uFE0F " + enemy.getName() + "对你使用了普通攻击，造成" + damage1 + "点伤害！");
                break;
            case "猛击":
                int damage2 = calculateDamage((int) (enemy.getAttack() * 1.5), myCharacter.getDefence());
                myCharacter.takeDamage(damage2);
                System.out.println("⚔\uFE0F " + enemy.getName() + "对你使用了猛击，造成" + damage2 + "点伤害！");
                break;
            case "快速攻击":
                int damage3 = 0;
                for (int i = 0; i < 2; i++) {
                    int temp = calculateDamage(enemy.getAttack() / 2, myCharacter.getDefence());
                    damage3 += temp;
                }
                myCharacter.takeDamage(damage3);
                System.out.println("⚔\uFE0F " + enemy.getName() + "对你使用了快速攻击，造成" + damage3 + "点伤害！");
                break;
            case "防御姿态":
                enemy.setDefending(true);
                System.out.println("\uD83D\uDEE1\uFE0F " + enemy.getName() + "摆出了防御姿态");
                break;
            case "火球术":
                int damage4 = calculateDamage((int) (enemy.getAttack() * 1.8), myCharacter.getDefence());
                myCharacter.takeDamage(damage4);
                System.out.println("⚔\uFE0F " + enemy.getName() + "对你使用了火球术，造成" + damage4 + "点伤害！");
                break;
        }
    }

    //计算双方战斗时造成的伤害
    public int calculateDamage(int attack, int defence) {
        return attack - defence >= 1 ? attack - defence : 1;
    }

    //获取当前血量
    public String getHealthbar(String name, int hp, int maxHp) {
        int barLength = 20;
        int filled = (int) (1.0 * hp / maxHp * 20);
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(": [");
        for (int i = 0; i < barLength; i++) {
            if (i < filled) {
                sb.append('█');
            } else {
                sb.append(' ');
            }

        }
        sb.append("] ").append(hp + "/" + maxHp + "HP");
        return sb.toString();
    }

    //获取当前蓝量条
    public String getManaBar(String name, int mp, int maxMp) {
        int barLength = 20;
        int filled = (int) (1.0 * mp / maxMp * barLength);
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(": [");
        for (int i = 0; i < barLength; i++) {
            if (i < filled) {
                sb.append('█');
            } else {
                sb.append(' ');
            }
        }
        sb.append("] ").append(mp).append("/").append(maxMp).append("MP");
        return sb.toString();
    }

    //创建游戏角色
    public MyCharacter createPlayerCharacter(String name) {
        System.out.println("创建您的角色：");
        System.out.println("您的角色名为：" + name);

        int points = 20;
        System.out.println("请分配属性点 (共20点):");
        System.out.println("1. 生命值 (每点+10 HP)");
        System.out.println("2. 攻击力 (每点+2 ATK)");
        System.out.println("3. 防御力 (每点+1 DEF)");
        System.out.println("4. 蓝量 (每点+10 MP)");

        String[] attributes = {"生命值", "攻击力", "防御力", "蓝量"};
        int[] values = new int[attributes.length];

        //用循环记录输入的属性值
        for (int i = 0; i < attributes.length; i++) {
            System.out.print("分配点数到" + attributes[i] + "（剩余点数:" + points + "):");
            int num = Input.readInt();
            if (num < 0) {
                System.out.println("无效输入！默认分配0点");
                num = 0;
            }
            if (num > points) {
                System.out.println("属性点不足，所有属性点分配到:" + attributes[i]);
                num = points;
            }
            values[i] = num;
            points -= values[i];
        }

        //添加角色属性值
        MyCharacter mycharacter = new MyCharacter(
                name,//名字
                100 + (values[0] * 10),//生命值
                10 + (values[1] * 2),//攻击力
                values[2],//防御力
                100 + (values[3] * 10)//蓝量
        );

        //创建角色技能
        mycharacter.getSkillList().add("普通攻击");
        mycharacter.getSkillList().add("强力一击");
        mycharacter.getSkillList().add("生命汲取");

        return mycharacter;
    }
}