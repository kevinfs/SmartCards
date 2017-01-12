package service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import model.RGB;
import model.RGBHistogram;

public class HistogramFileReader {

	private String fileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public HistogramFileReader(String fileName) {
		this.fileName = fileName;
	}

	public RGBHistogram getHistogramme() {
		RGB rgb;
		RGBHistogram rgbHistogram = new RGBHistogram();
		List<RGB> histoValues = new ArrayList<>();
		String[] lineValues;

		// Read
		try {
			InputStream ips = new FileInputStream(fileName);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);

			for (String line; (line = br.readLine()) != null;) {
				lineValues = line.split("\\s+");
				rgb = new RGB();
				rgb.setRed(Integer.valueOf(lineValues[0]));
				rgb.setGreen(Integer.valueOf(lineValues[1]));
				rgb.setBlue(Integer.valueOf(lineValues[2]));
				histoValues.add(rgb);
			}

			br.close();
		} catch (

		Exception e) {
			System.out.println(e.toString());
		}

		rgbHistogram.setHistogramValues(histoValues);
		return rgbHistogram;

	}

}
