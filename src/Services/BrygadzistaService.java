package Services;

import Database.Database;
import Model.Brygada;
import Model.Brygadzista;
import Model.Uzytkownik;

import java.util.List;
import java.util.Optional;

public class BrygadzistaService {
    private static final Database<Brygadzista> db = new Database<>(Brygadzista.class);

    public static List<Brygadzista> getBrygadzisci() {
        return db.getItems();
    }

    public static void addBrygadzista(Brygadzista b) {
        db.add(b);
    }

    public static void removeBrygadzista(Brygadzista b) {
        db.remove(b);
        initializeBrygadzistaCounter();
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

    public static void initializeBrygadzistaCounter() {
        int max = getBrygadzisci().stream()
                .mapToInt(Brygadzista::getBrygadzistaId)
                .max()
                .orElse(0);
        Brygadzista.setCounter(max);
    }
}
