package burlap.assignment4;

import burlap.assignment4.util.AgentPainter;
import burlap.assignment4.util.AtLocation;
import burlap.assignment4.util.GridMap;
import burlap.assignment4.util.LocationPainter;
import burlap.assignment4.util.Movement;
import burlap.assignment4.util.Position;
import burlap.assignment4.util.WallPainter;
import burlap.oomdp.auxiliary.DomainGenerator;
import burlap.oomdp.core.Attribute;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectClass;
import burlap.oomdp.core.objects.MutableObjectInstance;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.MutableState;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.SADomain;
import burlap.oomdp.visualizer.StateRenderLayer;
import burlap.oomdp.visualizer.Visualizer;

public class BasicGridWorld implements DomainGenerator {

	public static final String ATTX = "x";
	public static final String ATTY = "y";

	public static final String CLASSAGENT = "agent";
	public static final String CLASSLOCATION = "location";

	public static final String ACTIONNORTH = "north";
	public static final String ACTIONSOUTH = "south";
	public static final String ACTIONEAST = "east";
	public static final String ACTIONWEST = "west";

	public static final String PFAT = "at";

	// ordered so first dimension is x
	protected GridMap gridMap;

	public BasicGridWorld(GridMap gridMap) {
		this.gridMap = gridMap;
	}

	@Override
	public Domain generateDomain() {

		SADomain domain = new SADomain();

		Attribute xatt = new Attribute(domain, ATTX, Attribute.AttributeType.INT);
		xatt.setLims(0, this.gridMap.getMap().length - 1);

		Attribute yatt = new Attribute(domain, ATTY, Attribute.AttributeType.INT);
		yatt.setLims(0, this.gridMap.getMap()[0].length - 1);

		ObjectClass agentClass = new ObjectClass(domain, CLASSAGENT);
		agentClass.addAttribute(xatt);
		agentClass.addAttribute(yatt);

		ObjectClass locationClass = new ObjectClass(domain, CLASSLOCATION);
		locationClass.addAttribute(xatt);
		locationClass.addAttribute(yatt);

		new Movement(ACTIONNORTH, domain, 0, this.gridMap.getMap());
		new Movement(ACTIONSOUTH, domain, 1, this.gridMap.getMap());
		new Movement(ACTIONEAST, domain, 2, this.gridMap.getMap());
		new Movement(ACTIONWEST, domain, 3, this.gridMap.getMap());

		new AtLocation(domain);

		return domain;
	}

	public State getExampleState(Domain domain) {
		State s = new MutableState();

		ObjectInstance agent = new MutableObjectInstance(domain.getObjectClass(CLASSAGENT), "agent0");
		agent.setValue(ATTX, this.gridMap.startPosition.x);
		agent.setValue(ATTY, this.gridMap.startPosition.y);
		s.addObject(agent);

		int endPosCounter = 0;
		for (Position endPos : this.gridMap.endPositions) {
			ObjectInstance location = new MutableObjectInstance(domain.getObjectClass(CLASSLOCATION),
					"location" + endPosCounter);
			location.setValue(ATTX, endPos.x);
			location.setValue(ATTY, endPos.y);
			s.addObject(location);
			endPosCounter++;
		}
		return s;
	}

	public StateRenderLayer getStateRenderLayer() {
		StateRenderLayer rl = new StateRenderLayer();
		rl.addStaticPainter(new WallPainter(this.gridMap.getMap()));
		rl.addObjectClassPainter(CLASSLOCATION, new LocationPainter(this.gridMap.getMap()));
		rl.addObjectClassPainter(CLASSAGENT, new AgentPainter(this.gridMap.getMap()));

		return rl;
	}

	public Visualizer getVisualizer() {
		return new Visualizer(this.getStateRenderLayer());
	}

	public GridMap getGridMap() {
		return this.gridMap;
	}

	public void setGridMap(GridMap gridMap) {
		this.gridMap = gridMap;
	}

}
