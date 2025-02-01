# UML Generator Application

![License](https://img.shields.io/badge/license-MIT%20with%20Commons%20Clause-blue.svg)
![Java](https://img.shields.io/badge/Java-8%2B-blue.svg)
![PlantUML](https://img.shields.io/badge/PlantUML-1.2021.14-blue.svg)
![JavaParser](https://img.shields.io/badge/JavaParser-3.25.4-blue.svg)

## 📑 Table of Contents

1. [Introduction](#introduction)
2. [Features](#features)
3. [Demo](#demo)
4. [Installation](#installation)
   - [Prerequisites](#prerequisites)
   - [Setup](#setup)
5. [Usage](#usage)
6. [License](#license)
7. [Author](#author)
8. [Acknowledgements](#acknowledgements)
9. [Contributing](#contributing)
10. [Support](#support)

---

![UML Generator Logo](https://github.com/fabiobassini/aMULeto/demo/amuleto.png)  
*Figure 1: aMULeto logo.*


## 📌 Introduction

Welcome to the **UML Generator Application**!  
This tool analyzes a multi-file Java project using [JavaParser](https://javaparser.org/) and automatically generates a UML class diagram in [PlantUML](http://plantuml.com/) format. It is designed to help developers visualize the architecture of their Java applications, displaying classes, interfaces, enumerations, and their relationships.

### 🔹 Key Enhancements:
- **Grouping by Package:** Organizes all types into `package` blocks.
- **Exclusion of Generator Classes:** Ensures the diagram only includes domain classes.
- **Association Classes:** Special treatment for `@AssociationClass` with `<<association>>` stereotype.

---

## 🚀 Features

✔ **Multi-File Support** – Recursively scans Java files in the source directory.  
✔ **Automatic Package Grouping** – Classes are grouped into their respective packages.  
✔ **Exclusion of Generator Code** – Filters out internal classes (`App`, `UMLGenerator`, `UMLUtils`).  
✔ **Comprehensive UML Relationships**:
  - **Associations:** Links between classes with multiplicities.
  - **Aggregations & Compositions:** Displayed as `o-->` (aggregation) and `*-->` (composition).
  - **Dependencies:** Visualized as `..>`, based on method parameters.
  - **Inheritance & Implementation:** `<|--` for inheritance, `..|>` for interfaces.
✔ **Detailed Class Representation**:
  - Attributes, methods, constructors, and visibility markers (`+`, `-`, `#`, `~`).
  - **Association Classes:** Connected with dashed lines.

---

## 🎥 Demo

![UML Generator Demo](https://github.com/fabiobassini/aMULeto/demo/demo.png)  
*Figure 1: aMULeto demo diagram.*

---

## 🛠️ Installation

### Prerequisites
- **Java 8+**: Download from [java.com](https://www.java.com/download/).
- **JavaParser (3.25.4 or later)**: Used for Java source file parsing.
- **PlantUML**: Required to visualize the UML output (`diagramma.puml`).

### Setup
You can either build the project from source or use the precompiled JAR file.

#### Option 1: Build from Source
1. **Clone the Repository:**
   ```bash
   git clone https://github.com/fabiobassini/uml-generator.git
   cd uml-generator
   ```
2. **Build the Project:**
   ```bash
   mvn clean install
   ```

#### Option 2: Use Precompiled JAR
Download the latest release from the [Releases](https://github.com/fabiobassini/uml-generator/releases) section and use it directly.

---

## ⚙️ Usage

Run the application from the command line:
```bash
java -jar aMULeto.jar <source_directory> [output_directory]
```

- `<source_directory>`: Path to the Java project.
- `[output_directory]` *(optional)*: Path to save `diagramma.puml`.

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

For issues, open a GitHub [issue](https://github.com/fabiobassini/uml-generator/issues).




