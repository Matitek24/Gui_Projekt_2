package Model;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Praca implements Serializable {
    public enum rodzaj_pracy {
        Ogólna, Montaz, Demontaz, Wymiana
    }

    private rodzaj_pracy rodzajPracy;
    private int czasPracy;
    private boolean czyZrealizowane;
    private String opis;
    private static int COUNTER = 0;
    private final int id;

    public Praca(rodzaj_pracy rodzaj, int czas, String opis) {
        this.id = ++COUNTER;
        this.rodzajPracy = rodzaj;
        this.czasPracy = czas;
        this.opis = opis;
        this.czyZrealizowane = false;
    }

    public Praca(rodzaj_pracy rodzaj, int czas, String opis, java.util.List<Praca> prace) {
        this(rodzaj, czas, opis);
    }

    public int getId() {
        return id;
    }

    public rodzaj_pracy getRodzajPracy() {
        return rodzajPracy;
    }

    public void setRodzajPracy(rodzaj_pracy rodzajPracy) {
        this.rodzajPracy = rodzajPracy;
    }

    public int getCzasPracy() {
        return czasPracy;
    }

    public void setCzasPracy(int czasPracy) {
        this.czasPracy = czasPracy;
    }

    public boolean isCzyZrealizowane() {
        return czyZrealizowane;
    }

    public void setCzyZrealizowane(boolean czyZrealizowane) {
        this.czyZrealizowane = czyZrealizowane;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }


    @Override
    public String toString() {
        return id + " - " + opis + " (" + rodzajPracy + ")";
    }

    public static void setCounter(int value) {
        COUNTER = value;
    }
}

