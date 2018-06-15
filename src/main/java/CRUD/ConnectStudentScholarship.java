package CRUD;

import POJO.ScholarshipStudent;
import POJO.StudentScholarship;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import static org.neo4j.driver.v1.Values.parameters;

public class ConnectStudentScholarship {
    private final List<URI> routingUris = new ArrayList();
    private final Config config = Config.build().toConfig();
    private final String login = "neo4j";
    private final String password = "neo";
    
    public ConnectStudentScholarship() {
        try {
            routingUris.add(new URI("bolt+routing://localhost:7687"));
            routingUris.add(new URI("bolt+routing://localhost:7688"));
            routingUris.add(new URI("bolt+routing://localhost:7689"));
        } catch (URISyntaxException ex) { }
    }
    
    public int createRelationship(int id, String type, String since, String until) {
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (a:Student {id: $id}), (b:Scholarship {type: $type})"
                    + "MERGE (a)-[r:RECEIVES {since: $since, until: $until}]->(b) RETURN a.id";
            return session.writeTransaction((Transaction tx) -> {
                StatementResult resultSt = tx.run(query, parameters("id", id, "type", type, "since", since, "until", until));
                resultSt.next().get("a.id").asInt();
                return 0;
            });
        }
        catch (Exception ex) {
            return 1;
        }
    }
    
    public List<StudentScholarship> getStudentScholarship(int id) {
        List<StudentScholarship> result = new ArrayList();
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (a:Student {id: $id})-[r:RECEIVES]->(b:Scholarship) RETURN b.type, b.amount, r.since, r.until";
            StatementResult resultSt = session.beginTransaction().run(query, parameters("id", id));
            while (resultSt.hasNext()) {
                Record rec = resultSt.next();
                result.add(new StudentScholarship(rec.get("b.amount").asInt(), rec.get("b.type").asString(), 
                        rec.get("r.since").asString(), rec.get("r.until").asString()));
            }
            result.add(new StudentScholarship(-1, "last", "", ""));
            return result;
        }
        catch (Exception ex) {
            return result;
        }
    }
    
    public List<ScholarshipStudent> getScholarshipStudents(String type) {
        List<ScholarshipStudent> result = new ArrayList();
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (b:Scholarship{type:$type})<-[r:RECEIVES {until:\"-\"}]-(a:Student)-[p:IS_IN]->(g:Group)"
                    + "RETURN a.id, a.fullName, g.name";
            StatementResult resultSt = session.beginTransaction().run(query, parameters("type", type));
            while (resultSt.hasNext()) {
                Record rec = resultSt.next();
                result.add(new ScholarshipStudent(rec.get("a.id").asInt(), rec.get("a.fullName").asString(), rec.get("g.name").asString()));
            }
            result.add(new ScholarshipStudent(-1, "last", ""));
            return result;
        }
        catch (Exception ex) {
            return result;
        }
    }
    
    public int updateRelationship(int id, String type, String oldSinceDate, String since, String until) {
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query;
            if (since.isEmpty() || since.equals(" ") || since.equals("-1")) {
                query = "MATCH (a:Student {id: $id})-[r:RECEIVES {since: $oldSince}]->(b:Scholarship {type: $type}) "
                        + "SET r.until = $until RETURN r.since";
                return session.writeTransaction((Transaction tx) -> {
                    StatementResult resultSt = tx.run(query, parameters("id", id, "oldSince", oldSinceDate, "type", type, "until", until));
                    resultSt.next().get("r.since").asString();
                    return 0;
                });
            } else {
                if (until.isEmpty() || until.equals(" ") || until.equals("-1")) {
                    query = "MATCH (a:Student {id: $id})-[r:RECEIVES {since: $oldSince}]->(b:Scholarship {type: $type}) "
                            + "SET r.since = $since RETURN r.since";
                    return session.writeTransaction((Transaction tx) -> {
                        StatementResult resultSt = tx.run(query, parameters("id", id, "oldSince", oldSinceDate, "type", type, "since", since));
                        resultSt.next().get("r.since").asString();
                        return 0;
                    });
                } else {
                    query = "MATCH (a:Student {id: $id})-[r:RECEIVES {since: $oldSince}]->(b:Scholarship {type: $type})"
                            + "SET r.since = $since, r.until = $until RETURN r.since";
                    return session.writeTransaction((Transaction tx) -> {
                        StatementResult resultSt = tx.run(query, parameters("id", id, "oldSince", oldSinceDate, 
                                "type", type, "since", since, "until", until));
                        resultSt.next().get("r.since").asString();
                        return 0;
                    });
                }
            }
        }
        catch (Exception ex) {
            return 1;
        }
    }
}
