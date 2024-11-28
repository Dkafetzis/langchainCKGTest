# Code Knowledge Graph test

# Prerequisites
- A neo4j database with the code knowledge graph (see instructions below on how to generate)
- Java 21
- Maven 3.9 and up (wrapper is also provided)
- An openai api key

# Running the application
1. Startup the neo4j database
2. Set NEO4J_ADDRESS and OPENAI_API_KEY environment variables with the neo4j database address and openai api key respectively
3. Run `./mvnw exec:java`

# Setting up neo4j
Go to the codebase that you want to generate a code knowledge graph for and add the JQAssistant plugin
```xml
<plugin>
    <groupId>com.buschmais.jqassistant</groupId>
    <artifactId>jqassistant-maven-plugin</artifactId>
    <version>${version.jqassistant}</version>
    <executions>
        <execution>
            <goals>
                <goal>scan</goal>
                <goal>analyze</goal>
            </goals>
            <configuration>
                <warnOnSeverity>MINOR</warnOnSeverity>
                <failOnSeverity>MAJOR</failOnSeverity>
            </configuration>
        </execution>
    </executions>
</plugin>
```

Add `.jqassistant.yml` to the root director of the project with contents
```yaml
jqassistant:
  store:
    embedded:
      neo4j-plugins:
        - group-id: org.neo4j.procedure
          artifact-id: apoc-core
          classifier: core
          version: 5.24.0

```
Run `./mvnw jqassistant:scan` and `./mvnw jqassistant:analyze`

Finally run `./mvnw jqassistant:server` to serve the neo4j server locally

It might be necessary to prune the generated graph a bit to fit the shcema of the database
to the context window of the model. A pruner class is added for this purpose but depending
on the context size of the model it might or might not be needed to prune it more.