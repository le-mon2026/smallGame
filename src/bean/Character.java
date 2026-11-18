package bean;

public class Character {
    private String name;
    private int hp;
    private int maxHp;
    private int attack;
    private int defence;

    public Character() {
    }

    public Character(String name, int hp, int attack, int defence) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attack = attack;
        this.defence = defence;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    //1.判断是否存活
    public boolean isAlive() {
        return hp > 0;
    }

    //2.恢复生命
    public void heal(int amount) {
        hp += amount;
        if (hp > maxHp) {
            hp = maxHp;
        }
    }

    //3.受到伤害
    public void takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            hp = 0;
        }
    }

    //4.展示人物属性
    public void show() {
        System.out.println("\uD83C\uDF1F  属性: " + name + " HP：" + hp + "/" +maxHp + " ATK：" + attack + " DEF：" + defence);
    }
}
