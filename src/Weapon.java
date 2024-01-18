public class Weapon {
    private final String weaponName;
    private final int weaponAttack;
    private final int weaponDefense;
    private final int weaponCost;

    public Weapon (String name, int attack, int defense, int cost){
        this.weaponName = name;
        this.weaponAttack = attack;
        this.weaponDefense = defense;
        this.weaponCost = cost;
    }

    public String getWeaponName(){return weaponName;}
    public int getWeaponAttack(){return weaponAttack;}
    public int getWeaponDefense(){return weaponDefense;}
    public int getWeaponCost(){return weaponCost;}

    public String toString(){return weaponName;}
}
