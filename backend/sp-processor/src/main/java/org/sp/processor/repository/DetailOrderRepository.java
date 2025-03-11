package org.sp.processor.repository;

import jakarta.enterprise.context.ApplicationScoped;
import org.sp.processor.domain.order.DetailOrder;
import org.springframework.data.jpa.repository.JpaRepository;

@ApplicationScoped
public interface DetailOrderRepository extends JpaRepository<DetailOrder, Long> {

}
