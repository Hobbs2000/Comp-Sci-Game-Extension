package Graphics;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Calvin on 4/8/2016.
 * Holds a series of sprites and animates them as draw() is called
 */
public class Animation
{
    private Sprite[] sprites;
    private int sectionWidth, sectionHeight;
    private BufferedImage parentSpriteSheet;
    private int x, y;
    private double scale;
    private int startImageIndex;
    private int currentIndex = 0;
    private double maxStall;
    private double currentStall = 0;

    /**
     * Default constructor for Animation
     * @param spriteSheet The parentSprite sheet that the smaller sprite images are part of
     * @param subWidth The width of each sprite square
     * @param subHeight The height of each sprite square
     * @param number_of_images How many images are on the sprite sheet
     * @param startX Where the x-coords animation is initially set (expected to be changed)
     * @param startY Where the y-coords animation is initially set (expected to be changed)
     * @param scale How much larger or smaller the images will be displayed
     * @param stall The spacing between the changing of the next image
     */
    public Animation(BufferedImage spriteSheet, int subWidth, int subHeight, int number_of_images, int startX, int startY, double scale, double stall)
    {
        parentSpriteSheet = spriteSheet;
        sectionWidth = subWidth;
        sectionHeight = subHeight;
        x = startX;
        y = startY;
        this.scale = scale;
        sprites = new Sprite[number_of_images];
        maxStall = stall;
        setupImages();
    }

    /**
     * Everytime this is called the animation num varible increments, 
     * This is the speed that the animation goes through the sprites
     */
    public void increaseCount()
    {
        if (currentStall >= maxStall)
        {
            currentStall = 0;
            nextFrame();
        }
        currentStall++;
    }

    /**
     *
     */
    public void draw(Graphics g)
    {
        //Scale the image when drawing it
        g.drawImage(sprites[currentIndex].getImage(), x, y, (int) (sprites[currentIndex].getWidth() * scale), (int) (sprites[currentIndex].getHeight() * scale), null);
    }

    /**
     * Changes the image at currentIndex to be displayed
     */
    public void nextFrame()
    {
        currentIndex++;

        if (currentIndex >= sprites.length)
        {
            currentIndex = 0;
        }
    }

    /**
     * Will update the x and y so the images are drawn in the same location as the entity
     * Must be called every time the entity's location in moved
     */
    public void update(int newX, int newY)
    {
        this.x = newX;
        this.y = newY;
    }

    /**
     * Returns the sprite image at the index in the sprites array
     * @param index
     * @return
     */
    public BufferedImage imageAtIndex(int index)
    {
        if (index >= sprites.length)
        {
            return sprites[sprites.length - 1].getImage();
        }

        return sprites[index].getImage();
    }

    /**
     * Will run once
     * Fills in the sprites array with the sub-images from the parent sprite-sheet
     */
    private void setupImages()
    {
        int spriteIndex = 0;
        for(int row = 0; row < parentSpriteSheet.getHeight(); row += sectionHeight)
        {
            for (int col = 0; col < parentSpriteSheet.getWidth(); col += sectionWidth)
            {
                if (spriteIndex > sprites.length-1) {break;}

                sprites[spriteIndex] = new Sprite(parentSpriteSheet, col, row, sectionWidth, sectionHeight, scale);
                spriteIndex++;
            }
            if (spriteIndex > sprites.length-1) {break;}
        }
    }



}
