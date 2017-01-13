package service;

import java.io.IOException;

public class CHistogramCalculator {

	private static String histogramCommand = "/Users/gadarezgui/Documents/cours/M2/atelier/SmartCards/imageProcessing/irisRecognition/histogramCompute.sh";

	public static void getHistogramValue() throws IOException {

		try {
			String target = new String(histogramCommand);
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(target);
			proc.waitFor();

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
