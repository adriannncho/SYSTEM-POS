package org.sp.processor.domain.business;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "NEGOCIO")
public class Business {

    @Id
    @Column(name = "ID")
    private Long idBusiness;

    @Column(name = "NOMBRE")
    private String name;

    @Column(name = "TIPO_NEGOCIO")
    private String typeBusiness;

    @Column(name = "DIRECCION")
    private String address;

}
