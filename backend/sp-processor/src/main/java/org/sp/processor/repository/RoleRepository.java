package org.sp.processor.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.sp.processor.domain.user.TypeUser;

@ApplicationScoped
public class RoleRepository implements PanacheRepository<TypeUser> {
}
