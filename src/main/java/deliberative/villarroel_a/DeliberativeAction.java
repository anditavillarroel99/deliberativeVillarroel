package deliberative.villarroel_a;

import deliberative.IA.GoAndDeliver;
import logist.task.Task;
import logist.topology.Topology;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliberativeAction action1 = (DeliberativeAction) o;
        return Objects.equals(destination_city, action1.destination_city) &&
                action == action1.action &&
                Objects.equals(task, action1.task);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destination_city, action, task);
    }
}
