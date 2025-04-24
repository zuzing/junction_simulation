package datatypes

enum class SignalType {
    STANDARD_LIGHT, // On green: can always go STRAIGHT or RIGHT; can go LEFT or BACKWARDS if no oncoming traffic
    FILTER_LIGHT, // Similar to standard light, but additionally allows RIGHT turns on red if no oncoming traffic
    ARROW_LIGHT, // On green: can always go
}