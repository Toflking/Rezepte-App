package model;

// Klasse für Area Objekte
public class Area {
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
        return "model.Area{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
