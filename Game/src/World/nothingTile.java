package World;
import java.awt.*;

/**
 * Created by Calvin on 7/3/2016.
 */
public class nothingTile extends Tile
{
    /**
     *
     * @param x
     * @param y
     */
    public nothingTile(int x, int y)
    {
        super(x, y);
    }

    /**
     *
     * @return
     */
    public boolean isSolid()
    {
        return false;
    }

    public void draw(Graphics g)
    {
        if (isRed)
        {
            g.setColor(Color.RED);
            g.fillRect(getX(), getY(), getWidth(), getHeight());
        }
        if (isBlue)
        {
            g.setColor(Color.blue);
            g.fillRect(getX(), getY(), getWidth(), getHeight());
        }
        //Draw nothing
    }
}
