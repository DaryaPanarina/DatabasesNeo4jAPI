package database.api_neo4j_cluster;

import CRUD.ConnectStudentScholarship;
import CRUD.ScholarshipNode;
import POJO.Scholarship;
import POJO.ScholarshipStudent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Scholarship_crud {
    private final ScholarshipNode schNode = new ScholarshipNode();
    private final ConnectStudentScholarship relStSch = new ConnectStudentScholarship();
    
    public int createNode(int id, String type, int amount) {
        if (id < 0 || type.isEmpty() || type.equals(" ") || amount < 0)
            return 3;
        return schNode.createNode(id, type, amount);
    }
    
    public Scholarship findByIdOrType(int id, String type) {
        if ((id < 0 && type.isEmpty()) || (id > 0 && !type.isEmpty()))
            return null;
        return schNode.findByIdOrType(id, type);
    }
    
    public int getScholarshipsNumber() {
        return schNode.getScholarshipsNumber();
    }
    
    public List<Scholarship> getAllScholarships() {
        return schNode.getAllScholarships();
    }
    
    public int updateTypeOrAmountById(int id, String type, int amount) {
        if (id < 0 || ((type.isEmpty() || type.equals(" ")) && amount < 0))
            return 3;
        return schNode.updateTypeOrAmountById(id, type, amount);
    }
    
    public int deleteNodeById(int id) {
        if (id < 0)
            return 3;
        return schNode.deleteNodeById(id);
    }
    
    public int createRelationship(int id, String type, String since, String until) {
        try {
            DateFormat df = new SimpleDateFormat("dd.mm.yyyy");
            df.setLenient(false);
            df.parse(since);
            if (!until.equals("-"))
                df.parse(until);
            return relStSch.createRelationship(id, type, since, until);
        } catch (ParseException ex) {
            return 3;
        }
    }
    
    public int updateRelationship(int id, String type, String oldSinceDate, String since, String until) {
        if (id < 0 || type.isEmpty() || ((since.isEmpty() || since.equals("-")) && (until.isEmpty() || until.equals("-"))))
            return 3;
        try {
            DateFormat df = new SimpleDateFormat("dd.mm.yyyy");
            df.setLenient(false);
            df.parse(oldSinceDate);
            if ((since.isEmpty() || since.equals("-1")) && (until.isEmpty() || until.equals("-1")))
                return 3;
            if (!since.isEmpty() && !since.equals("-1"))
                df.parse(since);
            if (!until.isEmpty() && !until.equals("-1") && !until.equals("-"))
                df.parse(until);
            return relStSch.updateRelationship(id, type, oldSinceDate, since, until);
        } catch (ParseException ex) {
            return 3;
        }
    }
    
    public List<ScholarshipStudent> getScholarshipStudents(String type) {
        if (type.isEmpty())
            return new ArrayList();
        return relStSch.getScholarshipStudents(type);
    }
}
