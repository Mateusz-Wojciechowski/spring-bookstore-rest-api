package pl.edu.pwr.ztw.books.models;

import jakarta.persistence.*;

@Entity
@Table(name = "readers")
public class Reader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    public Reader() {
    }

    public Reader(String name) {
        this.name = name;
    }

    // Gettery i settery
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
}
