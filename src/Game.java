import java.util.*;

public class Game {
    private int numberOfPlayers;
    private Scanner in;
    private ArrayList<Player> players;
    private Player user;
    private int type;
    private Group<Mafia> mafiaGroup;
    private Group<Healer> healerGroup;
    private Group<Detective> detectiveGroup;
    private ArrayList<Detective> detectives;
    private ArrayList<Player> allInfo;
    private boolean [] isdead;
    Random rand;
    Game() {
        in = new Scanner(System.in);
        System.out.println("Welcome to Mafia");
        while (numberOfPlayers < 6) {
            System.out.print("Enter Number of Players : ");
            numberOfPlayers = in.nextInt();
        }
        isdead = new boolean[numberOfPlayers + 1];
        rand = new Random();
        players = new ArrayList<>();
        assignRoles();
    }

    private void assignRoles() {
        ArrayList<Integer> indices = new ArrayList<>();
        boolean [] chosen = new boolean[numberOfPlayers];

        for (int i = 0; i < numberOfPlayers/5; ++i) {
            int index = rand.nextInt(numberOfPlayers);
            while(chosen[index]) {
                index = rand.nextInt(numberOfPlayers);
            }
            indices.add(index);
            chosen[index] = true;
        }
        ArrayList<Mafia> mafias = new ArrayList<>();
        for (int index : indices) {
            Mafia mafia = new Mafia(index + 1);
            players.add(mafia);
            mafias.add(mafia);
        }
        indices.clear();
        for (int i = 0; i < Math.max(1, numberOfPlayers/10); ++i) {
            int index = rand.nextInt(numberOfPlayers);
            while(chosen[index]) {
                index = rand.nextInt(numberOfPlayers);
            }
            indices.add(index);
            chosen[index] = true;
        }
        ArrayList<Healer> healers = new ArrayList<>();
        for (int index : indices) {
            Healer healer = new Healer(index + 1);
            players.add(healer);
            healers.add(healer);
        }

        indices.clear();
        for (int i = 0; i < numberOfPlayers/5; ++i) {
            int index = rand.nextInt(numberOfPlayers);
            while(chosen[index]) {
                index = rand.nextInt(numberOfPlayers);
            }
            chosen[index] = true;
            indices.add(index);
        }
        detectives = new ArrayList<>();
        for (int index : indices) {
            Detective detective = new Detective(index + 1);
            players.add(detective);
            detectives.add(detective);
        }

        for (int i = 0; i < numberOfPlayers; ++i) {
            if (!chosen[i]) {
                players.add(new Commoner(i + 1));
            }
        }
        mafiaGroup = new Group<> (players, mafias);
        detectiveGroup = new Group<> (players, detectives);
        healerGroup = new Group<> (players, healers);
        allInfo = new ArrayList<>();
        allInfo.addAll(players);
        Collections.sort(players, new SortByID());
    }

    void assign(String type) {
        Player dummy;
        if (Objects.equals(type, "Mafia")) {
            dummy = new Mafia(-1);
            this.type = 1;
        } else if (Objects.equals(type, "Commoner")) {
            this.type = 4;
            dummy = new Commoner(-1);
        }else if (Objects.equals(type, "Healer")){
            dummy = new Healer(-1);
            this.type = 3;
        }else {
            dummy = new Detective(-1);
            this.type = 2;
        }
        for (Player player : players) {
            if (player.getClass() == dummy.getClass()) {
                user = player;
                break;
            }
        }
        System.out.println("You are " + user.getName());
        System.out.print("You are a " + type + ". Other " + type + "s are : ");
        for (Player player : players) {
            if (player.getClass() == user.getClass()) {
                if (user != player) {
                    System.out.print(player.getName() + " ");
                }
            }
        }
        System.out.println();
    }

    public void menu() {
        System.out.println("Choose a Character");
        System.out.println("1) Mafia");
        System.out.println("2) Detective");
        System.out.println("3) Healer");
        System.out.println("4) Commoner");
        System.out.println("5) Assign Randomly");
        int choice = in.nextInt();
        String [] mapping = new String[4];
        mapping[0] = "Mafia";
        mapping[1] = "Detective";
        mapping[2] = "Healer";
        mapping[3] = "Commoner";

        if (choice == 1) {
            assign(mapping[0]);
        }else if (choice == 2) {
            assign(mapping[1]);
        }else if (choice == 3) {
            assign(mapping[2]);
        }else if (choice == 4) {
            assign(mapping[3]);
        }else if (choice == 5) {
            int rnd = rand.nextInt(4);
            assign(mapping[rnd]);
        }else {
            System.out.println("Invalid Choice");
            menu();
        }
        gameRounds(1);
    }

