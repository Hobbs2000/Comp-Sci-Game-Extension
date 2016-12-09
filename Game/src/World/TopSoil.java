package World;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Calvin on 7/3/2016.
 */
public class TopSoil extends Tile
{

    /**
     *
     * @param x
     * @param y
     */
    public TopSoil(int x, int y)
    {
        //Each tile is 64 by 64 tiles big
        super(x, y);

        try
        {
            BufferedImage parent = ImageIO.read(getClass().getResource("/TileSheet1.png"));
            setSprite(parent.getSubimage(0, 0, 32, 32));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
