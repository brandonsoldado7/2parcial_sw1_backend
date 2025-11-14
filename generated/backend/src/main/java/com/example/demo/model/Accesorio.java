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

public class Accesorio {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Date fecha;






    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "electrodomestico_id", referencedColumnName = "id")
    private Electrodomestico electrodomestico;

}
