package World;
import Graphics.Animation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;

/**
 * Created by Calvin on 4/18/2016.
 */
public class Grass extends Tile
{
    /**
     *
     * @param x
     * @param y
     */
    public Grass(int x, int y)
    {
        super(x,y);
        try
        {
            BufferedImage parentSheet = ImageIO.read(getClass().getResource("/TileSheet1.png"));
            setSprite(parentSheet.getSubimage(32, 0, 32, 32));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
