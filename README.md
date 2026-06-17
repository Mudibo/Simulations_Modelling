## Bank Queue Simulation Dashboard

A Java Swing analytics dashboard that simulates a single-server bank queue using discrete-event simulation.

The application is designed for simulation coursework and demonstrations, with a focus on:

- reproducible queue metrics
- transparent formula and substitution breakdowns
- customer-level traceability
- professional desktop presentation

## What The Application Does

1. Accepts simulation parameters (customer count and random ranges).
2. Generates customer arrivals and service events.
3. Computes queue behavior and performance characteristics dynamically.
4. Displays:

- customer-level event table
- queue characteristics visual analytics
- detailed calculation breakdown (formula, substitution, result)
- customer explanation dialog for row-by-row interpretation

5. Exports results to:

- CSV
- PDF

## Key Features

- Modern Swing UI with FlatLaf (with fallback look and feel handling).
- Scrollable dashboard and tab content for presentation-friendly navigation.
- Queue Characteristics with dedicated pages:

	- Visual Analytics
	- Calculation Derivation

- Calculation Breakdown table:

	- read-only
	- dynamically generated from simulation outputs
	- formula and substitution columns rendered with monospaced styling

- Totals Used panel for verification of summation terms.
- Customer Explanation modal for detailed queue mechanics per selected customer.

## Queue Metrics Covered

The dashboard highlights queue characteristics including:

- Average Waiting Time For A Customer
- Probability That A Customer Has To Wait
- Proportion Of Idle Time Of Server
- Probability Server Is Busy
- Average Service Time
- Average Waiting Time For Those Who Waited
- Average Time Spent In System

## Architecture Overview

The codebase follows a clean layered structure:

- app

	- bootstrap and startup

- domain

	- simulation models and core services

- application

	- orchestration and use-case coordination

- infrastructure

	- random generation and file export adapters

- presentation

	- Swing frames, dialogs, panels, and table models

- util

	- validation and shared formatting helpers

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
			QueueStatusExplanationService.java
			RandomGeneratorService.java

	application/
		SimulationController.java
		CalculationBreakdown.java
		CalculationReportGenerator.java

	infrastructure/
		random/
			UniformRandomGenerator.java
		export/
			CsvExporter.java
			PdfExporter.java

	presentation/
		input/
			InputFrame.java
		output/
			OutputFrame.java
			CustomerExplanationDialog.java
		table/
			CustomerTableModel.java
			CalculationTableModel.java
		components/
			DashboardHeaderPanel.java
			StatisticCard.java
			SummaryPanel.java
			CalculationBreakdownPanel.java

	util/
		AppBranding.java
		FormulaFormatter.java
		ValidationUtil.java
```

## Prerequisites

- Java 17 or newer
- Maven 3.8+ (recommended)

## Setup And Run

### Option 1: IntelliJ IDEA (Recommended)

1. Open IntelliJ IDEA.
2. Choose Open and select the Simulations_Modelling project directory.
3. Ensure SDK is Java 17 or newer.
4. Let IntelliJ import the Maven project from pom.xml.
5. Run app.Main.

### Option 2: VS Code

1. Open the project folder in VS Code.
2. Install Java Extension Pack if prompted.
3. Ensure Java 17+ is selected.
4. Run app.Main using the Run icon above the main method.

### Option 3: Terminal (Maven)

```powershell
Set-Location "<project-folder>"
mvn clean compile
mvn exec:java -Dexec.mainClass=app.Main
```

Notes:

- Maven is required for dependency-based runs (FlatLaf and PDFBox).
- Running with plain javac/java without dependency classpath will fail for external libraries.

## Export Outputs

- Export CSV: customer-level simulation data.
- Export PDF: formatted analytics report including totals and calculation breakdown sections.

## Default Input Values

- Number of Customers: 100
- Minimum Inter-Arrival Time: 1
- Maximum Inter-Arrival Time: 8
- Minimum Service Time: 1
- Maximum Service Time: 6

