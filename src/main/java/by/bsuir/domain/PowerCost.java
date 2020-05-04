package by.bsuir.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PowerCost.
 */
@Entity
@Table(name = "power_cost")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "powercost")
public class PowerCost implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @DecimalMin(value = "0")
    @Column(name = "price_per_kilowatt")
    private Double pricePerKilowatt;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public PowerCost city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getPricePerKilowatt() {
        return pricePerKilowatt;
    }

    public PowerCost pricePerKilowatt(Double pricePerKilowatt) {
        this.pricePerKilowatt = pricePerKilowatt;
        return this;
    }

    public void setPricePerKilowatt(Double pricePerKilowatt) {
        this.pricePerKilowatt = pricePerKilowatt;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PowerCost)) {
            return false;
        }
        return id != null && id.equals(((PowerCost) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PowerCost{" +
            "id=" + getId() +
            ", city='" + getCity() + "'" +
            ", pricePerKilowatt=" + getPricePerKilowatt() +
            "}";
    }
}
