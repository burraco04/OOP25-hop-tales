package model.entity;

import java.awt.Graphics;

public interface Entity {
int getX();
int getY();

/**
     * Disegna l'entità tenendo conto dell'offset della camera X.
     *
     * @param g Graphics
     * @param camX offset della camera sull'asse X
*/

void draw(Graphics g, int camX); 
}
