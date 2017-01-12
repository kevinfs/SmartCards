package model;

import java.util.List;

public class RGBHistogram {

	private List<RGB> histogramValues;

	public RGBHistogram() {
		super();
	}

	public List<RGB> getHistogramValues() {
		return histogramValues;
	}

	public void setHistogramValues(List<RGB> histogramValues) {
		this.histogramValues = histogramValues;
	}

	public int  getHistogramTotalPixels() {
		int histogramTotalPixels = 0;

		for (int i = 0; i < histogramValues.size(); i++) {

			histogramTotalPixels += histogramValues.get(i).getBlue();
		}

		return histogramTotalPixels;
	}

	
}
