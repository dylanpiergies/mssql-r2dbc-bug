package com.example.with.fairly.lengthy.packagename;

import org.springframework.data.relational.core.mapping.Table;

@Table("my_table")
public record MyTableEntity(
    Integer id
) {
}
