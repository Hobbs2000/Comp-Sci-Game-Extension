package PlayerStuff;

import Global.Projectile;
import Graphics.Sprite;
import Graphics.Animation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Calvin on 6/28/2016.
 */
public class Bullet extends Projectile
{
    private BufferedImage sprite;

    private double scale = .75;

    /**
     *
     * @param x
     * @param y
     */
    public Bullet(int x, int y)
    {
//                  width  height  damage  cooldown  speed
        super(x, y,  32,     28,     100,     20,       15 );

        try
        {
            sprite = ImageIO.read(getClass().getResource("/projectile.png"));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     *
     */
    public void animate()
    {
        //TODO: Make a basic animation for a bullet
    }

    /**
     *
     * @param g
     */
    public void draw(Graphics g)
    {
        g.drawImage(sprite, getX(), getY(), (int)(getWidth() * scale), (int)(getHeight() * scale), null);
    }
}
