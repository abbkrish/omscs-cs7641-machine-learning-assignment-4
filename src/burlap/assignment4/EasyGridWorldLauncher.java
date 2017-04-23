package burlap.assignment4;

import burlap.assignment4.util.*;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.singleagent.environment.SimulatedEnvironment;
import burlap.oomdp.singleagent.explorer.VisualExplorer;
import burlap.oomdp.visualizer.Visualizer;

public class EasyGridWorldLauncher {
	//These are some boolean variables that affect what will actually get executed
	private static boolean visualizeInitialGridWorld = true; //Loads a GUI with the agent, walls, and goal
	
	//runValueIteration, runPolicyIteration, and runQLearning indicate which algorithms will run in the experiment
	private static boolean runValueIteration = true;
	private static boolean runPolicyIteration = true;
	private static boolean runQLearning = true;

	//showValueIterationPolicyMap, showPolicyIterationPolicyMap, and showQLearningPolicyMap will open a GUI
	//you can use to visualize the policy maps. Consider only having one variable set to true at a time
	//since the pop-up window does not indicate what algorithm was used to generate the map.
	private static boolean showValueIterationPolicyMap = true;
	private static boolean showPolicyIterationPolicyMap = true;
	private static boolean showQLearningPolicyMap = true;

	private static Integer MAX_ITERATIONS = 200;
	private static Integer NUM_INTERVALS = 200;
	private static Integer MAX_STEPS = 50000; // Stop stuck policies from causing an infinite loop and crashing the JVM.
	protected static String title = "Easy Grid World";

	protected static int[][] userMap = new int[][] { 
			{ 0, 0, 0, 0, 0},
			{ 0, 1, 1, 1, 0},
            { 0, 1, 1, 1, 0},
			{ 1, 0, 1, 1, 0},
			{ 0, 0, 0, 0, 0}, };

//	private static Integer mapLen = map.length-1;

	public static void main(String[] args) {
	    GridMap grid = null;
	    if (args.length > 0) {
	        try {
                grid = GridMap.LoadMap(args[0]);
                title = "'" + args[0] + "' Grid World";
				if (args.length > 1) {
					MAX_ITERATIONS = NUM_INTERVALS = Integer.parseInt(args[1]);
				}
				if (args.length > 2) {
					MAX_STEPS = Integer.parseInt(args[2]);
				}
            } catch (Exception e) {
	            System.out.println("Error: could not load map '" + args[0] + "'.\n");
	            e.printStackTrace(System.out);
            }
        }

        if (grid == null) {
            System.out.println("/////Running default hardcoded map./////\n");
            // convert to BURLAP indexing
            int[][] map = MapPrinter.mapToMatrix(userMap);
            int goalX = map.length-1;
            int goalY = map[0].length-1;
            grid = new GridMap(title, map, 0, 0, goalX, goalY);
        }

        runGridWorld(grid);
	}

	private static void runGridWorld(GridMap grid) {
		BasicGridWorld gen = new BasicGridWorld(grid.getMap(), grid.endX, grid.endY, grid.startX, grid.startY);
		Domain domain = gen.generateDomain();

		State initialState = gen.getExampleState(domain);

		RewardFunction rf = new BasicRewardFunction(grid.endX, grid.endY, 1); //Goal is at the top right grid
		TerminalFunction tf = new BasicTerminalFunction(grid.endX, grid.endY); //Goal is at the top right grid

		SimulatedEnvironment env = new SimulatedEnvironment(domain, rf, tf, initialState);
		//Print the map that is being analyzed
		System.out.println("\n|***| " + title + " Analysis |***|\n\n");
		MapPrinter.printMap(MapPrinter.matrixToMap(grid.getMap()));
		
		if (visualizeInitialGridWorld) {
			visualizeInitialGridWorld(domain, gen, env);
		}

		AnalysisRunner runner = new AnalysisRunner(MAX_ITERATIONS, NUM_INTERVALS, MAX_STEPS);
		if(runValueIteration){
			runner.runValueIteration(gen,domain,initialState, rf, tf, showValueIterationPolicyMap, grid.name);
		}
		if(runPolicyIteration){
			runner.runPolicyIteration(gen,domain,initialState, rf, tf, showPolicyIterationPolicyMap, grid.name);
		}
		if(runQLearning){
			runner.runQLearning(gen,domain,initialState, rf, tf, env, showQLearningPolicyMap, grid.name);
		}
		// AnalysisAggregator.printAggregateAnalysis();
	}

	private static void visualizeInitialGridWorld(Domain domain, BasicGridWorld gen, SimulatedEnvironment env) {
		Visualizer v = gen.getVisualizer();
		VisualExplorer exp = new VisualExplorer(domain, env, v);

		exp.addKeyAction("w", BasicGridWorld.ACTIONNORTH);
		exp.addKeyAction("s", BasicGridWorld.ACTIONSOUTH);
		exp.addKeyAction("d", BasicGridWorld.ACTIONEAST);
		exp.addKeyAction("a", BasicGridWorld.ACTIONWEST);

		exp.setTitle(title);
		exp.initGUI();
	}
}
