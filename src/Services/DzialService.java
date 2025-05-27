// nowy serwis
package Services;

import Interface.Identifiable;
import Model.Dzial;
import Database.*;

import java.util.List;

public class DzialService {
    private static final Database<Dzial> db = new Database<>(Dzial.class);

    public static void addDzial(Dzial d) {
        db.add(d);
    }

    public static void removeDzial(Dzial d) {
        db.remove(d);
        initializeCounter();
        Dzial.unregisterName(d.getNazwa_dzialu());
    }

    public static List<Dzial> getDzialy() {
        return db.getItems();
    }

    public static List<String> getNazwyDzialow() {
        return db.getItems().stream()
                .map(Dzial::getNazwa_dzialu)
                .toList();
    }
    public static void initializeCounter() {
        List<Dzial> dzialy = getDzialy();
        int maxId = dzialy.stream()
                .mapToInt(Dzial::getId)
                .max()
                .orElse(0);
        Dzial.setCounter(maxId);
    }
}
