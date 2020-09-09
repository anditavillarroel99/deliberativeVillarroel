package deliberative.villarroel_a;

import logist.task.Task;
import logist.topology.Topology;

import java.util.ArrayList;
import java.util.List;

public class State {
    private Topology.City current_city;
//    private Topology.City package_destination;

    private List<Task> list_of_package_to_deliver;
    private List<Task> list_of_available_tasks;

    private List<DeliberativeAction> possible_action_list;

    //    public State(Topology.City current_city,Topology.City package_destination , List<Task> list_of_package_to_deliver, List<Task> list_of_available_tasks ) {
    public State(Topology.City current_city, List<Task> list_of_package_to_deliver, List<Task> list_of_available_tasks) {
        this.current_city = current_city;
//        this.package_destination = package_destination;
        this.list_of_package_to_deliver = new ArrayList<>(list_of_package_to_deliver);
//        this.list_of_available_tasks = new ArrayList<>(list_of_available_tasks);

    }

    public State() {
    }

    public Topology.City getCurrent_city() {
        return current_city;
    }

//    public Topology.City getPackage_destination() {
//        return package_destination;
//    }

    public List<Task> getList_of_package_to_deliver() {
        return list_of_package_to_deliver;
    }

//    public List<Task> getList_of_available_tasks() {
//        return list_of_available_tasks;
//    }

    public boolean is_final_state() {
        return list_of_package_to_deliver.isEmpty();
    }

    public List<DeliberativeAction> get_possible_actions() {
        possible_action_list = new ArrayList<>();

        for (Task task : list_of_package_to_deliver) {
            possible_action_list.add(new DeliberativeAction(task.deliveryCity, ActionStates.DELIVER, task));
//            possible_action_list.add(new DeliberativeAction(task.deliveryCity, ActionStates.DELIVER));
        }

        return possible_action_list;
    }

    @Override
    public String toString() {
        return "State{" +
                "current_city=" + current_city +
//                ", package_destination=" + package_destination +
                ", list_of_package_to_deliver=" + list_of_package_to_deliver +
                ", list_of_available_tasks=" + list_of_available_tasks +
                '}';
    }



    //    public void setPackage_destination(Topology.City package_destination) {
//        this.package_destination = package_destination;
//    }
    public static Builder builder() {  return new Builder(); }

    public static class Builder {
        private final State state;

        private Builder(){
            state = new State();
        }

//        public Builder new_state(Topology.City current_city, List<Task> list_of_package_to_deliver, List<Task> list_of_available_tasks) {
        public Builder new_state(Topology.City current_city, List<Task> list_of_package_to_deliver) {
            state.current_city = current_city;
            state.list_of_package_to_deliver = list_of_package_to_deliver;
            return this;
        }

        public State build() {
            return state;
        }


    }

}
