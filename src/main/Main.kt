fun main(args: Array<String>) {
    val configFilePath = args[0] if args.isNotEmpty() else "config.json"
    val config = ConfigLoader.loadConfig(configFilePath)

    val instructions: List<Instruction> = listOf()

    val simulation = Simulation(config, instructions)
    simulation.run()
}