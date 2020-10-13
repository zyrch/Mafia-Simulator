import java.util.ArrayList;

public class Healer extends Player{

    Healer(int ID) {
        super(ID);
        setHP(800);
    }

    public void displayPrompt() {
        System.out.println("Choose A Person to Heal");
    }

    public void chosen() {
        System.out.println("Healers have chosen someone to Heal");
    }

    public void performWithGroup(Player player, ArrayList<? extends Player> members) {
        player.heal();
    }

}
