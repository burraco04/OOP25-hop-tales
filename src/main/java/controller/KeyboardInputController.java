package controller;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import controller.impl.PlayerController;

public class KeyboardInputController extends KeyAdapter{
    private PlayerController playerController; 

    public KeyboardInputController(){
        super();
        this.playerController = new PlayerController();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                playerController.setW();
                break;
            case KeyEvent.VK_A:
                playerController.setA();
                break;
            case KeyEvent.VK_S:
                playerController.setS();
                break;
            case KeyEvent.VK_D:
                playerController.setD();
                break;
            case KeyEvent.VK_SPACE:
                playerController.setSpace();
                break;
            default:
                break;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                playerController.negatesW();
                break;
            case KeyEvent.VK_A:
                playerController.negatesA();
                break;
            case KeyEvent.VK_S:
                playerController.negatesS();
                break;
            case KeyEvent.VK_D:
                playerController.negatesD();
                break;
            case KeyEvent.VK_SPACE:
                playerController.negatesSpace();
                break;
            default:
                break;
        }
    }

}
