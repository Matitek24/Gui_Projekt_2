package Services;

import Database.Database;
import Interface.AbstractCounterService;
import Model.Brygada;
import Model.Pracownik;
import Model.Uzytkownik;

import java.util.List;
import java.util.Optional;

public class UzytkownikService extends AbstractCounterService<Uzytkownik> {
    private static final Database<Uzytkownik> db = new Database<>(Uzytkownik.class);
    private static final PracownikService INSTANCE = new PracownikService();
    public static void addUzytkownik(Uzytkownik u) {
        db.add(u);
    }
    public static void removeUzytkownik(Uzytkownik u) {
        db.remove(u);
        INSTANCE.initializeCounter();
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
    protected List<Uzytkownik> getItems() {
        return getUzytkownicy();
    }
    @Override
    protected void setCounter(int value) {
        Uzytkownik.setCounter(value);
    }

    @Override
    protected int extractId(Uzytkownik item) {
        return item.getId();
    }
}
