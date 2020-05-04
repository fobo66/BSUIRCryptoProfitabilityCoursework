package by.bsuir.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A HardwareInfo.
 */
@Entity
@Table(name = "hardware_info")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "hardwareinfo")
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

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getHashPower() {
        return hashPower;
    }

    public void setHashPower(Double hashPower) {
        this.hashPower = hashPower;
    }

    public HardwareInfo hashPower(Double hashPower) {
        this.hashPower = hashPower;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public HardwareInfo price(Double price) {
        this.price = price;
        return this;
    }

    public Videocard getVideocard() {
        return videocard;
    }

    public void setVideocard(Videocard videocard) {
        this.videocard = videocard;
    }

    public HardwareInfo videocard(Videocard videocard) {
        this.videocard = videocard;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HardwareInfo)) {
            return false;
        }
        return id != null && id.equals(((HardwareInfo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "HardwareInfo{" +
            "id=" + getId() +
            ", hashPower=" + getHashPower() +
            ", price=" + getPrice() +
            "}";
    }
}
