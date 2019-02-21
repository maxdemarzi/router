package com.maxdemarzi.schema;

import org.neo4j.graphdb.RelationshipType;

public enum RelationshipTypes implements RelationshipType {
    CONNECTS_TO,
    TRANSLATES_TO,
    ROUTES_TO,
}
