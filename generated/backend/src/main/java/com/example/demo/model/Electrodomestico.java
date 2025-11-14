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
public class Electrodomestico {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marca;
    private String modelo;
    private double precio;
    private String consumo;









    @JsonIgnore
    @OneToMany(mappedBy = "electrodomestico")
    private List<Lavadora> lavadoras;








    @JsonIgnore
    @OneToMany(mappedBy = "electrodomestico")
    private List<Refrigerador> refrigeradors;


    
    @OneToMany(mappedBy = "electrodomestico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Material> materials;




    
    @OneToMany(mappedBy = "electrodomestico", cascade = CascadeType.PERSIST)
    private List<Accesorio> accesorios;








    
    @ManyToOne
    @JoinColumn(name = "tipo_id", referencedColumnName = "id")
    private Tipo tipo;

}
