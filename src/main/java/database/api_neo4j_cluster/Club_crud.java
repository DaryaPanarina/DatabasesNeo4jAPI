package database.api_neo4j_cluster;

import CRUD.ClubNode;
import CRUD.ConnectStudentClub;
import POJO.Club;
import POJO.StudentClub;
import java.util.List;

public class Club_crud {
    private final ClubNode clubNode = new ClubNode();
    private final ConnectStudentClub relStClub = new ConnectStudentClub();
    
    public int createNode(int id, String name, String address) {
        if (id < 0 || name.isEmpty() || name.equals(" ") || address.isEmpty() || address.equals(" "))
            return 3;
        return clubNode.createNode(id, name, address);
    }
    
    public Club findByIdOrName(int id, String name) {
        if ((id < 0 && name.isEmpty()) || (id > 0 && !name.isEmpty()))
            return null;
        return clubNode.findByIdOrName(id, name);
    }
    
    public int getClubsNumber() {
        return clubNode.getClubsNumber();
    }
    
    public List<Club> getAllClubs() {
        return clubNode.getAllClubs();
    }
    
    public int updateNameOrAddressById(int id, String name, String address) {
        if (id < 0 || ((name.isEmpty() || name.equals(" ")) && (address.isEmpty() || address.equals(" "))))
            return 3;
        return clubNode.updateNameOrAddressById(id, name, address);
    }
    
    public int deleteNodeById(int id) {
        if (id < 0)
            return 3;
        return clubNode.deleteNodeById(id);
    }
    
    public int createRelationship(int id, String clubName, String position) {
        if (!position.toUpperCase().equals("MEMBER") && !position.toUpperCase().equals("CHAIRMAN"))
            return 3;
        return relStClub.createRelationship(id, clubName, position);
    }
    
    public List<StudentClub> getClubStudents(String clubName) {
        return relStClub.getClubStudents(clubName);
    }
    
    public int updateRelationship(int id, String clubName, String position) {
        if (!position.toUpperCase().equals("MEMBER") && !position.toUpperCase().equals("CHAIRMAN"))
            return 3;
        return relStClub.updateRelationship(id, clubName, position);
    }
    
    public int deleteRelationship(int id, String clubName) {
        if (id < 0)
            return 3;
        return relStClub.deleteRelationship(id, clubName);
    }
}
