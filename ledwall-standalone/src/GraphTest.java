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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.Timer;

import hu.liferay.borkutip.ledwall.backend.BitMapFontGenerator;

/**
 * This class demonstrates how to load an Image from an external file
 */
public class GraphTest extends JApplet implements ActionListener {
	private static final JApplet applet = new GraphTest();
	private static final Timer timer = new Timer(1000, (ActionListener) applet);
	private BufferedImage img;
	
	BitMapFontGenerator bmfg = new BitMapFontGenerator("Hello", 8, 8);

	@Override
	public void paint(Graphics g) {
		if (img != null) {
			Graphics2D g2d = (Graphics2D) g;
			AffineTransform transform = new AffineTransform();
			transform.scale(30, 30);
			g2d.setTransform(transform);
			g2d.drawImage(img, 0, 0, null);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		img = bmfg.getNextChar();
		this.repaint();
	}

	public static void main(String[] args) {
		JFrame f = new JFrame("Create Image Sample");

		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				timer.stop();

				System.exit(0);
			}
		});

		// f.getContentPane().add("Center", new GraphTest());
		f.add(applet);
		f.pack();
		f.setSize(new Dimension(300, 300));

		f.setVisible(true);
		
		timer.setRepeats(true);
		timer.setDelay(1000);
		timer.start();
	}
}
