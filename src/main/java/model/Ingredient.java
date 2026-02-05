package model;

public class Ingredient {
    // Klassenvariablen
    private int id;
    private String name;

    // Getter und Setter
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

    // toString methode
    @Override
    public String toString() {
        return "model.Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
