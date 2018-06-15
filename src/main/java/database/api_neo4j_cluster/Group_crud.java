package database.api_neo4j_cluster;

import CRUD.ConnectStudentGroup;
import CRUD.ConnectTeacherGroup;
import CRUD.GroupNode;
import POJO.Group;
import POJO.Student;
import POJO.TeacherGroup;
import java.util.ArrayList;
import java.util.List;

public class Group_crud {
    private final GroupNode groupNode = new GroupNode();
    private final ConnectStudentGroup relStGr = new ConnectStudentGroup();
    private final ConnectTeacherGroup relTeachGr = new ConnectTeacherGroup();
    
    public int createNode(int id, String name) {
        if (id < 0 || name.isEmpty() || name.equals(" "))
            return 3;
        return groupNode.createNode(id, name);
    }
    
    public String findById(int id) {
        if (id < 0)
            return "";
        return groupNode.findById(id);
    }
    
    public int findByName(String name) {
        return groupNode.findByName(name);
    }
    
    public int getGroupsNumber() {
        return groupNode.getGroupsNumber();
    }
    
    public List<Group> getAllGroups() {
        return groupNode.getAllGroups();
    }
    
    public int updateNameById(int id, String name) {
        if (id < 0 || name.isEmpty() || name.equals(" "))
            return 2;
        return groupNode.updateNameById(id, name);
    }
    
    public int deleteNodeById(int id) {
        if (id < 0)
            return 3;
        return groupNode.deleteNodeById(id);
    }
    
    public List<Student> getGroupStudents(String name) {
        if (groupNode.findByName(name) == -1)
            return new ArrayList();
        return relStGr.getGroupStudents(name);
    }
    
    public List<TeacherGroup> getGroupTeachers(String name) {
        if (groupNode.findByName(name) == -1)
            return new ArrayList();
        return relTeachGr.getGroupTeachers(name);
    }
}
