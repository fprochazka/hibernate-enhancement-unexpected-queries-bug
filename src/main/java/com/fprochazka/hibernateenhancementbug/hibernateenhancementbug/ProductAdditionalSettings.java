package com.fprochazka.hibernateenhancementbug.hibernateenhancementbug;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
public class ProductAdditionalSettings
{

    @Id
    @Column
    @Type(type = "pg-uuid")
    @NotNull
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    private Product product;

    @Column
    @NotNull
    private boolean flag;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    private ProductAdditionalSettings previousSettings;

    public ProductAdditionalSettings(final Product product, final boolean flag)
    {
        this.id = UUID.randomUUID();
        this.product = product;
        this.flag = flag;
    }

    protected ProductAdditionalSettings()
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

    public boolean isFlag()
    {
        return flag;
    }

    public ProductAdditionalSettings getPreviousSettings()
    {
        return previousSettings;
    }

    public void setPreviousSettings(final ProductAdditionalSettings previousSettings)
    {
        this.previousSettings = previousSettings;
    }

    @Override
    public String toString()
    {
        return new StringJoiner(", ", ProductAdditionalSettings.class.getSimpleName() + "[", "]")
            .add("id=" + id)
            .add("flag=" + flag)
            .toString();
    }

}
