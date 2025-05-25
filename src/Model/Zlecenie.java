package Model;

import java.time.LocalDateTime;
import java.util.*;

public class Zlecenie {

    private enum stan_zlecenia{
        PLANOWANE, NIEPLANOWANE, REALIZOWANE, ZAKOŃCZONE
    };
    private static HashMap<Integer, Zlecenie> wszystkieZlecenia = new HashMap<>();
    private static int counter = 0;
    private final int id;
    private ArrayList<Praca> prace;
    private Brygada brygada;
    private final LocalDateTime dataUtworzenia;
    private LocalDateTime dataRozpoczecia;
    private LocalDateTime dataZakonczenia;
    private stan_zlecenia stan;


    public Zlecenie(boolean planowane) {
        synchronized (Zlecenie.class) {
            id = ++counter;
            wszystkieZlecenia.put(id, this);
        }
        this.prace = new ArrayList<>();
        this.brygada = null;

        dataUtworzenia = LocalDateTime.now();
    }
    public Zlecenie(boolean planowane, Brygada brygada) {
        this(planowane);
        this.brygada = brygada;
    }
    public Zlecenie(boolean planowane, ArrayList<Praca> prace) {
        this(planowane);
        this.prace.addAll(prace);
    }
    public Zlecenie(boolean planowane, ArrayList<Praca> prace, Brygada brygada) {
        this(planowane, prace);
        this.brygada = brygada;
    }
    public void addPraca(Praca praca) {
        if(praca != null){
            prace.add(praca);
        }
    }
    public boolean setBrygada(Brygada brygada) {
        if(this.brygada == null) {
            this.brygada = brygada;
            return true;
        }
        return false;
    }
    public String getStan_zlecenia() {
        return stan.toString();
    }
    public static Zlecenie getZlecenieById(int id) {
        synchronized (Zlecenie.class) {
            return wszystkieZlecenia.get(id);
        }
    }

    @Override
    public String toString() {
        return "Zlecenie#" + id + "[" + stan + ", prace=" + prace + "]";
    }

}