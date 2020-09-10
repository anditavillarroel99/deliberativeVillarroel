package deliberative.villarroel_a;

import deliberative.template.DeliberativeTemplate;
import logist.agent.Agent;
import logist.behavior.DeliberativeBehavior;
import logist.plan.Plan;
import logist.simulation.Vehicle;
import logist.task.Task;
import logist.task.TaskDistribution;
import logist.task.TaskSet;
import logist.topology.Topology;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    //    private void bfs(State initial_state){
    private State bfs(State initial_state) {
        LinkedList<State> q = new LinkedList<>();
        q.add(initial_state);

        State solution = null;

        do {
            State current_state = q.removeFirst();

            if (current_state.is_final_state()) {
                solution = current_state;
//                System.exit(0);
            }

            LinkedList<State> successors = new LinkedList<State>(get_next_states(current_state));

            q.addAll(successors);

        } while (!q.isEmpty());


        return solution;
    }


    public List<State> get_next_states(State initial_state) {
        List<State> next_states = new ArrayList<>();
        Topology.City current_city = initial_state.getCurrent_city();

        for (DeliberativeAction action : initial_state.get_possible_actions()) {
            List<Task> list_of_package_to_deliver = initial_state.getList_of_package_to_deliver();
//            TODO -> Crear funcion sucesora?
//            next_states.add(State.builder().new_state(action.getDestination_city(), list_of_package_to_deliver, null, false, 0).build());
            next_states.add(next_state(initial_state, action));
        }

        return next_states;
    }

    private State next_state(State initial_state, DeliberativeAction action){
        List<Task> list_of_package_to_deliver = initial_state.getList_of_package_to_deliver();
        List<DeliberativeAction> historial = initial_state.getList_of_visited_nodes();

        if(action.getPossible_action().equals(ActionStates.DELIVER)){
            list_of_package_to_deliver.remove(action.getTask());
        }else{ //Recoger Paquete?
            list_of_package_to_deliver.add(action.getTask());
        }

        historial.add(action);
        //TODO: Ver el peso de un paquete
        return (State.builder().new_state(action.getDestination_city(), list_of_package_to_deliver, historial, false, 0).build());
        //TODO -> applicable?

    }

    private Plan get_plan(State state) {
        Topology.City current_city = state.getCurrent_city();
        Plan plan = new Plan(current_city);

        for(DeliberativeAction action : state.getList_of_visited_nodes()) {
//            for (Topology.City city : current_city.pathTo(action.getTask().pickupCity)){
//                plan.appendMove(city);
//            }
            for (Topology.City city : current_city.pathTo(action.getDestination_city())) {
                plan.appendMove(city);

                if(action.getPossible_action().equals(ActionStates.DELIVER)){
                    plan.appendDelivery(action.getTask());
                }else { //PICKUP
                    plan.appendPickup(action.getTask());
                }

            }
            current_city = action.getDestination_city();

        }
        return plan;
    }

    @Override
    public Plan plan(Vehicle vehicle, TaskSet tasks) {
        Plan plan;

        plan = new Plan(vehicle.getCurrentCity());
//        plan = new Plan(vehicle.getCurrentCity(), null);

//        State.Builder initial_state = null;
//        initial_state.new_state(vehicle.getCurrentCity(), new ArrayList<>(vehicle.getCurrentTasks()));

        // Compute the plan with the selected algorithm.
        State optimal_state = null;
        switch (algorithm) {
            case ASTAR:
                plan = naivePlan(vehicle, tasks);
                System.out.println(" --------------------------- ");
                break;
            case BFS:
//                plan = naivePlan(vehicle, tasks);
                System.out.println("-> BFS");
                optimal_state = bfs(State.builder().new_state(vehicle.getCurrentCity(), new ArrayList<>(vehicle.getCurrentTasks()), new ArrayList<>(), false, vehicle.capacity()).build());

                break;
            default:
                throw new AssertionError("Should not happen.");
        }

        if(optimal_state != null ){
//      Obtener plan de estados?
            plan = get_plan(optimal_state);
        }
        return plan;
    }

    private Plan naivePlan(Vehicle vehicle, TaskSet tasks) {
        Topology.City current = vehicle.getCurrentCity();
        Plan plan = new Plan(current);

        for (Task task : tasks) {
            // move: current city => pickup location
            for (Topology.City city : current.pathTo(task.pickupCity))
                plan.appendMove(city);

            plan.appendPickup(task);

            // move: pickup location => delivery location
            for (Topology.City city : task.path())
                plan.appendMove(city);

            plan.appendDelivery(task);

            // set current city
            current = task.deliveryCity;
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
