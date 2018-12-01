package com.fprochazka.hibernateenhancementbug.hibernateenhancementbug;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
public class ProductName
{

    @Id
    @Column
    @Type(type = "pg-uuid")
    @NotNull
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Column
    @Type(type = "org.hibernate.type.TextType")
    @NotNull
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    private ProductName previousName;

    public ProductName(final Product product, final String name)
    {
        this.id = UUID.randomUUID();
        this.product = product;
        this.name = name;
    }

    protected ProductName()
    {
    }

    public UUID getId()
    {
        return id;
    }

    public Product getProduct()
    {
        return product;
    }

    public String getName()
    {
        return name;
    }

    public ProductName getPreviousName()
    {
        return previousName;
    }

    public void setPreviousName(final ProductName previousName)
    {
        this.previousName = previousName;
    }

    @Override
    public String toString()
    {
        return new StringJoiner(", ", ProductName.class.getSimpleName() + "[", "]")
            .add("id=" + id)
            .add("name='" + name + "'")
            .toString();
    }

}
