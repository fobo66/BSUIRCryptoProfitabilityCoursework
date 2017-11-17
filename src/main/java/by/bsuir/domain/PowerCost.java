package by.bsuir.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PowerCost.
 */
@Entity
@Table(name = "power_cost")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "powercost")
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

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public PowerCost city(String city) {
        this.city = city;
        return this;
    }

    public Double getPricePerKilowatt() {
        return pricePerKilowatt;
    }

    public void setPricePerKilowatt(Double pricePerKilowatt) {
        this.pricePerKilowatt = pricePerKilowatt;
    }

    public PowerCost pricePerKilowatt(Double pricePerKilowatt) {
        this.pricePerKilowatt = pricePerKilowatt;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PowerCost powerCost = (PowerCost) o;
        if (powerCost.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), powerCost.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PowerCost{" +
            "id=" + getId() +
            ", city='" + getCity() + "'" +
            ", pricePerKilowatt='" + getPricePerKilowatt() + "'" +
            "}";
    }
}
