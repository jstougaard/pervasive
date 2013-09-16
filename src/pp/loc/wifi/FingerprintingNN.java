package pp.loc.wifi;

import java.io.IOException;


public class FingerprintingNN {
	
	/**
	 * Execute Nearest Neighbor fingerprinting 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Running FingerprintingNN...");
		int kSize = 1;
		String resultPath = "results/fingerprint_NN";
		
		
		try {
			
			FingerPrinter fp = new FingerPrinter("data/MU.1.5meters.offline.trace", "data/MU.1.5meters.online.trace", 25, 5);
			fp.run(kSize, resultPath);
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
