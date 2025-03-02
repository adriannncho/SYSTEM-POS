package org.sp.processor.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.sp.processor.domain.user.TypeDocument;

@ApplicationScoped
public class TypeDocumentRepository implements PanacheRepository<TypeDocument> {
}
