import loaders.ConfigLoader

fun main(args: Array<String>) {
    val configFilePath = if (args.isNotEmpty()) args[0] else "config.json"
    val config = ConfigLoader.loadConfigFromJson(configFilePath)

    val instructions: List<Instruction> = listOf()

    val simulation = Simulation(config, instructions)
    simulation.run()
}