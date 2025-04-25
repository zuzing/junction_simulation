import instructions.Instruction
import loaders.ConfigLoader
import loaders.InstructionLoader

fun main(args: Array<String>) {
    val baseDir = "src/main/resources/"

    val instructionsFilePath = baseDir + if  (args.isNotEmpty()) args[0] else "instructions.json"
    val instructions: List<Instruction> = InstructionLoader.loadFromJson(instructionsFilePath)

    val loggerFilePath = baseDir + if (args.size > 1) args[1] else "log.json"
    val logger = Logger(loggerFilePath)

    val configFilePath = baseDir + if (args.size > 2) {
        println("Warning: currently only the default config.json is guaranteed to work")
        args[2]
    } else "config.json"
    val config = ConfigLoader.loadFromJson(configFilePath)

    val simulation = Simulation(config, instructions, logger)

    println("Starting simulation")

    simulation.run()
}