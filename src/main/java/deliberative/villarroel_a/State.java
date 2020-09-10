package deliberative.villarroel_a;

import logist.task.Task;
import logist.topology.Topology;

import java.util.ArrayList;
import java.util.List;

public class State {
    private Topology.City current_city;
//    private Topology.City package_destination;

    private List<Task> list_of_package_to_deliver;
    private List<DeliberativeAction>  list_of_visited_nodes; // historial?

    private boolean applicable ; // Verificar si el estado es optimo o accesible

    private double vehicle_capacity ; // capacidad maxima que el vehiculo pueda soportar

    public State(Topology.City current_city, List<Task> list_of_package_to_deliver, List<DeliberativeAction> list_of_visited_nodes, boolean applicable, double vehicle_capacity) {
        this.current_city = current_city;
        this.list_of_package_to_deliver = list_of_package_to_deliver;
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

    public boolean isApplicable() {
        return applicable;
    }

    public double getVehicle_capacity() {
        return vehicle_capacity;
    }

    public boolean is_final_state() {
        return list_of_package_to_deliver.isEmpty();
    }

    public List<DeliberativeAction> get_possible_actions() {
        List<DeliberativeAction> possible_action_list = new ArrayList<>();

        for (Task task : list_of_package_to_deliver) {
            possible_action_list.add(new DeliberativeAction(task.deliveryCity, ActionStates.DELIVER, task));
        }
        //TODO debe entregar todos los paquetes posibles
        // -> ver capacidad del vehiculo

        return possible_action_list;
    }

    @Override
    public String toString() {
        return "State{" +
                "current_city=" + current_city +
//                ", package_destination=" + package_destination +
                ", list_of_package_to_deliver=" + list_of_package_to_deliver +
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

        public Builder new_state(Topology.City current_city, List<Task> list_of_package_to_deliver, List<DeliberativeAction> list_of_visited_nodes, boolean applicable, double vehicle_capacity) {
            state.current_city = current_city;
            state.list_of_package_to_deliver = list_of_package_to_deliver;
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
