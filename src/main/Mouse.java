package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicBoolean;

public class Mouse extends MouseAdapter {

    public int x, y;
    public AtomicBoolean pressed = new AtomicBoolean();

    @Override
    public void mousePressed(MouseEvent e) {
        pressed.set(true);
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        pressed.set(false);
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }
}
