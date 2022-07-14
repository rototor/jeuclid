package net.sourceforge.jeuclid.layout;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

public class Path2DShapeObject implements GraphicsObject
{

    private final Path2D path2D;
    private final float width;

    private final Color col;

    private final boolean dash;
    private final EdgeStyle edgeStyle;

    public enum EdgeStyle
    {
        Miter, Round
    }

    public Path2DShapeObject(Path2D path2D, float width, Color col, boolean dash,
            EdgeStyle edgeStyle)
    {
        this.path2D = path2D;
        this.width = width;
        this.col = col;
        this.dash = dash;
        this.edgeStyle = edgeStyle;
    }

    @Override
    public void paint(float x, float y, Graphics2D g)
    {
        g.setColor(this.col);
        final Stroke oldStroke = g.getStroke();
        int cap = BasicStroke.CAP_SQUARE;
        int join = BasicStroke.JOIN_MITER;
        switch (edgeStyle)
        {
        case Round:
            cap = BasicStroke.CAP_ROUND;
            join = BasicStroke.JOIN_ROUND;
            break;
        case Miter:
        default:
            break;
        }
        if (this.dash)
        {
            final float dashWidth = 3.0f * this.width;
            g.setStroke(new BasicStroke(this.width, cap, join, this.width,
                    new float[] { dashWidth, dashWidth, }, 0));
        }
        else
        {
            g.setStroke(new BasicStroke(this.width, cap, join));
        }
        AffineTransform transform = g.getTransform();
        g.translate(x, y);
        g.draw(this.path2D);
        g.setTransform(transform);
        g.setStroke(oldStroke);
    }
}
