package org.sp.processor.domain.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PRODUCTOS")
public class Product implements Serializable {

    @Id
    @Column(name = "ID", length = 36)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idProduct;

    @Column(name = "NOMBRE")
    private String name;

    @Column(name = "DESCRIPCION")
    private String description;

    @Column(name = "PRECIO")
    private Long value;

    @Column(name = "CATEGORIA_ID")
    private int idCategory;

    @Column(name = "ACTIVO")
    private boolean status;
}
