package org.sp.processor.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.sp.processor.domain.product.Product;
import org.sp.processor.helper.exception.PVException;
import org.sp.processor.repository.ProductRepository;

import java.util.List;

@ApplicationScoped
public class ProductService {

    private final Logger LOG = Logger.getLogger(ProductService.class);

    @Inject
    private ProductRepository productRepository;

    public List<Product> getProducts(){

        List<Product> productsList = productRepository.listAll();

        if (productsList.isEmpty()){
            throw new PVException(Response.Status.NOT_FOUND.getStatusCode(), "No se encontraron productos.");
        }

        return productsList;
    }


}
