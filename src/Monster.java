public abstract class Monster {
    int health = 100;
    private final int lvl;
    static int numberOfMonsters = 0;

    public Monster(int monsterLvl){
    lvl = monsterLvl;
    numberOfMonsters++;
    }

    public int getLvl(){return lvl;}
    public void setMonsterHealth(Integer h){this.health = h;}
    public int getMonsterHealth(){return health;}

    public abstract void shout();
    public abstract int getDamage();

}
