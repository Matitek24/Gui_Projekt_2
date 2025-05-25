package Model;

import java.time.LocalDate;

public class Uzytkownik extends Pracownik{
    private String login;
    private String haslo;
    private String inicial;
    public Uzytkownik(String imie, String nazwisko, LocalDate data_urodzenia, Dzial dzial, String login, String haslo) {
        super(imie, nazwisko, data_urodzenia, dzial);
        this.login = login;
        this.haslo = haslo;
        setInicial();
    }
    // settery
    private void setInicial(){
        this.inicial = getImie().charAt(0) +""+ getNazwisko().charAt(0);
    }
    public void setNazwisko(String nazwisko){
        super.setNazwisko(nazwisko);
        setInicial();
    }
    public void setImie(String imie){
        super.setImie(imie);
        setInicial();
    }
    // gettery
    public String getLogin() {
        return login;
    }
    public String toString() {
        return super.toString() + " Inicjalizacja " +inicial;
    }
}
