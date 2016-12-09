package Enemies;

import Global.Collision;
import Global.Entity;
import Global.Projectile;
import PlayerStuff.Player;
import World.Level;
import World.Tile;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by Calvin on 4/8/2016.
 * Handles a single enemy's values in a separate thread
 * Will manage updating one enemy and checking for collisions
 * A lot of the logic will be here
 */
public class EnemyHandler implements Runnable
{
    private ArrayList<Entity> entities;
    private Player player;
    private ArrayList<Projectile> projectiles;
    private int sleep;

    private boolean moverRunning = false;

    private Path path;
    private Tile[] pathArray;
    private int pathIndex;
    private int newPathDelay = 20;

    private int changeDirTime = 20;
    private int x_direction = 0;
    private int y_direction = 0;

    private Level currentLevel;

    private Enemy thisEnemy;

    public int frameWidth, frameHeight;

    /**
     *
     */
    public EnemyHandler(Enemy handledEnemy, ArrayList<Entity> allEntities, ArrayList<Projectile> projectiles, int sleep, JFrame currentFrame, Level level)
    {
        //this.enemyList = enemies;
        this.entities = allEntities;
        this.projectiles = projectiles;
        this.thisEnemy = handledEnemy;
        this.sleep = sleep;
        this.frameWidth = currentFrame.getWidth();
        this.frameHeight = currentFrame.getHeight();
        this.currentLevel = level;
        path = new Path(thisEnemy, currentLevel);

        //Get the player out of the entities list
        this.player = (Player)entities.get(0);
    }

