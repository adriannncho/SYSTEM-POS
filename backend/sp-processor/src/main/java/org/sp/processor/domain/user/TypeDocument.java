package org.sp.processor.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "TIPO_DOCUMENTO")
public class TypeDocument {

    @Id
    @Column(name = "ID")
    private int idTypeDocument;

    @Column(name = "NOMBRE")
    private String name;
}
