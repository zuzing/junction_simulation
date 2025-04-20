import instructions.Instruction
import loaders.*

fun main(args: Array<String>) {
    val configFilePath = if (args.isNotEmpty()) args[0] else "config.json"
    val config = ConfigLoader.loadFromJson(configFilePath)

    val instructionsFilePath = if (args.size > 1) args[1] else "instructions.json"
    val instructions: List<Instruction> = InstructionLoader.loadFromJson(instructionsFilePath)

    val simulation = Simulation(config, instructions)
    simulation.run()
}