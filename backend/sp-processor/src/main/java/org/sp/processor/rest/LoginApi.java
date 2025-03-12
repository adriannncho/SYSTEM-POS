package org.sp.processor.rest;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.sp.processor.domain.user.LoginDTO;
import org.sp.processor.domain.user.UserDTO;
import org.sp.processor.helper.exception.HandlerException;
import org.sp.processor.helper.exception.ProblemException;
import org.sp.processor.helper.jwt.Token;
import org.sp.processor.service.LoginService;

@Path("/sp-processor")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginApi {

    @Inject
    LoginService loginService;

    @POST
    @Transactional
    @Path("/login")
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Se accede de forma correcta con el usuario y contraseña asignados."
                    ),
                    @APIResponse(
                            responseCode = "404",
                            description = "No hay registros de usuarios en base de datos.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(
                                            implementation = ProblemException.class,
                                            properties = {
                                                    @SchemaProperty(
                                                            name = "detail",
                                                            example = "No se encontro el usuario con el que se intenta acceder."
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
            summary = "Acceso a la aplicación",
            description = "Se da acceso de forma exitosa a la aplicación"
    )
    public Response validateLogin(
            @Valid LoginDTO loginDTO
    ) {
        return Response.ok().entity(loginService.validateLogin(loginDTO)).build();
    }

    @POST
    @Path("/auth/refresh")
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Token refrescado correctamente.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(
                                            implementation = Token.class
                                    )
                            )
                    ),
                    @APIResponse(
                            responseCode = "401",
                            description = "Usuario no encontrado o token inválido.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(
                                            implementation = ProblemException.class,
                                            properties = {
                                                    @SchemaProperty(
                                                            name = "detail",
                                                            example = "No se encontró el usuario con el que se intenta refrescar el token."
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
            summary = "Refrescar el token de acceso",
            description = "Permite refrescar el token de acceso mediante el uso de un refresh token válido."
    )
    public Response refreshToken(@HeaderParam("Authorization") String authHeader){
        return Response.ok().entity(loginService.refreshToken(authHeader)).build();
    }

    @POST
    @Transactional
    @Path("/registerUser")
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Usuario registrado exitosamente."
                    ),
                    @APIResponse(
                            responseCode = "400",
                            description = "Errores de validación de entrada",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(
                                            example = """
                                                    {
                                                      El campo documentNumber (número de documento) no puede ser nulo.,
                                                      El campo userTypeId (ID de tipo de usuario) no puede ser nulo.,
                                                      El campo documentTypeId (ID de tipo de documento) no puede ser nulo.,
                                                      El campo userStatus (ID de estado de usuario) no puede ser nulo.,
                                                      El campo firstName (nombre) no puede ser nulo ni estar vacío.,
                                                      El campo phone (teléfono) no puede ser nulo ni estar vacío.,
                                                      El campo email (correo) no puede ser nulo ni estar vacío.,
                                                      El campo email (correo) debe ser una dirección de correo válida.,
                                                      El campo password (contraseña) no puede ser nulo ni estar vacío.,
                                                      El campo password (contraseña) debe tener al menos 8 caracteres.
                                                    }"""
                                    )
                            )
                    ),
                    @APIResponse(
                            responseCode = "409",
                            description = "El usuario ya existe.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = ProblemException.class)
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
            summary = "Registro de usuario",
            description = "Registra un nuevo usuario en la aplicación"
    )
    public Response saveUser(
            @Valid UserDTO userDTO
    ) {
        loginService.saveUser(userDTO);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Transactional
    @Path("/updateUser")
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Usuario actualizado exitosamente."
                    ),
                    @APIResponse(
                            responseCode = "400",
                            description = "Errores de validación de entrada",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(
                                            example = """
                                                    {
                                                      El campo documentNumber (número de documento) no puede ser nulo.,
                                                      El campo userTypeId (ID de tipo de usuario) no puede ser nulo.,
                                                      El campo documentTypeId (ID de tipo de documento) no puede ser nulo.,
                                                      El campo userStatus (ID de estado de usuario) no puede ser nulo.,
                                                      El campo name (nombre) no puede ser nulo ni estar vacío.,
                                                      El campo phone (teléfono) no puede ser nulo ni estar vacío.,
                                                      El campo email (correo) no puede ser nulo ni estar vacío.,
                                                      El campo email (correo) debe ser una dirección de correo válida.,
                                                      El campo password (contraseña) no puede ser nulo ni estar vacío.,
                                                      El campo password (contraseña) debe tener al menos 8 caracteres.
                                                    }"""
                                    )
                            )
                    ),
                    @APIResponse(
                            responseCode = "404",
                            description = "No se encontro el usuario a actualizar.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(
                                            implementation = ProblemException.class,
                                            properties = {
                                                    @SchemaProperty(
                                                            name = "detail",
                                                            example = "Usuario no encontrado."
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
            summary = "Actualización de usuario",
            description = "Actualiza un usuario en la aplicación"
    )
    public Response updaterUser(
            @Valid UserDTO userDTO
    ) {
        loginService.updateUser(userDTO);
        return Response.status(Response.Status.OK).build();
    }

    @PUT
    @Transactional
    @Path("/changeStatusUser/{userId}")
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Se cambio estado del usuario exitosamente."
                    ),
                    @APIResponse(
                            responseCode = "400",
                            description = "Errores de validación de entrada",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(
                                            example = """
                                                    {
                                                      El documento del usuario debe ser un número positivo.
                                                    }"""
                                    )
                            )
                    ),
                    @APIResponse(
                            responseCode = "404",
                            description = "No se encontro el usuario a eliminar.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(
                                            implementation = ProblemException.class,
                                            properties = {
                                                    @SchemaProperty(
                                                            name = "detail",
                                                            example = "Usuario no encontrado."
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
            summary = "Cambiar estado de usuario",
            description = "Se cambio estado del usuario en la aplicación"
    )
    public Response changeStatusUser(
            @PathParam("userId")
            @Positive(message = "El documento del usuario debe ser un número positivo.")
            Long userId
    ) {
        loginService.changeStatusUser(userId);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/typeDocument")
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Se obtiene el listado de tipos de documentos correctamente"
                    ),
                    @APIResponse(
                            responseCode = "404",
                            description = "No hay registros de tipos de documentos en base de datos.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(
                                            implementation = ProblemException.class,
                                            properties = {
                                                    @SchemaProperty(
                                                            name = "detail",
                                                            example = "No se encontraron registros de tipos de documentos."
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
            summary = "Obtener listado de tipos de documentos",
            description = "Se obtiene el listado con la información de los tipos de documentos registrados"
    )
    public Response getTypeDocument() {
        return Response.ok().entity(loginService.getTypeDocument()).build();
    }

    @GET
    @Path("/role")
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Se obtiene el listado de roles correctamente"
                    ),
                    @APIResponse(
                            responseCode = "404",
                            description = "No hay registros de roles en base de datos.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(
                                            implementation = ProblemException.class,
                                            properties = {
                                                    @SchemaProperty(
                                                            name = "detail",
                                                            example = "No se encontraron registros de roles."
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
            summary = "Obtener listado de roles",
            description = "Se obtiene el listado con la información de los roles registrados"
    )
    public Response getRole() {
        return Response.ok().entity(loginService.getRole()).build();
    }
}
