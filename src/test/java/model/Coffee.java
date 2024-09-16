package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Coffee {
    private Integer id;
    private String brand;
    private String description;
    @JsonProperty("origin_country")
    private String originCountry;
    @JsonProperty("roast_level")
    private String roastLevel;
    @JsonProperty("bean_type")
    private String beanType;
    private String weight;
    @JsonProperty("flavor_notes")
    private List<String> flavorNotes;
    @JsonProperty("grind_type")
    private String grindType;
    private Double price;
    @JsonProperty("in_stock")
    private Boolean inStock;

    public Integer getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getDescription() {
        return description;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public String getRoastLevel() {
        return roastLevel;
    }

    public String getBeanType() {
        return beanType;
    }

    public String getWeight() {
        return weight;
    }

    public List<String> getFlavorNotes() {
        return flavorNotes;
    }

    public String getGrindType() {
        return grindType;
    }

    public Double getPrice() {
        return price;
    }

    public Boolean getInStock() {
        return inStock;
    }
}