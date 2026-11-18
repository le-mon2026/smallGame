package bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class EnemyCharacter extends Character {

    private String skill;
    private boolean defending;

    //所有敌人共用一份嘲讽语；静态常量 + 复用的 Random，不必每次 new
    private static final ArrayList<String> TAUNTS = new ArrayList<>();
    private static final Random RANDOM = new Random();

    //添加嘲讽的话
    static {
        TAUNTS.add("sb");
        TAUNTS.add("lj");
        TAUNTS.add("fw");
        TAUNTS.add("gg");
    }

    public EnemyCharacter() {
        //默认技能不能为 null，否则 enemyTurn 里的 switch(action) 会空指针
        this("未知敌人", 100, 10, 0, "普通攻击");
    }

    public EnemyCharacter(String name, int hp, int attack, int defence, String skill) {
        super(name, hp, attack, defence);
        this.skill = skill;
    }

    //添加嘲讽的话
    public static void addTaunt(String str) {
        TAUNTS.add(str);
    }

    //随机取一句嘲讽；一条都没配置时返回 null，由调用方决定怎么处理
    public static String randomTaunt() {
        if (TAUNTS.isEmpty()) {
            return null;
        }
        return TAUNTS.get(RANDOM.nextInt(TAUNTS.size()));
    }

    public static List<String> getTaunt() {
        return Collections.unmodifiableList(TAUNTS);
    }


    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public boolean isDefending() {
        return defending;
    }

    public void setDefending(boolean defending) {
        this.defending = defending;
    }


    @Override
    public void takeDamage(int damage) {
        if(defending){
            damage = damage/2 > 1 ? damage/2 : 1;
            defending = false;
        }
        super.takeDamage(damage);
    }
}
