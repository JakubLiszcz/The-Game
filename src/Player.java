import java.io.*;
import java.util.ArrayList;
public class Player implements Serializable {
    ArrayList<String> equipment = new ArrayList<>();
    private String name;
    private int health = 100;
    private int lvl = 1;
    private String classP;
    private int attack;
    private int defense;
    private int money = 10;
    private int exp = 0;


    public void setExp(int expP){
        this.exp += expP;
        if (exp - 100 > 0){lvl++;}
    }
    public int getLvl(){return lvl;}
    public void setMoney(int moneyP){this.money = moneyP;}
    public int getMoney(){return money;}
    public void setName(String nameP){this.name = nameP;}
    public String getName(){return name;}
    public void setClass(String classP){this.classP = classP;}
    public String getClassP(){return classP;}
    public void setHealth(int healthP){this.health = healthP;}
    public int getHealth (){return health;}
    public int getAttack() {return attack;}
    public int getDefense() {return defense;}

    public void  setEquipment (Weapon sword){

        if (sword.getWeaponName().equals("Normal sword")){
            equipment.add("Normal sword");
            attack += sword.getWeaponAttack();
            defense += sword.getWeaponDefense();
            health += sword.getWeaponDefense();
            money -=2;
        }
        if (sword.getWeaponName().equals("Dark sword")) {
            equipment.add("Dark sword");
            attack += sword.getWeaponAttack();
            defense += sword.getWeaponDefense();
            health += sword.getWeaponDefense();
            money -= 5;
        }
    }
}