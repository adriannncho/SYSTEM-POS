package org.sp.processor.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.sp.processor.domain.product.Product;
import org.sp.processor.domain.product.ProductDTO;
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

    public void productUpdate(ProductDTO productDTO){
        LOG.infof("@productUpdate SERV > Start service for product update with id %s", productDTO.getIdProduct());

        LOG.infof("@productUpdate SERV > Search product with id %s", productDTO.getIdProduct());
        Product product = productRepository.findById((long) productDTO.getIdProduct());

        LOG.infof("@productUpdate SERV > Validate product with id %s", productDTO.getIdProduct());
        validateProduct(product);

        LOG.infof("@productUpdate SERV > Update data product with id %s", productDTO.getIdProduct());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setValue(productDTO.getValue());
        product.setStatus(productDTO.isStatus());
        product.setIdCategory(productDTO.getIdCategory());

        LOG.infof("@productUpdate SERV > Save product with id %s", productDTO.getIdProduct());

        productRepository.persist(product);

        LOG.infof("@productUpdate SERV > Successfully save product with id %s", productDTO.getIdProduct());

    }

    public void changeStatusProduct(Long idProduct) throws PVException{
        LOG.infof("@changeStatusProduct SERV > Start service to desactive product with ID %d", idProduct);

        LOG.infof("@changeStatusProduct SERV > Serch producto to desactive with ID %d", idProduct);
        Product existingProduct = productRepository.findById(idProduct);

        LOG.infof("@changeStatusProduct SERV > Validate product with id %s", idProduct);
        validateProduct(existingProduct);

        LOG.infof("@changeStatusProduct SERV > Desactive product with id %s", idProduct);

        existingProduct.setStatus(!existingProduct.isStatus());
        productRepository.persist(existingProduct);

        LOG.infof("@changeStatusProduct SERV > Successfully save product with id %s", idProduct);
    }

    private void validateProduct(Product product){
        LOG.info("@validateProduct SERV > Validating if product exists");

        if(product == null){
            LOG.warn("@validateProduct SERV > No product found, throwing NOT_FOUND exception");
            throw new PVException(Response.Status.NOT_FOUND.getStatusCode(), "No se encontró el producto con el número de id ingresado.");
        }
    }



}
