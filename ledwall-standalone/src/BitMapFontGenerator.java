import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

class BitMapFontGenerator {
		public final int width;
		public final int height;

		private int charIndex = 0;
		public final String text;

		public BitMapFontGenerator(String s, int width, int height) {
			this.text = s;
			this.width = width;
			this.height = height;
		}
		
		public synchronized int getIndex() {
			return charIndex;
		}
		
		public synchronized void setIndex(int index) {
			if (index > -1 && index < text.length()) {
				charIndex = index;
			}
		}
	
		/**
		 * Here is a more efficient method, but I do not have time to
		 * test that
		 * http://stackoverflow.com/questions/6524196/java-get-pixel-array-from-image
		 * 
		 * 
		 * @param img
		 * @return
		 */
		public static String getBufferedImageAsString(BufferedImage img) {
			StringBuilder s = new StringBuilder("1"); // means Gray image to arduino
			if (img == null) {
				return "";
			}

			final int width = img.getWidth();
			final int height = img.getHeight();
			
			if ((width == 0 ) && (height == 0)) {
				return "";
			}
			
			int iType = img.getType();
			
			if (iType != BufferedImage.TYPE_BYTE_GRAY) {
				return "";
			}
			
			for (int h = 0; h < height; h++) {
				for (int w = 0; w < width; w++) {
					int c = img.getRGB(w, h);
					byte gray = (byte)c; // lowest byte enough?
					if (gray == 0) gray++;  // "\0" : trouble to c++"
					if (gray == 10) gray++; // "\r" : trouble to serial comm
					if (gray == 13) gray++; // "\n" : trouble to serial comm
				
					s.append(",");s.append(Byte.toUnsignedInt(gray)); // without toUnsignedInt 255 will be -1 
				}
				// FOR debug!
				// s.append("\n");
			}

			return s.toString();
		}

		public BufferedImage getNextChar() {
			String s;
			synchronized (this) {
				s = text.substring(charIndex, charIndex + 1);
				charIndex++;
	
				if (charIndex >= text.length()) {
					charIndex = 0;
				}
			}
			
			System.out.println("char:" + s);

			BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
			Graphics2D g2 = img.createGraphics();
			Font font = new Font("Monospaced", Font.PLAIN, 8);

			g2.setFont(font);

			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

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
			// Shape outline = textTl.getOutline(null);
			// Rectangle r = outline.getBounds();
			transform = g2.getTransform();
			transform.scale(8d / r.getWidth(), 8d / (r.getHeight() - 1d));
			g2.transform(transform);
			g2.drawString(s, 0, height - 1);
			
			// System.out.println(getBufferedImageAsString(img));

			return img;
		}
	}