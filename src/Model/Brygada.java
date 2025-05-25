package Model;

import Exception.NotAcceptClassException;
import java.util.ArrayList;

public class Brygada {
    private String name;
    private Brygadzista brygadzista;
    private ArrayList<Pracownik> listaPracownikow = new ArrayList<Pracownik>();
    public Brygada(String name , Brygadzista brygadzista) {
        this.name = name;
        this.brygadzista = brygadzista;
        brygadzista.addBrygade(this);
    }
    public void changeBrygadzista(Brygadzista newbrygadzista) {
        if(newbrygadzista == this.brygadzista) {
            return;
        }
        Brygadzista oldBrygadzista = this.brygadzista;

        oldBrygadzista.removeBrygade(this);
        newbrygadzista.addBrygade(this);
        this.brygadzista = newbrygadzista;
    }
    public void appendPracownik(Pracownik pracownik) throws NotAcceptClassException {
        if(pracownik instanceof Uzytkownik && !(pracownik instanceof Brygadzista)) {
            throw new NotAcceptClassException("Blad w dodawaniu Uzytkownik");
        }
        listaPracownikow.add(pracownik);
    }
    // dodanie pracownikow z listy pracownikow
    public void appendPracownik(ArrayList<Pracownik> pracownicy) throws NotAcceptClassException {
        for(Pracownik pracownik : pracownicy) {
            if(pracownik instanceof Uzytkownik && !(pracownik instanceof Brygadzista)) {
                throw new NotAcceptClassException("Blad w dodawaniu Uzytkownik");
            }
        }
        listaPracownikow.addAll(pracownicy);
    }
    //gettery
    public String getName() {
        return name;
    }
    public Brygadzista getBrygadzista() {
        return brygadzista;
    }
    public ArrayList<Pracownik> getListaPracownikow() {
        return listaPracownikow;
    }
    public String getPracownicy(){
        String pracownicy = " ";
        for(Pracownik pracownik : listaPracownikow){
            pracownicy += pracownik.toString();
        }
        return pracownicy;
    }


    @Override
    public String toString() {
        return "Brygada{"
                + "Nazwa="
                + name
                + " Brygadzista "
                + brygadzista.toString()
                + " Pracownicy "
                + getPracownicy() + " }";
    }
}