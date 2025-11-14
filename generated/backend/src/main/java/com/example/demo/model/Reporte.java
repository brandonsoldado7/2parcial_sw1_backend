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

public class Reporte {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;










    
    @ManyToOne
    @JoinColumn(name = "baseDeDatos_id", referencedColumnName = "id")
    private BaseDeDatos baseDeDatos;








    @JsonIgnore
    @OneToMany(mappedBy = "reporte")
    private List<Figura> figuras;

}