    private void gameRounds(int roundNumber) {
        System.out.println("Round " + roundNumber);
        System.out.print(players.size() + " Players are Remaining : ");
        for (Player player : players) {
            System.out.print(player.getName() + " ");
        }
        System.out.println("are alive");
        Player player;
        if (type == 1 && user.isAlive()) {
            player = mafiaGroup.choose();
        }else {
            player = mafiaGroup.chooseAuto();
        }
        mafiaGroup.performOperation(player);

        if (type == 2 && user.isAlive()) {
            player = detectiveGroup.choose();
        }else {
            player = detectiveGroup.chooseAuto();
        }
        detectiveGroup.performOperation(player);

        if (type == 3 && user.isAlive()) {
            player = healerGroup.choose();
        }else {
            player = healerGroup.chooseAuto();
        }
        healerGroup.performOperation(player);
        for (Player player1 : players) {
            if (player1.getHP() == 0 && !player1.isMafia()) {
                player1.kill();
                isdead[player1.getID()] = true;
                System.out.println(player1.getName() + " died");
            }
        }
        refresh();
        checkifended();
        detectives = detectiveGroup.getMembers();
        boolean voted = false;
        for (Player player1 : players) {
            if (player1.isIdentified()) {
                voteOut(player1);
                voted = true;
            }
        }
        if (!voted) {
            voting();
        }

        mafiaGroup.refresh();
        detectiveGroup.refresh();
        healerGroup.refresh();

        System.out.println("--End of Actions--");

        if (!checkifended()) {
            gameRounds(roundNumber + 1);
        }

    }

    private boolean checkifended() {
        int count = 0;
        for (Player player : players) {
            if (!(player instanceof Mafia)) {
                count++;
            }
        }
        int mafiasCount = mafiaGroup.getGroupSize();
        if (count <= mafiasCount) {
            System.out.println("Game Over");
            System.out.println("Mafias Won");
            printallInfo();
            return true;
        }else if (mafiasCount == 0) {
            System.out.println("Game Over");
            System.out.println("Mafias Lost");
            printallInfo();
            return true;
        }
        return false;
    }

    private void printallInfo() {
        for (Player player : allInfo) {
            if (player instanceof Mafia) {
                System.out.print(player.getName() + " ");
                if (user == player) {
                    System.out.print("[User] ");
                }
            }
        }
        System.out.println("were Mafia");

        for (Player player : allInfo) {
            if (player instanceof Detective) {
                System.out.print(player.getName() + " ");
                if (user == player) {
                    System.out.print("[User] ");
                }
            }
        }
        System.out.println("were Detectives");

        for (Player player : allInfo) {
            if (player instanceof Healer) {
                System.out.print(player.getName() + " ");
                if (user == player) {
                    System.out.print("[User] ");
                }
            }
        }
        System.out.println("were Healers");

        for (Player player : allInfo) {
            if (player instanceof Commoner) {
                System.out.print(player.getName() + " ");
                if (user == player) {
                    System.out.print("[User] ");
                }
            }
        }
        System.out.println("were Commoners");
    }

    private void voting() {

        int [] voteCount = new int[numberOfPlayers];
        if (!isdead[user.getID()]) {
            int choice = user.vote();
            while(isdead[choice + 1]) {
                System.out.println("Invalid Vote");
                choice = user.vote();
            }
            voteCount[choice] += 1;
        }

        int choice;
        for (Player player : players) {
            if (player == user) continue;
            choice = player.autovote(numberOfPlayers);
            while(isdead[choice + 1]) {
                choice = player.autovote(numberOfPlayers);
            }
            voteCount[choice] += 1;
        }
        int max = 0;
        int maxCount = 0;
        int maxID = -1;
        for (int i = 0; i < numberOfPlayers; ++i) {
            if (max < voteCount[i]) {
                max = voteCount[i];
                maxCount = 1;
                maxID = i;
            }else if (max == voteCount[i]) {
                maxCount++;
            }
        }
        maxID++;
        if (maxCount == 2) {
            System.out.println("Tied");
            voting();
        }else {

            for (Player player1 : players) {
                if (player1.getID() == maxID) {
                    voteOut(player1);
                    break;
                }
            }
        }
    }

    private void voteOut(Player player) {
        System.out.println(player.getName() + " has been voted out");
        player.kill();
        isdead[player.getID()] = true;
        refresh();
    }

    private void refresh() {
        ArrayList<Player> toRemove = new ArrayList<>();
        for (Player player1 : players) {
            if (!player1.isAlive()) {
                toRemove.add(player1);
            }
        }
        for (Player player1 : toRemove) {
            players.remove(player1);
        }
        mafiaGroup.refresh();
        healerGroup.refresh();
        detectiveGroup.refresh();
    }

}
