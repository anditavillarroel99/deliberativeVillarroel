package deliberative.villarroel_a;

import logist.agent.Agent;
import logist.behavior.DeliberativeBehavior;
import logist.plan.Plan;
import logist.simulation.Vehicle;
import logist.task.Task;
import logist.task.TaskDistribution;
import logist.task.TaskSet;
import logist.topology.Topology;

import java.util.*;

public class DeliberativeVillarroel implements DeliberativeBehavior {

    enum Algorithm {BFS, ASTAR}

    /* Environment */
    Topology topology;
    TaskDistribution td;

    /* the properties of the agent */
    Agent agent;
    int capacity;

    /* the planning class */
    Algorithm algorithm;


    @Override
    public void setup(Topology topology, TaskDistribution taskDistribution, Agent agent) {
        this.topology = topology;
        this.td = taskDistribution;
        this.agent = agent;

        // initialize the planner
        int capacity = agent.vehicles().get(0).capacity();
        String algorithmName = agent.readProperty("algorithm", String.class, "ASTAR");

        // Throws IllegalArgumentException if algorithm is unknown
        algorithm = Algorithm.valueOf(algorithmName.toUpperCase());

    }

    private State a_star(State initial_state) {
        LinkedList<State> q = new LinkedList<>(); // frontera
        q.add(initial_state);

        Optional<State> bestChoice = Optional.empty();

        while (!q.isEmpty() && bestChoice.isEmpty()) {
//            bestChoice = q.stream().min(Comparator.comparingInt(State::h1));
           bestChoice = q.stream().min((s1,s2) -> Double.compare(s1.getHeuristic(), s2.getHeuristic()));
        }
        return bestChoice.get();
    }

    private State bfs(State initial_state) {
        LinkedList<State> q = new LinkedList<>();
        q.add(initial_state);

        State solution = null;

        do {
            State current_state = q.removeFirst();

            if (current_state.is_final_state()) {
                solution = current_state;
            }

            LinkedList<State> successors = new LinkedList<State>(get_next_states(current_state));

            q.addAll(successors);

        } while (!q.isEmpty());

        return solution;
    }


    public List<State> get_next_states(State initial_state) {
        List<State> next_states = new ArrayList<>();

        for (DeliberativeAction action : initial_state.get_possible_actions()) {
                next_states.add(get_next_state(initial_state, action));
        }

        return next_states;
    }

    private DeliberativeAction get_action_for_the_same_city(State initial_state, DeliberativeAction action) {
        Topology.City city = action.getDestination_city();
        DeliberativeAction new_action = null;

        for (DeliberativeAction deliberativeAction : initial_state.get_possible_actions()) {
            if(!action.equals(deliberativeAction) && deliberativeAction.getDestination_city().equals(city) ){
                new_action = deliberativeAction;
            }
        }
        return new_action;
    }

    private State get_next_state(State initial_state, DeliberativeAction action) {
        TaskSet delivery_list = initial_state.getDelivery_list();
        TaskSet pickup_list = initial_state.getPickup_list();

        double capacity = initial_state.getVehicle_capacity();

        List<DeliberativeAction> historial = initial_state.getList_of_visited_nodes();

        if (action.getPossible_action().equals(ActionStates.DELIVER)) {
            delivery_list.remove(action.getTask());
            capacity = capacity + action.getTask().weight;

        } else { //Recoger Paquete?
            delivery_list.add(action.getTask());
            pickup_list.remove(action.getTask());
            capacity = capacity - action.getTask().weight;
        }

        historial.add(action);
        double heuristic = initial_state.getHeuristic() - (initial_state.getCurrent_city().distanceTo(action.getDestination_city()) * agent.vehicles().get(0).costPerKm());

        return (State.builder().new_state(action.getDestination_city(), delivery_list, pickup_list, historial, capacity, heuristic).build());
    }

    private Plan get_plan(State state, Topology.City current_city) {

        Plan plan = new Plan(current_city);

        for (DeliberativeAction action : state.getList_of_visited_nodes()) {

            for (Topology.City city : current_city.pathTo(action.getDestination_city())) {
                plan.appendMove(city);
            }

            if (action.getPossible_action().equals(ActionStates.DELIVER)) {
                plan.appendDelivery(action.getTask());
            } else { // PICKUP
                plan.appendPickup(action.getTask());
            }

            current_city = action.getDestination_city();

        }

        return plan;
    }

    @Override
    public Plan plan(Vehicle vehicle, TaskSet tasks) {
        Plan plan;

        // Compute the plan with the selected algorithm.
        State optimal_state = null;
        switch (algorithm) {
            case ASTAR:
                System.out.println("-> ASTAR");
                optimal_state = a_star(State.builder().new_state(vehicle.getCurrentCity(), vehicle.getCurrentTasks(), tasks, new ArrayList<>(), vehicle.capacity(),0).build());
                plan = get_plan(optimal_state, vehicle.getCurrentCity());
                break;
            case BFS:

                System.out.println("-> BFS");
                optimal_state = bfs(State.builder().new_state(vehicle.getCurrentCity(), vehicle.getCurrentTasks(), tasks, new ArrayList<>(), vehicle.capacity(), 0).build());
                plan = get_plan(optimal_state, vehicle.getCurrentCity());

                break;
            default:
                throw new AssertionError("Should not happen.");
        }

        return plan;
    }


    @Override
    public void planCancelled(TaskSet carriedTasks) {

        if (!carriedTasks.isEmpty()) {
            // This cannot happen for this simple agent, but typically
            // you will need to consider the carriedTasks when the next
            // plan is computed.
        }
    }
}
