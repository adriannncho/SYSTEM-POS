package org.sp.processor.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.sp.processor.helper.exception.HandlerException;
import org.sp.processor.helper.exception.ProblemException;
import org.sp.processor.service.ProductService;

@Path("/sp-processor")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductApi {

    @Inject
    ProductService productService;

    @GET
    @Path("/products")
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Se obtiene el listado de productos correctamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(
                                            type = SchemaType.ARRAY,
                                            example = """
                                                    [
                                                         {
                                                             "idProduct": 1,
                                                             "name": "Hamburguesa Clásica",
                                                             "description": "Hamburguesa con carne 100% res",
                                                             "value": 15,
                                                             "idCategory": 2,
                                                             "status": true
                                                         },
                                                         {
                                                             "idProduct": 2,
                                                             "name": "Papas Fritas",
                                                             "description": "Papas fritas crujientes",
                                                             "value": 5,
                                                             "idCategory": 1,
                                                             "status": true
                                                         },
                                                         {
                                                             "idProduct": 3,
                                                             "name": "Coca-Cola",
                                                             "description": "Bebida gaseosa 350ml",
                                                             "value": 2,
                                                             "idCategory": 3,
                                                             "status": true
                                                         },
                                                         {
                                                             "idProduct": 4,
                                                             "name": "Cheesecake",
                                                             "description": "Tarta de queso con frutos rojos",
                                                             "value": 6,
                                                             "idCategory": 4,
                                                             "status": true
                                                         }
                                                     ]"""
                                    )
                            )
                    ),
                    @APIResponse(
                            responseCode = "404",
                            description = "No hay registros de productos en base de datos.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(
                                            implementation = ProblemException.class,
                                            properties = {
                                                    @SchemaProperty(
                                                            name = "detail",
                                                            example = "No se encontraron registro de productos."
                                                    )
                                            }
                                    )
                            )
                    ),
                    @APIResponse(
                            responseCode = "500",
                            description = "Error interno de servidor",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = HandlerException.ResponseError.class)
                            )
                    )
            }
    )
    @Operation(
            summary = "Obtener listado de productos",
            description = "Se obtiene el listado con la información de los productos registrados"
    )
    public Response getProducts() {
        return Response.ok().entity(productService.getProducts()).build();
    }

}
