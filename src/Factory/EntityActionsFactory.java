package Factory;

import Interface.EntityActions;
import Services.DzialActions;
import Services.PracownikActions;
import View.CenterPanel;

import java.awt.*;

public class EntityActionsFactory {
    public static EntityActions createActions(String entityName, CenterPanel centerPanel, Component parent) {
        return switch (entityName){
            case "Dział pracowników" -> new DzialActions(centerPanel, parent);
            case "Pracownik" -> new PracownikActions(centerPanel, parent);
            default -> null;
        };
    }
}
