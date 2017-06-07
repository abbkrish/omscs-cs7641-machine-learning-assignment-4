package burlap.assignment4.util;

import burlap.assignment4.BasicGridWorld;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.RewardFunction;

public class BasicRewardFunction implements RewardFunction {

	GridMap gridMap;

	public BasicRewardFunction(GridMap gridMap) {
		this.gridMap = gridMap;
	}

	@Override
	public double reward(State s, GroundedAction a, State sprime) {

		// get location of agent in next state
		ObjectInstance agent = sprime.getFirstObjectOfClass(BasicGridWorld.CLASSAGENT);
		int ax = agent.getIntValForAttribute(BasicGridWorld.ATTX);
		int ay = agent.getIntValForAttribute(BasicGridWorld.ATTY);

		// are they at goal location?
		if (this.gridMap.getMap()[ax][ay] == GridMap.TERMINAL_GOAL_LOCATION_VALUE) {
			return this.gridMap.getSize()*2;
		}
		else if (this.gridMap.getMap()[ax][ay] == GridMap.TERMINAL_TRAP_LOCATION_VALUE) {
			return -this.gridMap.getSize()*2;
		}
		else if (this.gridMap.getMap()[ax][ay] == GridMap.TEMPORARY_TRAP_LOCATION_VALUE) {
			return -this.gridMap.getSize();
		}

		return -1;
	}
}
