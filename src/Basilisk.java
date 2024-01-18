import java.util.Random;

public class Basilisk extends Monster{
    private int damage;
    Random rand = new Random();
    public Basilisk (int bLvl){super(bLvl);}
    public void shout(){System.out.println("Basilisk ahhhhh");}

    public int getDamage(){
        if (getLvl() < 10){
            damage = 1 + rand.nextInt(3);
        }
        if (getLvl() > 10){
            damage = 3 + rand.nextInt(8);
        }

        return damage;
    }
}

