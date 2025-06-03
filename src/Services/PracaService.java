package Services;

import Database.Database;
import Interface.AbstractCounterService;
import Model.Praca;

import java.util.List;
import java.util.Optional;

public class PracaService extends AbstractCounterService<Praca> {
    private static final Database<Praca> db = new Database<>(Praca.class);

    // Singleton (podobnie jak w ZlecenieService) – inicjalizacja licznika przy starcie
    private static final PracaService INSTANCE = new PracaService();

    public static List<Praca> getPrace() {
        return db.getItems();
    }

    public static void addPraca(Praca p) {
        db.add(p);
    }

    public static void removePraca(Praca p) {
        db.remove(p);
        INSTANCE.initializeCounter();
    }

    public static Optional<Praca> getById(int id) {
        return db.getItems().stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }

    public static void updatePraca(Praca p) {
        List<Praca> list = db.getItems();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == p.getId()) {
                list.set(i, p);
                break;
            }
        }
        db.saveItems(list);
    }

    @Override
    protected List<Praca> getItems() {
        return getPrace();
    }

    @Override
    protected void setCounter(int value) {
        Praca.setCounter(value);
    }

    @Override
    protected int extractId(Praca item) {
        return item.getId();
    }
}
