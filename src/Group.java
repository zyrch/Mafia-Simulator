import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Group <T extends Player> {

    private ArrayList<T> members;
    private ArrayList<Player> players;
    public ArrayList<T> getMembers() {
        return members;
    }

    private T dummy;
    Group (ArrayList<Player> players, ArrayList<T> members) {
        this.members = members;
        dummy = members.get(0);
        build(players);
    }

    public int getGroupSize() {
        return members.size();
    }

    private void build(@NotNull ArrayList<Player> players) {

        this.players = new ArrayList<>();
        for (Player player : players) {
            if ((dummy instanceof Healer) || player.getClass() != dummy.getClass()) {
                this.players.add(player);
            }
        }
    }

    public Player chooseAuto() {
        Random rand = new Random();
        int choice = rand.nextInt(players.size());
        dummy.chosen();
        return players.get(choice);
    }

    public Player choose() {
        Scanner in = new Scanner(System.in);
        dummy.displayPrompt();
        int choice = in.nextInt();
        for (Player player : players) {
            if (choice == player.getID()) {
                return player;
            }
        }
        System.out.println("Invalid Choice");
        return choose();
    }


    public void performOperation(Player player) {
        if (members.size() == 0) return;
        dummy.performWithGroup(player, members);
    }

    public void refresh() {
        ArrayList<Player> toRemove = new ArrayList<>();
        for (Player player : members) {
            if (!player.isAlive()) {
                toRemove.add(player);
            }
            if (player instanceof Detective) {
                ((Detective) player).refresh();
            }
        }
        for (Player player : toRemove) {
            members.remove((T)player);
        }
        toRemove = new ArrayList<>();
        for (Player player : players) {
            if (!player.isAlive()) {
                toRemove.add(player);
            }
        }
        for (Player player : toRemove) {
            players.remove(player);
        }
    }

}
