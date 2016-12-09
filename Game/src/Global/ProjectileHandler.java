package Global;

import World.Level;

import javax.swing.text.AbstractDocument;
import java.util.ArrayList;

/**
 * Created by Calvin on 6/28/2016.
 */
public class ProjectileHandler implements Runnable
{
    private boolean running = false;
    private ArrayList<Projectile> projectiles;
    private Level currentLevel;
    private int size = 0;

    /**
     *
     * @param projectiles
     */
    public ProjectileHandler(ArrayList<Projectile> projectiles, Level level)
    {
        this.projectiles = projectiles;
        this.currentLevel = level;
        size = projectiles.size();
    }

    /**
     *
     */
    public void run()
    {
        running = true;

        while (running)
        {

            synchronized (projectiles)
            {
                for (int i = 0; i < projectiles.size(); i++)
                {
                    //Going UP
                    if (projectiles.get(i).getDirection() == 1)
                    {
                        projectiles.get(i).update(projectiles.get(i).getX(), projectiles.get(i).getY() - projectiles.get(i).getSpeed());
                        if (Collision.tileCollisionUp(projectiles.get(i), 1, this.currentLevel))
                        {
                            projectiles.remove(i);
                        }
                    }
                    //Going RIGHT
                    else if (projectiles.get(i).getDirection() == 2)
                    {
                        projectiles.get(i).update(projectiles.get(i).getX() + projectiles.get(i).getSpeed(), projectiles.get(i).getY());
                        if (Collision.tileCollisionRight(projectiles.get(i), 1, this.currentLevel))
                        {
                            projectiles.remove(i);
                        }
                    }
                    //Going DOWN
                    else if (projectiles.get(i).getDirection() == 3)
                    {
                        projectiles.get(i).update(projectiles.get(i).getX(), projectiles.get(i).getY() + projectiles.get(i).getSpeed());
                        if (Collision.tileCollisionDown(projectiles.get(i), 1, this.currentLevel))
                        {
                            projectiles.remove(i);
                        }
                    }
                    //Going LEFT
                    else if (projectiles.get(i).getDirection() == 4)
                    {
                        projectiles.get(i).update(projectiles.get(i).getX() - projectiles.get(i).getSpeed(), projectiles.get(i).getY());
                        if (Collision.tileCollisionLeft(projectiles.get(i), 1, this.currentLevel))
                        {
                            projectiles.remove(i);
                        }
                    }
                    else
                    {
                        projectiles.get(i).update(projectiles.get(i).getX() + projectiles.get(i).getSpeed(), projectiles.get(i).getY());
                    }


                }
            }


            try
            {
                Thread.sleep(10);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }


    }
}
