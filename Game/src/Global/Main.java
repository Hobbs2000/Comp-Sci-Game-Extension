package Global;
import Enemies.*;
import PlayerStuff.Player;
import PlayerStuff.PlayerHandler;
import World.Level;
import Graphics.Renderer;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.*;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Calvin on 4/7/2016.
 *
 */
public class Main extends Canvas implements Runnable
{
    public JFrame frame;
    private boolean running = false;
    private ArrayList<Entity> entities = new ArrayList<Entity>();
    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    private Level level;
    private final int WAIT = 10; //All thread sleeps need to be the same time to prevent visible stuttering

    /**
     *
     */
    public Main()
    {
        Dimension size = new Dimension(1920, 1080);
        //setPreferredSize is in the canvas class
        setPreferredSize(size);
        frame = new JFrame();
    }

    /**
     * Creates a new Main object and starts it
     */
    public static void main(String[] args)
    {
        //Checks the Java version that the user has installed
        String JDK_version = System.getProperty("java.version").toString();
        System.out.println("Current Java Version: "+JDK_version);
        //Checks the folder it is contained in
        //Deals with setting up error and info text files and getting the path of the running jar program
        PrintWriter err_writer = null;
        PrintWriter info_writer = null;
        try
        {
            //Have to use scanner to go through the path and remove the jar file from it so it can be used for other purposes
            String path = URLDecoder.decode(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getPath().toString(), "UTF-8");
            System.out.println("Path of JAR: " + path);
            Scanner checker = new Scanner(path).useDelimiter("\\\\");
            int count = 0;
            while (checker.hasNext())
            {
                count++;
                checker.next();
            }
            checker.close();
            checker = new Scanner(path).useDelimiter("\\\\");
            String realPath = "";
            for (int i = 0; i < count-1; i++)
            {
                realPath += checker.next()+"\\";
            }
            System.out.println("Containing Folder: " + realPath);

            //Checks to see if an error text file exists or not
            //If not, creates an error text file
            File err_file = new File(realPath+"err.txt");
            if(!err_file.exists())
            {
                //Creates a new err txt file
                System.out.println("Creating new err txt file");
                err_writer = new PrintWriter(realPath+"err.txt", "UTF-8");
                err_file = new File(realPath+"err.txt");
            }
            //Creates new printWriter to write errors to the err file
            err_writer = new PrintWriter(new FileWriter(err_file, true));

            //Checks to see if an info text file exists or not
            //If not, creates an info text file
            File info_file = new File(realPath+"info.txt");
            if(!info_file.exists())
            {
                //Creates a new err txt file
                System.out.println("Creating new info txt file");
                info_writer = new PrintWriter(realPath+"info.txt", "UTF-8");
                info_file =  new File(realPath+"info.txt");
            }
            //Creates new printWriter to write to info file
            info_writer = new PrintWriter(new FileWriter(info_file, true));
            //Write the Version of Java the user had installed when running the program
            info_writer.println("Java "+JDK_version);
            info_writer.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        //If the version is not 1.8.0_60 or greater then the user is given an error that says their version needs to be updated
        if (System.getProperty("java.version").toString().compareTo("1.8.0_60") < 0)
        {
            System.err.println("ERR - UPDATE JAVA TO LATEST VERSION \nJava needs to be at least version 1.8.0_60 \nPress enter to exit");
            //Write error to error file
            err_writer.println("ERR - NEED UPDATE TO LATEST VERSION OF JAVA");
            err_writer.flush();
            new Scanner(System.in).nextLine();
            return;
        }
        else
        {
            System.out.print("This version is... acceptable");
        }

        //Start the program
        Main main = new Main();
        main.frame.setResizable(false);
        main.frame.setTitle("Game");
        main.frame.add(main);
        main.frame.pack();

        main.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        main.frame.setLocationRelativeTo(null);
        main.frame.setVisible(true);

        //Make the window not have to be clicked on to get input (Set is as the main focus when it begins)
        main.requestFocusInWindow();

        //Start the program
        main.start();
    }

    /**
     * Adds the player to the first index of enities
     * Sets up everything to for the player, including creating and starting the playerHandler
     * Sets up an enemy spawner and starts it
     * Creates a renderer and starts rendering
     * Begins the main thread
     */
    public synchronized void start()
    {
        running = true;

        try
        {
            level = new Level(ImageIO.read(getClass().getResource("/background_1.png")));
            level.createWorld(getWidth(), getHeight());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        //Starting all player stuff
        entities.add(new Player(400, 140, 2.7));
        Controls controls = new Controls();
        super.addKeyListener(controls);
        PlayerHandler playerHandler = new PlayerHandler((Player)entities.get(0), entities, projectiles, WAIT, frame, controls, level);
        Thread playerThread = new Thread(playerHandler);
        playerThread.start();

        //Starts spawning enemies
        //EnemySpawner spawner = new EnemySpawner(entities, projectiles, level);
        //spawner.startZombieSpawner(frame);
        entities.add(new Zombie(200, 450, 3));
        new Thread(new EnemyHandler((Enemy)entities.get(entities.size()-1),entities, projectiles, 50, frame, level)).start();

        //Starts managing projectiles
        for (int i = 0; i < entities.size(); i++)
        {
            if (entities.get(i) instanceof Projectile)
            {
                projectiles.add((Projectile)entities.get(i));
            }
        }
        ProjectileHandler P_handler= new ProjectileHandler(projectiles, level);
        new Thread(P_handler).start();

        //Start rendering everything
        Renderer renderer = new Renderer(this, frame, entities, projectiles, level);
        Thread renderThread = new Thread(renderer);
        renderThread.start();


        //Start main thread to keep program running
        Thread mainThread = new Thread(this);
        mainThread.start();
    }

    /**
     *
     */
    public void run()
    {
        while (running)
        {
            for (Entity entity : entities)
            {
                //This DOES NOT DRAW the entities to the screen
                //It increases the animation count at a consistant rate thanks to the delay
                //This stops faster computers causing the animations to go faster than intended, and vice versa
                entity.animate();
            }


            try
            {
                Thread.sleep(WAIT);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
