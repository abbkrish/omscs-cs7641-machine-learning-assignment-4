package burlap.assignment4.util;

import burlap.assignment4.BasicGridWorld;
import burlap.behavior.policy.EpsilonGreedy;
import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.auxiliary.StateReachability;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.ValueFunctionVisualizerGUI;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.behavior.singleagent.planning.stochastic.policyiteration.PolicyIteration;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.behavior.valuefunction.ValueFunction;
import burlap.domain.singleagent.gridworld.GridWorldDomain;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.singleagent.SADomain;
import burlap.oomdp.singleagent.environment.SimulatedEnvironment;
import burlap.oomdp.statehashing.HashableStateFactory;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;

import java.util.List;

public class AnalysisRunner {

	final SimpleHashableStateFactory hashingFactory = new SimpleHashableStateFactory();

	private int MAX_ITERATIONS;
	private int NUM_INTERVALS;
	private int MAX_STEPS;

	public AnalysisRunner(int MAX_ITERATIONS, int NUM_INTERVALS, int MAX_STEPS){
		this.MAX_ITERATIONS = MAX_ITERATIONS;
		this.NUM_INTERVALS = NUM_INTERVALS;
		this.MAX_STEPS = MAX_STEPS;
		
		int increment = MAX_ITERATIONS/NUM_INTERVALS;
		for(int numIterations = increment;numIterations<=MAX_ITERATIONS;numIterations+=increment ){
			AnalysisAggregator.addNumberOfIterations(numIterations);

		}

	}
	public void runValueIteration(BasicGridWorld gen, Domain domain,
			State initialState, RewardFunction rf, TerminalFunction tf, boolean showPolicyMap, String name) {
		System.out.println("//Value Iteration Analysis//");
		ValueIteration vi = null;
		Policy p = null;
		EpisodeAnalysis ea = null;
		int increment = MAX_ITERATIONS/NUM_INTERVALS;
		for(int numIterations = increment;numIterations<=MAX_ITERATIONS;numIterations+=increment ){
			long startTime = System.nanoTime();
			vi = new ValueIteration(
					domain,
					rf,
					tf,
					0.99, // gamma
					hashingFactory,
					0.001, // max delta
					numIterations);

			// run planning from our initial state
			p = vi.planFromState(initialState);
			AnalysisAggregator.addMillisecondsToFinishValueIteration((int) (System.nanoTime()-startTime)/1000000);

			// evaluate the policy with one roll out visualize the trajectory
			ea = p.evaluateBehavior(initialState, rf, tf, MAX_STEPS);
			AnalysisAggregator.addValueIterationReward(calcRewardInEpisode(ea));
			AnalysisAggregator.addStepsToFinishValueIteration(ea.numTimeSteps());
			AnalysisAggregator.addValueIterationConvergence(vi.latestDelta);
		}
		
//		Visualizer v = gen.getVisualizer();
//		new EpisodeSequenceVisualizer(v, domain, Arrays.asList(ea));
		// AnalysisAggregator.printValueIterationResults();
		MapPrinter.printPolicyMap(vi.getAllStates(), p, gen.getMap());
		System.out.println("\n\n");
		AnalysisAggregator.writeValueIterationToCsv(name + "_valueiteration_results" + MAX_ITERATIONS + ".csv");
		if(showPolicyMap){
			simpleValueFunctionVis((ValueFunction)vi, p, initialState, domain, hashingFactory, "Value Iteration");
		}
	}

	public void runPolicyIteration(BasicGridWorld gen, Domain domain,
			State initialState, RewardFunction rf, TerminalFunction tf, boolean showPolicyMap, String name) {
		System.out.println("//Policy Iteration Analysis//");
		PolicyIteration pi = null;
		Policy p = null;
		EpisodeAnalysis ea = null;
		int increment = MAX_ITERATIONS/NUM_INTERVALS;
		for(int numIterations = increment;numIterations<=MAX_ITERATIONS;numIterations+=increment ){
			long startTime = System.nanoTime();
			pi = new PolicyIteration(
					domain,
					rf,
					tf,
					0.99, // gamma
					hashingFactory,
					0.001, // max delta
					20, //If you look at the older code, the second last arg to the
					// constructor of policy iteration is set to 1. It really ought to be 10 or 20 in
					// the literature -- jontay ( alyssa -- changing this to 20)
					numIterations);
	
			// run planning from our initial state
			p = pi.planFromState(initialState);
			AnalysisAggregator.addMillisecondsToFinishPolicyIteration((int) (System.nanoTime()-startTime)/1000000);

			// evaluate the policy with one roll out visualize the trajectory
			ea = p.evaluateBehavior(initialState, rf, tf, MAX_STEPS);
			AnalysisAggregator.addPolicyIterationReward(calcRewardInEpisode(ea));
			AnalysisAggregator.addStepsToFinishPolicyIteration(ea.numTimeSteps());
			AnalysisAggregator.addPolicyIterationConvergence(pi.lastPIDelta);
		}

//		Visualizer v = gen.getVisualizer();
//		new EpisodeSequenceVisualizer(v, domain, Arrays.asList(ea));
		// AnalysisAggregator.printPolicyIterationResults();

		MapPrinter.printPolicyMap(getAllStates(domain,rf,tf,initialState), p, gen.getMap());
		System.out.println("\n\n");

		//visualize the value function and policy.
		AnalysisAggregator.writePolicyIterationToCsv(name + "_policyiteration_results" + MAX_ITERATIONS + ".csv");
		if(showPolicyMap){
			simpleValueFunctionVis(pi, p, initialState, domain, hashingFactory, "Policy Iteration");
		}
	}

