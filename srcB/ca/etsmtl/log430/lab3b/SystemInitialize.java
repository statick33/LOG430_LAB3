package ca.etsmtl.log430.lab3b;

import java.io.PipedWriter;

import ca.etsmtl.log430.lab3b.FileReaderFilter;
import ca.etsmtl.log430.lab3b.FileWriterFilter;
import ca.etsmtl.log430.lab3b.MergeFilter;
import ca.etsmtl.log430.lab3b.StateFilter;
import ca.etsmtl.log430.lab3b.StatusFilter;

/**
 * This class contains the main method for assignment 3. The program
 * consists of these files:<br><br>
 * 
 * 1) SystemInitialize: instantiates all filters and pipes, starts all filters.<br>
 * 2) FileReaderFilter: reads input file and sends each line to its output pipe.<br>
 * 3) StatusFilter: separates the input stream in two project types (REG, CRI) and writes
 *    lines to the appropriate output pipe.<br>
 * 4) StateFilter: determines if an entry contains a particular state specified
 *    at instantiation. If so, sends the whole line to its output pipe.<br>
 * 5) MergeFilter: accepts inputs from 2 input pipes and writes them to its output pipe.<br>
 * 6) FileWriterFilter: sends its input stream to a text file.<br><br>
 * 
 * Pseudo Code:
 * <pre>
 * instantiate all filters and pipes
 * start FileReaderFilter
 * start StatusFilter
 * start StateFilter for RIS
 * start StateFilter for DIF
 * start MergeFilter
 * start FileWriterFilter
 * </pre>
 * 
 * Running the program:
 * <pre>
 * java SystemInitialize InputFile OutputFile > DebugFile
 * 
 * SystemInitialize - Program name
 * InputFile - Text input file (see comments below)
 * OutputFile - Text output file with students
 * DebugFile - Optional file to direct debug statements
 * </pre>
 * 
 * @author A.J. Lattanze
 */

public class SystemInitialize {

	public static void main(String argv[]) {
		// Let's make sure that input and output files are provided on the
		// command line

		if (argv.length != 3) {

			System.out
					.println("\n\nNombre incorrect de parametres d'entree. Utilisation:");
			System.out
					.println("\njava SystemInitialize <fichier d'entree> <fichier de sortie 1> <fichier de sortie 2>");

		} else {
			// These are the declarations for the pipes.
			PipedWriter pipe01 = new PipedWriter();
			PipedWriter pipe02 = new PipedWriter();
			PipedWriter pipe03 = new PipedWriter();
			PipedWriter pipe04 = new PipedWriter();
			PipedWriter pipe05 = new PipedWriter();
			PipedWriter pipe06 = new PipedWriter();
			PipedWriter pipe07 = new PipedWriter();
			PipedWriter pipe08 = new PipedWriter();
			PipedWriter pipe09 = new PipedWriter();

			// Instantiate Filter Threads
			Thread fileReaderFilter = new FileReaderFilter(argv[0], pipe01);
			Thread statusFilter = new StatusFilter(pipe01, pipe02, pipe03);
			Thread stateFilter1 = new StateFilter("DIF", pipe02, pipe04);
			//Thread stateFilter2 = new StateFilter("RIS", pipe02, pipe04);
			Thread stateFilter3 = new StateFilter("RIS", pipe03, pipe05);
			Thread percentageFilter1 = new PercentageFilter(50, "<", pipe04, pipe06);
			Thread percentageFilter2 = new PercentageFilter(25, "=", pipe05, pipe07);
			//Thread percentageFilter3 = new PercentageFilter(75, ">", pipe05, pipe08);
			//Thread mergeFilter = new MergeFilter(pipe07, pipe08, pipe09);
			Thread fileWriterFilter1 = new FileWriterFilter(argv[1], pipe06);
			Thread fileWriterFilter2 = new FileWriterFilter(argv[2], pipe09);

			// Start the threads
			fileReaderFilter.start();
			statusFilter.start();
			stateFilter1.start();
			//stateFilter2.start();
			stateFilter3.start();
			percentageFilter1.start();
			percentageFilter2.start();
			//percentageFilter3.start();
			//mergeFilter.start();
			fileWriterFilter1.start();
			fileWriterFilter2.start();
			
		}  // if
		
	} // main
	
} // SystemInitialize