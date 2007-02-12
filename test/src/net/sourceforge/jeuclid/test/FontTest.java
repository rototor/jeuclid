/*
 * Copyright 2002 - 2006 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.test;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;

/**
 * A class for testing font properties.
 *
 * @author <a href="mailto:stephan@vern.chem.tu-berlin.de">Stephan Michels</a>
 * @author <a href="mailto:sielaff@vern.chem.tu-berlin.de">Marco Sielaff</a>
 * @version %I%, %G%
 */
public class FontTest extends Canvas
{

  /**
     * 
     */
    private static final long serialVersionUID = 5813533159577687967L;

/**
   * Paints this component.
   *
   * @param g The graphics to paint to.         
   */
  public void paint(Graphics g)
  {
    // Graphics2D g2 = (Graphics2D) g;
    // Font font = new Font("Courier", Font.PLAIN, 40);
    // Font font = new Font("Default", Font.PLAIN, 14);
    // Font font = new Font("Symbol", Font.PLAIN, 20);
    Font font = new Font("Arial Unicode MS", Font.PLAIN, 20);  

    g.setFont(font);

    /* try
     {
       InputStream is = getClass().getResourceAsStream("cmex10.ttf");
       font = Font.createFont(Font.TRUETYPE_FONT, is);
       font = font.deriveFont(Font.PLAIN, 12);
     } catch (Exception e)
     {
       e.printStackTrace();
     }*/

    /*FontMetrics metrics = getFontMetrics(font);
    Graphics2D g2d = (Graphics2D) g;
    RenderingHints hints = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
                                              RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    // hints.put(RenderingHints.KEY_FRACTIONALMETRICS, fracMetrics);
    g2d.setRenderingHints(hints);

    g2d.setFont(font);

    FontRenderContext frc = g2d.getFontRenderContext();
    Font g2dFont = g2d.getFont();

    GlyphVector gv;
    int[] glyphIndices = new int[ 1 ];

    g.setColor(Color.black);
    g.setFont(font);

    int ascent = metrics.getAscent();
    int leading = metrics.getLeading();
    int maxadvance = metrics.getMaxAdvance();
    int height = metrics.getHeight();
    int descent = metrics.getDescent();

    glyphIndices[ 0 ] = 167;
    gv = g2dFont.createGlyphVector(frc, glyphIndices);
    g2d.drawGlyphVector(gv, 50, 600);
    glyphIndices[ 0 ] = 169;
    gv = g2dFont.createGlyphVector(frc, glyphIndices);
    g2d.drawGlyphVector(gv, 50, 600 + ascent - 1);

    glyphIndices[ 0 ] = 167;
    gv = g2dFont.createGlyphVector(frc, glyphIndices);
    g2d.drawGlyphVector(gv, 150, 600);
    glyphIndices[ 0 ] = 168;
    gv = g2dFont.createGlyphVector(frc, glyphIndices);
    g2d.drawGlyphVector(gv, 150, 600 + ascent - 1);
    glyphIndices[ 0 ] = 169;
    gv = g2dFont.createGlyphVector(frc, glyphIndices);
    g2d.drawGlyphVector(gv, 150, 600 + 2 * ascent - 2);

    glyphIndices[ 0 ] = 183;
    gv = g2dFont.createGlyphVector(frc, glyphIndices);
    g2d.drawGlyphVector(gv, 170, 600);
    glyphIndices[ 0 ] = 184;
    gv = g2dFont.createGlyphVector(frc, glyphIndices);
    g2d.drawGlyphVector(gv, 170, 600 + ascent - 1);
    glyphIndices[ 0 ] = 185;
    gv = g2dFont.createGlyphVector(frc, glyphIndices);
    g2d.drawGlyphVector(gv, 170, 600 + 2 * ascent - 2);*/

    //int y;
    char c;

    /*for (int i = 0; i < 16; i++)
    {
      y = i * (height + 5) + 50;

      g.setColor(Color.red);
      g.drawLine(50, y, 200, y);
      g.drawLine(50, 50, 50, 0);

      g.setColor(Color.blue);
      g.drawLine(50, y - ascent, 700, y - ascent);

      g.setColor(Color.green);
      g.drawLine(50, y - leading, 700, y - leading);

      g.setColor(Color.yellow);
      g.drawLine(50, y - maxadvance, 700, y - maxadvance);

      g.setColor(Color.magenta);
      g.drawLine(50, y - height, 700, y - height);

      g.setColor(Color.darkGray);
      g.drawLine(50, y + descent, 700, y + descent);

      // g.setColor(Color.black);
      // g.drawString("(abc*){(|)}",50,50);

      g.setColor(Color.blue);
      g.drawLine(50, 100, 200, 100);

      g.setColor(Color.black);
      g.drawString("(abc*)",50,100+(int)(metrics.getAscent()*0.4));

      /*for (int j = 0; j < 32; j++)
      {
        c = (char) (i * 32 + j /* +start *//*);
        // g.drawString(String.valueOf(c),50+j*20, y);
        glyphIndices[ 0 ] = i * 32 + j;
        gv = g2dFont.createGlyphVector(frc, glyphIndices);
        g2d.drawGlyphVector(gv, 50 + j * 20, y);
      }*//*
    }*/

    String[] names = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    for(int i=0; i<names.length; i++)
      System.out.println(names[i]);

    Font[] fontlist= GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
    System.out.println("Available fonts: ");
    for(int i=0; i<fontlist.length; i++)
      System.out.println("fontname="+fontlist[i].getFontName()+" name="+fontlist[i].getName()+
              " family="+fontlist[i].getFamily());
    System.out.println();

    System.out.println("fontname="+g.getFont().getFontName()+" name="+g.getFont().getName()+
              " family="+g.getFont().getFamily()+" size="+g.getFont().getSize());

    /*char c;
    //int start = 2048/*1024+512;*/
    for(int start=0; start<10000; start+=512)

    for(int i=0; i<32; i++)
      for(int j=0; j<16; j++)
      {
        c = (char)(j*32+i);
        //System.out.print(((int)c)+" ");
        g.drawString(String.valueOf(c),50+i*20,120+j*20);
      }
    System.out.println();
  }

  /**
   * Main method.
   *
   * @param args Command line arguments
   */
  public static void main(String[] args)
  {
    Frame frame = new Frame("Test MathComponent");

    frame.setLayout(new BorderLayout());
    FontTest component = new FontTest();

    frame.add(component, BorderLayout.CENTER);
    frame.setVisible(true);
    frame.setSize(700, 500);
    frame.invalidate();
    frame.validate();

    frame.addWindowListener(new java.awt.event.WindowAdapter()
    {
      public void windowClosing(java.awt.event.WindowEvent evt)
      {
        System.exit(0);
      }
    });
  }
}
