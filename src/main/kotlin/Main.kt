import instructions.Instruction
import loaders.ConfigLoader
import loaders.InstructionLoader

fun main(args: Array<String>) {
    val instructionsFilePath = if  (args.isNotEmpty()) args[0] else "instructions.json"
    val instructions: List<Instruction> = InstructionLoader.loadFromJson(instructionsFilePath)

    val loggerFilePath = if (args.size > 2) args[2] else "log.json"
    val logger = Logger(loggerFilePath)

    val configFilePath = if (args.size > 1) args[1] else "config.json"
    val config = ConfigLoader.loadFromJson(configFilePath)

    val simulation = Simulation(config, instructions, logger)
    simulation.run()
}