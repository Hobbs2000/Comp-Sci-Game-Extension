
package World;
import Global.Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Calvin on 4/16/2016.
 * The basic tile that all other tiles will extend off of
 */
public class Tile extends Entity
{
    public boolean isRed = false;
    public boolean isBlue = false;

    private BufferedImage sprite;

    /**
     *
     * @param x
     * @param y
     */
    public Tile(int x, int y)
    {
        //Each tile is 32 by 32 pixels big
        super(x, y, 32, 32);
    }

    /**
     *
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public Tile(int x, int y, int width, int height)
    {
        super(x, y, width, width);
    }

    /**
     *
     * @param newSprite
     */
    public void setSprite(BufferedImage newSprite)
    {
        sprite = newSprite;
    }

    public void draw(Graphics g)
    {
        if (!isRed && !isBlue)
            g.drawImage(sprite, getX(), getY(), getWidth(), getHeight(), null);
        else if (isRed)
        {
            g.setColor(Color.RED);
            g.fillRect(getX(), getY(), getWidth(), getHeight());
        }
        if (isBlue)
        {
            g.setColor(Color.RED);
            g.fillRect(getX(), getY(), getWidth(), getHeight());
        }
    }

    /**
     *
     * @param row
     * @param col
     */
    public void setLoc(int row, int col)
    {
        setX(getWidth() * col);
        setY(getHeight() * row);
    }

    /**
     * The default tile is solid
     * @return true
     */
    public boolean isSolid()
    {
        return true;
    }

    public void makeRed()
    {
        isRed = true;
    }

    /**
     *
     * @return
     */
    public int getCenterX()
    {
        return getX() + (getWidth()/2);
    }

    /**
     *
     * @return
     */
    public int getCenterY()
    {
        return getY() + (getHeight()/2);
    }

    /**
     *
     */
    public Point getCenterPoint()
    {
        return new Point(getCenterX(), getCenterY());
    }
}
