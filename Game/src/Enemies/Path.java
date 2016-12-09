package Enemies;

import Global.Collision;
import Global.Entity;
import World.Level;
import World.Tile;

import java.awt.*;
import java.util.ArrayList;

/**
 * This class deals with all things path finding for enemies
 * Created by Calvin on 7/3/2016.
 */
public class Path
{
    private Level currentLevel;
    private Entity ent;
    private Tile[] allTiles;

    private ArrayList<Tile> closedTiles = new ArrayList<>();
    private ArrayList<Integer> closedHScores = new ArrayList<>();
    private ArrayList<Integer> closedGScores = new ArrayList<>();

    private ArrayList<Tile> openTiles = new ArrayList<>();
    private ArrayList<Integer> openHScores = new ArrayList<>();
    private ArrayList<Integer> openGScores = new ArrayList<>();

    private Tile[] sizeBasedGrid;

    /**
     *
     * @param ent
     * @param level
     */
    public Path(Entity ent, Level level)
    {
        this.ent = ent;
        this.currentLevel = level;
        allTiles = level.get1DTilesArray();
    }



    /**
     * This creates a grid where each tile is the size passed in
     * This is used for entity path making with entities that are different sizes than the main tiles of the world
     * Prerequisite: Needs to fit evenly into the size of the screen
     * @param entityWidth
     * @param entityHeight
     */
    public void CreateSizeBasedGrid(int entityWidth, int entityHeight, int screenWidth, int screenHeight)
    {

        int gridWidth = screenWidth/entityWidth;
        int gridHeight = screenHeight/entityHeight;

        sizeBasedGrid = new Tile[gridWidth*gridHeight];

        //X = (i % gridWidth)*tileWidth
        //Y = (i / gridWidth)*tileHeight
        for (int i = 0; i < sizeBasedGrid.length; i++)
        {
            int x = (i % gridWidth)*entityWidth;
            int y = (i / gridWidth)*entityHeight;

            sizeBasedGrid[i] = new Tile(x, y, entityWidth, entityHeight);
        }

        //Adds these tiles to the path tiles arrayList for the currentLevel
        for (int i = 0; i < sizeBasedGrid.length; i++)
        {
            currentLevel.pathTiles.add(sizeBasedGrid[i]);
        }
    }

