package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pracownik implements Serializable {
    private String imie;
    private String nazwisko;
    private LocalDate dataUrodzenia;
    private Dzial dzial;
    private static int counter = 0;
    private final int id;

    public Pracownik(String imie, String nazwisko, LocalDate data_urodzenia, Dzial dzial) {
        this.id = ++counter;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.dataUrodzenia = data_urodzenia;
        this.dzial = dzial;
        dzial.addPracownika(this);
    }
    public String getImie() {
        return imie;
    }
    public String getNazwisko() {
        return nazwisko;
    }
    public Dzial getDzial() {
        return dzial;
    }
    public int getId() {
        return id;
    }
    public LocalDate getDataUrodzenia() {
        return dataUrodzenia;
    }
    public void setImie(String imie) {
        this.imie = imie;
    }
    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }
    public void setDzial(Dzial dzial) {
        this.dzial = dzial;
    }
    public void setDataUrodzenia(LocalDate data_urodzenia) {
        this.dataUrodzenia = data_urodzenia;
    }
    public static void setCounter(int value) {
        counter = value;
    }

    @Override
    public String toString(){
        return "ID: " + id
                + " Nazwisko: " + nazwisko
                + " Imie: " + imie
                + " Data urodzenia: " + dataUrodzenia
                + " Dzial: " + dzial.getNazwa_dzialu();
    }
}