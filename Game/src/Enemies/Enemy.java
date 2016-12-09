package Enemies;

import Global.Entity;

/**
 * Created by Calvin on 4/7/2016.
 * Will provide the basis for all enemies
 */
public class Enemy extends Entity
{
    private int damage, health, speed;

    /**
     *
     * @param newDamage
     * @param newHealth
     * @param newSpeed
     * @param newX
     * @param newY
     * @param width
     * @param height
     */
    public Enemy(int newDamage, int newHealth, int newSpeed, int newX, int newY, int width, int height)
    {
        super(newX, newY, width, height);
        this.damage = newDamage;
        this.health = newHealth;
        this.speed  = newSpeed;
    }

    /**
     * Returns the speed of the enemy
     * @return The speed of the enemy
     */
    public int getSpeed()
    {
        return this.speed;
    }

    /**
     * Returns the damage dealt out per a hit by the enemy
     * @return The damage the enemy deals to the player
     */
    public int getDamage()
    {
        return this.damage;
    }

    /**
     * All enemies are collidable
     * @return True because all enemies are collidable
     */
    public boolean isCollidable()
    {
        return true;
    }

    /**
     *
     * @return
     */
    public int getHealth()
    {
        return this.health;
    }

    /**
     *
     * @param newHealth
     */
    public void setHealth(int newHealth)
    {
        this.health = newHealth;
    }
}
