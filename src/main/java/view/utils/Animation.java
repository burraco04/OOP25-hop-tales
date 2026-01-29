package view.utils;

import java.awt.Image;

import model.GameConstants;

public final class Animation {
    private final Image[] frames;
    private final long frameDuration;
    private int currentFrame;
    private long lastFrameTime = -1;

    public Animation(final Image[] frames, final long frameDuration) {
        if (frames == null || frames.length == 0) {
            throw new IllegalArgumentException("frames vuoto");
        }
        this.currentFrame = 0;
        this.frames = frames;
        this.frameDuration = frameDuration;
    }

    public Image getFrame(final long nowMillis) {
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
