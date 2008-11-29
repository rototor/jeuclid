package net.sourceforge.jeuclid.test.testsuite;

import java.io.Serializable;

public class RenderInfo implements Serializable {
    private static final float FUZZYNESS = 0.1f;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final String elementName;

    private final float ascent;

    private final float descent;

    private final float width;

    private final float posx;

    private final float posy;

    public RenderInfo(final String elementName, final float ascent,
            final float descent, final float width, final float posx,
            final float posy) {
        super();
        if (elementName == null) {
            this.elementName = "";
        } else {
            this.elementName = elementName;
        }
        this.ascent = ascent;
        this.descent = descent;
        this.width = width;
        this.posx = posx;
        this.posy = posy;
    }

    public boolean isSimilar(final RenderInfo other) {
        boolean isSim = true;
        isSim &= (this.elementName.equals(other.elementName));
        isSim &= (this.isClose(this.ascent, other.ascent));
        isSim &= (this.isClose(this.descent, other.descent));
        isSim &= (this.isClose(this.width, other.width));
        isSim &= (this.isClose(this.posx, other.posx));
        isSim &= (this.isClose(this.posy, other.posy));
        return isSim;
    }

    private boolean isClose(final float should, final float is) {
        final float maxdelta = Math.max(Math.abs(should
                * RenderInfo.FUZZYNESS), 0.1f);
        return Math.abs(should - is) <= (maxdelta);
    }

    public String getElementName() {
        return this.elementName;
    }

}
