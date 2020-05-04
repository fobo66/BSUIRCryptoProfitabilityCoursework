package by.bsuir.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A HardwareInfo.
 */
@Entity
@Table(name = "hardware_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "hardwareinfo")
public class HardwareInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "hash_power", nullable = false)
    private Double hashPower;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "price", nullable = false)
    private Double price;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Videocard videocard;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getHashPower() {
        return hashPower;
    }

    public HardwareInfo hashPower(Double hashPower) {
        this.hashPower = hashPower;
        return this;
    }

    public void setHashPower(Double hashPower) {
        this.hashPower = hashPower;
    }

    public Double getPrice() {
        return price;
    }

    public HardwareInfo price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Videocard getVideocard() {
        return videocard;
    }

    public HardwareInfo videocard(Videocard videocard) {
        this.videocard = videocard;
        return this;
    }

    public void setVideocard(Videocard videocard) {
        this.videocard = videocard;
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
        HardwareInfo hardwareInfo = (HardwareInfo) o;
        if (hardwareInfo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), hardwareInfo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HardwareInfo{" +
            "id=" + getId() +
            ", hashPower='" + getHashPower() + "'" +
            ", price='" + getPrice() + "'" +
            "}";
    }
}
