package deliberative.villarroel_a;

import logist.task.Task;
import logist.topology.Topology;

public class DeliberativeAction {

    private Topology.City destination_city;//package_destination
    private ActionStates possible_action;
    private Task task;

    public DeliberativeAction(Topology.City destination_city, ActionStates possible_action, Task task) {
        this.destination_city = destination_city;
        this.possible_action = possible_action;
        this.task = task;
    }

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

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return "DeliberativeAction{" +
                "destination_city=" + destination_city +
                ", possible_action=" + possible_action +
                ", task=" + task +
                '}';
    }
}
