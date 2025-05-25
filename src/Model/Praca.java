package Model;

import java.util.ArrayList;
import java.util.HashMap;

public class Praca extends Thread {
    public enum rodzaj_pracy{
        Ogólna, Montaz, Demontaz, Wymiana
    }
    private rodzaj_pracy rodzajPracy;
    private int czasPracy;
    private boolean czyZrealizowane;
    private String opis;
    private static int counter = 0;
    private final int id;
    public Praca(rodzaj_pracy rodzaj, int czas, String opis) {
        synchronized (Praca.class) {
            id = ++counter;
        }
        this.rodzajPracy = rodzaj;
        this.czasPracy = czas;
        this.opis = opis;
        this.czyZrealizowane = false;
        setName(opis);
    }
    public Praca(rodzaj_pracy rodzaj, int czas, String opis, ArrayList<Praca> prace) {
        this(rodzaj, czas, opis);
    }

    public boolean isCzy_zrealizowane() {
        return czyZrealizowane;
    }

    @Override
    public String toString() {
        return id + " - " + opis + " (" + rodzajPracy + ")";
    }
}