package com.example.nikhil.eatnjoy.model;

/**
 * Created by HP 840 G1 ULTRABOOK on 7/6/2018.
 */

public class Bean
{
    String name;
    String url;
    String price;
    String description;
    String quantity;
    String id;


    public Bean()
    {

    }

    public Bean(String name,String url,String price,String description,String quantity)
    {
        this.name = name;
        this.url = url;
        this.price=price;
        this.description=description;
        this.quantity=quantity;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
}
