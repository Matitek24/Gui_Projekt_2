package Model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class Zlecenie implements Serializable {

    public enum stan_zlecenia{
        PLANOWANE, NIEPLANOWANE, REALIZOWANE, ZAKOŃCZONE
    };
    private static HashMap<Integer, Zlecenie> wszystkieZlecenia = new HashMap<>();
    private static int counter = 0;
    private final int id;
    private ArrayList<Praca> prace;
    private Brygada brygada;
    private LocalDateTime dataUtworzenia;
    private LocalDateTime dataRozpoczecia;
    private LocalDateTime dataZakonczenia;
    public stan_zlecenia stan;


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
    public boolean isFinished() {
        return prace.stream().allMatch(Praca::isCzyZrealizowane);
    }

    public static void setCounter(int ct) {
        counter = ct;
    }

    public int getId() {
        return id;
    }
    public ArrayList<Praca> getPraca() {
        return prace;
    }
    public Brygada getBrygada() {
        return brygada;
    }
    public LocalDateTime getDataUtworzenia() {
        return dataUtworzenia;
    }
    public LocalDateTime getDataRozpoczecia() {
        return dataRozpoczecia;
    }
    public LocalDateTime getDataZakonczenia() {
        return dataZakonczenia;
    }
    public void setDataUtworzenia(LocalDateTime dataUtworzeni) {
        dataUtworzenia = dataUtworzeni;
    }
    public void setDataRozpoczecia(LocalDateTime dataRozpoczeci) {
        dataRozpoczecia = dataRozpoczeci;
    }
    public void setDataZakonczenia(LocalDateTime dataZakonczeni) {
        dataZakonczenia = dataZakonczeni;
    }
    public void setStan(stan_zlecenia stan) {
        this.stan = stan;
    }


    @Override
    public String toString() {
        return "Zlecenie#" + id + "[" + stan + ", prace=" + prace + "]";
    }

}