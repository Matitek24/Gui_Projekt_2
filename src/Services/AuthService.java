package Services;

import Model.Brygadzista;
import Model.Dzial;
import Model.Uzytkownik;
import java.time.LocalDate;
import java.util.Optional;

import static Model.Dzial.createDzial;

public class AuthService {
    public Optional<Uzytkownik> login(String login, String hasloPlain){

        if ("admin".equals(login) && "admin".equals(hasloPlain)) {
            // Tymczasowy dział dla admina
            Dzial tymczasowyDzial = createDzial("Administracja");

            Uzytkownik admin = new Uzytkownik(
                    "Administrator",
                    "Systemowy",
                    LocalDate.now(),
                    tymczasowyDzial,
                    "admin",
                    "admin"
            );
            return Optional.of(admin);
        }

        for (Uzytkownik u : UzytkownikService.getUzytkownicy()) {

            if (u.getLogin().equals(login)) {
                if (u.getHaslo().equals(hasloPlain)) {
                    return Optional.of(u);
                } else {
                    return Optional.empty();
                }
            }
        }
        for (Brygadzista b : BrygadzistaService.getBrygadzisci()) {
            if (b.getLogin().equals(login)) {
                if (b.getHaslo().equals(hasloPlain)) {
                    return Optional.of(b);
                } else {
                    return Optional.empty();
                }
            }
        }

        return Optional.empty();

    }

    public boolean changePassword(Uzytkownik user, String oldHaslo, String newHaslo) {
        if (!user.getHaslo().equals(oldHaslo)) {
            return false;
        }
        user.setHaslo(newHaslo);
        if (user instanceof Brygadzista) {
            BrygadzistaService.updateBrygadzista((Brygadzista) user);
        } else {
            UzytkownikService.updateUzytkownik(user);
        }
        return true;
    }
}
