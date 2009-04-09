/*
 * Copyright 2002 - 2007 JEuclid, http://jeuclid.sf.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* $Id$ */

package euclid.layout;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;
import org.w3c.dom.*;

import euclid.DOMBuilder;
import euclid.LayoutContext;
import euclid.context.Parameter;
import euclid.elements.presentation.token.Mo;

/**
 * @version $Revision$
 */
public class JEuclidView implements AbstractView, LayoutView, EventListener {

	private final Document oDoc;
    private final LayoutableDocument document;
    private final Map<Node, LayoutInfo> layoutMap;
    private final LayoutContext context;
    private final Graphics2D graphics;

    /**
     * Default Constructor.
     * 
     * @param node
     *            document to layout.
     * @param layoutGraphics
     *            Graphics context to use for layout calculations. This should
     *            be compatible to the context used for painting, but does not
     *            have to be the same.
     * @param layoutContext
     *            layoutContext to use.
     */
    // node ist normal das document, layoutContext in MathComponentUI parameters
    public JEuclidView(final Document oDoc, final Node node, final LayoutContext layoutContext,
            final Graphics2D layoutGraphics) {
     
    	this.oDoc = oDoc;
    	if (node instanceof LayoutableDocument) {
    		document = (LayoutableDocument) node;
        } else {
            document = DOMBuilder.getInstance().createJeuclidDom(node);
        }
        graphics = layoutGraphics;
        context = layoutContext;
        layoutMap = new HashMap<Node, LayoutInfo>();
    }

    /** {@inheritDoc} */
    public DocumentView getDocument() {
        return this.document;
    }

    /**
     * Draw this view onto a Graphics context.
     * 
     * @param x
     *            x-offset for left edge
     * @param y
     *            y-offset for baseline
     * @param g
     *            Graphics context for painting. Should be compatible to the
     *            context used during construction, but does not have to be
     *            the same.
     */
    public void draw(final Graphics2D g, final float x, final float y) {
    	
    	layout(document, LayoutStage.STAGE2, context);
        RenderingHints hints = g.getRenderingHints();
        if ((Boolean) (context.getParameter(Parameter.ANTIALIAS))) {
            hints.add(new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON));
        }
        hints.add(new RenderingHints(RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_NORMALIZE));
        hints.add(new RenderingHints(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY));
        g.setRenderingHints(hints);

