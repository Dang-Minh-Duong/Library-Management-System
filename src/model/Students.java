package model;

public class Students {
    private String name, university;
    private int id;
    public Students (int id,String name, String university) {
        this.id = id;
        this.name=name;
        this.university=university;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }
}
