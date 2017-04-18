package burlap.assignment4.util;

import burlap.behavior.policy.EpsilonGreedy;
import burlap.behavior.valuefunction.QFunction;
import burlap.oomdp.core.AbstractGroundedAction;
import burlap.oomdp.core.states.State;

/**
 * This class defines a decaying epsilon-greedy policy over Q-values and requires a QComputable valueFunction to be specified.
 * With probability epsilon the policy will return a random action (with uniform distribution over all possible actions).
 * With probability 1 - epsilon the policy will return the greedy action. If multiple actions tie for the highest Q-value,
 * then one of the tied actions is randomly selected.
 * Each time an action is queried the value of epsilon will decay until it reaches 0 and the policy stops being stochastic.
 * @author David Duffy
 *
 */
public class DecayingEpsilonGreedy extends EpsilonGreedy {
    protected double epsilonDecayRate;

    /**
     * Initializes with the value of epsilon, where epsilon is the probability of taking a random action.
     * @param epsilon the probability of taking a random action.
     * @param epsilonDecayRate the decay rate to apply to epsilon each time an action is taken.
     */
    public DecayingEpsilonGreedy(double epsilon, double epsilonDecayRate) {
        super(epsilon);
        this.epsilonDecayRate = epsilonDecayRate;
    }

    /**
     * Initializes with the QComputablePlanner to use and the value of epsilon to use, where epsilon is the probability of taking a random action.
     * @param planner the QComputablePlanner to use
     * @param epsilon the probability of taking a random action.
     * @param epsilonDecayRate the decay rate to apply to epsilon each time an action is taken.
     */
    public DecayingEpsilonGreedy(QFunction planner, double epsilon, double epsilonDecayRate) {
        super(planner, epsilon);
        this.epsilonDecayRate = epsilonDecayRate;
    }

    @Override
    public AbstractGroundedAction getAction(State s) {
        this.epsilon *= this.epsilonDecayRate;
        return super.getAction(s);
    }

    @Override
    public boolean isStochastic() {
        return epsilon > 0.;
    }
}
