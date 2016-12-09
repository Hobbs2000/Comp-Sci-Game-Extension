package Global;
import java.awt.*;
/**
 * Created by Calvin on 5/9/16
 * A bar to show how much health an entity has
 */
public class HealthBar extends Rectangle
{
    int maxWidth;
    int entityHealth = 100;
    Color color = Color.RED;

    /**
     *
     */
    public HealthBar(int x, int y, int width, int height)
    {
        super(x, y, width, height);
        maxWidth = width;
    }

    /**
     * Sets the current color of the healthBar
     */
    public void setColor(Color newColor)
    {
        color = newColor;
    }

    /**
     * This draws the healthBar in its current location and also draws the the health number
     */
    public void draw(Graphics g)
    {
        g.setColor(this.color);
        g.fillRect((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
        Graphics2D g2 = (Graphics2D)g;
        Integer healthInteger = (Integer)entityHealth;
        g2.setColor(Color.BLACK);
        g2.drawString(healthInteger.toString(), (int)((getX()+(getWidth()/2))-10), (int)((getY()+(getHeight()/2))+5));
    }

    /**
     * Scales the healthBar according to the health of the entity
     */
    public void scale(int currentHealth)
    {
        if (currentHealth >= 0)
        {
            //This should not be a set number (should be different max healths for different entities)
            double percent = (currentHealth / 100.0);
            setSize((int)(maxWidth*percent), (int)getHeight());
            entityHealth = currentHealth;
        }
    }
}