package org.sp.processor.domain.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.vavr.collection.Tree;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sp.processor.domain.product.Product;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DETALLE_PEDIDO")
public class DetailOrder implements Serializable {

    @Id
    @Column(name = "ID", length = 36)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDetailOrder;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "PEDIDO_ID")
    private Orders order;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "PRODUCTO_ID")
    private Product product;

    @Column(name = "CANTIDAD")
    private Long quantity;

    @Column(name = "OBSERVACION")
    private String observation;

    @Column(name = "PRECIO_UNITARIO")
    private Long priceUnit;

    @Column(name = "SUBTOTAL")
    private Long subTotal;
}
