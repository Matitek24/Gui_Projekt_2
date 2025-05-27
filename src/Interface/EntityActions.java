package Interface;

import javax.swing.*;
import java.util.List;

public interface EntityActions {
    void onAdd();
    void onEdit();
    void onDelete();

    default List<JButton> getExtraButtons(){
        return List.of();
    }
}