    /**
     * Finds a path of tiles that leads to the destination coordinates
     * Only picks tiles that can be reached by the entity (uses the size based grid for that)
     * @param xDest
     * @param yDest
     */
    public void findSizeBasedGridPath(int xDest, int yDest)
    {
        //Empties the closed tile list
        closedTiles.clear();

        int startX = (int)ent.getCenter().getX();
        int startY = (int)ent.getCenter().getY();

        Tile startTile = null;
        Tile endTile = null;

        //Get all the tiles in the current level in a 1D array
        Tile[] levelTiles = currentLevel.get1DTilesArray();

        //Finds the starting and ending tiles for the path
        //Uses the sizeBasedGrid
        for (int i = 0; i < sizeBasedGrid.length; i++)
        {
            if (Collision.PointWithinEntity(startX, startY, sizeBasedGrid[i]))
            {
                startTile = sizeBasedGrid[i];
            }

            if (Collision.PointWithinEntity(xDest, yDest, sizeBasedGrid[i]))
            {
                endTile = sizeBasedGrid[i];
            }
        }

        //If either the startTile or the EndTile could not be found,
        //then the method ends
        if (startTile == null || endTile == null)
        {
            return;
        }

        closedTiles.add(startTile);
        int closedIndex = 0;
        int openIndex = 0;

        //Continues repeating until the latest tile added to the closedTiles list is the endTile
        while (closedTiles.get(closedTiles.size() - 1) != endTile)
        {
            if (closedIndex >= closedTiles.size() || (closedTiles.size() > 1 && closedTiles.get(closedIndex) == closedTiles.get(closedIndex - 1))) //|| closedTiles.size() > 100)
            {
                break;
            }
            Tile currentTile = closedTiles.get(closedIndex);

            Tile[] possibleTiles = new Tile[4];
            int[] fScores = new int[4];

            //Get the tile Above the current tile
            possibleTiles[0] = this.arrayGetTileAt(sizeBasedGrid, currentTile.getCenterX(), currentTile.getCenterY() - currentTile.getHeight());
            //Calc fScore of tile above the current tile (currently just the distance between the above tile and the end tile + the above tile and the starting tile)
            fScores[0] = Collision.getTotalDist(possibleTiles[0].getCenterX(), possibleTiles[0].getCenterY(), endTile.getCenterX(), endTile.getCenterY()) + Collision.getTotalDist(possibleTiles[0].getCenterX(), possibleTiles[0].getCenterY(), startTile.getCenterX(), startTile.getCenterY());

            //Get the tile Below the current
            possibleTiles[1] = this.arrayGetTileAt(sizeBasedGrid, currentTile.getCenterX(), currentTile.getCenterY() + currentTile.getHeight());
            //Calc the fScore of the tile below the current tile (currently just the distance between the below tile and the end tile + the below tile and the starting tile)
            fScores[1] = Collision.getTotalDist(possibleTiles[1].getCenterX(), possibleTiles[1].getCenterY(), endTile.getCenterX(), endTile.getCenterY()) + Collision.getTotalDist(possibleTiles[1].getCenterX(), possibleTiles[1].getCenterY(), startTile.getCenterX(), startTile.getCenterY());

            //Get the tile to the Right of the current tile
            possibleTiles[2] = this.arrayGetTileAt(sizeBasedGrid, currentTile.getCenterX() + currentTile.getWidth(), currentTile.getCenterY());
            //Calc the fScore of the tile to the Right of the current tile (currently just the distance between to the Right tile and the end tile + the Right tile and starting tile)
            fScores[2] = Collision.getTotalDist(possibleTiles[2].getCenterX(), possibleTiles[2].getCenterY(), endTile.getCenterX(), endTile.getCenterY()) + Collision.getTotalDist(possibleTiles[2].getCenterX(), possibleTiles[2].getCenterY(), startTile.getCenterX(), startTile.getCenterY());

            //Get the tile to the Left of the current tile
            possibleTiles[3] = this.arrayGetTileAt(sizeBasedGrid, currentTile.getCenterX() - currentTile.getWidth(), currentTile.getCenterY());
            //Calc the fScore of the tile to the Left of the current tile (currently just the distance between to the Left tile and the end tile + the Left tile and the starting tile)
            fScores[3] = Collision.getTotalDist(possibleTiles[3].getCenterX(), possibleTiles[3].getCenterY(), endTile.getCenterX(), endTile.getCenterY()) + Collision.getTotalDist(possibleTiles[3].getCenterX(), possibleTiles[3].getCenterY(), startTile.getCenterX(), startTile.getCenterY());


            //Sort both the possibleTiles list and fScores list based on the fScores list
            //Sorted from low to high (closest to end tile to furthest)
            int min;
            for (int i = 0; i < fScores.length-1; i++)
            {
                min = i;
                for (int j = i+1; j < fScores.length; j++)
                {
                    if (fScores[j] < fScores[min])
                    {
                        min = j;
                    }
                }
                int tempF = fScores[i];
                Tile tempTile = possibleTiles[i];

                fScores[i] = fScores[min];
                possibleTiles[i] = possibleTiles[min];

                fScores[min] = tempF;
                possibleTiles[min] = tempTile;
            }

            boolean obstructed0 = false;
            boolean obstructed1 = false;
            boolean obstructed2 = false;
            boolean obstructed3 = false;
            //Determine which tiles cannot be used because solid level tiles are in the way
            //Checks the possible tiles against all the world tiles
            for (int i = 0; i < levelTiles.length; i++)
            {
                if (levelTiles[i].isSolid())
                {
                    if (Collision.collided(possibleTiles[0], levelTiles[i]))
                    {
                        obstructed0 = true;
                    }

                    if (Collision.collided(possibleTiles[1], levelTiles[i]))
                    {
                        obstructed1 = true;
                    }

                    if (Collision.collided(possibleTiles[2], levelTiles[i]))
                    {
                        obstructed2 = true;
                    }

                    if (Collision.collided(possibleTiles[3], levelTiles[i]))
                    {
                        obstructed3 = true;
                    }
                }
            }

            //Choose the tile to be added to the path
            if (!obstructed0 && !isIn(possibleTiles[0], closedTiles))
            {
                closedTiles.add(possibleTiles[0]);
                possibleTiles[0].makeRed(); //Makes the tile red for testing purposes
                closedIndex++;
            }
            else if (!obstructed1 && !isIn(possibleTiles[1], closedTiles))
            {
                closedTiles.add(possibleTiles[1]);
                possibleTiles[1].makeRed(); //Makes the tile red for testing purposes
                closedIndex++;
            }
            else if (!obstructed2 && !isIn(possibleTiles[2], closedTiles))
            {
                closedTiles.add(possibleTiles[2]);
                possibleTiles[2].makeRed(); //Makes the tile red for testing purposes
                closedIndex++;
            }
            else if (!obstructed3 && !isIn(possibleTiles[3], closedTiles))
            {
                closedTiles.add(possibleTiles[3]);
                possibleTiles[3].makeRed(); //Makes the tile red for testing purposes
                closedIndex++;
            }
            else
            {
                break;
            }
        }
    }


