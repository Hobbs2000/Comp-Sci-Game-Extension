package Enemies;

import Global.Entity;
import Global.Projectile;
import World.Level;
import World.Tile;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by Calvin on 4/10/2016.
 * Periodically spawns enemies
 * Has different spawner methods for different types of enemies
 */
public class EnemySpawner
{

    private ArrayList<Entity> entities;
    private ArrayList<Projectile> projectiles;
    private int sleep;

    private boolean zombieSpawing = false;

    Level currentLevel;

    private JFrame frame;

    /**
     *
     * @param startEntities
     */
    public EnemySpawner(ArrayList<Entity> startEntities, ArrayList<Projectile> projectiles, Level level)
    {
        currentLevel = level;
        this.entities = startEntities;
        this.projectiles = projectiles;
    }

    public void startZombieSpawner(JFrame frame)
    {
        this.frame = frame;
        zombieSpawing = true;
        new Thread(() -> zombieSpawner()).start();
    }

    /**
     * Periodically adds a new zombie to the main entity list
     */
    public void zombieSpawner()
    {
        while (zombieSpawing)
        {
            try
            {
                Thread.sleep(2500);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            //Repeatidly pick a coordinate and check to see if it will collide with any tiles
            //If not, then spawn the enemy in that location
            boolean hasSpawned = false;
            do
            {
                int x = (int)(Math.random() * frame.getWidth());
                int y = (int)(Math.random() * frame.getHeight());

                //TODO: get rid of these magic numbers
                int topRightY = y;
                int topRightX = x + 100;

                int bottomLeftY = y + 100;
                int bottomLeftX = x;

                int bottomRightY = y + 100;
                int bottomRightX = x + 100;

                Tile tile1 = currentLevel.getTile(x, y);
                Tile tile2 = currentLevel.getTile(topRightX, topRightY);
                Tile tile3 = currentLevel.getTile(bottomRightX, bottomRightY);
                Tile tile4 = currentLevel.getTile(bottomLeftX, bottomLeftY);

                if ((tile1 != null && tile1.isSolid()) ||
                        (tile2 != null && tile2.isSolid()) ||
                        (tile3 != null && tile3.isSolid()) ||
                        (tile4 != null && tile4.isSolid()))
                {
                    hasSpawned = true;
                    //Any thread that uses/changes the entities ArrayList must be in a synchronized code block
                    //This is because the list is spread across multiple threads
                    //and synchronization prevents two threads trying to change the list at the same time
                    synchronized (entities)
                    {
                        //Adds a new zombie and creates a handler for it
                        entities.add(new Zombie(x, y, 3));
                        int index = entities.size() - 1;
                        new Thread(new EnemyHandler((Enemy)entities.get(entities.size()-1),entities, projectiles, 50, frame, currentLevel)).start();
                    }
                }
            } while(!hasSpawned);
        }
    }
}
