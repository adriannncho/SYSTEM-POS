package org.sp.processor.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.sp.processor.domain.user.UserData;

@ApplicationScoped
public class UserDataRepository implements PanacheRepository<UserData> {

    public UserData findByDocumentNumber(long documentNumber) {
        return find("documentNumber", documentNumber).firstResult();
    }
}
