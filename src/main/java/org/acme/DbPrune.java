package org.acme;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

public class DbPrune {
    public static void main(String[] args) {
        try (Driver driver = GraphDatabase.driver(System.getenv("NEO4J_ADDRESS"), AuthTokens.none())) {
//          This is the biggest node and relations that are not useful in our case, similar queries can be added below
//          to remove more things from the database if the schema still cannot fit in the context.
            driver.executableQuery("MATCH(N:ByteCode) DETACH DELETE N");

        }
    }
}
