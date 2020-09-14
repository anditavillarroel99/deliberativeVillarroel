package deliberative.villarroel_a;

import logist.task.Task;
import logist.task.TaskSet;
import logist.topology.Topology;

import java.util.*;

public class State {

    private Topology.City current_city;

    private TaskSet delivery_list;
    private TaskSet pickup_list;

    private List<DeliberativeAction> list_of_visited_nodes;

    private double vehicle_capacity;
    private double heuristic;

    public State() {
        this.delivery_list = null;
        this.pickup_list = null;
        this.list_of_visited_nodes = new ArrayList<>();
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
            if (task.weight <= getVehicle_capacity()) {
                possible_action_list.add(new DeliberativeAction(task.pickupCity, ActionStates.PICKUP, task));
            }
        }

        return possible_action_list;
    }

    public static Builder builder() {
        return new Builder();
    }

    public double getHeuristic() {
        return heuristic;
    }

    public static class Builder {
        private final State state;

        public Builder() {
            state = new State();
        }

        public Builder new_state(Topology.City current_city, TaskSet delivery_list, TaskSet pickup_list, List<DeliberativeAction> list_of_visited_nodes, double vehicle_capacity, double heuristic) {
            state.current_city = current_city;
            state.delivery_list = delivery_list.clone();
            state.pickup_list = pickup_list.clone();
            state.list_of_visited_nodes = new ArrayList<>(list_of_visited_nodes);
            state.vehicle_capacity = vehicle_capacity;
            state.heuristic = heuristic;

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
                ", vehicle_capacity=" + vehicle_capacity +
                ", heuristic=" + heuristic +
                '}';
    }


//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        State state = (State) o;
//        return state.getCurrent_city() == this.current_city
//                && state.getDelivery_list() == this.delivery_list
//                && state.getPickup_list() == this.pickup_list
//                && state.getList_of_visited_nodes() == this.list_of_visited_nodes
//                && state.getVehicle_capacity() == this.vehicle_capacity
//                && state.getHeuristic() == this.heuristic;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(current_city, delivery_list, pickup_list, list_of_visited_nodes, vehicle_capacity, heuristic);
//    }
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        State state = (State) o;
//        return state.getCurrent_city() == this.current_city
//                && state.getDelivery_list() == this.delivery_list
//                && state.getPickup_list() == this.pickup_list
//                && state.getList_of_visited_nodes() == this.list_of_visited_nodes
//                && state.getVehicle_capacity() == this.vehicle_capacity
//                && state.getHeuristic() == this.heuristic;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(current_city, delivery_list, pickup_list, list_of_visited_nodes, vehicle_capacity, heuristic);
//    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        State state = (State) o;
//        return Double.compare(state.vehicle_capacity, vehicle_capacity) == 0 &&
//                Double.compare(state.heuristic, heuristic) == 0 &&
//                current_city.equals(state.current_city) &&
//                delivery_list.equals(state.delivery_list) &&
//                pickup_list.equals(state.pickup_list) &&
//                list_of_visited_nodes.equals(state.list_of_visited_nodes);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(current_city, delivery_list, pickup_list, list_of_visited_nodes, vehicle_capacity, heuristic);
//    }


    //    @Override
//    public boolean equals(Object o) {
//        if(o instanceof State) {
//            State oState = (State) o;
//            return oState.getList_of_visited_nodes().equals(list_of_visited_nodes)  &&
//                    oState.getCurrent_city().equals(current_city);
//        } else {
//            return false;
//        }
//    }
//
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(delivery_list, pickup_list, current_city);
//    }
}
