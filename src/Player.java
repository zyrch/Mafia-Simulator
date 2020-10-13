import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

abstract public class Player implements Comparable<Player>{
    private String name;
    private int ID;
    private double HP;
    private Random rand;
    private boolean isAlive;
    private Scanner in;
    private boolean identified = false;

    public boolean isIdentified() {
        return identified;
    }

    public void setIdentified(boolean identified) {
        this.identified = identified;
    }

    Player(int ID) {
        in = new Scanner(System.in);
        isAlive = true;
        rand = new Random();
        this.ID = ID;
        name = "Player" + ID;
    }

    public int getID() {
        return ID;
    }

    public void takeDamage(double value) {
        HP -= value;
        HP = Math.max(HP, 0);
    }

    public double getHP() {
        return HP;
    }

    public int vote() {
        System.out.println("Select a person to Vote");
        int choice = in.nextInt();
        return choice - 1;
    }
    public int autovote(int N) {
        return rand.nextInt(N);
    }

    public String getName() {
        return name;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public boolean refreshAlive() {
        return getHP() > 0;
    }
    public void heal() {
        HP += 500;
    }
    public boolean isMafia() {
        return false;
    }

    public void kill() {
        isAlive = false;
    }

    abstract public void displayPrompt();
    abstract public void chosen();
    abstract public void performWithGroup(Player player, ArrayList<? extends Player> members);

    public int compareTo(@NotNull Player other) {
        return Double.compare(other.getHP(), getHP());
    }
}