        final boolean debug = (Boolean) (context
                .getParameter(Parameter.DEBUG));
        drawNode(document, g, x, y, debug);

    }

    private void drawNode(final LayoutableNode node, final Graphics2D g,
            final float x, final float y, final boolean debug) {
    	final LayoutInfo myInfo = getInfo(node);
//    	final float x1 = x;
//        final float x2 = x + myInfo.getWidth(LayoutStage.STAGE2);
//        final float y1 = y - myInfo.getAscentHeight(LayoutStage.STAGE2);
//        final float y2 = y + myInfo.getDescentHeight(LayoutStage.STAGE2);
//        g.setColor(Color.BLUE);
//        g.draw(new Line2D.Float(x1, y1, x2, y1));
//        g.draw(new Line2D.Float(x1, y1, x1, y2));
//        g.draw(new Line2D.Float(x2, y1, x2, y2));
//        g.draw(new Line2D.Float(x1, y2, x2, y2));
//        g.setColor(Color.RED);
//        g.draw(new Line2D.Float(x1, y, x2, y));
    	
    	// zeichnet die Zahlenwerte, die Buchstaben und Operatoren
        for (final GraphicsObject go : myInfo.getGraphicObjects()) {
        	go.paint(x, y, g);
        }
        // wohl eher Behälter wie mn, mo, mi, mrow ...
        for (final LayoutableNode child : node.getChildrenToDraw()) {
        	final LayoutInfo childInfo = this.getInfo(child);
            drawNode(child, g,
                    x + childInfo.getPosX(LayoutStage.STAGE2), 
                    y + childInfo.getPosY(LayoutStage.STAGE2), debug);
        }
    }

    private LayoutInfo layout(final LayoutableNode node,
            final LayoutStage toStage, final LayoutContext parentContext) {
        final LayoutInfo info = this.getInfo(node);

        if (node instanceof EventTarget) {
        	final EventTarget evtNode = (EventTarget) node;
            evtNode.addEventListener("DOMSubtreeModified", this, false);
            evtNode.addEventListener(Mo.MOEVENT, this, false);
        }

        if (LayoutStage.NONE.equals(info.getLayoutStage())) {
        	LayoutStage childMinStage = LayoutStage.STAGE2;
            int count = 0;
            for (final LayoutableNode l : node.getChildrenToLayout()) {
                final LayoutInfo in = layout(l, LayoutStage.STAGE1, node
                        .getChildLayoutContext(count, parentContext));
                count++;
                if (LayoutStage.STAGE1.equals(in.getLayoutStage())) {
                    childMinStage = LayoutStage.STAGE1;
                }
            }
            node.layoutStage1(this, info, childMinStage, parentContext);
        }
        if (LayoutStage.STAGE1.equals(info.getLayoutStage())
                && LayoutStage.STAGE2.equals(toStage)) {
        	int count = 0;
            for (final LayoutableNode l : node.getChildrenToLayout()) {
                this.layout(l, LayoutStage.STAGE2, node
                        .getChildLayoutContext(count, parentContext));
                count++;
            }
            node.layoutStage2(this, info, parentContext);
        }
        return info;
    }

    /** {@inheritDoc} */
    public LayoutInfo getInfo(final LayoutableNode node) {
        if (node == null) {
            return null;
        }
        LayoutInfo info = this.layoutMap.get(node);
        if (info == null) {
            info = new LayoutInfoImpl();
            this.layoutMap.put(node, info);
        }
        return info;
    }

    /**
     * @return width of this view.
     */
    public float getWidth() {
        final LayoutInfo info = this.layout(this.document,
                LayoutStage.STAGE2, this.context);
        return info.getWidth(LayoutStage.STAGE2);
    }

    /**
     * @return ascent height.
     */
    public float getAscentHeight() {
        final LayoutInfo info = this.layout(this.document,
                LayoutStage.STAGE2, this.context);
        return info.getAscentHeight(LayoutStage.STAGE2);
    }

    /**
     * @return descent height.
     */
    public float getDescentHeight() {
        final LayoutInfo info = this.layout(this.document,
                LayoutStage.STAGE2, this.context);
        return info.getDescentHeight(LayoutStage.STAGE2);
    }

    /** {@inheritDoc} */
    public Graphics2D getGraphics() {
        return this.graphics;
    }

    /** {@inheritDoc} */
    public void handleEvent(final Event evt) {
        final EventTarget origin = evt.getCurrentTarget();
        if (origin instanceof LayoutableNode) {
            final LayoutableNode lorigin = (LayoutableNode) origin;
            final LayoutInfo info = this.getInfo(lorigin);
            info.setLayoutStage(LayoutStage.NONE);
        }
    }

    class Node2{
    	public Node oNode; 
    	public LayoutableNode lNode;
    	public Node2(Node oNode, LayoutableNode lNode){
    		this.oNode = oNode; 
    		this.lNode = lNode;
    	}
    }
    
    public Node getNode(int xToFind, int yToFind){ 
    	Node2 node2 = new Node2(oDoc, document);
    	float ascend = getInfo(document).getAscentHeight(LayoutStage.STAGE2);
    	node2 = getLastNode(node2, 0f, ascend, xToFind, yToFind);
    	if (node2==null) {
    		return null;
    	} else {
    		return node2.oNode;
    	}
    }
    
    private Node2 getLastNode(Node2 node, float xStart, float yStart, int x, int y){
    	if (node!=null && node.lNode!=null){
    		LayoutInfo myInfo = getInfo(node.lNode);
    		float x1 = xStart;
            float x2 = xStart + myInfo.getWidth(LayoutStage.STAGE2);
            float y1 = yStart - myInfo.getAscentHeight(LayoutStage.STAGE2);
            float y2 = yStart + myInfo.getDescentHeight(LayoutStage.STAGE2);
//            System.out.println("SearchNode" + node.lNode.getNodeName());
            // im Node
            if (x1<=x && x<=x2 && y1<=y && y<=y2){
            	// evtl sogar im FirstChild oder dessen Brüdern
            	if (node.lNode.getFirstChild()!=null && node.oNode.getFirstChild()!=null && node.lNode.getFirstChild() instanceof org.w3c.dom.Element) {
            		LayoutableNode child = (LayoutableNode) node.lNode.getFirstChild();
            		LayoutInfo childInfo = this.getInfo(child);
                    float childbaseX = xStart + childInfo.getPosX(LayoutStage.STAGE2); 
                    float childbaseY = yStart + childInfo.getPosY(LayoutStage.STAGE2);
                    return getLastNode(new Node2(node.oNode.getFirstChild(), child), childbaseX, childbaseY, x, y);
            	}
            	// wenn nicht, bin ich der Treffer oder mein rechter Sibling
            	System.out.println("Node zurück");
            	if ("mo".equals(node.oNode.getNodeName()) && node.lNode.getNextSibling()!=null && node.oNode.getNextSibling()!=null){
        			return new Node2(node.oNode.getNextSibling(), (LayoutableNode) node.lNode.getNextSibling());
            	} else {
            		return node;
            	}
            }
        	// evtl in einem Bruder
        	if (node.lNode.getNextSibling()!=null && node.oNode.getNextSibling()!=null) {	
        		LayoutableNode sibling = (LayoutableNode) node.lNode.getNextSibling();
        		if (sibling instanceof org.w3c.dom.Element) {
        			LayoutInfo sibInfo = this.getInfo(sibling);
        			float sibbaseX = xStart + sibInfo.getPosX(LayoutStage.STAGE2)-myInfo.getPosX(LayoutStage.STAGE2); 
        			float sibbaseY = yStart + sibInfo.getPosY(LayoutStage.STAGE2)-myInfo.getPosY(LayoutStage.STAGE2);
        			
        			return getLastNode(new Node2(node.oNode.getNextSibling(), sibling), sibbaseX, sibbaseY, x, y);
        		}
        	}
        	if (node.lNode.getParentNode()!=null && node.lNode.getParentNode() instanceof org.w3c.dom.Element){
        		System.out.println("ParentNode zurück");
        		return new Node2(node.oNode.getParentNode(), (LayoutableNode) node.lNode.getParentNode());
        	}
    	}
//    	System.out.println("getLastNode mit null aufgerufen" + (node==null));
    	return null;
    } 
    
    public float getXMax(){  	
    	LayoutInfo myInfo = getInfo(document);
    	return myInfo.getWidth(LayoutStage.STAGE2);
    }
    
    public float getYMax(){  	
    	LayoutInfo myInfo = getInfo(document);
    	return myInfo.getDescentHeight(LayoutStage.STAGE2);
    }
    
    public float getYMin(){  	
    	LayoutInfo myInfo = getInfo(document);
    	return myInfo.getAscentHeight(LayoutStage.STAGE2);
    }
    
}
