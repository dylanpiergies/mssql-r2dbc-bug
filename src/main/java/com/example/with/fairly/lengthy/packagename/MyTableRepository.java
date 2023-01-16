package com.example.with.fairly.lengthy.packagename;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyTableRepository extends R2dbcRepository<MyTableEntity, Integer> {
}
