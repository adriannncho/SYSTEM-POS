package org.sp.processor.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TIPO_DOCUMENTO")
public class TypeDocument {

    @Id
    @Column(name = "ID")
    private int idTypeDocument;

    @Column(name = "NOMBRE")
    private String name;
}
