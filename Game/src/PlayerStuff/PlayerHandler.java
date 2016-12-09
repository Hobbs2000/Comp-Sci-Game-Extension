
package PlayerStuff;
import Enemies.Enemy;
import Enemies.Path;
import Global.*;
import World.Level;
import World.Tile;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by Calvin on 4/12/2016
 * This will handle the players movements and collision detection
 * Will be on a separate thread
 * Has a keyboard listener class that will use input for player control
 * Most of the logic for the player will be here
 */
public class PlayerHandler implements Runnable
{

    private ArrayList<Entity> entities;
    private ArrayList<Projectile> projectiles;
    private int sleep;
    private boolean running;

    //This stuff is for jumping
    //yVelocity is the speed at which the player rises and falls
    private int yVelocity = -20;
    //This is what the yVelocity will be reset to when a jumping sequence is over
    private int maxY_Velocity = -20;
    //Gravity is what is added to yVelocity for making a parabola like jump
    private int GRAVITY = 2;
    private boolean jumping = false;

    //The time in between the player taking damage
    //The actual amount of time is sleep * invulnerableTime
    private int invulnerableTime = 70;

    private int weaponCooldown = 0;

    private Player thisPlayer;
    private Controls controls;

    private Level currentLevel;

    private Path testPath;

    public int frameWidth, frameHeight;

    /**
     *
     * @param player
     * @param entityList
     * @param frame
     */
    public PlayerHandler(Player player, ArrayList<Entity> entityList, ArrayList<Projectile> projectileList, int sleep, JFrame frame, Controls controls, Level level)
    {
        this.thisPlayer = player;
        this.entities = entityList;
        this.projectiles = projectileList;
        this.controls = controls;
        this.sleep = sleep;
        this.currentLevel = level;
        this.frameWidth = frame.getWidth();
        this.frameHeight = frame.getHeight();

        testPath = new Path(this.thisPlayer, currentLevel);
    }

    /**
     * Handles all player stuff
     * Includes handling actions such as moving and collisions
     */
    public void run()
    {
        running = true;

        while(running)
        {
            //Get the key/s pressed for this loop
            controls.update();

            //Go through all entities and check for collisions with enemies
            //This is how the player is damaged
            for (Entity entity : entities)
            {
                if (entity instanceof Enemy)
                {
                    Enemy enemy = (Enemy) entity;

                    if (Collision.collided(this.thisPlayer, enemy) && invulnerableTime <= 0)
                    {
                        this.thisPlayer.health -= enemy.getDamage();
                        invulnerableTime = 70;
                    }
                }
            }
            invulnerableTime--;


            //Checks to see if the space key was pressed, which starts the jumping sequence by setting jumping to true
            /*
            if (controls.space)
            {
                jumping = true;
                this.thisPlayer.isStill = false;
            }
            if (jumping)
            {
                if (yVelocity < 0)
                {
                    movePlayerUp(-1*yVelocity);
                    yVelocity += GRAVITY;
                }
                else if (yVelocity >= 0)
                {
                    movePlayerDown(yVelocity);
                    //If the player is moving faster than the size of a tile, it would clip through the tile
                    if (yVelocity >= 31)
                    {
                        yVelocity = 31;
                    }
                    else
                    {
                        yVelocity += GRAVITY;
                    }
                }
            }
            */

            //Move down because of gravity
            //This is for regular falling, not jump falling, that is why it only executes when the player is not jumping
            /*
            if (!jumping)
            {
                movePlayerDown(10);
            }
            */

            //Fires a projectile if space bar is pressed
            if (controls.space && weaponCooldown <= 0)
            {
                Bullet bullet = new Bullet(this.thisPlayer.getX() + 5, this.thisPlayer.getY() + 10);

                synchronized (entities)
                {
                    synchronized (projectiles)
                    {
                        //entities.add(bullet);
                        projectiles.add(bullet);
                    }
                }

                //Decide the direction the projectile will go based on key pressed
                if (controls.right)
                {
                    bullet.setDirection(2);
                }
                else if (controls.left)
                {
                    bullet.setDirection(4);
                }
                else if (controls.up)
                {
                    bullet.setDirection(1);
                }
                else if (controls.down)
                {
                    bullet.setDirection(3);
                }
                else
                {
                    bullet.setDirection(2);
                }

                weaponCooldown = bullet.getCooldown();
            }
            weaponCooldown--;


            //Move the player based on collisions and what keys were pressed
            if (controls.D)
            {
                //movePlayerRight(this.thisPlayer.getSpeed());
                if (Collision.tileCollisionRight(this.thisPlayer, this.thisPlayer.getSpeed(), currentLevel) == false)
                {
                    this.thisPlayer.setX(this.thisPlayer.getX() + this.thisPlayer.getSpeed());
                }
                //Change the animation to the walking left animation
                this.thisPlayer.setCurrentAnimation(this.thisPlayer.WALK_RIGHT_ANIMATION);
                this.thisPlayer.isStill = false;
            }
            if (controls.A)
            {
                //movePlayerLeft(this.thisPlayer.getSpeed());
                if (Collision.tileCollisionLeft(this.thisPlayer, this.thisPlayer.getSpeed(), currentLevel) == false)
                {
                    this.thisPlayer.setX(this.thisPlayer.getX() - this.thisPlayer.getSpeed());
                }
                //Change the animation to the walking left animation
                this.thisPlayer.setCurrentAnimation(this.thisPlayer.WALK_LEFT_ANIMATION);
                this.thisPlayer.isStill = false;
            }
            if (controls.W)
            {
                //movePlayerUp(this.thisPlayer.getSpeed());
                if (Collision.tileCollisionUp(this.thisPlayer, this.thisPlayer.getSpeed(), currentLevel) == false)
                {
                    this.thisPlayer.setY(this.thisPlayer.getY() - this.thisPlayer.getSpeed());
                }
                //Change animation to walking up animation

                this.thisPlayer.isStill = false;
            }
            if (controls.S)
            {
                //movePlayerDown(this.thisPlayer.getSpeed());
                if (Collision.tileCollisionDown(this.thisPlayer, this.thisPlayer.getSpeed(), currentLevel))
                {
                    jumping = false;
                    this.thisPlayer.isStill = true;
                    yVelocity = maxY_Velocity;
                }
                else
                {
                    this.thisPlayer.setY(this.thisPlayer.getY() + this.thisPlayer.getSpeed());
                }

                //Change animation to walking down animation

                this.thisPlayer.isStill = false;
            }


            //If not moving, the player is standing still, which will display a different image
            if (!controls.D && !controls.A && !controls.W && !controls.S && !jumping)
            {
                this.thisPlayer.isStill = true;
            }


            try
            {
                Thread.sleep(this.sleep);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Stops the run loop which ends the thread
     */
    public void stop()
    {
        running = false;
    }

}
