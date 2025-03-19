package org.sp.processor.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.sp.processor.domain.product.*;
import org.sp.processor.helper.exception.SPException;
import org.sp.processor.repository.CategoryRepository;
import org.sp.processor.repository.ProductRepository;

import java.util.List;

@ApplicationScoped
public class ProductService {

    private final Logger LOG = Logger.getLogger(ProductService.class);

    @Inject
    private ProductRepository productRepository;

    @Inject
    private CategoryRepository categoryRepository;

    public List<Product> getProducts() {
        LOG.infof("@getProducts SERV > Start service to retrieve products");

        List<Product> productsList = productRepository.listAll();

        if (productsList.isEmpty()) {
            LOG.warnf("@getProducts SERV > No products found");
            throw new SPException(Response.Status.NOT_FOUND.getStatusCode(), "No se encontraron productos.");
        }

        LOG.infof("@getProducts SERV > Retrieved %d products", productsList.size());
        return productsList;
    }

    public void saveProduct(ProductSaveDTO productSaveDTO) {

        LOG.infof("@saveProduct SERV > Start service to save a new product");

        if (productRepository.findProductByNameValueAndCategory(productSaveDTO.getName(),
                productSaveDTO.getValue(), productSaveDTO.getIdCategory()) != null) {

            LOG.warnf("@saveProduct SERV > Product already exists with name : %s, value : %s, idCategory : %s",
                    productSaveDTO.getName(), productSaveDTO.getValue(), productSaveDTO.getIdCategory());

            throw new SPException(Response.Status.CONFLICT.getStatusCode(), "El producto ya existe.");
        }

        LOG.infof("@saveProduct SERV > Creating product entity from DTO");
        Product product = Product.builder()
                .name(productSaveDTO.getName())
                .description(productSaveDTO.getDescription())
                .category(Category.builder().idCategory(productSaveDTO.getIdCategory()).build())
                .value(productSaveDTO.getValue())
                .status(true)
                .build();

        LOG.infof("@saveProduct SERV > Persisting product with name %s", productSaveDTO.getName());
        productRepository.persist(product);
    }

    public void productUpdate(ProductDTO productDTO) {
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
        product.setCategory(Category.builder().idCategory(productDTO.getIdCategory()).build());

        LOG.infof("@productUpdate SERV > Save product with id %s", productDTO.getIdProduct());

        productRepository.persist(product);

        LOG.infof("@productUpdate SERV > Successfully save product with id %s", productDTO.getIdProduct());
    }

    public void changeStatusProduct(Long idProduct) throws SPException {
        LOG.infof("@changeStatusProduct SERV > Start service to deactivate product with ID %d", idProduct);

        LOG.infof("@changeStatusProduct SERV > Search product to deactivate with ID %d", idProduct);
        Product existingProduct = productRepository.findById(idProduct);

        LOG.infof("@changeStatusProduct SERV > Validate product with id %s", idProduct);
        validateProduct(existingProduct);

        LOG.infof("@changeStatusProduct SERV > Deactivating product with id %s", idProduct);

        existingProduct.setStatus(!existingProduct.isStatus());
        productRepository.persist(existingProduct);

        LOG.infof("@changeStatusProduct SERV > Successfully save product with id %s", idProduct);
    }

    private void validateProduct(Product product) {
        LOG.info("@validateProduct SERV > Validating if product exists");

        if (product == null) {
            LOG.warn("@validateProduct SERV > No product found, throwing NOT_FOUND exception");
            throw new SPException(Response.Status.NOT_FOUND.getStatusCode(), "No se encontró el producto con el número de id ingresado.");
        }
    }

    public List<Category> getCategory() {
        LOG.infof("@getCategory SERV > Start service to retrieve products");

        List<Category> categoryList = categoryRepository.listAll();

        if (categoryList.isEmpty()) {
            LOG.warnf("@getCategory SERV > No categories found");
            throw new SPException(Response.Status.NOT_FOUND.getStatusCode(), "No se encontraron categorías");
        }
        LOG.infof("@getCategory SERV > Retrieved %d categories", categoryList.size());

        return categoryList;
    }
    public void saveCategory(CategorySaveDTO categorySaveDTO){

        LOG.infof("@saveCategory SERV > Start service to save a new category");
        if(categoryRepository.searchCategoryByName(categorySaveDTO.getName()) !=null){

            LOG.warnf("@saveCategory SERV > The category name already exists : %s",categorySaveDTO.getName());
            throw new SPException(Response.Status.CONFLICT.getStatusCode(), "La categoría ya existe.");

        }
        LOG.infof("@saveCategory SERV > Create new category in entity DTO ");

        Category category = Category.builder()
                .name(categorySaveDTO.getName())
                .build();

        LOG.infof("@saveCategory SERV > Persisting category with name %s", categorySaveDTO.getName());
        categoryRepository.persist(category);
    }

}
