package org.sp.processor.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sp.processor.domain.business.Business;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USUARIOS")
public class UserData {
    @Id
    @Column(name = "NUMERO_DOCUMENTO")
    private Long documentNumber;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "ID_TIPO_USUARIO")
    private TypeUser userType;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "ID_TIPO_DOCUMENTO")
    private TypeDocument documentTypeId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "NEGOCIO_ID")
    private Business business;

    @Column(name = "NOMBRE")
    private String name;

    @Column(name = "TELEFONO")
    private String phone;

    @Column(name = "DIRECCION")
    private String address;

    @Column(name = "CORREO")
    private String email;

    @Column(name = "CONTRASENA")
    private String password;

    @Column(name = "ACTIVO")
    private boolean userStatusId;
}

