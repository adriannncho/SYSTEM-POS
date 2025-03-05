package org.sp.processor.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.sp.processor.domain.product.Product;
import org.sp.processor.domain.user.UserData;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {

    public Product findProductByNameValueAndCategory(String name, long value, long idCategory) {
        return find("name = ?1 and value = ?2 and category.idCategory = ?3", name, value, idCategory).firstResult();
    }

}
