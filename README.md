# Junction Simulation
This is a programming exercise that simulates traffic flow at a road intersection.  
Given a set of vehicle instructions and a configuration of the intersection, the program evaluates different combinations of traffic signal patterns and aims to select the optimal one based on a chosen performance metric (e.g., vehicle throughput or wait time).


## Installation

### 1. Clone or download the repository

### 2. Build the project using Gradle
From the root of the project:
- On Linux: `./gradlew build`  
- On Windows: `gradlew.bat build`

Ensure the following files are placed in `src/main/resources/`:
- `instructions.json`
- `log.json`
- `config.json`

---

## Usage

### Run the simulation with default files

```bash
./gradlew run
```

This will load the default files from `src/main/resources/`.

---

### Run the simulation with custom filenames

```bash
./gradlew run --args="custom_instructions.json custom_log.json custom_config.json"
```

Provide up to **three arguments** in order:
1. `instructions.json` (or custom name)
2. `log.json` (or custom name)
3. `config.json` (or custom name)

These files must still be located in `src/main/resources/`.

## Simulation description

### Parameters
- **@instructions** – A sequence of instructions to add vehicles or progress the simulation by one step.
- **@vehicles per lane per step** – Maximum number of vehicles that can pass through a lane in one step *(not yet implemented)*.
- **@light change duration** – Number of steps it takes for a traffic light to change from green to red or vice versa (i.e., yellow light duration).
- **@max algorithm depth** – Number of steps to simulate ahead when searching for the optimal signal pattern.
- **@junction configuration** – Structure and logic of the intersection *(work in progress)*.

### Simulating flow of vehicles on crossroad
Each pair of **direction** (`NORTH`, `EAST`, `SOUTH`, `WEST`) and **movement** (`LEFT`, `FORWARD`, `RIGHT`, `BACKWARDS`-*(to be added)*)—represented by one or more lanes—is assigned a **STRONG** or **WEAK** priority.

For example, if there are two lanes coming from the **NORTH**:
- One lane allows `FORWARD` and `RIGHT` movement,
- The other allows `LEFT` and `FORWARD`,

Then the system creates three movement pairs:
- `NORTH, FORWARD`
- `NORTH, LEFT`
- `NORTH, RIGHT`

This abstraction allows easier grouping of lanes from the same direction and simplifies changing corresponding traffic lights together.

- **Vehicles on STRONG lanes** always proceed.
- **Vehicles on WEAK lanes** may proceed *only if* doing so does not conflict with any STRONG lane traffic.
- **Conflicts between two WEAK lanes** are currently resolved based on the number of vehicles waiting on each lane (i.e., the fuller lane wins).


### Algorithm
The algorithm evaluates different combinations of traffic signals by recursively simulating their outcomes to a specified depth (a number of future steps).  
At each level:
1. It applies all instructions up to the next step.
2. It generates and tests all valid, non-conflicting signal combinations.
3. It scores the resulting state using a provided evaluator.

The combination that produces the highest score is selected, effectively performing a **depth-limited search** for the optimal traffic signal pattern.

### Simulating change of lights
The algorithm tests many signal combinations, ensuring that only **valid transitions** are considered.  
For example, a light transitioning from **red → yellow** cannot immediately switch back to **red**—it must first go **green**, respecting realistic traffic signal behavior.



