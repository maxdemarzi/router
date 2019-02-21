package com.maxdemarzi;

import com.maxdemarzi.schema.RelationshipTypes;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.traversal.BranchState;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;

public class NetworkExpander implements PathExpander<String> {
    private String ip;
    private InetAddress address;

    public NetworkExpander(String ip) {
        this.ip = ip;
        try {
            this.address = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Iterable<Relationship> expand(Path path, BranchState branchState) {
        Node last = path.endNode();
        Relationship lastRel = path.lastRelationship();

        HashSet<Relationship> relationships = new HashSet<>();
        // Layer 3
        for(Relationship routes_to : last.getRelationships(RelationshipTypes.ROUTES_TO, Direction.OUTGOING)) {
            String[] routes = (String[])routes_to.getProperty("routes", new String[]{});
            for(String route : routes) {
                try {
                    Subnet subnet = Subnet.createInstance(route);
                    if(subnet.isInNet(address)) {
                        relationships.add(routes_to);
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }

        // Layer 2
        for(Relationship translates_to : last.getRelationships(RelationshipTypes.TRANSLATES_TO, Direction.OUTGOING)) {
            String ip = (String)translates_to.getProperty("ip");
            if (ip.equals(this.ip)) {
                relationships.add(translates_to);
            }
        }
        String mac = "";
        if(lastRel != null) {
            mac = (String)lastRel.getProperty("mac", "");
        }

        for(Relationship connects_to : last.getRelationships(RelationshipTypes.CONNECTS_TO, Direction.OUTGOING)) {
            String mac_too = (String)connects_to.getProperty("mac", "");
            if(mac.equals(mac_too)){
                relationships.add(connects_to);
            }
        }

        return relationships;
    }

    @Override
    public PathExpander reverse() {
        return null;
    }
}
