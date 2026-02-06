package view.utils;

import java.awt.Image;

/**
 * Handles entity animations.
 */
public final class Animation {
    private final Image[] frames;
    private final long frameDuration;
    private int currentFrame;
    private long lastFrameTime = -1;

    /**
     * class constructor.
     *
     * @param frames number of frames used for animation
     * @param frameDuration the time intervall between frame changes
     */
    public Animation(final Image[] frames, final long frameDuration) {
        if (frames == null || frames.length == 0) {
            throw new IllegalArgumentException("frames vuoto");
        }
        this.currentFrame = 0;
        this.frames = new Image[frames.length];
        for (int i = 0; i < frames.length; i++) {
        this.frames[i] = frames[i];
        }
        this.frameDuration = frameDuration;
    }

    /**
     * Updates and returns the current animation frame.
     *
     * @param nowMillis time passed since the game started.
     * @return the current frame of the animation
     */
    public Image getFrame(final long nowMillis) {
        //first frame
        if (lastFrameTime < 0) {
            lastFrameTime = nowMillis;
            return frames[currentFrame];
        }

        final long timePassed = nowMillis - lastFrameTime;
        if (timePassed >= frameDuration) {
            final long steps = timePassed / frameDuration; //used in case of bugs (steps usually is equal to 1)
            currentFrame = (int) ((currentFrame + steps) % frames.length);
            lastFrameTime += steps * frameDuration;
        }
        return frames[currentFrame];
    }
}
