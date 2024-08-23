package com.springboot.blog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "category" ,cascade = CascadeType.ALL,orphanRemoval = true)

    //mapped by  attribute tells the hibernate that when using oneToMany Bidirectional mapping don't create the additional join column
    //cascadeType.All ->when ever perform this operation on parent entity also applicable for child entity
    //orphanRemoval = true ->tells hibernate that remove all remove all orphan entity database table
    // it means child entity dont have parents reference that hibernate remove those child entity from database table
    private List<Post> posts;


}
