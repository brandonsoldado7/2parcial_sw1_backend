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

public class Refrigerador extends Electrodomestico {



    private int capacidad;
    private boolean congelador;









    
    @ManyToOne
    @JoinColumn(name = "electrodomestico_id", referencedColumnName = "id")
    private Electrodomestico electrodomestico;

}
