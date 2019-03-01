# Router
POC Network Routing inside Neo4j

This project requires Neo4j 3.4.x or higher

Instructions
------------ 

This project uses maven, to build a jar-file with the procedure in this
project, simply package the project with maven:

    mvn clean package

This will produce a jar-file, `target/procedures-1.0-SNAPSHOT.jar`,
that can be copied to the `plugin` directory of your Neo4j instance.

    cp target/procedures-1.0-SNAPSHOT.jar neo4j-enterprise-3.4.9/plugins/.
    

Restart your Neo4j Server. A new Stored Procedure is available:


    CALL com.maxdemarzi.router(from, to)
    CALL com.maxdemarzi.router('10.175.64.2', '10.175.122.10')
    
Sample data:

    CREATE (r1:Router {id: 'l3-r1', ip:'10.175.64.2'})
    CREATE (r2:Router {id: 'l3-r2', ip:'10.175.66.6'})
    CREATE (r3:Router {id: 'l3-r3', ip:'10.182.32.3'})
    CREATE (r4:Router {id: 'l3-r4', ip:'10.175.32.1'})
    CREATE (i1:Interface {id: 'Vlan111'})
    CREATE (i2:Interface {id: 'port-channel1'})
    CREATE (i3:Interface {id: 'Vlan200'})
    CREATE (i4:Interface {id: 'Vlan205'})
    CREATE (s1:Switch {id: 'l2-s1'})
    CREATE (s2:Switch {id: 'l2-s2'})
    CREATE (s3:Switch {id: 'l2-s3'})
    CREATE (s4:Switch {id: 'l2-s4'})
    CREATE (s5:Switch {id: 'l2-s5'})
    CREATE (ser1:Server {ip: '10.175.122.10'})
    CREATE (ser2:Server {ip: '10.175.122.12'})
    CREATE (ser3:Server {ip: '10.175.122.13'})
    CREATE (s1)-[:TRANSLATES_TO]->(r1)
    CREATE (r1)-[:ROUTES_TO {routes:['10.175.112.0/20', '10.175.108.0/22', '10.182.14.64/26', '10.182.22.0/25']}]->(r2)
    CREATE (r1)-[:ROUTES_TO {routes:['10.182.32.0/29']}]->(r3)
    CREATE (r1)-[:ROUTES_TO {routes:['10.182.32.1/32']}]->(r4)
    CREATE (r2)-[:TRANSLATES_TO {ip:'10.175.66.9', mac:'0081.c4dc.6161' }]->(i1)
    CREATE (r2)-[:TRANSLATES_TO {ip:'10.175.66.5', mac:'e4c7.221c.87c1' }]->(i2)
    CREATE (r2)-[:TRANSLATES_TO {ip:'10.175.122.2', mac:'0081.c4dc.6161' }]->(i3)
    CREATE (r2)-[:TRANSLATES_TO {ip:'10.175.122.4', mac:'1402.ec86.c6f8' }]->(i3)
    CREATE (r2)-[:TRANSLATES_TO {ip:'10.175.122.5', mac:'1402.ec86.c664' }]->(i3)
    CREATE (r2)-[:TRANSLATES_TO {ip:'10.175.122.6', mac:'1402.ec86.c6f8' }]->(i3)
    CREATE (r2)-[:TRANSLATES_TO {ip:'10.175.122.7', mac:'1402.ec86.c664' }]->(i3)
    CREATE (r2)-[:TRANSLATES_TO {ip:'10.175.122.8', mac:'1402.ec86.c63c' }]->(i3)
    CREATE (r2)-[:TRANSLATES_TO {ip:'10.175.122.9', mac:'1402.ec86.c6f0' }]->(i3)
    CREATE (r2)-[:TRANSLATES_TO {ip:'10.175.122.10', mac:'1402.ec86.c63c' }]->(i3)
    CREATE (r2)-[:TRANSLATES_TO {ip:'10.175.122.11', mac:'1402.ec86.c6f0' }]->(i3)
    CREATE (r2)-[:TRANSLATES_TO {ip:'10.175.122.12', mac:'1402.ec86.c610' }]->(i3)
    CREATE (r2)-[:TRANSLATES_TO {ip:'10.175.122.13', mac:'1402.ec86.c634' }]->(i3)
    CREATE (r2)-[:TRANSLATES_TO {ip:'10.175.122.14', mac:'1402.ec86.c610' }]->(i3)
    CREATE (r2)-[:TRANSLATES_TO {ip:'10.175.122.47', mac:'1402.ec86.c674' }]->(i3)
    CREATE (r2)-[:TRANSLATES_TO {ip:'10.175.122.48', mac:'1402.ec86.c674' }]->(i3)
    CREATE (r2)-[:TRANSLATES_TO {ip:'10.175.122.65', mac:'0000.0c9f.f0cd' }]->(i4)
    CREATE (r2)-[:TRANSLATES_TO {ip:'10.175.122.66', mac:'0081.c4dc.6161' }]->(i4)
    CREATE (i3)-[:CONNECTS_TO { port:'Po111', mac:'0081.c4dc.6161'}]->(s4)
    CREATE (i3)-[:CONNECTS_TO { port:'Po2', mac:'1402.ec86.c610'}]->(s3)
    CREATE (i3)-[:CONNECTS_TO { port:'Po2', mac:'1402.ec86.c634'}]->(s3)
    CREATE (i3)-[:CONNECTS_TO { port:'Po3', mac:'1402.ec86.c63c'}]->(s5)
    CREATE (i3)-[:CONNECTS_TO { port:'Po111', mac:'1402.ec86.c640'}]->(s4)
    CREATE (s3)-[:CONNECTS_TO { port:'Eth1/9', mac:'1402.ec86.c610' }]->(ser2)
    CREATE (s3)-[:CONNECTS_TO { port:'Eth1/10', mac:'1402.ec86.c634' }]->(ser3)
    CREATE (s5)-[:CONNECTS_TO { port:'Eth1/8', mac:'1402.ec86.c63c' }]->(ser1)    
    
    