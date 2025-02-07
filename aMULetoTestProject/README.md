# AMULETO Test Project

This test project was created to demonstrate the use of custom annotations provided by **AMULETO** for the automatic generation of UML diagrams. The annotations allow defining relationships and properties directly in the source code, making it possible to create accurate UML diagrams that reflect the relationships between classes and their characteristics.

## Custom Annotations

AMULETO uses the following annotations to indicate various types of relationships and properties:

- **@Aggregation**  
  Indicates an aggregation relationship between classes.  
  *Example:* In the `Department` class, the list of `Course` is annotated with `@Aggregation`, suggesting that the department "owns" the courses, but these can exist independently of the department.

- **@Composition**  
  Denotes a composition relationship, a strong form of aggregation where the parts are an integral part of the whole.  
  *Example:* In the `Course` class, both the `Program` field and the list of `Lesson` are annotated with `@Composition`, indicating that the program and lessons are an essential part of the course and their lifecycle is tied to that of the course.

- **@Readonly**  
  Specifies that a field is read-only and should not be modified after initialization.  
  *Example:* In `Lesson`, the `lessonDate` field is annotated with `@Readonly`, as well as the `firstName` and `lastName` fields in the abstract `Person` class.

- **@Unique**  
  Indicates that a field's value must be unique within the class.  
  *Example:* In `Enrollment`, the `enrollmentDate` field is annotated with `@Unique`, ensuring that each enrollment has a unique registration date.

- **@Ordered**  
  Used on collection-type fields to indicate that the order of elements is relevant.  
  *Example:* In the `Program` class, the list of `topics` is annotated with `@Ordered`, emphasizing that the sequence of topics is important.

- **@Navigable**  
  Signals that the relationship is navigable from a given class.  
  *Example:* In `Student`, the list of `Enrollment` is annotated with `@Navigable`, indicating that one can navigate from a student to their enrollments.

- **@NonNavigable**  
  (Not used in this example)  
  Can be employed to indicate that a relationship is not navigable.

- **@AssociationClass**  
  Indicates that a class serves as an association class between other classes and can contain additional attributes related to the relationship.  
  *Example:* The `Enrollment` class is annotated with `@AssociationClass`, as it represents the link between `Student` and `Course` and includes, for instance, the `grade` field.

## Project Structure

The project contains the following classes and interfaces:

- **Course**  
  Represents a course, with a name, a `Program` (composition), a list of enrollments, and a list of lessons (both in composition).

- **Department**  
  Represents a department, containing a list of courses annotated with `@Aggregation`.

- **Example**  
  Main class demonstrating the use of annotations by creating instances of `Course`, `Department`, `Professor`, and `Student`.

- **Enrollment**  
  Association class (annotated with `@AssociationClass`) that links a student to a course. It also uses the `@Unique` annotation for the enrollment date.

- **Lesson**  
  Represents a lesson, with a date (annotated with `@Readonly`), a topic, and a classroom.

- **Person** (abstract class)  
  Defines the basic properties of a person (`firstName` and `lastName`), which are annotated as `@Readonly`.

- **Professor**  
  Extends `Person` and implements the `Evaluable` interface. Uses `@Aggregation` to represent the relationship with courses.

- **Program**  
  Represents a course program with a list of topics, ordered through the `@Ordered` annotation.

- **Student**  
  Extends `Person` and contains a list of enrollments, marked as navigable via `@Navigable`.

- **Evaluable**  
  Interface defining the `evaluate()` method.

## How to Use Annotations with AMULETO

1. **Annotate Your Code:**  
   Use AMULETO-provided annotations to specify the desired relationships and properties. For example:
   - Use `@Composition` to indicate that an object is an integral part of another.
   - Use `@Aggregation` for looser relationships, where the part object can exist independently of the whole.
   - Apply `@Readonly` to mark fields that should not be modified after initialization.
   - Set `@Unique` on fields that must have unique values.
   - Use `@Ordered` on collections to indicate that order is significant.
   - Define navigable relationships with `@Navigable` (and, if necessary, `@NonNavigable`).
   - Identify association classes with `@AssociationClass`.

2. **Generate UML Diagrams:**  
   Run AMULETO on your project. The tool will analyze the code, interpret the annotations, and generate UML diagrams. Relationships will be visually represented (e.g., composition with filled diamonds, aggregation with hollow diamonds, etc.), and special properties (readonly, unique, ordered) can be displayed as notes or stereotypes.

3. **Verify the Diagram:**  
   The generated diagrams should accurately reflect the relationships and properties defined in your code, facilitating the understanding of the system’s structure and constraints.

## Example of Annotated Code

Below is an example from the `Course` class:

```java
package com.fabio.org.test;

import java.util.ArrayList;
import java.util.List;

public class Corso {
    public String nomeCorso;
    @Composition
    private Programma programma;
    private List<Iscrizione> iscrizioni;
    @Composition
    private List<Lezione> lezioni;

    public Corso(String nomeCorso, Programma programma) {
        this.nomeCorso = nomeCorso;
        this.programma = programma;
        this.iscrizioni = new ArrayList<>();
        this.lezioni = new ArrayList<>();
    }

    // Methods for managing enrollments and lessons...
}
```

In this example:

- The ```program``` field and the ```lessons``` list are annotated with ```@Composition```, indicating that they are essential parts of the course.
- Other project classes use similar annotations to define their relationships and properties.