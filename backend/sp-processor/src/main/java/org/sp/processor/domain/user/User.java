package org.sp.processor.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sp.processor.domain.business.Business;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USUARIOS")
public class User {

    @Id
    @Column(name = "NUMERO_DOCUMENTO")
    private Long documentNumber;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "TIPO_DOCUMENTO_ID")
    private TypeDocument documentTypeId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "TIPO_USUARIO_ID")
    private TypeUser userType;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "NEGOCIO_ID")
    private Business businessId;

    @Column(name = "ACTIVO")
    private boolean userStatus;

    @Column(name = "NOMBRE")
    private String name;

    @Column(name = "TELEFONO")
    private String phone;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "CONTRASENA")
    private String password;
}
