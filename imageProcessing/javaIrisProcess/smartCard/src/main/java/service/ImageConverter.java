package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageConverter {

	public String convert(String inputFilePath, String format) throws Exception {
		if (getFileExtensionFromPath(inputFilePath).equals(format)) {
			throw new Exception("Formats can't be the same!");
		}
		if (getFormats().contains(format) && getFormats().contains(getFileExtensionFromPath(inputFilePath))) {

			String outputPath = getOutputPathFromInputPath(inputFilePath, format);
			String convertPath = "";
			if (System.getProperty("os.name").equals("Linux")) {
				convertPath = "/opt/ImageMagick/bin/convert";
			} else {
				convertPath = "/opt/ImageMagick/bin/convert";
			}

			if (format.equals("ppm")) {
				executeCommand(convertPath + " -depth 8 " + inputFilePath + " "
						+ getOutputPathFromInputPath(inputFilePath, format));
			} else {
				executeCommand(
						convertPath + " " + inputFilePath + " " + getOutputPathFromInputPath(inputFilePath, format));
			}

			return outputPath;
		} else {
			throw new Exception("Format not yet implemented");
		}
	}

	private static ArrayList<String> getFormats() {
		return new ArrayList<>(Arrays.asList("jpg", "jpeg", "png", "ppm", "gif"));
	}

	private static String getFileExtensionFromPath(String path) {
		int i = path.lastIndexOf('.');
		if (i > 0) {
			return path.substring(i + 1);
		}
		return "";
	}

	private static String getOutputPathFromInputPath(String path, String format) {
		return path.substring(0, path.lastIndexOf('.')) + "." + format;
	}

	private static String executeCommand(String command) {

		StringBuilder output = new StringBuilder();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line).append("\n");
			}

		} catch (IOException | InterruptedException ex) {
			Logger.getLogger(ImageConverter.class.getName()).log(Level.SEVERE, null, ex);
		}

		return output.toString();

	}

}