package Model;

import Interface.IDzialowy;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Brygadzista extends Uzytkownik implements Serializable, IDzialowy {
    private static int counter = 0;
    private ArrayList<Brygada> listaBrygad = new ArrayList<>();
    private ArrayList<String> historiaBrygad = new ArrayList<>();
    private final int brygadzistaId;
    public Brygadzista(String imie, String nazwisko, LocalDate data_urodzenia, Dzial dzial, String login, String haslo) {
        super(imie, nazwisko, data_urodzenia, dzial, login, haslo);
        this.brygadzistaId = ++counter;
    }
    public void addBrygade(Brygada brygada) {
        listaBrygad.add(brygada);
        historiaBrygad.add(brygada.getName());
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
    public List<String> getHistoriaBrygad() {
        return historiaBrygad;
    }

    public String toString() {
        return super.toString();
    }

}