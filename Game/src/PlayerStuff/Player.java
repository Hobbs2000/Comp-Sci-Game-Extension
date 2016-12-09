
package PlayerStuff;
import Global.Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.awt.image.BufferedImage;

import Global.HealthBar;
import Graphics.Animation;

/**
 * The user controlled player
 * There may only be one player
 * Created by Calvin on 4/7/2016.
 */
public class Player extends Entity
{
    public Animation WALK_RIGHT_ANIMATION;
    public Animation WALK_LEFT_ANIMATION;
    private Animation currentAnimation;

    private BufferedImage stillSprite;
    public boolean isStill = true;
    //Make the actual size 2 pixel smaller before scaling
    private int originalSpriteWidth = 14;
    private int originalSpriteHeight = 30;
    private double scale;
    private int animationSpeed = 5;

    //These are the default values and may be changed
    public int damage = 10;
    public int health = 100;
    private HealthBar healthBar;
    private int speed = 5;

    /**
     *
     * @param startX
     * @param startY
     * @param scale
     */
    public Player(int startX, int startY, double scale)
    {
        super(startX, startY, 16, 32);

        this.scale = scale;

        healthBar = new HealthBar(startX - 20, startY - 20, 80, 10);

        try
        {
            WALK_RIGHT_ANIMATION = new Animation(ImageIO.read(getClass().getResource("/BasicPlayerSheet.png")), 16, 32, 5, super.getX(), super.getY(), this.scale, this.animationSpeed);
            currentAnimation = WALK_RIGHT_ANIMATION;

            WALK_LEFT_ANIMATION = new Animation(ImageIO.read(getClass().getResource("/BasicPlayerSheetLeft.png")), 16, 32, 5, super.getX(), super.getY(), this.scale, this.animationSpeed);

            stillSprite = ImageIO.read(getClass().getResource("/StandingBasicPlayer.png"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * The player IS collidable with other entities
     * @return
     */
    public boolean isCollidable()
    {
        return true;
    }

    /**
     * Increments the animation count,
     * This is the speed of the animation
     */
    public void animate()
    {
        currentAnimation.increaseCount();
    }

    /**
     * Calls the draw method on what every animation/image should be displayed
     */
    public void draw(Graphics g)
    {
        healthBar.scale(this.health);
        healthBar.draw(g);
        if (isStill)
        {
            //Will render a still image of the player sprite if the player is still 
            g.drawImage(stillSprite, getX(), getY(), (int)(stillSprite.getWidth() * scale), (int) (stillSprite.getHeight() * scale), null);
        }
        else
        {
            currentAnimation.draw(g);
        }
    }

    /**
     * Returns true since all players have an animation
     * Not really needed
     * @return Returns true
     */
    public boolean hasAnimation()
    {
        return true;
    }

    /**
     * @return speed - The speed in pixels of the player
     */
    public int getSpeed()
    {
        return this.speed;
    }

    /**
     * Will multiply the original pixel width of the player by its scale to get the current real pixel width
     * @return Returns the actually width of the player
     */
    public int getWidth()
    {
        return (int)(originalSpriteWidth * scale);
    }

    /**
     * Will multiply the original pixel width of the player by its scale to get the current real pixel width
     * @return Returns the actual width of the player
     */
    public int getHeight()
    {
        return (int)(originalSpriteHeight * scale);
    }

    /**
     * Sets the new x-coordinate of the player
     * @param newX
     */
    public void setX(int newX)
    {
        super.setX(newX);
        //Update health bar
        healthBar.setLocation((newX - 20), (int)healthBar.getY());
        //Need to update all animation coordinates
        WALK_RIGHT_ANIMATION.update(super.getX(), super.getY());
        WALK_LEFT_ANIMATION.update(super.getX(), super.getY());
    }

    /**
     * Sets the new y-coordinate of the player
     * @param newY
     */
    public void setY(int newY)
    {
        super.setY(newY);
        //Update health bar
        healthBar.setLocation((int)healthBar.getX(), (newY - 20));
        //Need to update all animation coordinates
        WALK_RIGHT_ANIMATION.update(super.getX(), super.getY());
        WALK_LEFT_ANIMATION.update(super.getX(), super.getY());
    }

    /**
     * Will return the animation that is currently being displayed
     * @return The animation that is currently being displayed (this animation will not be rendered if the player is standing still)
     */
    public Animation getCurrentAnimation()
    {
        return currentAnimation;
    }

    /**
     * Sets the currentAnimation to be displayed.
     * Should only use animation variables from the Player class
     */
    public void setCurrentAnimation(Animation newAnimation)
    {
        currentAnimation = newAnimation;
    }

}
