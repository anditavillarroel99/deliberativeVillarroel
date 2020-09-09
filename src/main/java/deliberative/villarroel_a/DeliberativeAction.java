package deliberative.villarroel_a;

import logist.topology.Topology;

public class DeliberativeAction {
    private Topology.City destination_city;
    private ActionStates possible_action;

    public DeliberativeAction(Topology.City destination_city, ActionStates possible_action) {
        this.destination_city = destination_city;
        this.possible_action = possible_action;
    }

    public DeliberativeAction(){}

    public Topology.City getDestination_city() {
        return destination_city;
    }

    public void setDestination_city(Topology.City destination_city) {
        this.destination_city = destination_city;
    }

    public ActionStates getPossible_action() {
        return possible_action;
    }

    public void setPossible_action(ActionStates possible_action) {
        this.possible_action = possible_action;
    }
}
