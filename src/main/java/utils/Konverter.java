package utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author zi
 */

public class Konverter {
	
	public static byte[] imageToByteArray(BufferedImage image) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", baos);
		return baos.toByteArray();
	}
	
	public static BufferedImage byteArrayToImage(byte[] byteArray) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
		return ImageIO.read(bais);
	}
}
