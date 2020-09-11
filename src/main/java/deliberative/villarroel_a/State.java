package deliberative.villarroel_a;

import logist.task.Task;
import logist.task.TaskSet;
import logist.topology.Topology;

import java.util.*;

public class State {
    private Topology.City current_city;

    private TaskSet delivery_list;
    private TaskSet pickup_list;

    private List<DeliberativeAction> list_of_visited_nodes;  // historial?

    private boolean applicable; // Verificar si el estado es optimo o accesible

    private double vehicle_capacity; // capacidad maxima del vehiculo?

    public State() {
    }

    public Topology.City getCurrent_city() {
        return current_city;
    }

    public List<DeliberativeAction> getList_of_visited_nodes() {
        return list_of_visited_nodes;
    }

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
        return delivery_list.isEmpty() && pickup_list.isEmpty();
    }

    public List<DeliberativeAction> get_possible_actions() {
        List<DeliberativeAction> possible_action_list = new ArrayList<>();

        for (Task task : delivery_list) {
            possible_action_list.add(new DeliberativeAction(task.deliveryCity, ActionStates.DELIVER, task));
        }

        for (Task task : pickup_list) {
//            if (is_possible_to_pickup()) {
            if (task.weight <= getVehicle_capacity()) {
                possible_action_list.add(new DeliberativeAction(task.pickupCity, ActionStates.PICKUP, task));
            }
        }
        return possible_action_list;
    }

    public boolean is_possible_to_pickup() {
        return (delivery_list.weightSum() <= getVehicle_capacity());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final State state;

        public Builder() {
            state = new State();
        }

        public Builder new_state(Topology.City current_city, TaskSet delivery_list, TaskSet pickup_list, List<DeliberativeAction> list_of_visited_nodes, boolean applicable, double vehicle_capacity) {
            state.current_city = current_city;
            state.delivery_list = delivery_list.clone();
            state.pickup_list = pickup_list.clone();
            state.list_of_visited_nodes = new ArrayList<>(list_of_visited_nodes);
            state.applicable = applicable;
            state.vehicle_capacity = vehicle_capacity;
            return this;
        }

        public State build() {
            return state;
        }

    }

    @Override
    public String toString() {
        return "State{" +
                "current_city=" + current_city +
                ", delivery_list=" + delivery_list +
                ", pickup_list=" + pickup_list +
                ", list_of_visited_nodes=" + list_of_visited_nodes +
                ", applicable=" + applicable +
                ", vehicle_capacity=" + vehicle_capacity +
                '}';
    }

}
