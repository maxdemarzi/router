package com.maxdemarzi;

import com.maxdemarzi.schema.Labels;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.traversal.*;
import org.neo4j.logging.Log;
import org.neo4j.procedure.*;

import java.util.stream.Stream;

public class Procedures {

    // This field declares that we need a GraphDatabaseService
    // as context when any procedure in this class is invoked
    @Context
    public GraphDatabaseService db;

    // This gives us a log instance that outputs messages to the
    // standard log, normally found under `data/log/console.log`
    @Context
    public Log log;

    @Procedure(name = "com.maxdemarzi.router", mode = Mode.READ)
    @Description("CALL com.maxdemarzi.router(from, to) - traverse paths")
    public Stream<PathResult> router(@Name("from") String from, @Name("to") String to) {
        Node router = db.findNode(Labels.Router, "ip", from);
        Node server = db.findNode(Labels.Server, "ip", to);
        if (router != null && server != null) {
            TraversalDescription td = db.traversalDescription()
                    .depthFirst()
                    .expand(new NetworkExpander(to))
                    .evaluator(Evaluators.endNodeIs(Evaluation.INCLUDE_AND_PRUNE,
                            Evaluation.EXCLUDE_AND_CONTINUE, server));

            return td.traverse(router).iterator().stream().map(PathResult::new);
        }
        return Stream.of(new PathResult(null));
    }

}
