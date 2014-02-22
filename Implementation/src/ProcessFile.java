import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class ProcessFile {
	public static void main(String[] args) {
		File input = new File("/Users/steffiostrowski/Documents/Thesis/Implementation/src/count.txt");  
		try {
			Scanner scanLine = new Scanner(input); 
			scanLine.nextLine(); // ignore headers
			
			System.out.printf("%-10s %-10s %-10s\n", "N", "d", "Count");
			
			int currN = 10; 
			int currD = 3;
			int totalCount = 0; 
			int num = 0;
			while (scanLine.hasNextLine()) {
				String line = scanLine.nextLine();  
				String[] data = line.split("\\s+");

				int N = Integer.parseInt(data[1]);
				int d = Integer.parseInt(data[2]);
				
				try {
					int count = Integer.parseInt(data[0]);
				
					if (N != currN || d != currD) {
						System.out.printf("%-10d %-10d %-10f\n", currN, currD, ((double) totalCount) / num);
						
						totalCount = 0; 
						num = 0; 
						currN = N;
						currD = d; 
					}
					
					num++; 
					totalCount += count; 
				} catch (NumberFormatException e) {
					// do nothing -- skip ones that threw exceptions
				}
			}
			
		} catch (FileNotFoundException e) {
			
		}

             
	}
}
