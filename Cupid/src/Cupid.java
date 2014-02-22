import java.io.*;
import java.util.*;

public class Cupid {
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("quiz.txt"));
		String line = null;
		int[] weight = {0, 5, 0, 3, 4, 3, 0, 2, 5, 0, 5, 5, 1, 5, 5, 3, 1, 3, 0, 3, 2, 5};
		
		List<List<String>> people = new ArrayList<List<String>>();
		List<List<Match>> results = new ArrayList<List<Match>>();
		while ((line = reader.readLine()) != null) {
			List<String> person = Arrays.asList(line.split(","));
			people.add(person);
		}
		
		for (List<String> person : people) {
			List<Match> result = new ArrayList<Match>(); 
			//for (List<String> person2 : people) {
			for (int j = 0; j < people.size(); j++) {
				List<String> person2 = people.get(j);
				int similarities = 0; 
				double total = 0; 
				for (int i = 1; i < person.size(); i++) {
					if (person.get(i).equals(person2.get(i))) {
						similarities += weight[i]; 
					}
					total += weight[i];
				}
				result.add(new Match(person2.get(0), j, similarities / total));
			}
			results.add(result);
		}
		
		List<List<Match>> topThrees = new ArrayList<List<Match>>();
		List<Set<Integer>> topSets = new ArrayList<Set<Integer>>();
		for (int i = 0; i < results.size(); i++) {
			List<Match> result = results.get(i); 
			Collections.sort(result);
			Collections.reverse(result);
			
			List<Match> topThree = new ArrayList<Match>(); 
			Set<Integer> indices = new HashSet<Integer>(); 
			for (int j = 1; j < 4; j++) {
				topThree.add(result.get(j));
				indices.add(result.get(j).getIndex());
			}
			topThrees.add(topThree);
			topSets.add(indices);
		}
		
		List<Set<Match>> mutualSets = new ArrayList<Set<Match>>(); 
		
		for (int i = 0; i < topThrees.size(); i++) {
			Set<Match> mutual = new HashSet<Match>(); 
			List<Match> topThree = topThrees.get(i);
			for (Match m : topThree) {
				if (topSets.get(m.getIndex()).contains(i)) {
					mutual.add(m);
				}
			}
			mutualSets.add(mutual);
		}
		
		for (int i = 0; i < mutualSets.size(); i++) {
			System.out.println(people.get(i).get(0) + ": " + mutualSets.get(i));
		}
		System.out.println(); 
		for (int i = 0; i < results.size(); i++) {
			System.out.println(people.get(i).get(0) + ": " + results.get(i));
		}
	}
	
	public static class Match implements Comparable<Match> {
		String name; 
		int index; 
		double percent;
		
		public Match(String name, int index, double percent) {
			this.name = name; 
			this.index = index; 
			this.percent = percent * 100;
		}
		
		public double getPercent() {
			return percent;
		}
		
		public int getIndex() {
			return index; 
		}
		
		public int compareTo(Match m) {
			if (percent < m.percent) {
				return -1; 
			} else if (percent > m.percent) {
				return 1;
			} else {
				return 0; 
			}
		}
		
		public String toString() {
			String percentString = String.format("%1$,.0f", percent);
			return name + " " + percentString + "%";
		}
	}
}