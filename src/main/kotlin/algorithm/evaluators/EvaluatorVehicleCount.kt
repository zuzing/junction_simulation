package algorithm.evaluators

import junction.Crossroad

object EvaluatorVehicleCount : Evaluator {
    override fun eval(crossroad: Crossroad): Int {
        var totalVehicles = 0
        for (lane in crossroad.lanes.values.flatten()) {
            totalVehicles += lane.queueLength()
        }
        return -totalVehicles
    }
}