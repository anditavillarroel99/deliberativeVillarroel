# **Informe del Agente Deliverativo**

El estado del mundo se definió a partir de 3 Clases 
- La clase `State`, que cuenta con: 
    
    - La ciudad actual `current_city` en la que se encuentra el vehículo
    - Una lista de espera de paquetes a entregar `delivery_list`
    - Una lista de los paquetes o tareas existentes, que pueden ser tomadas `pickup_list`
    - Un historial de las acciones realizadas `list_of_visited_nodes`
    - La capacidad del vehículo según el estado, en el que se encuentre `vehicle_capacity`
    - La heuristica `heuristic`,que será recalculada según las condiciones en las que se encuentre el vehículo si realiza cierta acción.
  
 - La clase `ActionStates`, que simplemente definirá si la acción esperada a realizar es recoger (`PICKUP`) o entregar (`DELIVER`) un paquete.
 
 - La clase `DeliberativeAction` que cuenta con:
    - La tarea específica `task`
    - El tipo de acción a realizar `action` que estará definida por la clase `ActionStates`
    - La ciudad de Destino `destination_city` según el tipo de acción  
   
Para definir a cada estado sucesor, de manera óptima, se utilizó el patrón Builder y todas las acciones posibles según el `delivery_list` y el `pickup_list`, es decir, que si existen elementos en la lista de `pickup_list`, estos elementos pueden ser "recogidos", así mismo, si existen elementos en el `delivery_list`, significa que existen elementos en espera a ser entregados, y se los define como tal llamando a la clase `DeliberativeAction`.
Por su parte, una vez que se haya definido el tipo de acción a realizar, se saca la acción de la lista correspondiente, se agrega la acción al historial (`list_of_visited_nodes`), y se recalcula el valor de  `heurístic` y `vehicle_capacity` para verificar si el vehículo pueda seguir recogiendo paquetes, y si la acción a realizar es óptima o no.

**Definición de la Heurística para el Algoritmo ASTAR**

Para la heurística, se tomó en cuenta, el valor la heurística del nodo padre más, la distancia de la ciudad en la que se encuentra el vehículo en realición a la ciudad de la posible acción a realizar, multiplicado por el costo del vehículo por kilómetro. 

**Algoritmo ASTAR**

El algoritmo ASTAR busca el valor más pequeño de la lista de estados posibles; para ello, utiliza al patrón Comparator, y la herística más la cantidad de estados que se encuentran en el historial más el número de estados vistos.
Como el algoritmo busca el valor pequeño posible, una vez escogido, se filtra la lista de estados para buscar el siguiente más óptimo 