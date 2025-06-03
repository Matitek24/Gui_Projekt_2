package Services;

import Database.Database;
import Interface.AbstractCounterService;
import Model.Zlecenie;

import java.util.List;
import java.util.Optional;

public class ZlecenieService extends AbstractCounterService<Zlecenie> {
    private static final Database<Zlecenie> db = new Database<>(Zlecenie.class);

    public static List<Zlecenie> getZlecenia() {
        return db.getItems();
    }

    public static void addZlecenie(Zlecenie z) {
        db.add(z);
    }

    public static void removeZlecenie(Zlecenie z) {
        db.remove(z);
        INSTANCE.initializeCounter();
    }

    public static Optional<Zlecenie> getById(int id) {
        return db.getItems().stream().filter(z -> z.getId() == id).findFirst();
    }

    public static void updateZlecenie(Zlecenie z) {
        List<Zlecenie> list = db.getItems();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == z.getId()) {
                list.set(i, z);
                break;
            }
        }
        db.saveItems(list);
    }

    @Override
    protected List<Zlecenie> getItems() {
        return getZlecenia();
    }

    @Override
    protected void setCounter(int value) {
        Zlecenie.setCounter(value);
    }

    @Override
    protected int extractId(Zlecenie item) {
        return item.getId();
    }

    private static final ZlecenieService INSTANCE = new ZlecenieService();

}
