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

public class DependenciaTarea {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;










    
    @ManyToOne
    @JoinColumn(name = "tarea_id", referencedColumnName = "id")
    private Tarea tarea;








    
    @ManyToOne
    @JoinColumn(name = "tarea_id", referencedColumnName = "id")
    private Tarea tarea;

}
