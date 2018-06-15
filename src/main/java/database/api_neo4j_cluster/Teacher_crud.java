package database.api_neo4j_cluster;

import CRUD.ConnectTeacherGroup;
import CRUD.GroupNode;
import CRUD.TeacherNode;
import POJO.TeacherGroup;
import java.util.ArrayList;
import java.util.List;

public class Teacher_crud {
    private final TeacherNode teacherNode = new TeacherNode();
    private final GroupNode groupNode = new GroupNode();
    private final ConnectTeacherGroup relStGr = new ConnectTeacherGroup();
    
    public int createNode(int id, String surname, String name, String patronymic) {
        if (id < 0 || surname.isEmpty() || surname.equals(" ") || name.isEmpty() || name.equals(" "))
            return 3;        
        return teacherNode.createNode(id, surname, name, patronymic);
    }
    
    public String findById(int id) {
        if (id < 0)
            return "";
        return teacherNode.findById(id);
    }
    
    public int getTeachersNumber() {
        return teacherNode.getTeachersNumber();
    }
    
    public int updateFullNameById(int id, String fullName) {
        if (id < 0 || fullName.isEmpty() || fullName.equals(" ") || fullName.equals("-"))
            return 3;
        return teacherNode.updateFullNameById(id, fullName);
    }
    
    public int deleteNodeById(int id) {
        if (id < 0)
            return 3;
        return teacherNode.deleteNodeById(id);
    }
    
    public int createRelationship(int id, String groupName, String subject) {
        return relStGr.createRelationship(id, groupName, subject);
    }
    
    public int deleteRelationship(int id, String groupName, String subject) {
        if (id < 0)
            return 3;
        return relStGr.deleteRelationship(id, groupName, subject);
    }
    
    public List<TeacherGroup> getTeacherGroups(int id) {
        if (id < 0)
            return new ArrayList();
        return relStGr.getTeacherGroups(id);
    }
}