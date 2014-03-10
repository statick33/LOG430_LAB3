package ca.etsmtl.log430.lab3b;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class is intended to be a filter that will send the information on an output pipe
 * specific to each project type. In other terms, this filter splits the data by project type.
 * <br><br>
 * 
 * <pre>
 * Pseudo Code:
 *
 * connect to input pipe
 * connect to output pipe
 *
 * while not end of line
 *     if REG appears on line of text 
 *         write line of text to REG output pipe
 *         flush pipe
 *     else if CRI appears on line of text
 *         write line of text to CRI output pipe
 *         flush pipe
 *     end if
 * end while
 * 
 * close pipes
 * </pre>
 *
 * @author R. Champagne
 * @version 1.0
 */

public class DataFilter extends Thread {

	// Declarations

	boolean done;
	
	ArrayList<String> projects = new ArrayList<String>();

	PipedReader inputPipe = new PipedReader();
	
	PipedWriter outputPipe1 = new PipedWriter();

	public DataFilter(PipedWriter inputPipe, PipedWriter outputPipe1) {

		try {
			// Connect InputPipes to upstream filters
			this.inputPipe.connect(inputPipe);
			System.out.println("DataFilter:: connected to upstream filters.");
		} catch (Exception Error) {
			System.out.println("DataFilter:: Error connecting input pipes.");
		} // try/catch

		try {
			// Connect outputPipe to downstream filter
			this.outputPipe1 = outputPipe1;
			System.out.println("DataFilter:: connected to downstream filter.");
		} catch (Exception Error) {
			System.out.println("DataFilter:: Error connecting output pipe.");
		} // catch

	} // Constructor

	// This is the method that is called when the thread is started

	public void run() {

		// Declarations

		char[] characterValue = new char[1];
		// char array is required to turn char into a string
		String lineOfText = "";
		// string is required to look for the status code
		int integerCharacter; // the integer value read from the pipe

		
		try {

			done = false;

			while (!done) {
				integerCharacter = inputPipe.read();
				characterValue[0] = (char) integerCharacter;

				if (integerCharacter == -1) { // pipe is closed

					done = true;

				} else {

					if (integerCharacter == '\n') { // end of line
					
						System.out.println("DataFilter:: received: " + lineOfText + ".");
						System.out.println("DataFilter:: sending: "
								+ lineOfText + " to output pipe 1 .");
						lineOfText += new String(characterValue);
						String[] parts = lineOfText.split(" ");
						String description = "";
						for(int i=6;i<parts.length;i++)
						{
							description = description + " " + parts[i];
						}
						String newFormat = parts[1] + " " + parts[5] + " " + parts[4] + " " + description;
						System.out.println(newFormat);
						projects.add(newFormat);

						lineOfText = "";

					} else {
						lineOfText += new String(characterValue);

					} // if //

				} // if

			} // while
		} catch (Exception error) {

			System.out.println(error.getMessage());

		} // try/catch

		Collections.sort(projects);
		for(int i=0;i<projects.size();i++)
		{
			try {
				outputPipe1.write(projects.get(i), 0, projects.get(i).length());
				outputPipe1.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {

			inputPipe.close();
			System.out.println("DataFilter:: input pipe closed.");

			outputPipe1.close();
			System.out.println("DataFilter:: output pipes closed.");

		} catch (Exception Error) {

			System.out.println("DataFilter:: Error closing pipes.");

		} // try/catch

	} // run

} // class