package Services;

import Database.Database;
import Model.Uzytkownik;

import java.util.List;
import java.util.Optional;

public class UzytkownikService {
    private static final Database<Uzytkownik> db = new Database<>(Uzytkownik.class);

    public static void addUzytkownik(Uzytkownik u) {
        db.add(u);
    }
    public static void removeUzytkownik(Uzytkownik u) {
        db.remove(u);
        initializeCounter();
    }
    public static void updateUzytkownik(Uzytkownik u) {
        List<Uzytkownik> list = db.getItems();
        for (int i=0; i<list.size(); i++) {
            if (list.get(i).getId()==u.getId()) {
                list.set(i, u);
                break;
            }
        }
        db.saveItems(list);
    }
    public static List<Uzytkownik> getUzytkownicy() {
        return db.getItems();
    }
    public static Optional<Uzytkownik> getById(int id) {
        return db.getItems().stream().filter(u->u.getId()==id).findFirst();
    }
    private static void initializeCounter() {
        int max = db.getItems().stream().mapToInt(Uzytkownik::getId).max().orElse(0);
        Uzytkownik.setCounter(max);
    }
}
