package deliberative.villarroel_a;

import logist.task.Task;
import logist.topology.Topology;

public class DeliberativeAction {

    private final Topology.City destination_city;
    private final ActionStates action;
    private final Task task;

    public DeliberativeAction(Topology.City destination_city, ActionStates action, Task task) {
        this.destination_city = destination_city;
        this.action = action;
        this.task = task;
    }

    public Topology.City getDestination_city() {
        return destination_city;
    }

    public ActionStates getAction() {
        return action;
    }

    public Task getTask() {
        return task;
    }

    @Override
    public String toString() {
        return "DeliberativeAction{" +
                "destination_city=" + destination_city +
                ", action=" + action +
                ", task=" + task +
                '}';
    }
}
