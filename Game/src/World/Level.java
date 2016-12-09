
package World;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Calvin on 4/17/2016.
 * This is what the player and all entities will be in
 */
public class Level
{
    //This is all of the world tiles
    //They are arranged in how they will be displayed on screen
    private Tile[][] worldTiles;
    public ArrayList<Tile> pathTiles = new ArrayList<Tile>();

    private BufferedImage background;

    /**
     *
     */
    public Level(BufferedImage background)
    {
        this.background = background;
    }

    /**
     * Generates a 2d array of ints, 
     * and each int in the array stands for a different tile, and each
     * value determines which tile will be in that position.
     * After the 2d int array is created, it is passed into the interperetWorld method
     */
    public void createWorld(int screenWidth, int screenHeight)
    {
        //This randomly creates a level (could be a really garbage level)
        //If the level is terrible, just restart until a decent one is created


        int widthTiles = screenWidth / 32;
        int heightTiles = screenHeight / 32;
        int[][] worldCode = new int[heightTiles][widthTiles];
        int currentPlatformLen = 0;

        for (int r = 0; r < worldCode.length; r++)
        {
            for (int c = 0; c < worldCode[r].length; c++)
            {
                if ((r == 0 || c == 0 || r == (worldCode.length - 1) || c == (worldCode[r].length - 1)))
                {
                    worldCode[r][c] = 2;
                }
                else if ((currentPlatformLen <= 5 && r >= 4))
                {
                    //If the tile directly above is empty, make this tile a top soil tile
                    if (((r-1) >= 0) && worldCode[r-1][c] == 0)
                    {
                        worldCode[r][c] = 1;
                    }
                    //If the tile directly above is not empty, make this a regular grass tile 
                    else if (((r-1) >= 0) && worldCode[r-1][c] != 0)
                    {
                        worldCode[r][c] = 2;
                    }
                    currentPlatformLen++;
                }
                else
                {
                    worldCode[r][c] = 0;
                }

                //Decides if a new Platform will be made
                int makeNewPlatform = (int)(Math.random() * 100);
                if ((makeNewPlatform > 20 && makeNewPlatform < 23) && currentPlatformLen >= 5)
                {
                    currentPlatformLen = 0;
                }
            }
        }


        /*
        for (int r  = 0; r < worldCode.length; r++)
        {
            for (int c = 0; c < worldCode[r].length; c++)
            {
                worldCode[r][c] = 0;
            }
        }




        //Each number in the code array represents a different type of tile
        //This is a preset level

/*
        int[][] worldCode = {
                {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},
                {2,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
                {2,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
                {2,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
                {2,0,0,0,0,0,0,2,0,0,0,0,0,0,2},
                {2,0,0,0,0,0,0,2,0,0,0,0,0,0,2},
                {2,0,0,0,0,0,0,2,0,0,0,0,0,0,2},
                {2,0,0,0,0,0,0,2,0,0,0,0,0,0,2},
                {2,0,0,0,0,0,0,2,0,0,0,0,0,0,2},
                {2,0,0,0,0,0,0,2,0,0,0,0,0,0,2},
                {2,0,0,0,0,0,0,2,0,0,0,0,0,0,2},
                {2,0,0,0,0,0,0,2,0,0,0,0,0,0,2},
                {2,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
                {2,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
                {2,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
                {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},
        };
*/


        //This turns the simple list of ints into a list of tiles
        worldTiles = Level.interpretWorld(worldCode);
    }

    /**
     * Draws all the tiles that are not null on the screen
     * @param g The graphics context
     */
    public void drawWorld(Graphics g)
    {
        for (int r = 0; r < worldTiles.length; r++)
        {
            for (int c = 0; c < worldTiles[r].length; c++)
            {
                if (worldTiles[r][c] != null)
                {
                    worldTiles[r][c].draw(g);
                }
            }
        }

        for (int i = 0; i < pathTiles.size(); i++)
        {
            pathTiles.get(i).draw(g);
        }
    }

    /**
     * Draws the background image for the level
     * Should be drawn before everything else
     */
    public void drawBackground(Graphics g)
    {
        g.drawImage(background, 0,0, null);
    }

    /**
     * Gets the tile at the specified coordinates,
     * If no tile at those coordinates it returns a null reference
     * @param x The x coordinate (not the col index) 
     * @param y The y coordinate (not the row index)
     * @return
     */
    public Tile getTile(int x, int y)
    {
        for (int r = 0; r < worldTiles.length; r++)
        {
            for (int c = 0; c < worldTiles[r].length; c++)
            {
                if (worldTiles[r][c] != null)
                {
                    int right = worldTiles[r][c].getX() + worldTiles[r][c].getWidth();
                    int bottom = worldTiles[r][c].getY() + worldTiles[r][c].getHeight();

                    if (x >= worldTiles[r][c].getX() && x <= right && y >= worldTiles[r][c].getY() && y <= bottom)
                    {
                        return worldTiles[r][c];
                    }
                }
            }
        }
        return null;
    }

    /**
     * Creates and returns a 2d array of Tiles, which are then initialized based on what values are in 
     * the 2d array of ints code
     * @param code A 2d array of ints used to determine what will be put in the Tiles array
     * @return worldTiles - A 2d array of Tiles 
     */
    public static Tile[][] interpretWorld(int[][] code)
    {
        Tile[][] worldTiles = new Tile[code.length][code[0].length];

        for (int r = 0; r < code.length; r++)
        {
            for (int c = 0; c < code[r].length; c++)
            {
                int tileInt = code[r][c];

                switch (tileInt)
                {
                    case 1:
                        worldTiles[r][c] = new TopSoil((c*32), (r*32));
                        break;
                    case 2:
                        worldTiles[r][c] = new Grass((c*32), (r*32));
                        break;
                    default:
                        worldTiles[r][c] = new nothingTile((c*32), (r*32));
                        break;
                }
            }
        }

        return worldTiles;
    }

    /**
     *
     * @return
     */
    public Tile[] get1DTilesArray()
    {
        Tile[] allTiles = new Tile[worldTiles.length * worldTiles[0].length];
        int count = 0;
        for (int r = 0; r < worldTiles.length; r ++)
        {
            for (int c = 0; c < worldTiles[r].length; c ++)
            {
                allTiles[count] = worldTiles[r][c];
                count++;
            }
        }

        return allTiles;
    }

    /**
     *
     * @return
     */
    public Tile[][] get2DTilesArray()
    {
        return worldTiles;
    }

    /**
     *
     * @param x
     * @param y
     */
    public void highlightTileAt(int x, int y)
    {
        Tile tile = getTile(x, y);
        if (tile != null)
        {
            tile.isBlue = true;
        }
    }

}
