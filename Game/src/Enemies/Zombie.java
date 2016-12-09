package Enemies;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import Graphics.Animation;
import java.io.IOException;

/**
 * Created by Calvin on 4/7/2016.
 */
public class Zombie extends Enemy
{
    private Animation walkAnimation;
    private Animation currentAnimation;
    private double scale;
    private int originalSpriteWidth;
    private int originalSpriteHeight;
    private int DY = -20;
    private boolean jumping = false;

    /**
     *
     */
    public Zombie(int spawnX, int spawnY, double scale)
    {
        /*
        Damage = 5
        Health = 20
        Speed  = 10*/
        super(5, 20, 10, spawnX, spawnY, 16, 16);
        originalSpriteHeight = 16;
        originalSpriteWidth = 16;
        this.scale = scale;
        try
        {
            walkAnimation = new Animation(ImageIO.read(getClass().getResource("/TestSpriteSheet.jpg")), 16, 16, 2, super.getX(), super.getY(), this.scale, 8);
            currentAnimation = walkAnimation;
            //walkAnimation = new Animation(ImageIO.read(getClass().getResource("/scottpilgrim_sheet.jpg")), 32, 36, 8, super.getX(), super.getY(), this.scale, 1.5);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void setX(int newX)
    {
        super.setX(newX);
        currentAnimation.update(newX, this.getY());
    }

    /**
     *
     */
    public void setY(int newY)
    {
        super.setY(newY);
        currentAnimation.update(this.getX(), newY);
    }

    /**
     * Increments the animation count,
     * This is the speed of the animation
     * @param g
     */
    public void animate()
    {
        currentAnimation.increaseCount();
    }

    /**
     * Draws the current animation/image of the Zombie
     */
    public void draw(Graphics g)
    {
        currentAnimation.draw(g);
    }

    /**
     * @return True
     */
    public boolean hasAnimation()
    {
        return true;
    }

    /**
     * @return The actually pixel width of the zombie 
     */
    public int getWidth()
    {
        return (int)(originalSpriteWidth * scale);
    }

    /**
     * @return The actually pixel height of the zombie 
     */
    public int getHeight()
    {
        return (int)(originalSpriteHeight * scale);
    }




}
