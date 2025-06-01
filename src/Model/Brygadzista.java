package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Brygadzista extends Uzytkownik implements Serializable {
    private static int counter = 0;
    private ArrayList<Brygada> listaBrygad = new ArrayList<>();
    private final int brygadzistaId;
    public Brygadzista(String imie, String nazwisko, LocalDate data_urodzenia, Dzial dzial, String login, String haslo) {
        super(imie, nazwisko, data_urodzenia, dzial, login, haslo);
        this.brygadzistaId = ++counter;
    }
    public void addBrygade(Brygada brygada) {
        listaBrygad.add(brygada);
    }
    public static int getCounter(){
        return counter;
    }
    public int getBrygadzistaId() {
        return brygadzistaId;
    }
    public static void setCounter(int value){
        counter = value;
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