	public void simpleValueFunctionVis(ValueFunction valueFunction, Policy p, 
			State initialState, Domain domain, HashableStateFactory hashingFactory, String title){

		List<State> allStates = StateReachability.getReachableStates(initialState,
				(SADomain)domain, hashingFactory);
		ValueFunctionVisualizerGUI gui = GridWorldDomain.getGridWorldValueFunctionVisualization(
				allStates, valueFunction, p);
		gui.setTitle(title);
		gui.initGUI();

	}
	
	public void runQLearning(BasicGridWorld gen, Domain domain,
			State initialState, RewardFunction rf, TerminalFunction tf,
			SimulatedEnvironment env, boolean showPolicyMap, String name) {
		System.out.println("//Q Learning Analysis//");

		QLearning agent = null;
		Policy p = null;
		EpisodeAnalysis ea = null;

		agent = new QLearning(
			domain,
			0.99, // Gamma - discount rate.
			hashingFactory,
			0.99,   // Initial Q value.
			0.99,  // Learning rate.
			MAX_STEPS);
		// DecayingEpsilonGreedy epsilonPolicy = new DecayingEpsilonGreedy(agent, 0.5, 0.999);
		//DecayingEpsilonGreedy epsilonPolicy = new DecayingEpsilonGreedy(agent, 0.5, 0.9999);
		EpsilonGreedy epsilonPolicy = new EpsilonGreedy(agent, 0.1);
		agent.setLearningPolicy(epsilonPolicy);

		int increment = MAX_ITERATIONS/NUM_INTERVALS;
		for(int numIterations = increment; numIterations <= MAX_ITERATIONS; numIterations += increment) {
			long startTime = System.nanoTime();

			ea = agent.runLearningEpisode(env);
			env.resetEnvironment();

			agent.initializeForPlanning(rf, tf, 1);
			p = agent.planFromState(initialState);
			AnalysisAggregator.addQLearningReward(calcRewardInEpisode(ea));
			AnalysisAggregator.addMillisecondsToFinishQLearning((int) (System.nanoTime()-startTime)/1000000);
			AnalysisAggregator.addStepsToFinishQLearning(ea.numTimeSteps());
			AnalysisAggregator.addQLearningConvergence(agent.maxQChangeInLastEpisode);
			AnalysisAggregator.addQLearningDecayedEpsilon(epsilonPolicy.getEpsilon());
		}
		// AnalysisAggregator.printQLearningResults();
		MapPrinter.printPolicyMap(getAllStates(domain,rf,tf,initialState), p, gen.getMap());
		System.out.println("\n\n");
		AnalysisAggregator.writeQLearningToCsv(name + "_qlearner_results" + MAX_ITERATIONS + ".csv");

		//visualize the value function and policy.
		if(showPolicyMap){
			simpleValueFunctionVis((ValueFunction)agent, p, initialState, domain, hashingFactory, "Q-Learning");
		}
	}
	
	private static List<State> getAllStates(Domain domain,
			 RewardFunction rf, TerminalFunction tf,State initialState){
		ValueIteration vi = new ValueIteration(
				domain,
				rf,
				tf,
				0.99,
				new SimpleHashableStateFactory(),
				.001, 100);
		vi.planFromState(initialState);

		return vi.getAllStates();
	}
	
	public double calcRewardInEpisode(EpisodeAnalysis ea) {
		double myRewards = 0;

		//sum all rewards
		for (int i = 0; i<ea.rewardSequence.size(); i++) {
			myRewards += ea.rewardSequence.get(i);
		}
		return myRewards;
	}
	
}
