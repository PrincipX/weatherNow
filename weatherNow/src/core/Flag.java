package core;

import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Flag {
	public ImageIcon image;
	
	public Flag(String country) {
		this.image = getFlag(country);
	}
	
	private ImageIcon getFlag(String country) {
		country.replaceAll(" ", "%20");
		URL url;
		try {
			url = new URL("https://countryflagsapi.com/png/"+country);
			BufferedImage c = ImageIO.read(url);
			return new ImageIcon(c);
		} catch (Exception e) {
			return null;
		}
	}

}
