package org.sp.processor.domain.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sp.processor.domain.business.Business;
import org.sp.processor.domain.user.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PEDIDOS")
public class Orders implements Serializable {

    @Id
    @Column(name = "ID", length = 36)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idOrder;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "NEGOCIO_ID")
    private Business businessId;

    @Column(name = "MESA_ID")
    private Long numberTable;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "USUARIO_ID")
    private User userId;

    @Column(name = "ESTADO")
    private String status;

    @Column(name = "FECHA_CREACION")
    private LocalDateTime dateCreation;

    @Column(name = "TOTAL")
    private Long amount;

    @JsonManagedReference
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetailOrder> detailsOrders;

}
