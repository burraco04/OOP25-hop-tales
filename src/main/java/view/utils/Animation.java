package view.utils;

import java.awt.Image;

/**
 * calls for animate the entities.
 */
public final class Animation {
    private final Image[] frames;
    private final long frameDuration;
    private int currentFrame;
    private long lastFrameTime = -1;

    /**
     * constructor.
     *
     * @param frames number of frames for entity
     * @param frameDuration time for change frame
     */
    public Animation(final Image[] frames, final long frameDuration) {
        if (frames == null || frames.length == 0) {
            throw new IllegalArgumentException("frames vuoto");
        }
        this.currentFrame = 0;
        this.frames = frames;
        this.frameDuration = frameDuration;
    }

    /**
     * change the frame.
     *
     * @param nowMillis time passed whe the game had started.
     * @return the new frame for the entity.
     */
    public Image getFrame(final long nowMillis) {
        //first frame
        if (lastFrameTime < 0) {
            lastFrameTime = nowMillis;
            return frames[currentFrame];
        }
        
        final long elapsed = nowMillis - lastFrameTime;
        if (elapsed >= frameDuration) {
            final long steps = elapsed / frameDuration; 
            currentFrame = (int) ((currentFrame + steps) % frames.length);
            lastFrameTime += steps * frameDuration;
        }
        return frames[currentFrame];
    }
}
