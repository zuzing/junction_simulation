import instructions.Instruction
import loaders.SimulationConfig
import junction.Crossroad

class Simulation(private val configuration: SimulationConfig, private val instructions: List<Instruction>) {

    private val crossroad = Crossroad(configuration.lanes)


    fun run() {

    }
}