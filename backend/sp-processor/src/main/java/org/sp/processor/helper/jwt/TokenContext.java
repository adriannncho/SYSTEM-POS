package org.sp.processor.helper.jwt;

import jakarta.enterprise.context.RequestScoped;
import lombok.Getter;
import lombok.Setter;

@RequestScoped
@Setter
@Getter
public class TokenContext {
    private String token;
}