    /**
     * Uses a version of the A* path finding algorithm to plot a path to the specified location
     */
    public void findPath(int xDest, int yDest)
    {
        closedTiles.clear();

        int startX = (int)ent.getCenter().getX();
        int startY = (int)ent.getCenter().getY();

        Tile startTile = currentLevel.getTile(startX, startY);
        Tile endTile = currentLevel.getTile(xDest, yDest);


        closedTiles.add(startTile);
        int closedIndex = 0;
        int openIndex = 0;


        while (closedTiles.get(closedTiles.size() - 1) != endTile)
        {
            if (closedIndex >= closedTiles.size() || (closedTiles.size() > 1 && closedTiles.get(closedIndex) == closedTiles.get(closedIndex - 1)) || closedTiles.size() > 100)
            {
                break;
            }
            Point closedCenter = closedTiles.get(closedIndex).getCenterPoint();

            Tile[] possibleTiles = new Tile[4];
            int[] fScores = new int[4];
            
            //Above
            possibleTiles[0] = currentLevel.getTile((int) closedCenter.getX(), (int) closedCenter.getY() - closedTiles.get(closedIndex).getHeight());
            fScores[0] = (Collision.getTotalDist((int)closedCenter.getX(), (int)closedCenter.getY(), possibleTiles[0].getX(), possibleTiles[0].getY())) + (Collision.getTotalDist(endTile.getX(), endTile.getY(), possibleTiles[0].getX(), possibleTiles[0].getY()));

            //Below
            possibleTiles[1] = currentLevel.getTile((int)closedCenter.getX(), (int)closedCenter.getY() + closedTiles.get(closedIndex).getHeight());
            fScores[1] = (Collision.getTotalDist((int)closedCenter.getX(), (int)closedCenter.getY(), possibleTiles[1].getX(), possibleTiles[1].getY())) + (Collision.getTotalDist(endTile.getX(), endTile.getY(), possibleTiles[1].getX(), possibleTiles[1].getY()));

            //Right
            possibleTiles[2] = currentLevel.getTile((int)closedCenter.getX() + closedTiles.get(closedIndex).getWidth(), (int)closedCenter.getY());
            fScores[2] = (Collision.getTotalDist((int)closedCenter.getX(), (int)closedCenter.getY(), possibleTiles[2].getX(), possibleTiles[2].getY())) + (Collision.getTotalDist(endTile.getX(), endTile.getY(), possibleTiles[2].getX(), possibleTiles[2].getY()));

            //Left
            possibleTiles[3] = currentLevel.getTile((int)closedCenter.getX() - closedTiles.get(closedIndex).getWidth(), (int)closedCenter.getY());
            fScores[3] = (Collision.getTotalDist((int)closedCenter.getX(), (int)closedCenter.getY(), possibleTiles[3].getX(), possibleTiles[3].getY())) + (Collision.getTotalDist(endTile.getX(), endTile.getY(), possibleTiles[3].getX(), possibleTiles[3].getY()));

            //Sort both the possibleTiles list and fScores list based on the fScores list
            //Sorted from low to high
            int min;
            for (int i = 0; i < fScores.length-1; i++)
            {
                min = i;
                for (int j = i+1; j < fScores.length; j++)
                {
                    if (fScores[j] < fScores[min])
                    {
                        min = j;
                    }
                }
                int tempF = fScores[i];
                Tile tempTile = possibleTiles[i];

                fScores[i] = fScores[min];
                possibleTiles[i] = possibleTiles[min];

                fScores[min] = tempF;
                possibleTiles[min] = tempTile;
            }

            if (possibleTiles[0].isSolid() == false &&
                    !isIn(possibleTiles[0], closedTiles))
            {
                closedTiles.add(possibleTiles[0]);
                possibleTiles[0].makeRed();
                closedIndex++;
            }
            else if (possibleTiles[1].isSolid() == false &&
                    !isIn(possibleTiles[1], closedTiles))
            {
                closedTiles.add(possibleTiles[1]);
                possibleTiles[1].makeRed();
                closedIndex++;
            }
            else if (possibleTiles[2].isSolid() == false &&
                    !isIn(possibleTiles[2], closedTiles))
            {
                closedTiles.add(possibleTiles[2]);
                possibleTiles[2].makeRed();
                closedIndex++;
            }
            else if (possibleTiles[3].isSolid() == false &&
                    !isIn(possibleTiles[3], closedTiles))
            {
                closedTiles.add(possibleTiles[3]);
                possibleTiles[3].makeRed();
                closedIndex++;
            }
            else
            {
                break;
            }


        }

    }

    /**
     * Clears the current path array of tiles
     */
    public void clearCurrentPath()
    {
        closedTiles.clear();
    }

    /**
     * Returns the current path that was found
     * @return
     */
    public Tile[] getPathArray()
    {
        Tile[] path = new Tile[closedTiles.size()];

        for (int i = 0; i < closedTiles.size(); i++)
        {
            path[i] = closedTiles.get(i);
        }

        return path;
    }


    /**
     *
     * @param tile
     * @param tileList
     * @return
     */
    private boolean isIn(Tile tile, Tile[] tileList)
    {
        for (int i = 0; i < tileList.length; i++)
        {
            if (tileList[i] == tile)
            {
                return true;
            }
        }

        return false;
    }

    /**
     *
     * @param tile
     * @param tileList
     * @return
     */
    private boolean isIn(Tile tile, ArrayList<Tile> tileList)
    {
        for (int i = 0; i < tileList.size(); i++)
        {
            if (tileList.get(i) == tile)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the tile from the passed in array of tiles that contains
     * the point coordinates passed in
     * @param tiles
     * @param x
     * @param y
     * @return
     */
    private Tile arrayGetTileAt(Tile[] tiles, int x, int y)
    {
        for (int i = 0; i < tiles.length; i++)
        {
            if (Collision.PointWithinEntity(x, y, tiles[i]))
            {
                return tiles[i];
            }
        }

        return null;
    }
}
