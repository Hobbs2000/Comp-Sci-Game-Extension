
package Graphics;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Created by Calvin on 4/8/2016.
 * Will
 */
public class Sprite
{
    private BufferedImage image;
    private int width, height;
    private double scale;

    /**
     *
     * @param spriteSheet
     * @param xStart
     * @param yStart
     * @param width
     * @param height
     * @param scale
     */
    public Sprite(BufferedImage spriteSheet, int xStart, int yStart, int width, int height, double scale)
    {
        if (spriteSheet != null)
        {
            image = spriteSheet.getSubimage(xStart, yStart, width, height);
            this.width = width;
            this.height = height;
            this.scale = scale;
        }
    }

    /**
     * Returns this sprites image
     * @return Image This sprites image
     */
    public BufferedImage getImage()
    {
        return image;
    }


    /**
     *
     * @return Width the original width of the sprite image
     */
    public int getWidth()
    {
        return width;
    }

    /**
     *
     * @return Height the original height of the sprite image
     */
    public int getHeight()
    {
        return height;
    }
}
