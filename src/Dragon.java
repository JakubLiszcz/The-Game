import java.util.Random;

public class Dragon extends Monster{
    private int damage;
    Random rand = new Random();
    public Dragon (int dLvl){super(dLvl);}
    public void shout(){System.out.println("Dragon arghhh");}

    public int getDamage(){
        if (getLvl() < 10){
            damage = 1 + rand.nextInt(5);
        }
        if (getLvl() > 10){
            damage = 5 + rand.nextInt(10);
        }

        return damage;
    }
}
