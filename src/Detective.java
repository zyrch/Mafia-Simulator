import java.util.ArrayList;

public class Detective extends Player{

    private boolean identifiedMafia;
    private Player lastPersonChecked;

    Detective(int ID) {
        super(ID);
        setHP(800);
        identifiedMafia = false;
    }

    public void displayPrompt() {
        System.out.println("Choose A Person to Test");
    }

    public void chosen() {
        System.out.println("Detectives have chosen someone to Test");
    }

    public void performWithGroup(Player player, ArrayList<? extends Player> members) {
        for (Player member : members) {
            Detective member1 = (Detective) member;
            member1.performOperation(player);
        }
        this.performOperation(player);
        if (identifiedMafia) {
            System.out.println(player.getName() + " is a Mafia");
        }else {
            System.out.println(player.getName() + " is not a Mafia");
        }
    }

    public Player getLastPersonChecked() {
        return lastPersonChecked;
    }

    public void performOperation(Player player) {
        lastPersonChecked = player;
        if (player.isMafia()) {
            player.setIdentified(true);
            identifiedMafia = true;
        }
    }

    public boolean isIdentifiedMafia() {
        return identifiedMafia;
    }

    public void refresh() {
        identifiedMafia = false;
    }
}
