package com.severaldatabases.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
public class Product {

    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Id
    private int id;
    private String name;
    private double price;

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Product [name=")
                .append(name)
                .append(", id=")
                .append(id)
                .append("]");
        return builder.toString();
    }
}
