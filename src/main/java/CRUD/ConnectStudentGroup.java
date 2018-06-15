package CRUD;

import POJO.Student;
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

public class ConnectStudentGroup {
    private final List<URI> routingUris = new ArrayList();
    private final Config config = Config.build().toConfig();
    private final String login = "neo4j";
    private final String password = "neo";
    
    public ConnectStudentGroup() {
        try {
            routingUris.add(new URI("bolt+routing://localhost:7687"));
            routingUris.add(new URI("bolt+routing://localhost:7688"));
            routingUris.add(new URI("bolt+routing://localhost:7689"));
        } catch (URISyntaxException ex) { }
    }
    
    public int createRelationship(int id, String groupName) {
        if (!findStudentGroup(id).isEmpty())
            return 1;
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (a:Student {id: $id}), (b:Group {name: $name}) MERGE (a)-[r:IS_IN]->(b) RETURN a.id";
            return session.writeTransaction((Transaction tx) -> {
                StatementResult resultSt = tx.run(query, parameters("id", id, "name", groupName));
                resultSt.next().get("a.id").asInt();
                return 0;
            });
        }
        catch (Exception ex) {
            return 1;
        }
    }
    
    public String findStudentGroup(int id) {
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (a:Student {id: $id})-[r:IS_IN]->(b:Group) RETURN b.name";
            StatementResult result = session.beginTransaction().run(query, parameters("id", id));
            return result.next().get("b.name").asString();
        }
        catch (Exception ex) {
            return "";
        }
    }
    
    public List<Student> getGroupStudents(String name) {
        List<Student> result = new ArrayList();
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (a:Student)-[r:IS_IN]->(b:Group {name: $name}) RETURN a.id, a.fullName";
            StatementResult resultSt = session.beginTransaction().run(query, parameters("name", name));
            while (resultSt.hasNext()) {
                Record rec = resultSt.next();
                result.add(new Student(rec.get("a.id").asInt(), rec.get("a.fullName").asString()));
            }
            result.add(new Student(-1, "last"));
            return result;
        }
        catch (Exception ex) {
            return result;
        }
    }
    
    public int deleteRelationship(int id, String groupName) {
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (a:Student {id: $id})-[r:IS_IN]->(b:Group {name: $name}) DELETE r RETURN a.id";
            return session.writeTransaction((Transaction tx) -> {
                StatementResult resultSt = tx.run(query, parameters("id", id, "name", groupName));
                resultSt.next().get("a.id").asInt();
                return 0;
            });
        }
        catch (Exception ex) {
            return 1;
        }
    }
}