package CRUD;

import POJO.Group;
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

public class GroupNode {
    private final List<URI> routingUris = new ArrayList();
    private final Config config = Config.build().toConfig();
    private final String login = "neo4j";
    private final String password = "neo";

    public GroupNode() {
        try {
            routingUris.add(new URI("bolt+routing://localhost:7687"));
            routingUris.add(new URI("bolt+routing://localhost:7688"));
            routingUris.add(new URI("bolt+routing://localhost:7689"));
        } catch (URISyntaxException ex) { }
    }
    
    public int createNode(int id, String name) {
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "CREATE (g:Group{id: $id, name: $name})";
            return session.writeTransaction((Transaction tx) -> {
                tx.run(query, parameters("id", id, "name", name));
                return 0;
            });
        }
        catch (Exception ex) {
            return 1;
        }
    }
    
    public String findById(int id) {
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (g:Group {id: $id}) RETURN g.name";
            StatementResult result = session.beginTransaction().run(query, parameters("id", id));
            return result.next().get("g.name").asString();
        }
        catch (Exception ex) {
            return "";
        }
    }
    
    public int findByName(String name) {
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (g:Group {name: $name}) RETURN g.id";
            StatementResult result = session.beginTransaction().run(query, parameters("name", name));
            return result.next().get("g.id").asInt();
        }
        catch (Exception ex) {
            return -1;
        }
    }
    
    public int getGroupsNumber() {
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (g:Group) RETURN count(g) AS number";
            StatementResult result = session.beginTransaction().run(query);
            return result.next().get("number").asInt();
        }
        catch (Exception ex) {
            return -1;
        }
    }
    
    public List<Group> getAllGroups() {
        List<Group> result = new ArrayList();
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (g:Group) RETURN g.id, g.name";
            StatementResult resultSt = session.beginTransaction().run(query);
            while (resultSt.hasNext()) {
                Record rec = resultSt.next();
                result.add(new Group(rec.get("g.id").asInt(), rec.get("g.name").asString()));
            }
            result.add(new Group(-1, "last"));
            return result;
        }
        catch (Exception ex) {
            return result;
        }
    }
    
    public int updateNameById(int id, String name) {
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (g:Group {id: $id}) SET g.name = $name RETURN g.name";
            return session.writeTransaction((Transaction tx) -> {
                StatementResult resultSt = tx.run(query, parameters("id", id, "name", name));
                resultSt.next().get("g.name").asString();
                return 0;
            });
        }
        catch (Exception ex) {
            return 1;
        }
    }
    
    public int deleteNodeById(int id) {
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (g:Group {id: $id}) DETACH DELETE g";
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