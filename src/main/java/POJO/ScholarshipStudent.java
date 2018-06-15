package POJO;

public class ScholarshipStudent {
    private Integer id;
    private String fullName, group;

    public ScholarshipStudent(Integer id, String fullName, String group) {
        this.id = id;
        this.fullName = fullName;
        this.group = group;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    } 
}
