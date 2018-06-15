package CRUD;

import POJO.Scholarship;
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

public class ScholarshipNode {
    private final List<URI> routingUris = new ArrayList();
    private final Config config = Config.build().toConfig();
    private final String login = "neo4j";
    private final String password = "neo";
    
    public ScholarshipNode() {
        try {
            routingUris.add(new URI("bolt+routing://localhost:7687"));
            routingUris.add(new URI("bolt+routing://localhost:7688"));
            routingUris.add(new URI("bolt+routing://localhost:7689"));
        } catch (URISyntaxException ex) { }
    }
    
    public int createNode(int id, String type, int amount) {
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "CREATE (s:Scholarship{id: $id, type: $type, amount: $amount})";            
            return session.writeTransaction((Transaction tx) -> {
                tx.run(query, parameters("id", id, "type", type, "amount", amount));
                return 0;
            });
        }
        catch (Exception ex) {
            return 1;
        }
    }
    
    public Scholarship findByIdOrType(int id, String type) {
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query;
            StatementResult result;
            if (id < 0) {
                query = "MATCH (s:Scholarship {type: $type}) RETURN s.id, s.type, s.amount";
                result = session.beginTransaction().run(query, parameters("type", type));
            } else {
                query = "MATCH (s:Scholarship {id: $id}) RETURN s.id, s.type, s.amount";
                result = session.beginTransaction().run(query, parameters("id", id));
            }
            Record rec = result.single();
            return new Scholarship(rec.get("s.id").asInt(), rec.get("s.type").asString(), rec.get("s.amount").asInt());
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public int getScholarshipsNumber() {
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (s:Scholarship) RETURN count(s) AS number";
            StatementResult result = session.beginTransaction().run(query);
            return result.next().get("number").asInt();
        }
        catch (Exception ex) {
            return -1;
        }
    }
    
    public List<Scholarship> getAllScholarships() {
        List<Scholarship> result = new ArrayList();
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query = "MATCH (s:Scholarship) RETURN s.id, s.type, s.amount";
            StatementResult resultSt = session.beginTransaction().run(query);
            while (resultSt.hasNext()) {
                Record rec = resultSt.next();
                result.add(new Scholarship(rec.get("s.id").asInt(), rec.get("s.type").asString(), rec.get("s.amount").asInt()));
            }
            result.add(new Scholarship(-1, "last", -1));
            return result;
        }
        catch (Exception ex) {
            return result;
        }
    }
    
    public int updateTypeOrAmountById(int id, String type, int amount) {
        try (Session session = GraphDatabase.routingDriver(routingUris, AuthTokens.basic(login, password), config).session()) {
            String query;
            if (type.isEmpty() || type.equals(" ")) {
                query = "MATCH (s:Scholarship {id: $id}) SET s.amount = $amount RETURN s.type";
                return session.writeTransaction((Transaction tx) -> {
                    StatementResult resultSt = tx.run(query, parameters("id", id, "amount", amount));
                    resultSt.next().get("s.type").asString();
                    return 0;
                });
            } else {
                if (amount < 0) {
                    query = "MATCH (s:Scholarship {id: $id}) SET s.type = $type RETURN s.type";
                    return session.writeTransaction((Transaction tx) -> {
                        StatementResult resultSt = tx.run(query, parameters("id", id, "type", type));
                        resultSt.next().get("s.type").asString();
                        return 0;
                    });
                } else {
                    query = "MATCH (s:Scholarship {id: $id}) SET s.type = $type, s.amount = $amount RETURN s.type";
                    return session.writeTransaction((Transaction tx) -> {
                        StatementResult resultSt = tx.run(query, parameters("id", id, "type", type, "amount", amount));
                        resultSt.next().get("s.type").asString();
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
            String query = "MATCH (s:Scholarship {id: $id}) DETACH DELETE s";
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
