package org.acme;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.neo4j.Neo4jContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.store.graph.neo4j.Neo4jGraph;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

import java.util.List;

public class Main {
    public static  void main(String[] args) {
        try (Driver driver = GraphDatabase.driver(System.getenv("NEO4J_ADDRESS"), AuthTokens.none())) {
            try (Neo4jGraph graph = Neo4jGraph.builder().driver(driver).build()) {
                graph.refreshSchema();
                OpenAiChatModel chatModel = OpenAiChatModel
                        .builder()
                        .apiKey(System.getenv("OPENAI_API_KEY"))
                        .modelName(OpenAiChatModelName.GPT_4_O)
                        .build();
                Neo4jContentRetriever retriever = Neo4jContentRetriever.builder()
                        .graph(graph)
                        .chatLanguageModel(chatModel)
                        .build();

                //TODO move this to command line argument maybe
                Query query = new Query("Find me artifacts that look like jipijapa and describe them");

                List<Content> contents = retriever.retrieve(query);

                System.out.println(contents);

                //TODO maybe do some more prompt engineering here but works for now as a POC
                System.out.print(chatModel.generate("Based on the context here:\n"+contents+"\n answer the following question:\n" + query));
            }
        }
    }
}