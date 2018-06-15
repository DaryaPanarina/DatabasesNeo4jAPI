package database.api_neo4j_cluster;

import CRUD.ConnectStudentClub;
import CRUD.ConnectStudentGroup;
import CRUD.ConnectStudentScholarship;
import CRUD.StudentNode;
import CRUD.GroupNode;
import POJO.StudentClub;
import POJO.StudentScholarship;
import java.util.ArrayList;
import java.util.List;


public class Student_crud {
    private final StudentNode studentNode = new StudentNode();
    private final GroupNode groupNode = new GroupNode();
    private final ConnectStudentGroup relStGr = new ConnectStudentGroup();
    private final ConnectStudentClub relStClub = new ConnectStudentClub();
    private final ConnectStudentScholarship relStSch = new ConnectStudentScholarship();
    
    public int createNode(int id, String surname, String name, String patronymic, String group) {
        int code;
        if (id < 0 || surname.isEmpty() || surname.equals(" ") || name.isEmpty() || name.equals(" "))
            return 3;
        if (groupNode.findByName(group) != 0)
            return 3;
        code = studentNode.createNode(id, surname, name, patronymic);
        if (code != 0)
            return code;
        code = relStGr.createRelationship(id, group);
        if (code != 0)
            studentNode.deleteNodeById(id);
        return code;
    }
    
    public String findById(int id) {
        if (id < 0)
            return "";
        String fullName = studentNode.findById(id);
        String group = relStGr.findStudentGroup(id);
        if (fullName.isEmpty() || group.isEmpty())
            return "";
        return fullName + ", группа " + group;
    }
    
    public int getStudentsNumber() {
        return studentNode.getStudentsNumber();
    }
    
    public int updateFullNameById(int id, String fullName, String groupName) {
        String prevGroup;
        int code;
        if (id < 0 || fullName.isEmpty() || fullName.equals(" "))
            return 3;
        if (!groupName.equals("-")) {
            prevGroup = relStGr.findStudentGroup(id);
            if (prevGroup.isEmpty())
                return 3;
            if (!prevGroup.equals(groupName)) {
                if (groupNode.findByName(groupName) == -1)
                    return 3;
                code = relStGr.deleteRelationship(id, prevGroup);
                if (code != 0)
                    return code;
                code = relStGr.createRelationship(id, groupName);
                if (code != 0)
                    return code;
            }
        }
        if (fullName.equals("-"))
            return 0;
        else
            return studentNode.updateFullNameById(id, fullName);
    }
    
    public int deleteNodeById(int id) {
        if (id < 0)
            return 3;
        return studentNode.deleteNodeById(id);
    }
    
    public List<StudentScholarship> getStudentScholarship(int id) {
        if (id < 0)
            return new ArrayList();
        return relStSch.getStudentScholarship(id);
    }
    
    public List<StudentClub> getStudentClubs(int id) {
        if (id < 0)
            return new ArrayList();
        return relStClub.getStudentClubs(id);
    }
}
