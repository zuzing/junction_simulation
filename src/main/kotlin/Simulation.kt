
import algorithm.TrafficController
import algorithm.evaluators.EvaluatorVehicleCount
import datatypes.SimulationConfig
import instructions.Instruction
import instructions.StepInstruction
import junction.Crossroad
import junction.SignalController

class Simulation(private val configuration: SimulationConfig, private val instructions: List<Instruction>,
                 private val logger: Logger) {

    private val signalController = SignalController(configuration.lanes, configuration.yellowLightDuration)
    private val crossroad = Crossroad(configuration.lanes, signalController, configuration.numVehiclesPerStep)

    private val trafficController = TrafficController(EvaluatorVehicleCount)
    fun run() {
        val queue = instructions.toMutableList()
        while (queue.isNotEmpty()) {
            when (val instr = queue.removeAt(0)) {
                is StepInstruction -> {
                    val bestCombination = trafficController.performAlgorithm(crossroad, queue, configuration.maxAlgorithmDepth)
                    if (bestCombination.isNotEmpty()) {
                        crossroad.signalController.changeLights(bestCombination)
                    } else {
                        throw IllegalArgumentException("No valid signal pattern found")
                    }

//                    instr.runOn(crossroad) uncomment when crossroad copy constructor is fixed

                    logger.logVehiclesLeft(crossroad)
                }
                else -> {
//                    instr.runOn(crossroad) uncomment when crossroad copy constructor is fixed
                }
            }
        }
        logger.finish()
    }
}