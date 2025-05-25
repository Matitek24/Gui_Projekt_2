package Database;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Database<T extends Serializable> implements Serializable {
    private List<T> items;

    public Database() {
        readFromFile();
    }

    public List<T> getItems() {
        return items;
    }

    public void add(T object) {
        items.add(object);
        saveToFile();
    }

    public void remove(T object) {
        items.remove(object);
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
            items = new ArrayList<>();
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(f))) {
            Database<T> db = (Database<T>) ois.readObject();
            this.items = db.items;
        } catch (Exception e) {
            e.printStackTrace();
            items = new ArrayList<>();
        }
    }

    private String getFileName() {
        // np. src/dane/db_<nazwa_klasy>.bin
        return "src/Dane/db_" +
                itemsClassName() + ".bin";
    }

    private String itemsClassName() {
        // proste wyciągnięcie nazwy typu, np. "Dzial"
        if (items.isEmpty()) return "Unknown";
        return items.get(0).getClass().getSimpleName();
    }
}