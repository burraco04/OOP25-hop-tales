package controller.api;

import java.util.Optional;

public interface Controllermain {
    void handleEvent(Evento e, Optional<?> data);

}
