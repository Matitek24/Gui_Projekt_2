package Services;

import Database.Database;
import Model.Pracownik;

import java.util.List;

public class PracownikService {
    private static final Database<Pracownik> db = new Database<>(Pracownik.class);

    public static void addPracownik(Pracownik pracownik) {
        db.add(pracownik);
    }
    public static void removePracownik(Pracownik pracownik) {
        db.remove(pracownik);
    }
    public static List<Pracownik> getPracownicy() {
        return db.getItems();
    }
}
