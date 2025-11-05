import java.util.Optional;

import controller.api.Controllermain;
import controller.api.Evento;
import controller.impl.ControllermainImpl;
import view.impl.SwingView;

public class HopTales {
    public static void main(String[] args) {
        
            final SwingView view = new SwingView();
            final Controllermain controller = new ControllermainImpl(view);
            controller.handleEvent(Evento.MAIN_MENU, Optional.empty());
        
    }
}
