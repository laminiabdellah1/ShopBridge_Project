package com.lamini.ecommerce.category;


import com.lamini.ecommerce.product.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
public class Category {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String description;

    @OneToMany(mappedBy = "category",cascade = CascadeType.REMOVE)//when i remove a category , products will be removed
    private List<Product> products;
}
