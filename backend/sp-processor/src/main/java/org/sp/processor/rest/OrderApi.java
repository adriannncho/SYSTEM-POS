package org.sp.processor.rest;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.sp.processor.domain.order.OrderDTO;
import org.sp.processor.domain.product.ProductSaveDTO;
import org.sp.processor.helper.exception.HandlerException;
import org.sp.processor.helper.exception.ProblemException;
import org.sp.processor.service.OrderService;

@Path("/sp-processor")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderApi {

    @Inject
    OrderService orderService;

    @GET
    @Path("/orders")
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Se obtiene el listado de pedidos correctamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(
                                            type = SchemaType.ARRAY,
                                            example = """
                                                    [
                                                        {
                                                            "idOrder": 18,
                                                            "businessId": {
                                                                "idBusiness": 1,
                                                                "name": "Restaurante La Fresa",
                                                                "typeBusiness": "RESTAURANTE",
                                                                "address": "Calle 123 #45-67, Ciudad A"
                                                            },
                                                            "numberTable": 2,
                                                            "userId": {
                                                                "documentNumber": 121212121,
                                                                "documentTypeId": {
                                                                    "idTypeDocument": 1,
                                                                    "name": "TARAJETA_IDENTIDAD"
                                                                },
                                                                "userType": {
                                                                    "idTypeUser": 1,
                                                                    "name": "ADMINISTRADOR"
                                                                },
                                                                "businessId": {
                                                                    "idBusiness": 1,
                                                                    "name": "Restaurante La Fresa",
                                                                    "typeBusiness": "RESTAURANTE",
                                                                    "address": "Calle 123 #45-67, Ciudad A"
                                                                },
                                                                "userStatus": true,
                                                                "name": "Adrian Prueba con Token",
                                                                "phone": "1234567890",
                                                                "email": "doepruebaaa@example.com",
                                                                "password": "$2a$10$TOidbnEfHmfdrUzs6cF.nek/lWLSb3lGw3toKKsFjMKPKr9CNgaDK"
                                                            },
                                                            "status": "PENDIENTE",
                                                            "dateCreation": "2025-03-06T19:23:01",
                                                            "amount": 50,
                                                            "detailsOrders": [
                                                                {
                                                                    "idDetailOrder": 26,
                                                                    "product": {
                                                                        "idProduct": 1,
                                                                        "name": "Hamburguesa Clásica",
                                                                        "description": "Hamburguesa con carne 100% res",
                                                                        "value": 15,
                                                                        "category": {
                                                                            "idCategory": 2,
                                                                            "name": "Platos Fuertes"
                                                                        },
                                                                        "status": true
                                                                    },
                                                                    "quantity": 2,
                                                                    "observation": "Sin cebolla",
                                                                    "priceUnit": 10000,
                                                                    "subTotal": 20000
                                                                },
                                                                {
                                                                    "idDetailOrder": 27,
                                                                    "product": {
                                                                        "idProduct": 2,
                                                                        "name": "Papas Fritas",
                                                                        "description": "Papas fritas crujientes",
                                                                        "value": 5,
                                                                        "category": {
                                                                            "idCategory": 1,
                                                                            "name": "Entradas"
                                                                        },
                                                                        "status": true
                                                                    },
                                                                    "quantity": 1,
                                                                    "observation": "Extra queso",
                                                                    "priceUnit": 15000,
                                                                    "subTotal": 15000
                                                                }
                                                            ]
                                                        },
                                                        {
                                                            "idOrder": 19,
                                                            "businessId": {
                                                                "idBusiness": 1,
                                                                "name": "Restaurante La Fresa",
                                                                "typeBusiness": "RESTAURANTE",
                                                                "address": "Calle 123 #45-67, Ciudad A"
                                                            },
                                                            "numberTable": 1,
                                                            "userId": {
                                                                "documentNumber": 121212121,
                                                                "documentTypeId": {
                                                                    "idTypeDocument": 1,
                                                                    "name": "TARAJETA_IDENTIDAD"
                                                                },
                                                                "userType": {
                                                                    "idTypeUser": 1,
                                                                    "name": "ADMINISTRADOR"
                                                                },
                                                                "businessId": {
                                                                    "idBusiness": 1,
                                                                    "name": "Restaurante La Fresa",
                                                                    "typeBusiness": "RESTAURANTE",
                                                                    "address": "Calle 123 #45-67, Ciudad A"
                                                                },
                                                                "userStatus": true,
                                                                "name": "Adrian Prueba con Token",
                                                                "phone": "1234567890",
                                                                "email": "doepruebaaa@example.com",
                                                                "password": "$2a$10$TOidbnEfHmfdrUzs6cF.nek/lWLSb3lGw3toKKsFjMKPKr9CNgaDK"
                                                            },
                                                            "status": "CONFIRMADO",
                                                            "dateCreation": "2025-03-06T19:23:01",
                                                            "amount": 75,
                                                            "detailsOrders": [
                                                                {
                                                                    "idDetailOrder": 28,
                                                                    "product": {
                                                                        "idProduct": 2,
                                                                        "name": "Papas Fritas",
                                                                        "description": "Papas fritas crujientes",
                                                                        "value": 5,
                                                                        "category": {
                                                                            "idCategory": 1,
                                                                            "name": "Entradas"
                                                                        },
                                                                        "status": true
                                                                    },
                                                                    "quantity": 3,
                                                                    "observation": "Picante",
                                                                    "priceUnit": 8500,
                                                                    "subTotal": 17000
                                                                }
                                                            ]
                                                        },
                                                        {
                                                            "idOrder": 20,
                                                            "businessId": {
                                                                "idBusiness": 2,
                                                                "name": "Cafetería El Buen Café",
                                                                "typeBusiness": "CAFETERIA",
                                                                "address": "Avenida Principal #89-12, Ciudad B"
                                                            },
                                                            "numberTable": 5,
                                                            "userId": {
                                                                "documentNumber": 121212121,
                                                                "documentTypeId": {
                                                                    "idTypeDocument": 1,
                                                                    "name": "TARAJETA_IDENTIDAD"
                                                                },
                                                                "userType": {
                                                                    "idTypeUser": 1,
                                                                    "name": "ADMINISTRADOR"
                                                                },
                                                                "businessId": {
                                                                    "idBusiness": 1,
                                                                    "name": "Restaurante La Fresa",
                                                                    "typeBusiness": "RESTAURANTE",
                                                                    "address": "Calle 123 #45-67, Ciudad A"
                                                                },
                                                                "userStatus": true,
                                                                "name": "Adrian Prueba con Token",
                                                                "phone": "1234567890",
                                                                "email": "doepruebaaa@example.com",
                                                                "password": "$2a$10$TOidbnEfHmfdrUzs6cF.nek/lWLSb3lGw3toKKsFjMKPKr9CNgaDK"
                                                            },
                                                            "status": "PENDIENTE",
                                                            "dateCreation": "2025-03-06T19:23:01",
                                                            "amount": 120,
                                                            "detailsOrders": [
                                                                {
                                                                    "idDetailOrder": 29,
                                                                    "product": {
                                                                        "idProduct": 1,
                                                                        "name": "Hamburguesa Clásica",
                                                                        "description": "Hamburguesa con carne 100% res",
                                                                        "value": 15,
                                                                        "category": {
                                                                            "idCategory": 2,
                                                                            "name": "Platos Fuertes"
                                                                        },
                                                                        "status": true
                                                                    },
                                                                    "quantity": 4,
                                                                    "observation": "Sin sal",
                                                                    "priceUnit": 30000,
                                                                    "subTotal": 120000
                                                                }
                                                            ]
                                                        }
                                                    ]"""
                                    )
                            )
                    ),
                    @APIResponse(
                            responseCode = "404",
                            description = "No hay registros de pedidos en base de datos.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(
                                            implementation = ProblemException.class,
                                            properties = {
                                                    @SchemaProperty(
                                                            name = "detail",
                                                            example = "No se encontraron registro de pedidos."
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
            summary = "Obtener listado de pedidos",
            description = "Se obtiene el listado con la información de los pedidos registrados"
    )
    public Response getOrders() {
        return Response.ok().entity(orderService.getOrders()).build();
    }

    @GET
    @Path("/orders/{numberTable}")
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Se obtiene el pedido en estado pendiente de la mesa 1 correctamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(
                                            type = SchemaType.ARRAY,
                                            example = """
                                                    [
                                                        {
                                                            "idOrder": 18,
                                                            "businessId": {
                                                                "idBusiness": 1,
                                                                "name": "Restaurante La Fresa",
                                                                "typeBusiness": "RESTAURANTE",
                                                                "address": "Calle 123 #45-67, Ciudad A"
                                                            },
                                                            "numberTable": 2,
                                                            "userId": {
                                                                "documentNumber": 121212121,
                                                                "documentTypeId": {
                                                                    "idTypeDocument": 1,
                                                                    "name": "TARAJETA_IDENTIDAD"
                                                                },
                                                                "userType": {
                                                                    "idTypeUser": 1,
                                                                    "name": "ADMINISTRADOR"
                                                                },
                                                                "businessId": {
                                                                    "idBusiness": 1,
                                                                    "name": "Restaurante La Fresa",
                                                                    "typeBusiness": "RESTAURANTE",
                                                                    "address": "Calle 123 #45-67, Ciudad A"
                                                                },
                                                                "userStatus": true,
                                                                "name": "Adrian Prueba con Token",
                                                                "phone": "1234567890",
                                                                "email": "doepruebaaa@example.com",
                                                                "password": "$2a$10$TOidbnEfHmfdrUzs6cF.nek/lWLSb3lGw3toKKsFjMKPKr9CNgaDK"
                                                            },
                                                            "status": "PENDIENTE",
                                                            "dateCreation": "2025-03-06T19:23:01",
                                                            "amount": 50,
                                                            "detailsOrders": []
                                                        }
                                                    ]"""
                                    )
                            )
                    ),
                    @APIResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(
                                            implementation = ProblemException.class,
                                            properties = {
                                                    @SchemaProperty(
                                                            name = "detail",
                                                            example = """
                                                                        [
                                                                               "El número de mesa debe ser un número positivo."
                                                                        ]
                                                                    """
                                                    )
                                            }
                                    )
                            )
                    ),
                    @APIResponse(
                            responseCode = "404",
                            description = "No hay registros de pedidos en base de datos.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(
                                            implementation = ProblemException.class,
                                            properties = {
                                                    @SchemaProperty(
                                                            name = "detail",
                                                            example = "No se encontraron detalles de pedidos pendientes de la mesa 1."
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
            summary = "Obtener pedido pendiente de una mesa.",
            description = "Se obtiene pedido pendiente de la mesa 1"
    )
    public Response getOrdersByNumberTable(
            @PathParam("numberTable")
            @Positive(message = "El número de mesa debe ser un número positivo.")
            Long numberTable
    ) {
        return Response.ok().entity(orderService.getOrdersByNumberTable(numberTable)).build();
    }

    @PUT
    @Transactional
    @Path("/changeStatusOrder")
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Se cambia el estado del pedido de la mesa 1 correctamente."
                    ),
                    @APIResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(
                                            implementation = ProblemException.class,
                                            properties = {
                                                    @SchemaProperty(
                                                            name = "detail",
                                                            example = """
                                                                        [
                                                                               "El número de mesa debe ser un número positivo.",
                                                                               "El estado no puede estar vacío."
                                                                        ]
                                                                    """
                                                    )
                                            }
                                    )
                            )
                    ),
                    @APIResponse(
                            responseCode = "404",
                            description = "No hay registros de pedidos en base de datos.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(
                                            implementation = ProblemException.class,
                                            properties = {
                                                    @SchemaProperty(
                                                            name = "detail",
                                                            example = "No se encontraron detalles de pedidos pendientes de la mesa 1."
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
            summary = "Actualizar un pedido pendiente de una mesa.",
            description = "Se actualiza el estado de un pedido de la mesa seleccionada."
    )
    public Response changeStatusOrder(
            @QueryParam("numberTable")
            @Positive(message = "El número de mesa debe ser un número positivo.")
            Long numberTable,

            @QueryParam("newStatus")
            @NotBlank(message = "El estado no puede estar vacío.")
            String newStatus
    ) {
        orderService.changeStatusOrder(numberTable, newStatus);
        return Response.ok().build();
    }

    @POST
    @Transactional
    @Path("/createOrderByTableNumber")
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Se crea el pedido de la mesa seleccionada correctamente"
                    ),
                    @APIResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(
                                            implementation = ProblemException.class,
                                            properties = {
                                                    @SchemaProperty(
                                                            name = "detail",
                                                            example = """
                                                                        [
                                                                            "El campo 'userId' (id de usuario) no debe estar vacío",
                                                                            "El campo 'tableNumber' (número de mesa) no debe estar vacío",
                                                                            "El campo 'businessId' (id de negocio) no debe estar vacío",
                                                                            "La lista 'detailsOrders' no debe estar vacía",
                                                                            "El ID del usuario debe ser un número positivo",
                                                                            "El número de mesa debe ser un número positivo",
                                                                            "El ID del negocio debe ser un número positivo",
                                                                            "La observación no debe superar los 255 caracteres",
                                                                            La cantidad mínima debe ser 1",
                                                                            "La cantidad debe ser un número positivo",
                                                                            "El campo quantity (cantidad) no debe estar vacío",
                                                                            "El ID del producto debe ser un número positivo",
                                                                            "El campo productId (Id del producto) no debe estar vacío"
                                                                        ]
                                                                    """
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
            summary = "Se crea el pedido con el número de mesa de forma exitosa",
            description = "Se crea el pedido con el número de mesa y con la informacion relacionada de su detalla" +
                    " de forma exitosa"
    )
    public Response createOrderByNumberTable(@Valid OrderDTO orderDTO){
        orderService.createOrderByNumberTable(orderDTO);
        return Response.status(Response.Status.CREATED).build();
    }
}
