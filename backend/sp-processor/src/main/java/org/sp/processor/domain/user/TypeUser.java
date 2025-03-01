package org.sp.processor.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data
@Table(name = "TIPO_USUARIO")
public class TypeUser {

    @Id
    @Column(name = "ID")
    private int idTypeUser;

    @Column(name = "NOMBRE")
    private String name;
}
