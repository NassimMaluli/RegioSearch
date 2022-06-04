package de.uni_marburg.sp21.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Model class for a product group (needed by Company)
 *
 * @author chris
 * @see Company
 */
public class ProductGroup implements Serializable {
    private String category;
    private Boolean rawProduct;
    private Integer producer;
    private List<String> productTags, seasons;

    /**
     * no arg constructor
     */
    public ProductGroup() {
    }

    /**
     * constructor for the product group
     * @param category the category of the product group
     * @param rawProduct if it is raw products
     * @param producer is it a producer?
     * @param productTags the tags regarding the product
     * @param seasons the seasons for the product group
     */
    public ProductGroup(String category, Boolean rawProduct, Integer producer, List<String> productTags, List<String> seasons) {
        this.category = category;
        this.rawProduct = rawProduct;
        this.producer = producer;
        this.productTags = productTags;
        this.seasons = seasons;
    }

    /**
     * category getter
     * @return the category of the product Group
     */
    public String getCategory() {
        return category;
    }

    /**
     * getter for raw product
     * @return true if it is raw product
     */
    public Boolean getRawProduct() {
        return rawProduct;
    }

    /**
     * getter for the producer
     * @return the producer of the productGroup
     */
    public Integer getProducer() {
        return producer;
    }

    /**
     * getter for the product tags
     * @return the product tags for the productGroup
     */
    public List<String> getProductTags() {
        return productTags;
    }

    /**
     * getter for the seasons
     * @return the seasons for the product group
     */
    public List<String> getSeasons() {
        return seasons;
    }

    /**
     * generates a string to represent the product group
     * @return the string representing the product group
     */
    @Override
    public String toString() {
        return "ProductGroup{" +
                "category='" + category + '\'' +
                ", rawProduct=" + rawProduct +
                ", producer=" + producer +
                ", productTags=" + productTags.toString() +
                ", seasons=" + seasons.toString() +
                '}';
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setRawProduct(Boolean rawProduct) {
        this.rawProduct = rawProduct;
    }

    public void setProducer(Integer producer) {
        this.producer = producer;
    }

    public void setProductTags(List<String> productTags) {
        this.productTags = productTags;
    }

    public void setSeasons(List<String> seasons) {
        this.seasons = seasons;
    }
}
