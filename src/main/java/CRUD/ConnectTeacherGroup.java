package CRUD;

import POJO.TeacherGroup;
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

public class ConnectTeacherGroup {
    private final List<URI> routingUris = new ArrayList();
    private final Config config = Config.build().toConfig();
    private final String login = "neo4j";
    private final String password = "neo";
    
    public ConnectTeacherGroup() {
        try {
            routingUris.add(new URI("bolt+routing://localhost:7687"));
            routingUris.add(new URI("bolt+routing://localhost:7688"));
            routingUris.add(new URI("bolt+routing://localhost:7689"));
        } catch (URISyntaxException ex) { }
    }
    
    public int createRelationship(int id, String groupName, String subject) {
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (a:Teacher {id: $id}), (b:Group {name: $name}) MERGE (a)-[r:TEACHES {subject: $subject}]->(b)"
                    + "RETURN a.id";
            return session.writeTransaction((Transaction tx) -> {
                StatementResult resultSt = tx.run(query, parameters("id", id, "subject", subject, "name", groupName));
                resultSt.next().get("a.id").asInt();
                return 0;
            });
        }
        catch (Exception ex) {
            return 1;
        }
    }
    
    public List<TeacherGroup> getTeacherGroups(int id) {
        List<TeacherGroup> result = new ArrayList();       
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (a:Teacher {id: $id})-[r:TEACHES]->(b:Group) RETURN b.name, r.subject";
            StatementResult resultSt = session.beginTransaction().run(query, parameters("id", id));
            while (resultSt.hasNext()) {
                Record rec = resultSt.next();
                result.add(new TeacherGroup(rec.get("b.name").asString(), rec.get("r.subject").asString()));
            }
            result.add(new TeacherGroup("last", ""));
            return result;
        }
        catch (Exception ex) {
            return result;
        }
    }
    
    public List<TeacherGroup> getGroupTeachers(String groupName) {
        List<TeacherGroup> result = new ArrayList();       
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (a:Teacher)-[r:TEACHES]->(b:Group {name: $name}) RETURN a.fullName, r.subject";
            StatementResult resultSt = session.beginTransaction().run(query, parameters("name", groupName));
            while (resultSt.hasNext()) {
                Record rec = resultSt.next();
                result.add(new TeacherGroup(rec.get("a.fullName").asString(), rec.get("r.subject").asString()));
            }
            result.add(new TeacherGroup("last", ""));
            return result;
        }
        catch (Exception ex) {
            return result;
        }
    }
    
    public int deleteRelationship(int id, String groupName, String subject) {
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (a:Teacher {id: $id})-[r:TEACHES {subject: $subject}]->(b:Group {name: $name}) DELETE r RETURN a.id";
            return session.writeTransaction((Transaction tx) -> {
                StatementResult resultSt = tx.run(query, parameters("id", id, "subject", subject, "name", groupName));
                resultSt.next().get("a.id").asInt();
                return 0;
            });
        }
        catch (Exception ex) {
            return 1;
        }
    }
}