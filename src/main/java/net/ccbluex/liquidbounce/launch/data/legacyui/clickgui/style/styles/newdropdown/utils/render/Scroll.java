package net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.newdropdown.utils.render;


import net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.newdropdown.utils.animations.Animation;
import net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.newdropdown.utils.animations.Direction;
import net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.newdropdown.utils.animations.impl.SmoothStepAnimation;
import org.lwjgl.input.Mouse;

/**
 * @author cedo
 * @author Foggy
 */
public class Scroll {

    private float maxScroll = Float.MAX_VALUE, minScroll = 0, rawScroll;
    private float scroll;
    private Animation scrollAnimation = new SmoothStepAnimation(0, 0, Direction.BACKWARDS);


    public void onScroll(int ms) {
        scroll = (float) (rawScroll - scrollAnimation.getOutput());
        rawScroll += Mouse.getDWheel() / 4f;
        rawScroll = Math.max(Math.min(minScroll, rawScroll), -maxScroll);
        scrollAnimation = new SmoothStepAnimation(ms, rawScroll - scroll, Direction.BACKWARDS);
    }

    public boolean isScrollAnimationDone() {
        return scrollAnimation.isDone();
    }

    public float getScroll() {
        scroll = (float) (rawScroll - scrollAnimation.getOutput());
        return scroll;
    }

    public void setMaxScroll(Float maxScroll1) {
        maxScroll = maxScroll1;
    }
}
