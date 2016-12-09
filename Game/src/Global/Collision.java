package Global;

import World.Level;
import World.Tile;

import java.util.ArrayList;

/**
 * A static final class that is used for collision detection
 * Created by Calvin on 4/15/2016.
 */
public final class Collision
{
    /**
     * Checks to see if entity1 is overlapping with entity2 
     * Not to be used for collision with tiles and solid objects
     * @param entity1 The current entity
     * @param entity2 The entity that entity1 is checking for a collision with
     * @return Returns if there is a collision or not
     */
    public static boolean collided(Entity entity1, Entity entity2)
    {
        int top1= entity1.getY();
        int bottom1 = entity1.getY() + entity1.getHeight();
        int right1 = entity1.getX() + entity1.getWidth();
        int left1 = entity1.getX();

        int top2 = entity2.getY();
        int bottom2 = entity2.getY() + entity2.getHeight();
        int right2 =entity2.getX() + entity2.getWidth();
        int left2 = entity2.getX();

        if ((left1 < right2 && right1 > left2 && top1 < bottom2 && bottom1 > top2))
        {
            return true;
        }
        return false;
    }

    /**
     * Returns the x-coordinate distance of two entities
     * @param obj1
     * @param obj2
     */
    public static int getXDist(Entity obj1, Entity obj2)
    {
        return (int)Math.abs((obj1.getX() - obj2.getX()));
    }

    /**
     *
     * @param obj1
     * @param obj2
     * @return
     */
    public static int getCenterXDist(Entity obj1, Entity obj2)
    {
       return (int)Math.abs(obj1.getCenter().getX() - obj2.getCenter().getX());
    }

    /**
     *
     * @param obj1
     * @param obj2
     * @return
     */
    public static int getCenterYDist(Entity obj1, Entity obj2)
    {
        return (int)Math.abs(obj1.getCenter().getY() - obj2.getCenter().getY());
    }


    /**
     *
     * @param obj1
     * @param obj2
     * @return
     */
    public static int getYDist(Entity obj1, Entity obj2)
    {
        return (int)Math.abs((obj1.getY() - obj2.getY()));
    }

    /**
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static int getTotalDist(int x1, int y1, int x2, int y2)
    {
       return (int)Math.sqrt((int) ((Math.pow((x2 - x1), 2)) + Math.pow((y2 - y1), 2)));
    }

    /**
     *
     * @param obj1
     * @param obj2
     * @return
     */
    public static int getTotalDist(Entity obj1, Entity obj2)
    {
        int x1 = obj1.getX();
        int y1 = obj1.getY();
        int x2 = obj2.getX();
        int y2 = obj2.getY();

        return getTotalDist(x1, y1, x2, y2);
    }

    /**
     * Gets the coordinates of 3 points along the right side of the bounding box of the obj (top right, middle right, bottom right)
     * Checks to see if any of those points would collide with a solid tile if the obj were to move dx to the right
     * If no collision, return false
     * @param obj
     * @param dx
     * @param level
     * @return
     */
    public static boolean tileCollisionRight(Entity obj, int dx, Level level)
    {
        //Top corner right y
        int tcRightY = obj.getY();
        //Top corner right x
        int tcRightX = obj.getX() + obj.getWidth();

        //Bottom corner right y
        int bcRightY = obj.getY() + obj.getHeight();
        //Bottom corner right x
        int bcRightX = tcRightX;

        //Testing the middle point is only neccesary if the obj is wider than a standard tile
        //Bottom middle y
        int rMiddleY = obj.getY() + (obj.getHeight() / 2);
        //Bottom middle x
        int rMiddleX = tcRightX;

        Tile tile1 = level.getTile(tcRightX + dx, tcRightY);
        Tile tile2 = level.getTile(bcRightX + dx, bcRightY);
        Tile tile3 = level.getTile(rMiddleX + dx, rMiddleY);

        if ((tile1 != null && tile1.isSolid()) || (tile2 != null && tile2.isSolid()) || (tile3 != null && tile3.isSolid()))
        {
            //Has collided
            return true;
        }
        else
        {
            //Did not collided
            return false;
        }
    }

