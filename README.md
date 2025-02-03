# UML Generator Application

![License](https://img.shields.io/badge/license-MIT%20with%20Commons%20Clause-blue.svg)
![Java](https://img.shields.io/badge/Java-8%2B-blue.svg)
![PlantUML](https://img.shields.io/badge/PlantUML-1.2021.14-blue.svg)
![JavaParser](https://img.shields.io/badge/JavaParser-3.25.4-blue.svg)

<div align="center">
  <img src="https://github.com/fabiobassini/aMULeto/blob/main/demo/amuleto.png" alt="Header" width="350" style="border-radius: 35px;"/>
</div>

## 📑 Table of Contents

1. [Introduction](#introduction)
2. [Features](#features)
3. [Demo](#demo)
4. [Installation](#installation)
   - [Prerequisites](#prerequisites)
   - [Setup](#setup)
5. [Usage](#usage)
   - [Command Line Mode](#command-line-mode)
   - [GUI Mode](#gui-mode)
6. [License](#license)
7. [Author](#author)
8. [Acknowledgements](#acknowledgements)
9. [Contributing](#contributing)
10. [Support](#support)

---

## 📌 Introduction

Welcome to the **UML Generator Application**!  
This tool analyzes a multi-file Java project using [JavaParser](https://javaparser.org/) and automatically generates a UML class diagram in [PlantUML](http://plantuml.com/) format. The application helps developers visualize the architecture of their Java applications by displaying classes, interfaces, enumerations, and their relationships.

### 🔹 Key Enhancements:
- **Grouping by Package:** Organizes types into package blocks.
- **Exclusion of Generator Classes:** Filters out internal generator classes.
- **Association Classes:** Supports `@AssociationClass` with a dedicated stereotype.
- **SVG Diagram Generation & Viewing (GUI Mode):** Generates a diagram in SVG format, saves it on the desktop under a folder named **aMULeto**, and displays it with zoom and pan controls.

---

## 🚀 Features

✔ **Multi-File Support** – Recursively scans Java files in the source directory.  
✔ **Automatic Package Grouping** – Organizes classes into their respective packages.  
✔ **Exclusion of Generator Code** – Omits internal classes (`App`, `UMLGenerator`, `UMLUtils`).  
✔ **Comprehensive UML Relationships**:
  - **Associations:** Displays links between classes with multiplicities.
  - **Aggregations & Compositions:** Represented as `o-->` (aggregation) and `*-->` (composition).
  - **Dependencies:** Visualized as `..>`, based on method parameter types.
  - **Inheritance & Implementation:** `<|--` for inheritance, `..|>` for interface implementations.
✔ **Detailed Class Representation**:
  - Attributes, methods, constructors, and visibility markers (`+`, `-`, `#`, `~`).
  - **Association Classes:** Marked with the `<<association>>` stereotype.
✔ **SVG Diagram Generation and Viewing (GUI Mode)**:
  - Generates an SVG diagram from a Java project.
  - Saves the SVG file in the **aMULeto** folder on the user's desktop.
  - Displays the diagram with pan and zoom capabilities.

---

## 🎥 Demo

![UML Generator Demo](https://github.com/fabiobassini/aMULeto/blob/main/demo/amuleto.gif)  
*Figure 1: aMULeto demo diagram.*

---

## 🛠️ Installation

### Prerequisites
- **Java 8+**: Download from [java.com](https://www.java.com/download/).
- **JavaParser (3.25.4 or later)**: Used for Java source file parsing.
- **PlantUML**: Required to visualize the UML output (`diagramma.puml`).
- **Batik**: Required for SVG diagram viewing (ensure inclusion of `batik-swing.jar` and related dependencies).

### Setup
You can either build the project from source or use the precompiled JAR file.

#### Option 1: Build from Source
1. **Clone the Repository:**
   ```bash
   git clone https://github.com/fabiobassini/uml-generator.git
   cd uml-generator
   ```
2. **Build the Project:**
  - Add a test project in config.properties
  ```bash
  projectDirectory=/Users/.../TestProject/src/main/
  ```
  
  - 
  ```bash
   mvn clean install
  ```

#### Option 2: Use Precompiled JAR
Download the latest release from the [Releases](https://github.com/fabiobassini/aMULeto/releases) section and use it directly.

---

## ⚙️ Usage

### Command Line Mode
Performs forward engineering (from Java code to UML diagram) and generates a diagram.puml file.

```bash
java -jar aMULeto.jar <source_directory> [output_directory]
```

- `<source_directory>`: Path to the Java project.
- `[output_directory]` *(optional)*: Path to save `diagramma.puml`.


### GUI Mode
Launch the graphical interface for editing and generating UML diagrams, including the ability to generate and view the SVG diagram with zoom and pan controls.
```bash
java -jar aMULeto.jar gui
```

In the GUI File menu, there is a View SVG Diagram option that:

- Asks to select the Java project directory.
- Generates the diagram in SVG format.
- Saves the SVG file in the aMULeto folder on the user's desktop.
- Opens a window to view the diagram, with a zoom slider and pan support.

---


## 📜 License

Distributed under the **MIT with Commons Clause** license. See [`LICENSE`](LICENSE) for details.

---

## 👤 Author

Developed by [Fabio Bassini](https://github.com/fabiobassini).

---

## 🙌 Acknowledgements

Thanks to:
- [JavaParser](https://javaparser.org/)
- [PlantUML](http://plantuml.com/)

---

## 🤝 Contributing

Contributions are welcome! Please submit a pull request with a detailed description.

---

## 📩 Support

For issues, open a GitHub [issue](https://github.com/fabiobassini/aMULeto/issues).




