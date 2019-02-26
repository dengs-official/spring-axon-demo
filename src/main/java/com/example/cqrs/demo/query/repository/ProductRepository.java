package com.example.cqrs.demo.query.repository;

import com.example.cqrs.demo.query.entries.ProductEntry;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "products", path = "products")
public interface ProductRepository extends PagingAndSortingRepository<ProductEntry, String> {
}
