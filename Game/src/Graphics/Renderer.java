package Graphics;
import Global.Entity;
import Global.Projectile;
import PlayerStuff.Player;
import World.Level;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Calvin on 4/27/2016
 * Draws everything onto the current canvas by calling the draw methods of all the entities and level tiles
 * Does this on a separate thread
 * Creates a triple buffered BufferStrategy to get the graphics context from
 */
public class Renderer implements Runnable
{
    private Canvas thisCanvas;
    private ArrayList<Entity> entities;
    private ArrayList<Projectile> projectiles;
    private JFrame frame;
    private Level currentLevel;
    private boolean running = false;
    private Animation explosion;

    /**
     * @param canvas The canvas to draw onto
     * @param currentFrame The current frame the canvas is on
     * @param entities The ArrayList of all the entities to be drawn
     * @param level The current level object
     */
    public Renderer(Canvas canvas, JFrame currentFrame, ArrayList<Entity> entities, ArrayList<Projectile> projectiles, Level level)
    {
        thisCanvas = canvas;
        frame = currentFrame;
        this.entities = entities;
        this.projectiles = projectiles;
        currentLevel = level;
    }

    /**
     * Repeatedly calls the renderAll() method
     */
    public void run()
    {
        try
        {
            explosion = new Animation(ImageIO.read(getClass().getResource("/originalBasicExplosion.png")), 15, 15, 9, 200, 400, 5, 50);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        running = true;
        while (running)
        {
            renderAll();
        }
    }

    /**
     * Displays the graphics on the screen using active rendering by creating a BufferStrategy
     * Will repeat as many times as possible in a second (no cap) 
     */
    public void renderAll()
    {
        BufferStrategy bs = this.thisCanvas.getBufferStrategy();
        //Checks to see if the BufferStrategy has already been created, it only needs to be created once
        if (bs == null)
        {
            //Always do triple buffering when possible (put 3 in the param)
            this.thisCanvas.createBufferStrategy(3);
            return;
        }

        //Links the bufferStrategy and graphics, creating a graphics context
        //Everything that deals with graphics is between the next line and g.dispose()
        Graphics g = bs.getDrawGraphics();

        //Set background to white
        g.setColor(Color.white);
        g.fillRect(0, 0, frame.getWidth(), frame.getHeight());

        //Draw the level background for this level
        currentLevel.drawBackground(g);

        //Draw the tiles in the world
        currentLevel.drawWorld(g);

        //Synchronized to avoid a concurrent modification error
        //Draws all of the entities, such as the enemies
        synchronized (entities)
        {
            for (Entity entity : entities)
            {
                if (entity.hasAnimation() == true && !(entity instanceof Player))
                {
                    //Animates every entity
                    entity.draw(g);
                }
            }
        }

        //Draws all of the projectiles
        synchronized (projectiles)
        {
            for (Projectile projectile : projectiles)
            {
                projectile.draw(g);
            }
        }


        //Player is drawn on the top most layer
        entities.get(0).draw(g);

        //Manually drawing an explosion for testing
        explosion.draw(g);
        explosion.increaseCount();

        g.dispose();

        //Swap out and destroy old buffer(Frame) and show the new one
        bs.show();
    }
}