package POJO;

public class StudentScholarship {
    private Integer amount;
    
    private String type, since, until;

    public StudentScholarship(Integer amount, String type, String since, String until) {
        this.amount = amount;
        this.type = type;
        this.since = since;
        this.until = until;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSince() {
        return since;
    }

    public void setSince(String since) {
        this.since = since;
    }

    public String getUntil() {
        return until;
    }

    public void setUntil(String until) {
        this.until = until;
    }
}
