package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Exception.NotUniqueNameException;

import javax.swing.*;

public class Dzial implements Serializable {
    private String nazwaDzialu;
    private ArrayList<Pracownik> pracownicy;
    private static Set<String> grupa_nazw = new HashSet<>();
    public static int counter = 0;
    private final int id;

    private Dzial(String nazwa_dzialu) throws NotUniqueNameException {
        if(grupa_nazw.contains(nazwa_dzialu)){
            throw new NotUniqueNameException(nazwa_dzialu + " dział juz istnieje");
        }
        this.nazwaDzialu = nazwa_dzialu;
        grupa_nazw.add(nazwa_dzialu);
        pracownicy = new ArrayList<>();
        id = ++counter;

    }
    public static Dzial createDzial(String nazwa_dzialu) throws NotUniqueNameException {
        return new Dzial(nazwa_dzialu);
    }
    public void addPracownika(Pracownik pracownik) {
        this.pracownicy.add(pracownik);

    }
    public void rename(String newName) throws NotUniqueNameException {
        if(grupa_nazw.contains(newName)){
            throw new NotUniqueNameException(newName + " juz istnieje");
        }
        grupa_nazw.remove(this.nazwaDzialu);
        this.nazwaDzialu = newName;
        grupa_nazw.add(newName);
    }
    public static void unregisterName(String nazwaDzialu) {
        grupa_nazw.remove(nazwaDzialu);
    }

    // Gettery
    public String getNazwa_dzialu() {
        return nazwaDzialu;
    }
    public int getId() {
        return id;
    }
    public static void setCounter(int value) {
        counter = value;
    }

    // ToString
    @Override
    public String toString() {
        return "Nazwa dzialu: " + nazwaDzialu + " Pracownicy w tym dziale " + pracownicy;
    }
}