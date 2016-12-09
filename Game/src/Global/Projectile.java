package Global;

import java.awt.*;
import Graphics.Animation;

/**
 * Created by Calvin on 6/28/2016.
 */
public class Projectile extends Entity
{
    private int damage;
    private int speed;
    private int cooldown;
    private int angle;
    private int direction = 0;

    public Projectile(int x, int y, int width, int height, int damage, int cooldown, int speed)
    {
        super(x, y, width, height);
        this.damage = damage;
        this.cooldown = cooldown;
        this.speed = speed;
    }

    /**
     *
     * @return
     */
    public boolean hasAnimation()
    {
        return true;
    }

    /**
     *
     * @param x
     * @param y
     */
    public void update(int x, int y)
    {
        setX(x);
        setY(y);
    }

    /**
     *
     */
    public boolean isCollidable()
    {
        return true;
    }

    /**
     *
     */
    public int getDamage()
    {
        return this.damage;
    }

    /**
     *
     * @return
     */
    public int getSpeed()
    {
        return speed;
    }

    /**
     *
     */
    public int getCooldown()
    {
        return cooldown;
    }

    /**
     *
     * @param NewDir
     */
    public void setDirection(int NewDir)
    {
        // 1 = UP,  2 = RIGHT,  3 = DOWN,  4 = LEFT
        this.direction = NewDir;
    }

    /**
     *
     */
    public int getDirection()
    {
        return this.direction;
    }

}
