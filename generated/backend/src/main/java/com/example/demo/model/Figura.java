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

@Inheritance(strategy = InheritanceType.JOINED)
public class Figura {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float area;









    @JsonIgnore
    @OneToMany(mappedBy = "figura")
    private List<Circulo> circulos;








    
    @ManyToOne
    @JoinColumn(name = "reporte_id", referencedColumnName = "id")
    private Reporte reporte;

}
