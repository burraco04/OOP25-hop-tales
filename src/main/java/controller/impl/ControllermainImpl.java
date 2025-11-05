package controller.impl;

import java.util.Optional;

import controller.api.Controllermain;
import controller.api.Evento;
import view.api.view;

public class ControllermainImpl implements Controllermain {

    private final view view;

    public ControllermainImpl(final view view) {
        this.view = view;
    }

    @Override
    public void handleEvent(final Evento e, final Optional<?> data) {
        if (e == Evento.MAIN_MENU) {
            this.view.showMainMenu();
        }
    }
}
