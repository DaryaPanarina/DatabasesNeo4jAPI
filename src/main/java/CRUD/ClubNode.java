package CRUD;

import POJO.Club;
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

public class ClubNode {
    private final List<URI> routingUris = new ArrayList();
    private final Config config = Config.build().toConfig();
    private final String login = "neo4j";
    private final String password = "neo";
    
    public ClubNode() {
        try {
            routingUris.add(new URI("bolt+routing://localhost:7687"));
            routingUris.add(new URI("bolt+routing://localhost:7688"));
            routingUris.add(new URI("bolt+routing://localhost:7689"));
        } catch (URISyntaxException ex) { }
    }
    
    public int createNode(int id, String name, String address) {
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "CREATE (c:Club{id: $id, name: $name, address: $address})";
            return session.writeTransaction((Transaction tx) -> {
                tx.run(query, parameters("id", id, "name", name, "address", address));
                return 0;
            });
        }
        catch (Exception ex) {
            return 1;
        }
    }
    
    public Club findByIdOrName(int id, String name) {
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query;
            StatementResult result;
            if (id < 0) {
                query = "MATCH (c:Club {name: $name}) RETURN c.id, c.name, c.address";
                result = session.beginTransaction().run(query, parameters("name", name));
            } else {
                query = "MATCH (c:Club {id: $id}) RETURN c.id, c.name, c.address";
                result = session.beginTransaction().run(query, parameters("id", id));
            }
            Record rec = result.single();
            return new Club(rec.get("c.id").asInt(), rec.get("c.name").asString(), rec.get("c.address").asString());
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public int getClubsNumber() {
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (c:Club) RETURN count(c) AS number";
            StatementResult result = session.beginTransaction().run(query);
            return result.next().get("number").asInt();
        }
        catch (Exception ex) {
            return -1;
        }
    }
    
    public List<Club> getAllClubs() {
        List<Club> result = new ArrayList();
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (c:Club) RETURN c.id, c.name, c.address";
            StatementResult resultSt = session.beginTransaction().run(query);
            while (resultSt.hasNext()) {
                Record rec = resultSt.next();
                result.add(new Club(rec.get("c.id").asInt(), rec.get("c.name").asString(), rec.get("c.address").asString()));
            }
            result.add(new Club(-1, "last", ""));
            return result;
        }
        catch (Exception ex) {
            return result;
        }
    }
    
    public int updateNameOrAddressById(int id, String name, String address) {
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query;
            if (name.isEmpty() || name.equals(" ")) {
                query = "MATCH (c:Club {id: $id}) SET c.address = $address RETURN c.name";
                return session.writeTransaction((Transaction tx) -> {
                    StatementResult resultSt = tx.run(query, parameters("id", id, "address", address));
                    resultSt.next().get("c.name").asString();
                    return 0;
                });
            } else {
                if (address.isEmpty() || address.equals(" ")) {
                    query = "MATCH (c:Club {id: $id}) SET c.name = $name RETURN c.name";
                    return session.writeTransaction((Transaction tx) -> {
                        StatementResult resultSt = tx.run(query, parameters("id", id, "name", name));
                        resultSt.next().get("c.name").asString();
                        return 0;
                    });
                } else {
                    query = "MATCH (c:Club {id: $id}) SET c.name = $name, c.address = $address RETURN c.name";
                    return session.writeTransaction((Transaction tx) -> {
                        StatementResult resultSt = tx.run(query, parameters("id", id, "name", name, "address", address));
                        resultSt.next().get("c.name").asString();
                        return 0;
                    });
                }
            }
        }
        catch (Exception ex) {
            return 1;
        }
    }
    
    public int deleteNodeById(int id) {
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (c:Club {id: $id}) DETACH DELETE c";
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
