package Factory;

import Interface.EntityActions;
import Services.*;
import View.CenterPanel;

import java.awt.*;

public class EntityActionsFactory {
    public static EntityActions createActions(String entityName, CenterPanel centerPanel, Component parent) {
        return switch (entityName){
            case "Dział pracowników" -> new DzialActions(centerPanel, parent);
            case "Pracownik" -> new PracownikActions(centerPanel, parent);
            case "Użytkownik" -> new UzytkownikActions(centerPanel, parent);
            case "Brygadzista" -> new BrygadzistaActions(centerPanel, parent);
            case "Brygada" -> new BrygadaActions(centerPanel, parent);
            default -> null;
        };
    }
}
