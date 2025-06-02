// nowy serwis
package Services;

import Interface.AbstractCounterService;
import Interface.Identifiable;
import Model.Brygada;
import Model.Brygadzista;
import Model.Dzial;
import Database.*;
import Model.Pracownik;

import java.util.List;

public class DzialService extends AbstractCounterService<Dzial> {
    private static final Database<Dzial> db = new Database<>(Dzial.class);
    private static final DzialService INSTANCE = new DzialService();

    public static void addDzial(Dzial d) {
        db.add(d);
    }

    public static void removeDzial(Dzial d) {
        db.remove(d);
        INSTANCE.initializeCounter();
        Dzial.unregisterName(d.getNazwa_dzialu());
    }

    public static List<Dzial> getDzialy() {
        return db.getItems();
    }
    public static void updateDzial(Dzial updated) {
        List<Dzial> list = db.getItems();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == updated.getId()) {
                list.set(i, updated);
                break;
            }
        }
        db.saveItems(list);
    }

    public static List<String> getNazwyDzialow() {
        return db.getItems().stream()
                .map(Dzial::getNazwa_dzialu)
                .toList();
    }
    protected List<Dzial> getItems() {
        return getDzialy();
    }
    @Override
    protected void setCounter(int value) {
        Brygada.setCounter(value);
    }

    @Override
    protected int extractId(Dzial item) {
        return item.getId();
    }
}
