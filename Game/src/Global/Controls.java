
package Global;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by Calvin on 4/12/20
 * <pre>This class is based off of the idea and some code from here:
 * http://gamedev.stackexchange.com/questions/56017/java-best-implementation-keylistener-for-games</pre>
 * Gets key presses to control the player
 * Could make this an inner class in player handler, but this controller could be used elsewhere
 */
public class Controls implements KeyListener
{

    //Creates an array for all the keys on the keyboard
    private boolean[] keys = new boolean[120];

    public boolean up, down, left, right, space;
    public boolean W, A, S, D;

    /**
     *
     */
    public void update()
    {
        //Will set up,right,down and left to the value (true or false) of their corresponding key
        up = keys[KeyEvent.VK_UP];
        W  = keys[KeyEvent.VK_W];
        right = keys[KeyEvent.VK_RIGHT];
        D = keys[KeyEvent.VK_D];
        down = keys[KeyEvent.VK_DOWN];
        S = keys[KeyEvent.VK_S];
        left = keys[KeyEvent.VK_LEFT];
        A = keys[KeyEvent.VK_A];
        space = keys[KeyEvent.VK_SPACE];
    }

    /**
     * When a key is pressed, the corresponding element at the index of keyCode is turned to true
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e)
    {
        /*Key code is an index which corresponds to the key pressed
        When a key is pressed, that boolean element in the array is turned to true*/
        keys[e.getKeyCode()] = true;
    }

    /**
     * When a key is released, the corresponding element at the index of keyCode is turned to false
     * @param e
     */
    @Override
    public void keyReleased(KeyEvent e)
    {
        keys[e.getKeyCode()] = false;
    }

    /**
     * Does nothing
     * @param e
     */
    @Override
    public void keyTyped(KeyEvent e)
    {
        //This does nothing
    }
}
