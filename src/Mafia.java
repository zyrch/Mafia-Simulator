import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class Mafia extends Player {
    private boolean isActive;
    private int numberOfActiveMafias;
    private double extra;
    private double toremove;

    public double getToremove() {
        return toremove;
    }

    public void displayPrompt() {
        System.out.println("Choose A target");
    }

    public void chosen() {
        System.out.println("Mafias have chosen their Target");
    }

    public void performWithGroup(Player player, ArrayList<? extends Player> members) {
        int activeMembers = members.size();
        double sum = 0;
        for (Player mafia : members) {
            Mafia mafia1 = (Mafia) mafia;
            sum += mafia1.getHP();
        }
        sum = Math.min(sum, player.getHP());
        Collections.sort(members);
        for (Player mafia : members) {
            Mafia mafia1 = (Mafia) mafia;
            mafia1.setToremove(sum/activeMembers);
            sum -= mafia1.getToremove();
            mafia1.performOperation(player);
            if (mafia1.getExtra() > 0) {
                sum += mafia1.getExtra();
                mafia1.setExtra(0);
            }
            activeMembers--;
        }
    }

    public void setToremove(double toremove) {
        this.toremove = toremove;
    }

    Mafia(int ID) {
        super(ID);
        extra = 0;
        isActive = true;
        setHP(2500);
    }

    void decrementActiveMafia() {
        numberOfActiveMafias--;
    }

    public void setExtra(double extra) {
        this.extra = extra;
    }

    public double getExtra() {
        return extra;
    }

    void performOperation(Player player) {
        if (this.getHP() < toremove) {
            isActive = false;
            decrementActiveMafia();
            extra = toremove - this.getHP();
            toremove = this.getHP();
        }
        player.takeDamage(toremove);
        this.takeDamage(toremove);
    }

    @Override
    public boolean refreshAlive() {
        return true;
    }
    @Override
    public boolean isMafia() {
        return true;
    }

    public int compareTo(@NotNull Player other) {
        return Double.compare(getHP(), other.getHP());
    }

}
