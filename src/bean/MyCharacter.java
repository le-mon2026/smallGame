package bean;

import java.util.ArrayList;

public class MyCharacter extends Character {

    private ArrayList<String> skillList ;
    private int mp;
    private int maxMP;


    public MyCharacter() {
        //给个安全的默认值，避免造出 name=null、maxHp=0 的半成品对象
        this("无名角色", 100, 10, 0,100);
    }

    public MyCharacter(String name, int hp, int attack, int defence, int mp) {
        super(name, hp, attack, defence);
        this.mp = mp;
        this.maxMP = mp;
        skillList = new ArrayList<>();
    }


    public int getMaxMP() {
        return maxMP;
    }

    public void setMaxMP(int maxMP) {
        this.maxMP = maxMP;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        //蓝量永远限制在 [0, maxMP] 之间
        this.mp = Math.min(Math.max(mp, 0), maxMP);
    }

    public ArrayList<String> getSkillList() {
        return skillList;
    }

    public void setSkillList(ArrayList<String> skillList) {
        this.skillList = skillList;
    }

    //展示技能
    public void showSkill(){
        for (int i = 0; i < skillList.size(); i++) {
            System.out.println("\uD83C\uDF1F  拥有技能:" + skillList.get(i) + " ");
        }
    }

    //展示人物属性：父类显示 HP/ATK/DEF，这里补上蓝量
    @Override
    public void show() {
        super.show();
        System.out.println("\uD83D\uDD35  蓝量: " + mp + "/" + maxMP + " MP");
    }

    //消耗蓝量：蓝量足够才扣，扣成功返回 true；不够返回 false，一点不扣
    public boolean consumeMP(int amount) {
        if (amount <= 0) {
            return true;
        }
        if (mp < amount) {
            return false;
        }
        mp -= amount;
        return true;
    }

    
}
