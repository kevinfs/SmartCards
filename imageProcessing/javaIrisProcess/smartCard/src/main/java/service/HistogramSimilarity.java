package service;

import java.util.ArrayList;
import java.util.List;
import model.RGB;
import model.RGBHistogram;
import java.lang.Math;



public class HistogramSimilarity {

	private RGBHistogram referenceHistogram;
	private RGBHistogram temporaryHistogram;

	public HistogramSimilarity(RGBHistogram referenceHistogram, RGBHistogram temporaryHistogram) {
		super();
		this.referenceHistogram = referenceHistogram;
		this.temporaryHistogram = temporaryHistogram;
	}

	public double distance() {
		
		double similarity = 0;
		double distance = 0;
		double distanceR = 0;
		double distanceG = 0;
		double distanceB = 0;
		double refrenceHistogramPixels = 0;

		List<RGB> refrenceHistoList = new ArrayList<>(referenceHistogram.getHistogramValues());
		List<RGB> temporaryHistoList = new ArrayList<>(temporaryHistogram.getHistogramValues());
		refrenceHistogramPixels = referenceHistogram.getHistogramTotalPixels();

		for (int i = 0; i < refrenceHistoList.size(); i++) {
			
			distanceR += Math.pow((refrenceHistoList.get(i).getRed() - temporaryHistoList.get(i).getRed()), 2);
			distanceG += Math.pow((refrenceHistoList.get(i).getGreen() - temporaryHistoList.get(i).getGreen()), 2);
			distanceB += Math.pow((refrenceHistoList.get(i).getBlue() - temporaryHistoList.get(i).getBlue()), 2);
		}
		
		distance = Math.sqrt((distanceR + distanceG + distanceB));
		similarity = (1 - (distance / refrenceHistogramPixels)) * 100;

		return similarity;
	}

}
