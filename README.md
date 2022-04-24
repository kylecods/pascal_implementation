## Pascal Java

This is an implementation of the Pascal language(possibly others) in Java. The reference book used is **Writing Compilers and Interpreters A Software Engineering Approach by Ronald Mak**.
This is an effort to further increase knowledge in programming language writing and design. The frontend and backend parts are decoupled making it quite possible to implement other languages easily.

#### Building
Currently, one can use the [Maven](https://maven.apache.org) build tool to compile the project. You can have the option to set up and use any build tool of your choice.

#### Running the program
Navigate to the compiled java file location and run the following to compile the pascal source:
```bash
java pascal.Pascal compile <PATH TO PASCAL FILE>
```
or to interpret the Pascal source:

```bash
java pascal.Pascal execute <PATH TO PASCAL FILE>
```
For the Pascal source, you can also test using the source files in the `examples` folder