package Factory;

import Interface.EntityActions;
import Model.Uzytkownik;
import Services.*;
import View.CenterPanel;

import java.awt.*;

public class EntityActionsFactory {
    public static EntityActions createActions(String entityName, CenterPanel centerPanel, Component parent, Uzytkownik logged) {
        return switch (entityName){
            case "Dział pracowników" -> new DzialActions(centerPanel, parent);
            case "Pracownik" -> new PracownikActions(centerPanel, parent);
            case "Użytkownik" -> new UzytkownikActions(centerPanel, parent, logged);
            case "Brygadzista" -> new BrygadzistaActions(centerPanel, parent, logged);
            case "Brygada" -> new BrygadaActions(centerPanel, parent);
            case "Zlecenie" -> new ZlecenieActions(centerPanel, parent, logged);
            case "Praca" -> new PracaActions(centerPanel, parent);
            default -> null;
        };
    }
}
