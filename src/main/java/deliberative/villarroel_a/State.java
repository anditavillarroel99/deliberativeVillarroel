package deliberative.villarroel_a;

import logist.task.Task;
import logist.task.TaskSet;
import logist.topology.Topology;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class State {
    private Topology.City current_city;
//    private Topology.City package_destination;

    private List<Task> list_of_package_to_deliver;
//    private Set<Task> delivery_list;
    private TaskSet delivery_list;
    private TaskSet pickup_list; //?
    private List<DeliberativeAction>  list_of_visited_nodes; // historial?

    private boolean applicable ; // Verificar si el estado es optimo o accesible

    private double vehicle_capacity ; // capacidad maxima del vehiculo?

//    public State(Topology.City current_city, List<Task> list_of_package_to_deliver, List<DeliberativeAction> list_of_visited_nodes, boolean applicable, double vehicle_capacity) {
//    public State(Topology.City current_city, Set<Task> delivery_list, List<DeliberativeAction> list_of_visited_nodes, boolean applicable, double vehicle_capacity) {
//    public State(Topology.City current_city, TaskSet delivery_list, List<DeliberativeAction> list_of_visited_nodes, boolean applicable, double vehicle_capacity) {
    public State(Topology.City current_city, TaskSet delivery_list, TaskSet pickup_list,List<DeliberativeAction> list_of_visited_nodes, boolean applicable, double vehicle_capacity) {
        this.current_city = current_city;
//        this.list_of_package_to_deliver = list_of_package_to_deliver;
        this.delivery_list = delivery_list;
        this.pickup_list = pickup_list;
        this.list_of_visited_nodes = list_of_visited_nodes;
        this.applicable = applicable;
        this.vehicle_capacity = vehicle_capacity;
    }

    public State() {}

    public Topology.City getCurrent_city() {
        return current_city;
    }

    public List<Task> getList_of_package_to_deliver() {
        return list_of_package_to_deliver;
    }

    public List<DeliberativeAction> getList_of_visited_nodes() {
        return list_of_visited_nodes;
    }

//    public Set<Task> getDelivery_list() {
//        return delivery_list;
//    }
//    public Set<Task> getPickup_list() {
//        return pickup_list;
//    }
    public TaskSet getDelivery_list() {
        return delivery_list;
    }

    public TaskSet getPickup_list() {
        return pickup_list;
    }

    public boolean isApplicable() {
        return applicable;
    }

    public double getVehicle_capacity() {
        return vehicle_capacity;
    }

    public boolean is_final_state() {
//        return list_of_package_to_deliver.isEmpty();
//        return delivery_list.isEmpty();
        return delivery_list.isEmpty() && pickup_list.isEmpty();
    }

    public List<DeliberativeAction> get_possible_actions() {
        List<DeliberativeAction> possible_action_list = new ArrayList<>();

//        for (Task task : list_of_package_to_deliver) {
        for (Task task : delivery_list) {
            possible_action_list.add(new DeliberativeAction(task.deliveryCity, ActionStates.DELIVER, task));
            //        possible_action_list.add(new DeliberativeAction(task.deliveryCity, ActionStates.PICKUP, task));

        }
        if(applicable){

            //TODO debe entregar todos los paquetes posibles
            // -> ver capacidad del vehiculo

            for(Task task: pickup_list){
                possible_action_list.add(new DeliberativeAction(task.pickupCity, ActionStates.PICKUP, task));
            }
        }

        return possible_action_list;
    }

    @Override
    public String toString() {
        return "State{" +
                "current_city=" + current_city +
//                ", package_destination=" + package_destination +
//                ", list_of_package_to_deliver=" + list_of_package_to_deliver +
                ", delivery_list=" + delivery_list +
                ", pickup_list=" + pickup_list +
                ", list_of_visited_nodes=" + list_of_visited_nodes +
                ", applicable=" + applicable +
                ", vehicle_capacity=" + vehicle_capacity +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final State state;

        private Builder() {
            state = new State();
        }

//        public Builder new_state(Topology.City current_city, List<Task> list_of_package_to_deliver, List<DeliberativeAction> list_of_visited_nodes, boolean applicable, double vehicle_capacity) {
//        public Builder new_state(Topology.City current_city, Set<Task> delivery_list, List<DeliberativeAction> list_of_visited_nodes, boolean applicable, double vehicle_capacity) {
//        public Builder new_state(Topology.City current_city, TaskSet delivery_list, List<DeliberativeAction> list_of_visited_nodes, boolean applicable, double vehicle_capacity) {
        public Builder new_state(Topology.City current_city, TaskSet delivery_list,TaskSet pickup_list ,List<DeliberativeAction> list_of_visited_nodes, boolean applicable, double vehicle_capacity) {
            state.current_city = current_city;
//            state.list_of_package_to_deliver = list_of_package_to_deliver;
            state.delivery_list = delivery_list;
            state.pickup_list = pickup_list;
            state.list_of_visited_nodes = list_of_visited_nodes;
            state.applicable = applicable;
            state.vehicle_capacity = vehicle_capacity;

            return this;
        }

        public State build() {
            return state;
        }


    }

}
