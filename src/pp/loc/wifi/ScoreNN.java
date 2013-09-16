package pp.loc.wifi;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.PositioningError;
import org.pi4.locutil.trace.TraceEntry;

public class ScoreNN {

	public static void main(String[] args) {
		System.out.println("Running ScoreNN...");
		
		if (args.length < 1) {
			System.out.println("USAGE: Please provide a file to read from. E.g. results/fingerprint_NN");
			return;
		}
		
		String fromFilename = args[0];
		String toFilename = fromFilename + "_score.csv";
		
		String newLine = System.getProperty("line.separator");
		String delimiter = ";";
		
		try {
			System.out.println("Reading from file: "+fromFilename);
			
			ArrayList<Double> errorValues = new ArrayList<Double>();


			// Read file
		    Scanner scanner = new Scanner(new FileInputStream(fromFilename));

		    while (scanner.hasNextLine()){
		        String line = scanner.nextLine();
		        double value = Double.parseDouble( line.substring(line.lastIndexOf(" ")+1) );

		        errorValues.add(value);

		    }
		  
		    scanner.close();
		    
		    // Sort array
		    Collections.sort(errorValues);
		    
		    // Save to new file
		    System.out.println("Saving results to: " + toFilename);
			FileWriter fstream = new FileWriter(toFilename);
			BufferedWriter out = new BufferedWriter(fstream);
			
			out.write( "\"Error value\"" + delimiter + "\"Accumulated percentage\"" + newLine);
			
			int i = 1;
			int count = errorValues.size();
			
			for (Double errorValue : errorValues) {
				
				out.write( errorValue + delimiter + ((double) i / count * 100) + newLine );
				i++;
			}
			
			//Close the output stream
			out.close();
			
			System.out.println("Results saved");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}


}
