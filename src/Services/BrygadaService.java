package Services;

import Database.Database;
import Interface.AbstractCounterService;
import Model.Brygada;
import Model.Brygadzista;
import Model.Pracownik;
import Exception.NotAcceptClassException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BrygadaService extends AbstractCounterService<Brygada> {
    private static final Database<Brygada> db = new Database<>(Brygada.class);
    private static final BrygadzistaService INSTANCE = new BrygadzistaService();

    public static List<Brygada> getBrygady() {
        return db.getItems();
    }

    public static void addBrygada(Brygada b) {
        db.add(b);
    }

    public static void removeBrygada(Brygada b) {
        db.remove(b);
        INSTANCE.initializeCounter();
    }


    public static Optional<Brygada> getById(long id) {
        return db.getItems().stream().filter(u->u.getId()==id).findFirst();
    }

    public static void appendPracownik(Brygada brygada, Pracownik pracownik) throws NotAcceptClassException {
        brygada.appendPracownik(pracownik);
        updateBrygada(brygada);
    }


    public static void appendPracownik(Brygada brygada, List<Pracownik> listaPracownikow) throws NotAcceptClassException {
        brygada.appendPracownik(new ArrayList<>(listaPracownikow));
        updateBrygada(brygada);
    }

    @Override
    protected List<Brygada> getItems() {
        return getBrygady();
    }
    @Override
    protected void setCounter(int value) {
        Brygada.setCounter(value);
    }

    @Override
    protected int extractId(Brygada item) {
        return item.getId();
    }

    public static void updateBrygada(Brygada b) {
        List<Brygada> list = db.getItems();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(b.getName())) {
                list.set(i, b);
                break;
            }
        }
        db.saveItems(list);
    }
}
