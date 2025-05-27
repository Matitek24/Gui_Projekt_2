package Services;

import Database.Database;
import Model.Pracownik;

import java.util.List;
import java.util.Optional;

public class PracownikService {
    private static final Database<Pracownik> db = new Database<>(Pracownik.class);

    public static void addPracownik(Pracownik pracownik) {
        db.add(pracownik);
    }
    public static void removePracownik(Pracownik pracownik) {
        db.remove(pracownik);
        initializeCounter();
    }
    public static List<Pracownik> getPracownicy() {
        return db.getItems();
    }
    public static Optional<Pracownik> getPracownikById(int id) {
        return db.getItems()
                .stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }

    public static void initializeCounter(){
        int maxId = db.getItems().stream()
                .mapToInt(Pracownik::getId)
                .max()
                .orElse(0);
        Pracownik.setCounter(maxId);
    }
    public static void updatePracownik(Pracownik updated) {
        List<Pracownik> list = db.getItems();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == updated.getId()) {
                list.set(i, updated);
                break;
            }
        }
        db.saveItems(list);
    }
}
