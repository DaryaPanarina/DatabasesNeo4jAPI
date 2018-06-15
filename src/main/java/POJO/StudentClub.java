package POJO;

public class StudentClub {
    private String name, group, position;

    public StudentClub(String name, String group, String position) {
        this.name = name;
        this.group = group;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
