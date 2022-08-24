## Pascal Java

This is an implementation of the Pascal language(possibly others) in Java. The reference book used is **Writing Compilers and Interpreters A Software Engineering Approach by Ronald Mak**.
This is an effort to further increase knowledge in programming language writing and design. The frontend and backend parts are decoupled making it quite possible to implement other languages easily.

#### Building
- Ensure you have `JDK 14` and above

- Install [Gradle](https://gradle.org/).

- Compile the project together with the dependencies:
```bash
 gradle build
```

- Make a java executable:
```bash
 gradle shadowJar
```

#### Running the program
Navigate to the java executable under `app/build/libs` then run the following to compile:

```bash
java -jar pascal.jar compile <PATH TO PASCAL FILE>
```
or to interpret the Pascal source:

```bash
java -jar pascal.jar execute <PATH TO PASCAL FILE>
```

To print the AST run:

```bash
java -jar pascal.jar execute | compile -i <PATH TO PASCAL FILE>
```

You can find test files under `app/examples` folder