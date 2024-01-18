import java.util.Random;

public class Witcher extends Monster{
    private int damage;
    Random rand = new Random();
    public Witcher (int wLvl){super(wLvl);}
    public void shout(){System.out.println("Witcher arghhh");}

    public int getDamage(){
        if (getLvl() < 10){
            damage = 1 + rand.nextInt(10);
        }
        if (getLvl() > 10){
            damage = 10 + rand.nextInt(20);
        }

        return damage;
    }
}
