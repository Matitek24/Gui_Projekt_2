package Model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Brygadzista extends Uzytkownik {
    private ArrayList<Brygada> listaBrygad = new ArrayList<>();
    public Brygadzista(String imie, String nazwisko, LocalDate data_urodzenia, Dzial dzial, String login, String haslo) {
        super(imie, nazwisko, data_urodzenia, dzial, login, haslo);
    }
    public void addBrygade(Brygada brygada) {
        listaBrygad.add(brygada);
    }
    public void removeBrygade(Brygada brygada) {
        listaBrygad.remove(brygada);
    }
    public ArrayList<Brygada> getListaBrygad() {
        return listaBrygad;
    }
    public String toString() {
        return super.toString();
    }
}