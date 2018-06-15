package CRUD;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import static org.neo4j.driver.v1.Values.parameters;

public class StudentNode {
    private final List<URI> routingUris = new ArrayList();
    private final Config config = Config.build().toConfig();
    private final String login = "neo4j";
    private final String password = "neo";
    
    public StudentNode() {
        try {
            routingUris.add(new URI("bolt+routing://localhost:7687"));
            routingUris.add(new URI("bolt+routing://localhost:7688"));
            routingUris.add(new URI("bolt+routing://localhost:7689"));
        } catch (URISyntaxException ex) { }
    }
    
    public int createNode(int id, String surname, String name, String patronymic) {
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "CREATE (s:Student {id: $id, fullName: $fullName})";
            String fullName;
            if (patronymic == null)
                fullName = surname + " " + name;
            else
                fullName = surname + " " + name + " " + patronymic;
            return session.writeTransaction((Transaction tx) -> {
                tx.run(query, parameters("id", id, "fullName", fullName));
                return 0;
            });
        }
        catch (Exception ex) {
            return 1;
        }
    }
    
    public String findById(int id) {
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (s:Student {id: $id}) RETURN s.fullName";
            StatementResult result = session.beginTransaction().run(query, parameters("id", id));
            return result.next().get("s.fullName").asString();
        }
        catch (Exception ex) {
            return "";
        }
    }
      
    public int getStudentsNumber() {
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (s:Student) RETURN count(s) AS number";
            StatementResult result = session.beginTransaction().run(query);
            return result.next().get("number").asInt();
        }
        catch (Exception ex) {
            return -1;
        }
    }
    
    public int updateFullNameById(int id, String fullName) {
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (s:Student {id: $id}) SET s.fullName = $fullName RETURN s.fullName";
            return session.writeTransaction((Transaction tx) -> {
                StatementResult resultSt = tx.run(query, parameters("id", id, "fullName", fullName));
                resultSt.next().get("s.fullName").asString();
                return 0;
            });
        }
        catch (Exception ex) {
            return 1;
        }
    }
    
    public int deleteNodeById(int id) {
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (s:Student {id: $id}) DETACH DELETE s";
            return session.writeTransaction((Transaction tx) -> {
                tx.run(query, parameters("id", id));
                return 0;
            });
        }
        catch (Exception ex) {
            return 1;
        }
    }
}