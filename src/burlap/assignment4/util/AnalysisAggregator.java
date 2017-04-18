package burlap.assignment4.util;

import burlap.oomdp.core.values.DoubleArrayValue;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public final class AnalysisAggregator {
	private static List<Integer> numIterations = new ArrayList<Integer>();
	private static List<Integer> stepsToFinishValueIteration = new ArrayList<Integer>();
	private static List<Integer> stepsToFinishPolicyIteration = new ArrayList<Integer>();
	private static List<Integer> stepsToFinishQLearning = new ArrayList<Integer>();
	
	private static List<Integer> millisecondsToFinishValueIteration = new ArrayList<Integer>();
	private static List<Integer> millisecondsToFinishPolicyIteration = new ArrayList<Integer>();
	private static List<Integer> millisecondsToFinishQLearning = new ArrayList<Integer>();

	private static List<Double> rewardsForValueIteration = new ArrayList<Double>();
	private static List<Double> rewardsForPolicyIteration = new ArrayList<Double>();
	private static List<Double> rewardsForQLearning = new ArrayList<Double>();

	private static List<Double> convergenceForValueIteration = new ArrayList<Double>();
	private static List<Double> convergenceForPolicyIteration = new ArrayList<Double>();
	private static List<Double> convergenceForQLearning = new ArrayList<Double>();
	private static List<Double> epsilonDecayedForQLearning = new ArrayList<Double>();

	public static void addNumberOfIterations(Integer numIterations1){
		numIterations.add(numIterations1);
	}
	public static void addStepsToFinishValueIteration(Integer stepsToFinishValueIteration1){
		stepsToFinishValueIteration.add(stepsToFinishValueIteration1);
	}
	public static void addStepsToFinishPolicyIteration(Integer stepsToFinishPolicyIteration1){
		stepsToFinishPolicyIteration.add(stepsToFinishPolicyIteration1);
	}
	public static void addStepsToFinishQLearning(Integer stepsToFinishQLearning1){
		stepsToFinishQLearning.add(stepsToFinishQLearning1);
	}
	public static void printValueIterationResults(){
		System.out.print("Value Iteration,");	
		printList(stepsToFinishValueIteration);
	}
	public static void printPolicyIterationResults(){
		System.out.print("Policy Iteration,");	
		printList(stepsToFinishPolicyIteration);
	}
	public static void printQLearningResults(){
		System.out.print("Q Learning,");	
		printList(stepsToFinishQLearning);
	}
	

	public static void addMillisecondsToFinishValueIteration(Integer millisecondsToFinishValueIteration1){
		millisecondsToFinishValueIteration.add(millisecondsToFinishValueIteration1);
	}
	public static void addMillisecondsToFinishPolicyIteration(Integer millisecondsToFinishPolicyIteration1){
		millisecondsToFinishPolicyIteration.add(millisecondsToFinishPolicyIteration1);
	}
	public static void addMillisecondsToFinishQLearning(Integer millisecondsToFinishQLearning1){
		millisecondsToFinishQLearning.add(millisecondsToFinishQLearning1);
	}
	public static void addValueIterationReward(double reward) {
		rewardsForValueIteration.add(reward);
	}
	public static void addPolicyIterationReward(double reward) {
		rewardsForPolicyIteration.add(reward);
	}
	public static void addQLearningReward(double reward) {
		rewardsForQLearning.add(reward);
	}
	public static void addValueIterationConvergence(double convergence) {
		convergenceForValueIteration.add(convergence);
	}
	public static void addPolicyIterationConvergence(double convergence) {
		convergenceForPolicyIteration.add(convergence);
	}
	public static void addQLearningConvergence(double convergence) {
		convergenceForQLearning.add(convergence);
	}
	public static void addQLearningDecayedEpsilon(double decayedEpsilon) {
		epsilonDecayedForQLearning.add(decayedEpsilon);
	}
	public static void printValueIterationTimeResults(){
		System.out.print("Value Iteration,");	
		printList(millisecondsToFinishValueIteration);
	}
	public static void printPolicyIterationTimeResults(){
		System.out.print("Policy Iteration,");
		printList(millisecondsToFinishPolicyIteration);
	}

	public static void printQLearningTimeResults(){
		System.out.print("Q Learning,");	
		printList(millisecondsToFinishQLearning);
	}

	public static void printValueIterationRewards(){
		System.out.print("Value Iteration Rewards,");
		printDoubleList(rewardsForValueIteration);
	}

	public static void printPolicyIterationRewards(){
		System.out.print("Policy Iteration Rewards,");
		printDoubleList(rewardsForPolicyIteration);
	}

	public static void printQLearningRewards(){
		System.out.print("Q Learning Rewards,");
		printDoubleList(rewardsForQLearning);
	}

	public static void printValueIterationConvergence() {
		System.out.print("Value Iteration Convergence,");
		printDoubleList(convergenceForValueIteration);
	}

	public static void printPolicyIterationConvergence() {
		System.out.print("Policy Iteration Convergence,");
		printDoubleList(convergenceForPolicyIteration);
	}

	public static void printQLearningConvergence() {
		System.out.print("Q Learning Convergence,");
		printDoubleList(convergenceForQLearning);
	}

	public static void printNumIterations(){
		System.out.print("Iterations,");	
		printList(numIterations);
	}
	private static void printList(List<Integer> valueList){
		int counter = 0;
		for(int value : valueList){
			System.out.print(String.valueOf(value));
			if(counter != valueList.size()-1){
				System.out.print(",");
			}
			counter++;
		}
		System.out.println();
	}
	private static void printDoubleList(List<Double> valueList){
		int counter = 0;
		for(double value : valueList){
			System.out.print(String.valueOf(value));
			if(counter != valueList.size()-1){
				System.out.print(",");
			}
			counter++;
		}
		System.out.println();
	}
	public static void printAggregateAnalysis(){
		System.out.println("//Aggregate Analysis//\n");
		System.out.println("The data below shows the number of steps/actions the agent required to reach \n"
				+ "the terminal state given the number of iterations the algorithm was run.");
		printNumIterations();
		printValueIterationResults();
		printPolicyIterationResults();
		printQLearningResults();
		System.out.println();
		System.out.println("The data below shows the number of milliseconds the algorithm required to generate \n"
				+ "the optimal policy given the number of iterations the algorithm was run.");
		printNumIterations();
		printValueIterationTimeResults();
		printPolicyIterationTimeResults();
		printQLearningTimeResults();

		System.out.println("\nThe data below shows the total reward gained for \n"
				+ "the optimal policy given the number of iterations the algorithm was run.");
		printNumIterations();
		printValueIterationRewards();
		printPolicyIterationRewards();
		printQLearningRewards();
	}

	public static void writeValueIterationToCsv(String filename) {
		writeToCsv(filename, numIterations, stepsToFinishValueIteration, millisecondsToFinishValueIteration, rewardsForValueIteration, convergenceForValueIteration);
	}

	public static void writePolicyIterationToCsv(String filename) {
		writeToCsv(filename, numIterations, stepsToFinishPolicyIteration, millisecondsToFinishPolicyIteration, rewardsForPolicyIteration, convergenceForPolicyIteration);
	}

	public static void writeQLearningToCsv(String filename) {
		writeToCsv(filename, numIterations, stepsToFinishQLearning, millisecondsToFinishQLearning, rewardsForQLearning, convergenceForQLearning, epsilonDecayedForQLearning);
	}

	public static void writeToCsv(
		String filename,
		List<Integer> iterations,
		List<Integer> stepsToFinish,
		List<Integer> millisecondsToFinish,
		List<Double> rewards,
		List<Double> convergence)
	{
		writeToCsv(filename, iterations, stepsToFinish, millisecondsToFinish, rewards, convergence, null);
	}

	public static void writeToCsv(
		String filename,
		List<Integer> iterations,
		List<Integer> stepsToFinish,
		List<Integer> millisecondsToFinish,
		List<Double> rewards,
		List<Double> convergence,
		List<Double> epsilon)
	{
		try {
			FileWriter fw = new FileWriter(filename);
			PrintWriter pw = new PrintWriter(fw);

			if (epsilon != null) {
				pw.println("iteration,time,reward,steps,epsilon,convergence");
			} else {
				pw.println("iteration,time,reward,steps,convergence");
			}
			int size = numIterations.size();
			for (int i = 0; i < size; i++) {
				// "iteration,time,reward,steps,epsilon,convergence"
				pw.print(String.valueOf(iterations.get(i)));
				pw.print(",");
				pw.print(String.valueOf(millisecondsToFinish.get(i)));
				pw.print(",");
				pw.print(String.valueOf(rewards.get(i)));
				pw.print(",");
				pw.print(String.valueOf(stepsToFinish.get(i)));
				if (epsilon != null) {
					pw.print(",");
					pw.print(String.valueOf(epsilon.get(i)));
				}
				pw.print(",");
				pw.println(String.valueOf(convergence.get(i)));
			}

			pw.flush();
			pw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
}
