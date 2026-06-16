## Bank Queue Simulation System

A production-quality Java Swing desktop application that simulates a single-server bank queue using discrete-event simulation.

## Features

- Two-window workflow:
	- Input window (`InputFrame`) for simulation parameters
	- Output window (`OutputFrame`) with customer-level table and queue statistics
- Clean Architecture separation across `domain`, `application`, `infrastructure`, and `presentation`
- Uniform random generation for inter-arrival and service times
- Full queue calculations per customer:
	- Inter-arrival time, arrival time
	- Service start, service end
	- Waiting time in queue, time in system
	- Server idle time
	- Number in queue, number in system
- Summary queue characteristics panel with 2-decimal formatting
- CSV export of all customer rows
- Input validation with clear error dialogs

## Technology

- Java 17+
- Java Swing
- JTable
- MVC-style controller/view interaction

## Project Structure

```text
src/
	app/
		Main.java

	domain/
		model/
			Customer.java
			SimulationConfig.java
			SimulationResult.java
			SimulationStatistics.java
		service/
			QueueSimulationService.java
			QueueSimulationServiceImpl.java
			RandomGeneratorService.java

	application/
		SimulationController.java

	infrastructure/
		random/
			UniformRandomGenerator.java
		export/
			CsvExporter.java

	presentation/
		input/
			InputFrame.java
		output/
			OutputFrame.java
		table/
			CustomerTableModel.java
		components/
			StatisticsPanel.java

	util/
		ValidationUtil.java
```

## Run In IntelliJ IDEA

1. Open IntelliJ IDEA.
2. Select **Open** and choose this project folder.
3. Ensure Project SDK is set to Java 17 or newer:
	 - `File -> Project Structure -> Project SDK`
4. Mark `src` as a source root if IntelliJ does not do it automatically:
	 - Right-click `src` -> **Mark Directory as** -> **Sources Root**
5. Open `src/app/Main.java`.
6. Click the green run icon next to `main` and run `Main`.

## Build And Run From Terminal (PowerShell)

```powershell
Set-Location "<project-folder>"
if (Test-Path out) { Remove-Item -Recurse -Force out }
New-Item -ItemType Directory -Path out | Out-Null
javac -d out (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName })
java -cp out app.Main
```

## Default Input Values

- Number of Customers: `100`
- Minimum Inter-Arrival Time: `1`
- Maximum Inter-Arrival Time: `8`
- Minimum Service Time: `1`
- Maximum Service Time: `6`
