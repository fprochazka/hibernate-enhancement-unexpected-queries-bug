package com.fprochazka.hibernateenhancementbug.hibernateenhancementbug;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.*;

@Entity
public class Product
{

    @Id
    @Column
    @NotNull
    @Type(type = "pg-uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    private ProductName name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    private ProductDescription description;

    @OneToOne(mappedBy = "product", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private ProductAdditionalSettings additionalSettings;

    @Column
    @NotNull
    private LocalDateTime createdTime;

    public Product(final Clock clock)
    {
        this.id = UUID.randomUUID();
        this.createdTime = LocalDateTime.now(clock);
    }

    protected Product()
    {
    }

    public UUID getId()
    {
        return id;
    }

    public LocalDateTime getCreatedTime()
    {
        return createdTime;
    }

    public ProductName getName()
    {
        return name;
    }

    public void setName(final ProductName name)
    {
        if (this.name != null) {
            name.setPreviousName(this.name);
        }
        this.name = name;
    }

    public ProductDescription getDescription()
    {
        return description;
    }

    public void setDescription(final ProductDescription description)
    {
        if (this.description != null) {
            description.setPreviousDescription(this.description);
        }
        this.description = description;
    }

    public ProductAdditionalSettings getAdditionalSettings()
    {
        return additionalSettings;
    }

    public void setAdditionalSettings(final ProductAdditionalSettings additionalSettings)
    {
        if (this.additionalSettings != null) {
            additionalSettings.setPreviousSettings(this.additionalSettings);
        }
        this.additionalSettings = additionalSettings;
    }

    @Override
    public String toString()
    {
        return new StringJoiner(", ", Product.class.getSimpleName() + "[", "]")
            .add("id=" + id)
            .add("name=" + Optional.ofNullable(name).map(ProductName::getName).orElse(null))
            .add("description=" + Optional.ofNullable(description).map(ProductDescription::getDescription).orElse(null))
            .add("createdTime=" + createdTime)
            .toString();
    }

}
