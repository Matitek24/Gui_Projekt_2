package Services;

import Database.Database;
import Interface.AbstractCounterService;
import Model.Brygada;
import Model.Brygadzista;
import Model.Uzytkownik;

import java.util.List;
import java.util.Optional;

public class BrygadzistaService extends AbstractCounterService<Brygadzista> {
    private static final Database<Brygadzista> db = new Database<>(Brygadzista.class);
    private static final BrygadzistaService INSTANCE = new BrygadzistaService();

    public static List<Brygadzista> getBrygadzisci() {
        return db.getItems();
    }

    public static void addBrygadzista(Brygadzista b) {
        db.add(b);
    }

    public static void removeBrygadzista(Brygadzista b) {
        db.remove(b);
        INSTANCE.initializeCounter();
    }

    public static Optional<Brygadzista> getById(int id) {
        return db.getItems().stream().filter(u->u.getBrygadzistaId()==id).findFirst();
    }

    public static void updateBrygadzista(Brygadzista b) {
        List<Brygadzista> list = db.getItems();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getBrygadzistaId() == b.getBrygadzistaId()) {
                list.set(i, b);
                break;
            }
        }
        db.saveItems(list);
    }

    public static void assignBrygadzista(Brygadzista bre, Brygada brygada) {
        bre.addBrygade(brygada);
        updateBrygadzista(bre);
    }

    @Override
    protected List<Brygadzista> getItems() {
        return getBrygadzisci();
    }
    @Override
    protected void setCounter(int value) {
        Brygada.setCounter(value);
    }

    @Override
    protected int extractId(Brygadzista item) {
        return item.getId();
    }
}