    /**
     * Gets the coordinates of 3 points along the left side of the obj's bounding box (top left, middle left, bottom left)
     * Checks to see if any of those points would collide with a solid tile if the obj were to move dx amount to the left
     * If no collision, return false
     * @param obj
     * @param dx
     * @param level
     * @return
     */
    public static boolean tileCollisionLeft(Entity obj, int dx, Level level)
    {
        //Top left corner left y
        int tcLeftY = obj.getY();
        //Top corner right x
        int tcLeftX = obj.getX();

        //Bottom left corner right y
        int bcLeftY = obj.getY() + obj.getHeight();
        //Bottom corner right x
        int bcLeftX = obj.getX();

        //Testing the middle point is only neccesary if the obj is wider than a standard tile
        //Bottom middle y
        int lMiddleY = obj.getY() + (obj.getHeight() / 2);
        //Bottom middle x
        int lMiddleX = obj.getX();

        Tile tile1 = level.getTile(tcLeftX - dx, tcLeftY);
        Tile tile2 = level.getTile(bcLeftX - dx, bcLeftY);
        Tile tile3 = level.getTile(lMiddleX - dx, lMiddleY);

        if ((tile1 != null && tile1.isSolid()) || (tile2 != null && tile2.isSolid()) || (tile3 != null && tile3.isSolid()))
        {
            //Did collided
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Gets the coordinates of 3 points along the bottom of the obj's bounding box (bottom right, bottom middle, bottom left)
     * Checks to see if any of those points would collide with a solid tile if the obj were to move dy amount down
     * If no collision, return false
     * @param obj
     * @param dy
     * @param level
     * @return
     */
    public static boolean tileCollisionDown(Entity obj, int dy, Level level)
    {
        //Bottom corner left y
        int bcLeftY = obj.getY() + obj.getHeight();

        //Bottom corner right y
        int bcRightY = bcLeftY;
        //Bottom corner right x
        int bcRightX = obj.getX() + obj.getWidth();

        //Testing the middle point is only neccesary if the obj is wider than a standard tile
        //Bottom middle y
        int bMiddleY = bcLeftY;
        //Bottom middle x
        int bMiddleX = obj.getX() + (obj.getWidth() / 2);

        Tile tile1 = level.getTile(obj.getX(), bcLeftY + dy);
        Tile tile2 = level.getTile(bcRightX, bcRightY + dy);
        Tile tile3 = level.getTile(bMiddleX, bMiddleY + dy);

        if ((tile1 != null && tile1.isSolid()) || (tile2 != null && tile2.isSolid()) || (tile3 != null && tile3.isSolid()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Gets the coordinates of 3 points along the top of the obj's bounding box (top right, top middle, top left)
     * will check to see if any of those points would collide with a solid tile if the obj were to move dy amount up.
     * If no collision, return false
     * @param obj
     * @param dy
     * @param level
     * @return
     */
    public static boolean tileCollisionUp(Entity obj, int dy, Level level)
    {
        //Top corner left y
        int tcLeftY = obj.getY();
        //Top corner left x
        int tcLeftX = obj.getX();

        //Bottom corner right y
        int tcRightY = tcLeftY;
        //Bottom corner right x
        int tcRightX = obj.getX() + obj.getWidth();

        //Testing the middle point is only neccesary if the obj is wider than a standard tile
        //Bottom middle y
        int tMiddleY = tcLeftY;
        //Bottom middle x
        int tMiddleX = obj.getX() + (obj.getWidth() / 2);

        Tile tile1 = level.getTile(tcLeftX, tcLeftY - dy);
        Tile tile2 = level.getTile(tcRightX, tcRightY - dy);
        Tile tile3 = level.getTile(tMiddleX, tMiddleY - dy);
        if ((tile1 != null && tile1.isSolid()) || (tile2 != null && tile2.isSolid()) || (tile3 != null && tile3.isSolid()))
        {
            return true;
        }
        else 
        {
            return false;
        }
    }

    /**
     * Checks to see if the given point's coordinates are within the given entity's bounding box
     * @param x1
     * @param y1
     * @param ent2
     * @return
     */
    public static boolean PointWithinEntity(int x1, int y1, Entity ent2)
    {
        int top2 = ent2.getY();
        int bottom2 = ent2.getY() + ent2.getHeight();
        int right2 = ent2.getX() + ent2.getWidth();
        int left2 = ent2.getX();

        if (x1 >= left2 && x1 <= right2 && y1 >= top2 && y1 <= bottom2)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}