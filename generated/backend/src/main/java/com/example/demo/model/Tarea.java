package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.*;
import java.time.*;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id"
)

public class Tarea {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;










    @JsonIgnore
    @OneToMany(mappedBy = "tarea")
    private List<DependenciaTarea> dependenciaTareas;








    @JsonIgnore
    @OneToMany(mappedBy = "tarea")
    private List<DependenciaTarea> dependenciaTareas;

}
