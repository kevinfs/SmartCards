package uCergy.smartCard.smartCard;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;

import service.CHistogramCalculator;
import service.ImageConverter;;

public class TakePictureExample {

	public static void main(String[] args) throws IOException {
		
		String fileName = "loginPicture"; 
		String pngPicture = fileName + ".png";
		//get default webcam and open it
		Webcam webcam = Webcam.getDefault();
		// get image
		Dimension dimension = new Dimension(640,480);
		webcam.setViewSize(dimension);
		webcam.open();
		
		// get picture form webcam
		BufferedImage image = webcam.getImage();	
		
		// save image to PNG file
		ImageIO.write(image, "PNG", new File(pngPicture));
		
		// Image converter to convert from jpeg to ppm 
		ImageConverter imConverter = new ImageConverter();
		try {
			imConverter.convert(pngPicture, "ppm");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Succesfully converted file!");
		
		 CHistogramCalculator.getHistogramValue();
		 
	}
}