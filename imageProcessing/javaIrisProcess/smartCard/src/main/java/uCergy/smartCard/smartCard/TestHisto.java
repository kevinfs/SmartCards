package uCergy.smartCard.smartCard;

import model.RGBHistogram;
import service.HistogramSimilarity;
import service.HistogramFileReader;


public class TestHisto {

	public static void main(String[] args) {

		double similarity;
		RGBHistogram rgbHistogram;
		RGBHistogram rgbTemporaryHistogram;

		HistogramFileReader refrencehistogramFile = new HistogramFileReader("/Users/glist/codespace/SmartCards/imageProcessing/javaIrisProcess/smartCard/src/main/resources/archives/historef.txt");
		HistogramFileReader temporaryhistogramFile = new HistogramFileReader("/Users/glist/codespace/SmartCards/imageProcessing/javaIrisProcess/smartCard/src/main/resources/archives/histoSH.txt");

		rgbHistogram = refrencehistogramFile.getHistogramme();
		double sumPixels = rgbHistogram.getHistogramTotalPixels();

		rgbTemporaryHistogram = temporaryhistogramFile.getHistogramme();
		HistogramSimilarity histogramSimilarity = new HistogramSimilarity(rgbHistogram, rgbTemporaryHistogram);
		similarity = histogramSimilarity.distance();


		System.out.println("The rate of similarity  :" + similarity);

		System.out.println("le nombre de pixel est :" + sumPixels);


	}
}
