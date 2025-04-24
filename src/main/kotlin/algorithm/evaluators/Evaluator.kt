package algorithm.evaluators

import junction.Crossroad

interface Evaluator {
    fun eval(crossroad: Crossroad): Int
}