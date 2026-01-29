package view.utils;

import java.awt.Image;

import model.GameConstants;

public final class Animation {
    private final Image[] frames;
    private final long frameDuration;
    private int currentFrame;
    private long timeCurrentFrame = 1000000; 

    public Animation(final Image[] frames, final long frameDuration) {
        if (frames == null || frames.length == 0) {
            throw new IllegalArgumentException("frames vuoto");
        }
        this.currentFrame = -1;
        this.frames = frames;
        this.frameDuration = frameDuration;
    }

    public Image getFrame(final long timePassed) {
        timeCurrentFrame += timePassed;
        if( timeCurrentFrame > frameDuration) {
            timeCurrentFrame = 0 ;

            if(currentFrame == frames.length -1){
                currentFrame = -1;
            }
            return frames[++currentFrame];
        }
        return frames[currentFrame];
    }
}
