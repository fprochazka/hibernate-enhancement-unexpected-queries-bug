# Hibernate unexpected queries with "bytebuddy enhancement"

first, start the database

```bash
docker-compose up
```

this will fire up postgres in docker on port `5435`

## without enhancement

Run

```bash
mvn clean install spring-boot:run
```

and observe the following queries were executed:

```sql
select
    product0_.id as id1_0_0_,
    productnam1_.id as id1_3_1_,
    productdes2_.id as id1_2_2_,
    product0_.created_time as created_2_0_0_,
    product0_.description_id as descript3_0_0_,
    product0_.name_id as name_id4_0_0_,
    productnam1_.name as name2_3_1_,
    productnam1_.previous_name_id as previous3_3_1_,
    productnam1_.product_id as product_4_3_1_,
    productdes2_.description as descript2_2_2_,
    productdes2_.previous_description_id as previous3_2_2_,
    productdes2_.product_id as product_4_2_2_ 
from
    product product0_ 
left outer join
    product_name productnam1_ 
        on product0_.name_id=productnam1_.id 
left outer join
    product_description productdes2_ 
        on product0_.description_id=productdes2_.id;

select
    productadd0_.id as id1_1_0_,
    productadd0_.flag as flag2_1_0_,
    productadd0_.previous_settings_id as previous3_1_0_,
    productadd0_.product_id as product_4_1_0_ 
from
    product_additional_settings productadd0_ 
where
    productadd0_.product_id=?;

select
    productadd0_.id as id1_1_0_,
    productadd0_.flag as flag2_1_0_,
    productadd0_.previous_settings_id as previous3_1_0_,
    productadd0_.product_id as product_4_1_0_ 
from
    product_additional_settings productadd0_ 
where
    productadd0_.product_id=?;

select
    productadd0_.id as id1_1_0_,
    productadd0_.flag as flag2_1_0_,
    productadd0_.previous_settings_id as previous3_1_0_,
    productadd0_.product_id as product_4_1_0_ 
from
    product_additional_settings productadd0_ 
where
    productadd0_.product_id=?;
```

### Conclusion

* As expected, first query selects products.
* But then there are additional three queries that are unwanted, but expected because hibernate cannot handle inverse-side toOne relations without extra queries.
* To fix the extra queries because of "inverse-side toOne", let's use the `hibernate-enhance-maven-plugin`

## with enhancement

Uncomment `hibernate-enhance-maven-plugin` in `pom.xml`, and run

```bash
mvn clean install spring-boot:run
```

and observe the following queries

```sql
select
    product0_.id as id1_0_0_,
    productnam1_.id as id1_3_1_,
    productdes2_.id as id1_2_2_,
    product0_.created_time as created_2_0_0_,
    product0_.description_id as descript3_0_0_,
    product0_.name_id as name_id4_0_0_,
    productnam1_.name as name2_3_1_,
    productnam1_.previous_name_id as previous3_3_1_,
    productnam1_.product_id as product_4_3_1_,
    productdes2_.description as descript2_2_2_,
    productdes2_.previous_description_id as previous3_2_2_,
    productdes2_.product_id as product_4_2_2_ 
from
    product product0_ 
left outer join
    product_name productnam1_ 
        on product0_.name_id=productnam1_.id 
left outer join
    product_description productdes2_ 
        on product0_.description_id=productdes2_.id;

select
    productnam0_.id as id1_3_0_,
    productnam0_.name as name2_3_0_,
    productnam0_.previous_name_id as previous3_3_0_,
    productnam0_.product_id as product_4_3_0_ 
from
    product_name productnam0_ 
where
    productnam0_.id=?;

select
    productnam0_.id as id1_3_0_,
    productnam0_.name as name2_3_0_,
    productnam0_.previous_name_id as previous3_3_0_,
    productnam0_.product_id as product_4_3_0_ 
from
    product_name productnam0_ 
where
    productnam0_.id=?;

select
    productdes0_.id as id1_2_0_,
    productdes0_.description as descript2_2_0_,
    productdes0_.previous_description_id as previous3_2_0_,
    productdes0_.product_id as product_4_2_0_ 
from
    product_description productdes0_ 
where
    productdes0_.id=?;
```

### Conclusion

* As expected, first query selects products.
* As expected, the three queries for the `product_additional_settings` are gone, because the application code doesn't touch them and Hibernate correctly doesn't load them. 
* But out of nowhere, there are now new unexpected queries, that weren't there before. - **this is a problem**
