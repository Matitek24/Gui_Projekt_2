package Database;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Database<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 1L;

    // Klasa przechowywanych elementów
    private transient Class<T> type;
    private String typeName;

    // Tu trzymamy obiekty
    private List<T> items;

    public Database(Class<T> type) {
        this.type = type;
        this.typeName = type.getSimpleName();
        readFromFile();

    }

    public List<T> getItems() {
        return items;
    }

    public void add(T o) {
        items.add(o);
        saveToFile();
    }

    public void remove(T o) {
        items.remove(o);
        saveToFile();
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(getFileName()))) {
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFromFile() {
        File f = new File(getFileName());
        if (!f.exists()) {
            // jeśli nie ma pliku, to nowa, pusta lista
            items = new ArrayList<>();
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(f))) {
            @SuppressWarnings("unchecked")
            Database<T> db = (Database<T>) ois.readObject();
            this.items = db.items;
        } catch (Exception e) {
            e.printStackTrace();
            items = new ArrayList<>();
        }
    }
    public void saveItems(List<T> items) {
        this.items = items;
        saveToFile();
    }

    private String getFileName() {
        // np. src/dane/db_Dzial.bin
        return "src/dane/db_" + typeName + ".bin";
    }
}
