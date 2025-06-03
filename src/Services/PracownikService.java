package Services;

import Database.Database;
import Interface.AbstractCounterService;
import Model.Brygada;
import Model.Dzial;
import Model.Pracownik;

import java.util.List;
import java.util.Optional;

public class PracownikService extends AbstractCounterService<Pracownik> {
    private static final Database<Pracownik> db = new Database<>(Pracownik.class);
    private static final PracownikService INSTANCE = new PracownikService();

    public static void addPracownik(Pracownik pracownik) {
        db.add(pracownik);
    }
    public static void removePracownik(Pracownik pracownik) {
        db.remove(pracownik);
        INSTANCE.initializeCounter();
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

    protected List<Pracownik> getItems() {
        return getPracownicy();
    }
    @Override
    protected void setCounter(int value) {
        Pracownik.setCounter(value);
    }

    @Override
    protected int extractId(Pracownik item) {
        return item.getId();
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
