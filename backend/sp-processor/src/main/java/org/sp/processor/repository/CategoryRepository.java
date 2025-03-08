package org.sp.processor.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.sp.processor.domain.product.Category;

@ApplicationScoped
public class CategoryRepository implements PanacheRepository<Category> {
}
