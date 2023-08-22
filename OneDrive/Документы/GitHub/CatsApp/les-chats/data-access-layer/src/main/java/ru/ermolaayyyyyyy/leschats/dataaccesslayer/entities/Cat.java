package ru.ermolaayyyyyyy.leschats.dataaccesslayer.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.models.Color;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@Entity
public class Cat {

    private String name;
    private LocalDate birthDate;

    private String breed;
    @Autowired
    private Color color;
    @ManyToOne
    @Setter(AccessLevel.PACKAGE)
    @Autowired
    private Owner owner;
    @ManyToMany(fetch = FetchType.EAGER)
    @Autowired
    @JoinTable(
            name="cat_cat",
            joinColumns = @JoinColumn(
                    name="cat_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name="friends_id", referencedColumnName = "id"
            )
    )
    private List<Cat> friends;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Cat(String name, LocalDate birthDate, String breed, Color color, Owner owner){
        this.name = name;
        this.birthDate = birthDate;
        this.breed = breed;
        this.color = color;
        this.owner = owner;
        friends = new ArrayList<>();
    }

    public void addFriend(Cat cat){
        friends.add(cat);
    }
    public void update(String name, LocalDate birthDate, String breed, Color color, Owner owner){
        if (name != null){
            setName(name);
        }
        if (birthDate != null){
            setBirthDate(birthDate);
        }
        if (breed != null){
            setBreed(breed);
        }
        if (color != null){
            setColor(color);
        }
        if (owner != null){
            setOwner(owner);
        }
    }
}