    /**
     *
     */
    public void run()
    {
        moverRunning = true;

        //Gets the starting path for this enemy
        //path.findPath(this.player.getX(), this.player.getY()); //This way of path finding does not take the entities size into account (this creates several issues)
        path.CreateSizeBasedGrid(thisEnemy.getWidth(), thisEnemy.getHeight(), frameWidth, frameHeight); //Must create a size based grid before finding a size based grid path
        path.findSizeBasedGridPath((int) this.player.getCenter().getX(), (int) this.player.getCenter().getY());
        pathArray = path.getPathArray();
        pathIndex = 1;
        newPathDelay = 10;

        while(moverRunning)
        {
            //Gets the path
            /*
            if (newPathDelay <= 0)
            {
                path.findSizeBasedGridPath((int) this.player.getCenter().getX(), (int) this.player.getCenter().getY());
                //path.findPath((int) this.player.getCenter().getX(), (int) this.player.getCenter().getY());
                pathArray = path.getPathArray();
                pathIndex = 1;
                newPathDelay = 10;
            }
            newPathDelay--;
            */

            if (!(pathIndex >= pathArray.length))
            {
                int targetX = pathArray[pathIndex].getX();
                int targetY = pathArray[pathIndex].getY();
            }
            else
            {
                pathIndex = pathArray.length-1;
            }


            boolean rightCollision = Collision.tileCollisionRight(this.thisEnemy, this.thisEnemy.getSpeed(), currentLevel);
            boolean leftCollision = Collision.tileCollisionLeft(this.thisEnemy, this.thisEnemy.getSpeed(), currentLevel);
            boolean upCollision = Collision.tileCollisionUp(this.thisEnemy, this.thisEnemy.getSpeed(), currentLevel);
            boolean downCollision = Collision.tileCollisionDown(this.thisEnemy, this.thisEnemy.getSpeed(), currentLevel);

            int centerXDist = Collision.getCenterXDist(this.thisEnemy, pathArray[pathIndex]);
            int centerYDist = Collision.getCenterYDist(this.thisEnemy, pathArray[pathIndex]);


            //Should/can go right
            if (Collision.tileCollisionRight(this.thisEnemy, this.thisEnemy.getSpeed(), currentLevel) == false &&
                    ((Math.abs((this.thisEnemy.getCenter().getX() + this.thisEnemy.getSpeed()) - pathArray[pathIndex].getCenterX()) < Math.abs(this.thisEnemy.getCenter().getX() - pathArray[pathIndex].getCenterX())) ||
                            (Math.abs(Collision.getCenterYDist(this.thisEnemy, pathArray[pathIndex]) - Collision.getCenterXDist(this.thisEnemy, pathArray[pathIndex])) > 14 && Math.abs(Collision.getCenterYDist(this.thisEnemy, pathArray[pathIndex]) - Math.abs((this.thisEnemy.getCenter().getX()+this.thisEnemy.getSpeed()) - pathArray[pathIndex].getCenterX())) < Math.abs(Collision.getCenterYDist(this.thisEnemy, pathArray[pathIndex]) - Collision.getCenterXDist(this.thisEnemy, pathArray[pathIndex])))))
            {
                this.thisEnemy.setX(this.thisEnemy.getX() + this.thisEnemy.getSpeed());
            }
            //Should/can go left
            else if (Collision.tileCollisionLeft(this.thisEnemy, this.thisEnemy.getSpeed(), currentLevel) == false &&
                    ((Math.abs((this.thisEnemy.getCenter().getX() - this.thisEnemy.getSpeed()) - pathArray[pathIndex].getCenterX())) < Math.abs(this.thisEnemy.getCenter().getX() - pathArray[pathIndex].getCenterX()) ||
                           (Math.abs(Collision.getCenterYDist(this.thisEnemy, pathArray[pathIndex]) - Collision.getCenterXDist(this.thisEnemy, pathArray[pathIndex])) > 14 && Math.abs(Collision.getCenterYDist(this.thisEnemy, pathArray[pathIndex]) - Math.abs((this.thisEnemy.getCenter().getX() - this.thisEnemy.getSpeed()) - pathArray[pathIndex].getCenterX())) < Math.abs(Collision.getCenterYDist(this.thisEnemy, pathArray[pathIndex]) - Collision.getCenterXDist(this.thisEnemy, pathArray[pathIndex])))))
            {
                this.thisEnemy.setX(this.thisEnemy.getX() - this.thisEnemy.getSpeed());
            }
            //Should/can go up
            if (Collision.tileCollisionUp(this.thisEnemy, this.thisEnemy.getSpeed(), currentLevel) == false &&
                    ((Math.abs((this.thisEnemy.getY() - this.thisEnemy.getSpeed()) - pathArray[pathIndex].getY()) < Math.abs(this.thisEnemy.getCenter().getY() - pathArray[pathIndex].getCenterY())) ||
                            (Math.abs(Collision.getCenterYDist(this.thisEnemy, pathArray[pathIndex]) - Collision.getCenterXDist(this.thisEnemy, pathArray[pathIndex])) > 14 && Math.abs(Collision.getCenterXDist(this.thisEnemy, pathArray[pathIndex]) - Math.abs((this.thisEnemy.getCenter().getY() - this.thisEnemy.getSpeed()) - pathArray[pathIndex].getCenterY())) < Math.abs(Collision.getCenterYDist(this.thisEnemy, pathArray[pathIndex]) - Collision.getCenterXDist(this.thisEnemy, pathArray[pathIndex])))))
            {
                this.thisEnemy.setY(this.thisEnemy.getY() - this.thisEnemy.getSpeed());
            }
            //Should/can go down
            else if (Collision.tileCollisionDown(this.thisEnemy, this.thisEnemy.getSpeed(), currentLevel) == false &&
                    (Math.abs((this.thisEnemy.getY() + this.thisEnemy.getSpeed()) - pathArray[pathIndex].getY()) < Math.abs(this.thisEnemy.getCenter().getY() - pathArray[pathIndex].getCenterY())))
            {
                this.thisEnemy.setY(this.thisEnemy.getY() + this.thisEnemy.getSpeed());
            }

            if (Collision.getTotalDist(this.thisEnemy, pathArray[pathIndex]) < 16)
            {
                pathIndex++;
                //System.out.println("next");
            }
            else
            {
                //System.out.println("X Dist: "+Collision.getCenterXDist(this.thisEnemy, pathArray[pathIndex])+"  Y Dist: "+Collision.getCenterYDist(this.thisEnemy, pathArray[pathIndex]));

            }


            //ALL OF THIS COMMENTED CODE IS FOR MOVING WITHOUT PATH FINDING AND UPDATED COLLISION TESTING
            //Will only have the possibility to change direction if the changeDirTime is 0 or below
            /*
            if (changeDirTime <= 0)
            {
                //Gets a random number that will determine which direction the enemy goes
                x_direction = (int)(Math.random() * 2);
                y_direction = (int)(Math.random() * 3);
                changeDirTime = 20;
            }
            changeDirTime --;

            /*
            //Move the correct direction
            if (x_direction <= 0)
            {
                //moveEnemyRight(10);
                if (Collision.tileCollisionRight(this.thisEnemy, this.thisEnemy.getSpeed(), currentLevel) == false)
                {
                    this.thisEnemy.setX(this.thisEnemy.getX() + this.thisEnemy.getSpeed());
                }
            }
            else if (x_direction >= 1)
            {
                //moveEnemyLeft(10);
                if (Collision.tileCollisionLeft(this.thisEnemy, this.thisEnemy.getSpeed(), currentLevel) == false)
                {
                    this.thisEnemy.setX(this.thisEnemy.getX() - this.thisEnemy.getSpeed());
                }
            }
            if(y_direction == 1)
            {
                //moveEnemyDown(10);
                if (Collision.tileCollisionDown(this.thisEnemy, this.thisEnemy.getSpeed(), currentLevel) == false)
                {
                    this.thisEnemy.setY(this.thisEnemy.getY() + this.thisEnemy.getSpeed());
                }
            }
            else if (y_direction >= 2)
            {
                //moveEnemyUp(10);
                if (Collision.tileCollisionUp(this.thisEnemy, this.thisEnemy.getSpeed(), currentLevel) == false)
                {
                    this.thisEnemy.setY(this.thisEnemy.getY() - this.thisEnemy.getSpeed());
                }
            }
            */

            //Should/Can go right
            /*
            if (Math.abs((this.thisEnemy.getX() + this.thisEnemy.getSpeed()) - this.player.getX()) < Collision.getCenterXDist(this.thisEnemy, this.player))
            {
                if (Collision.tileCollisionRight(this.thisEnemy, this.thisEnemy.getSpeed(), currentLevel) == false)
                {
                    this.thisEnemy.setX(this.thisEnemy.getX() + this.thisEnemy.getSpeed());
                }
                else
                {
                    if (Collision.tileCollisionLeft(this.thisEnemy, this.thisEnemy.getSpeed(), currentLevel) == false)
                    {
                        this.thisEnemy.setX(this.thisEnemy.getX() - this.thisEnemy.getSpeed());

                        if (Collision.tileCollisionDown(this.thisEnemy, this.thisEnemy.getSpeed(), currentLevel) == false)
                        {
                            this.thisEnemy.setY(this.thisEnemy.getY() + this.thisEnemy.getSpeed());
                        }
                        else if (Collision.tileCollisionUp(this.thisEnemy, this.thisEnemy.getSpeed(), currentLevel) == false)
                        {
                            this.thisEnemy.setY(this.thisEnemy.getY() - this.thisEnemy.getSpeed());
                        }
                    }
                }
            }
            //Should/Can go left
            else if (Collision.tileCollisionLeft(this.thisEnemy, this.thisEnemy.getSpeed(), currentLevel) == false &&
                    (Math.abs((this.thisEnemy.getX() - this.thisEnemy.getSpeed()) - this.player.getX())) < Collision.getCenterXDist(this.thisEnemy, this.player))
            {
                this.thisEnemy.setX(this.thisEnemy.getX() - this.thisEnemy.getSpeed());
            }
            //Should/Can go down
            if (Collision.tileCollisionDown(this.thisEnemy, this.thisEnemy.getSpeed(), currentLevel) == false &&
                    (Math.abs((this.thisEnemy.getY() + this.thisEnemy.getSpeed()) - this.player.getY()) < Collision.getCenterYDist(this.thisEnemy, this.player)))
            {
                this.thisEnemy.setY(this.thisEnemy.getY() + this.thisEnemy.getSpeed());
            }
            //Should/Can go up
            else if (Collision.tileCollisionUp(this.thisEnemy, this.thisEnemy.getSpeed(), currentLevel) == false &&
                    (Math.abs((this.thisEnemy.getY() - this.thisEnemy.getSpeed()) - this.player.getY()) < Collision.getCenterYDist(this.thisEnemy, this.player)))
            {
                this.thisEnemy.setY(this.thisEnemy.getY() - this.thisEnemy.getSpeed());
            }
            */

            //always apply downward gravity (when not jumping)
            //moveEnemyDown(20);

            //Checks for collisions with projectiles
            for (int i = 0; i < projectiles.size(); i++)
            {
                if (Collision.collided(this.thisEnemy, projectiles.get(i)))
                {
                    synchronized (projectiles)
                    {
                        this.thisEnemy.setHealth(this.thisEnemy.getHealth() - projectiles.get(i).getDamage());

                        projectiles.remove(i);

                        //If the enemy dies from this projectile, remove it from the ArrayList
                        if (this.thisEnemy.getHealth() <= 0)
                        {
                            synchronized (entities)
                            {
                                int index = entities.indexOf(this.thisEnemy);
                                entities.remove(index);
                                //Stop the loop to end this thread
                                moverRunning = false;
                            }
                        }
                    }
                }
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

    //Ends all threads for the handler
    public void stopAll()
    {
        moverRunning= false;
    }
}
