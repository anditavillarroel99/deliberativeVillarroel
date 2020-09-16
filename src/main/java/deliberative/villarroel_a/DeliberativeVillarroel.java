package deliberative.villarroel_a;

import logist.agent.Agent;
import logist.behavior.DeliberativeBehavior;
import logist.plan.Plan;
import logist.simulation.Vehicle;
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
        String algorithmName = agent.readProperty("algorithm", String.class, "ASTAR");

        // Throws IllegalArgumentException if algorithm is unknown
        algorithm = Algorithm.valueOf(algorithmName.toUpperCase());

    }

    private State a_star(State initial_state) {
        LinkedList<State> q = new LinkedList<>();
        q.add(initial_state);

        HashSet<State> seen = new HashSet<>();

        State bestChoice = null;

        do {
//            Optional<State> optional = q.stream().min((s1, s2) -> Double.compare(s1.getHeuristic() + s1.getList_of_visited_nodes().size(), s2.getHeuristic() + s2.getList_of_visited_nodes().size()));
            Optional<State> optionalState = q.stream().min(Comparator.comparingDouble(s -> s.getHeuristic() + (s.getList_of_visited_nodes().size() + seen.size())));
            if (optionalState.isPresent()) {

                State current_state = optionalState.get();
                System.out.println(current_state);

                q.removeIf(state -> !state.equals(current_state));

                if (current_state.is_final_state()) {
                    bestChoice = current_state;
                }

                q.remove(current_state);

                if (not_contains(seen, current_state)) {
                    LinkedList<State> successors = new LinkedList<>(get_next_states(current_state));
                    seen.add(current_state);
                    q.addAll(successors);
                }
            } else {
                throw new IllegalStateException("Unexpectedly, no state remains");
            }
        } while (!q.isEmpty() && bestChoice == null);

        return bestChoice;
    }


    private State bfs(State initial_state) {
        int cont = 0;

        LinkedList<State> q = new LinkedList<>();
        q.add(initial_state);

        HashSet<State> seen = new HashSet<>();
        State best_choice = null;

        do {
            State current_state = q.removeFirst();
            cont++;

            if (current_state.is_final_state()) {
                best_choice = current_state;
            }

            if (not_contains(seen, current_state)) {
                LinkedList<State> successors = new LinkedList<>(get_next_states(current_state));
                seen.add(current_state);
                q.addAll(successors);
            }

        } while (!q.isEmpty() && best_choice == null);

//        System.out.println("-> Solution found after " + cont + " states");

        return best_choice;
    }

    private static boolean not_contains(HashSet<State> seen, State state) {
        for (State s_state : seen) {
            if (s_state.is_equivalent(state)) {
                return false;
            }
        }
        return true;
    }

    public List<State> get_next_states(State initial_state) {
        List<State> next_states = new ArrayList<>();

        for (DeliberativeAction action : initial_state.get_possible_actions()) {
            next_states.add(get_next_state(initial_state, action));
        }

        return next_states;
    }

    private State get_next_state(State state, DeliberativeAction action) {

        TaskSet delivery_list = state.getDelivery_list().clone();
        TaskSet pickup_list = state.getPickup_list().clone();

        double capacity = state.getVehicle_capacity();
        List<DeliberativeAction> list_of_visited_nodes = new ArrayList<>(state.getList_of_visited_nodes());

        State.Builder new_builder = State.builder();

        if (action.getAction().equals(ActionStates.DELIVER)) {
            delivery_list.remove(action.getTask());
            if (agent.vehicles().get(0).capacity() >= capacity) {
                capacity = capacity + action.getTask().weight;
            }
        } else { //PICKUP
            delivery_list.add(action.getTask());
            pickup_list.remove(action.getTask());
            capacity = capacity - action.getTask().weight;
        }

        list_of_visited_nodes.add(action);

        double heuristic = state.getHeuristic()
                + (state.getCurrent_city().distanceTo(action.getDestination_city()) * agent.vehicles().get(0).costPerKm())
                + delivery_list.size() * 10 + pickup_list.size() * 10;//+ action.getTask().reward;

        return (new_builder.new_state(action.getDestination_city(), delivery_list, pickup_list, list_of_visited_nodes, capacity, heuristic).build());
    }

    private Plan get_plan(State state, Vehicle vehicle) {
        Topology.City current_city = vehicle.getCurrentCity();
        Plan plan = new Plan(current_city);

        for (DeliberativeAction action : state.getList_of_visited_nodes()) {

            for (Topology.City city : current_city.pathTo(action.getDestination_city())) {
                plan.appendMove(city);
            }

            current_city = action.getDestination_city();

            if (action.getAction().equals(ActionStates.DELIVER)) {
                plan.appendDelivery(action.getTask());

            } else { // PICKUP
                plan.appendPickup(action.getTask());
            }
        }

        return plan;
    }

    @Override
    public Plan plan(Vehicle vehicle, TaskSet tasks) {
        Plan plan;

        State best_option;

        switch (algorithm) {
            case ASTAR:
                System.out.println("-> ASTAR");
                best_option = a_star(State.builder().new_state(vehicle.getCurrentCity(), vehicle.getCurrentTasks(), tasks, new ArrayList<>(), agent.vehicles().get(0).capacity(), 0).build());
                plan = get_plan(best_option, vehicle);

                break;
            case BFS:
                System.out.println("-> BFS");
                best_option = bfs(State.builder().new_state(vehicle.getCurrentCity(), vehicle.getCurrentTasks(), tasks, new ArrayList<>(), agent.vehicles().get(0).capacity(), 0).build());
                plan = get_plan(best_option, vehicle);

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
