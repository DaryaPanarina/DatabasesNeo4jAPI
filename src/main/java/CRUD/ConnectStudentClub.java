package CRUD;

import POJO.StudentClub;
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

public class ConnectStudentClub {
    private final List<URI> routingUris = new ArrayList();
    private final Config config = Config.build().toConfig();
    private final String login = "neo4j";
    private final String password = "neo";
    
    public ConnectStudentClub() {
        try {
            routingUris.add(new URI("bolt+routing://localhost:7687"));
            routingUris.add(new URI("bolt+routing://localhost:7688"));
            routingUris.add(new URI("bolt+routing://localhost:7689"));
        } catch (URISyntaxException ex) { }
    }
    
    public int createRelationship(int id, String clubName, String position) {
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (a:Student {id: $id}), (b:Club {name: $name})"
                    + "MERGE (a)-[r:PARTICIPATES {position: $position}]->(b) RETURN a.id";
            return session.writeTransaction((Transaction tx) -> {
                StatementResult resultSt = tx.run(query, parameters("id", id, "name", clubName, "position", position.toUpperCase()));
                resultSt.next().get("a.id").asInt();
                return 0;
            });
        }
        catch (Exception ex) {
            return 1;
        }
    }
    
    public List<StudentClub> getStudentClubs(int id) {
        List<StudentClub> result = new ArrayList();
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (a:Student {id: $id})-[r:PARTICIPATES]->(b:Club) RETURN b.name, r.position";
            StatementResult resultSt = session.beginTransaction().run(query, parameters("id", id));
            while (resultSt.hasNext()) {
                Record rec = resultSt.next();
                result.add(new StudentClub(rec.get("b.name").asString(), "", rec.get("r.position").asString()));
            }
            result.add(new StudentClub("last", "", ""));
            return result;
        }
        catch (Exception ex) {
            return result;
        }
    }
    
    public List<StudentClub> getClubStudents(String clubName) {
        List<StudentClub> result = new ArrayList();
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (b: Club{name: $name})<-[r:PARTICIPATES]-(a:Student)-[p:IS_IN]->(g:Group)"
                    + "RETURN a.fullName, g.name, r.position";
            StatementResult resultSt = session.beginTransaction().run(query, parameters("name", clubName));
            while (resultSt.hasNext()) {
                Record rec = resultSt.next();
                result.add(new StudentClub(rec.get("a.fullName").asString(), rec.get("g.name").asString(), rec.get("r.position").asString()));
            }
            result.add(new StudentClub("last", "", ""));
            return result;
        }
        catch (Exception ex) {
            return result;
        }
    }
    
    public int updateRelationship(int id, String clubName, String position) {
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (a:Student {id: $id})-[r:PARTICIPATES]->(b:Club {name: $name})"
                    + "SET r.position = $position RETURN r.position";
            return session.writeTransaction((Transaction tx) -> {
                StatementResult resultSt = tx.run(query, parameters("id", id, "name", clubName, "position", position.toUpperCase()));
                resultSt.next().get("r.position").asString();
                return 0;
            });
        }
        catch (Exception ex) {
            return 1;
        }
    }
    
    public int deleteRelationship(int id, String clubName) {
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (a:Student {id: $id})-[r:PARTICIPATES]->(b:Club {name: $name}) DELETE r RETURN a.id";
            return session.writeTransaction((Transaction tx) -> {
                StatementResult resultSt = tx.run(query, parameters("id", id, "name", clubName));
                resultSt.next().get("a.id").asInt();
                return 0;
            });
        }
        catch (Exception ex) {
            return 1;
        }
    }
}
