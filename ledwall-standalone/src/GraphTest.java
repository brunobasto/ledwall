/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;


/**
 * This class demonstrates how to load an Image from an external file
 */
public class GraphTest extends Component {
          
    BufferedImage img;
    final int width = 8;
    final int height = 8;

    public void paint(Graphics g) {
    	Graphics2D g2d = (Graphics2D)g;
        AffineTransform transform = new AffineTransform();
        transform.scale(30, 30);
        g2d.setTransform(transform);
        g2d.drawImage(img, 0, 0, null);
    }

    public GraphTest() {
       img =
    	new BufferedImage(width, height,
    			BufferedImage.TYPE_BYTE_GRAY);
       Graphics2D g2 = img.createGraphics();
       Font font = new Font("Monospaced", Font.PLAIN, 8);

       String s = "g";

       g2.setFont(font);
       
       g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
               RenderingHints.VALUE_ANTIALIAS_OFF);

       g2.setRenderingHint(RenderingHints.KEY_RENDERING,
               RenderingHints.VALUE_RENDER_QUALITY);

    // get metrics from the graphics
       FontMetrics metrics = g2.getFontMetrics(font);
       // get the height of a line of text in this
       // font and render context
       int hgt = metrics.getHeight();
       // get the advance of my text in this font
       // and render context
       int adv = metrics.stringWidth(s);
       // calculate the size of a box to hold the
       // text with some padding.
       Dimension size = new Dimension(adv, hgt + 1);
       
       Rectangle2D r = metrics.getStringBounds(s, g2);
      
       FontRenderContext frc = g2.getFontRenderContext();
       TextLayout textTl = new TextLayout(s, font, frc);
       AffineTransform transform = new AffineTransform();
       //Shape outline = textTl.getOutline(null);
       //Rectangle r = outline.getBounds();
       transform = g2.getTransform();
       transform.scale(8d/r.getWidth(), 8d/(r.getHeight()-1d));
       g2.transform(transform);
       g2.drawString(s, 0, height - 1);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Create Image Sample");
            
        f.addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });

        //f.getContentPane().add("Center", new GraphTest());
        f.add(new GraphTest());
        f.pack();
        f.setSize(new Dimension(300, 300));

        f.setVisible(true);
    }
}
