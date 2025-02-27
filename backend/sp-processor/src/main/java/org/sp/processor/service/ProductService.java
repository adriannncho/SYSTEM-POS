package org.sp.processor.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.sp.processor.domain.product.Product;
import org.sp.processor.domain.product.ProductSaveDTO;
import org.sp.processor.helper.exception.PVException;
import org.sp.processor.repository.ProductRepository;

import java.util.List;

@ApplicationScoped
public class ProductService {

    private final Logger LOG = Logger.getLogger(ProductService.class);

    @Inject
    private ProductRepository productRepository;

    public List<Product> getProducts() {
        LOG.infof("@getProducts SERV > Start service to retrieve products");

        List<Product> productsList = productRepository.listAll();

        if (productsList.isEmpty()) {
            LOG.warnf("@getProducts SERV > No products found");
            throw new PVException(Response.Status.NOT_FOUND.getStatusCode(), "No se encontraron productos.");
        }

        LOG.infof("@getProducts SERV > Retrieved %d products", productsList.size());
        return productsList;
    }


    public void saveProduct(ProductSaveDTO productSaveDTO){

        LOG.infof("@saveProduct SERV > Start service to save a new product");

        LOG.infof("@saveProduct SERV > Creating product entity from DTO");
        Product product = Product.builder()
                .name(productSaveDTO.getName())
                .description(productSaveDTO.getDescription())
                .idCategory(productSaveDTO.getIdCategory())
                .value(productSaveDTO.getValue())
                .status(true)
                .build();

        LOG.infof("@saveProduct SERV > Persisting product with name %s", productSaveDTO.getName());
        productRepository.persist(product);
    }

}
