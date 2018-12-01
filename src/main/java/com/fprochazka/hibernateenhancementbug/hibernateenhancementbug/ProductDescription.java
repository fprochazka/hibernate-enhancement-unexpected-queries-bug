package com.fprochazka.hibernateenhancementbug.hibernateenhancementbug;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
public class ProductDescription
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
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    private ProductDescription previousDescription;

    public ProductDescription(final Product product, final String description)
    {
        this.id = UUID.randomUUID();
        this.product = product;
        this.description = description;
    }

    protected ProductDescription()
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

    public String getDescription()
    {
        return description;
    }

    public ProductDescription getPreviousDescription()
    {
        return previousDescription;
    }

    public void setPreviousDescription(final ProductDescription previousDescription)
    {
        this.previousDescription = previousDescription;
    }

    @Override
    public String toString()
    {
        return new StringJoiner(", ", ProductDescription.class.getSimpleName() + "[", "]")
            .add("id=" + id)
            .add("description='" + description + "'")
            .toString();
    }

}
