import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class SortByID implements Comparator<Player> {

    public int compare(@NotNull Player a, Player b) {
        return Integer.compare(a.getID(), b.getID());
    }

}
