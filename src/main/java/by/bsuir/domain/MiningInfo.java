package by.bsuir.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A MiningInfo.
 */
@Entity
@Table(name = "mining_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "mininginfo")
public class MiningInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "difficulty", nullable = false)
    private Float difficulty;

    @NotNull
    @Min(value = 0)
    @Column(name = "block_reward", nullable = false)
    private Integer blockReward;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Cryptocurrency cryptocurrency;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getDifficulty() {
        return difficulty;
    }

    public MiningInfo difficulty(Float difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public void setDifficulty(Float difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getBlockReward() {
        return blockReward;
    }

    public MiningInfo blockReward(Integer blockReward) {
        this.blockReward = blockReward;
        return this;
    }

    public void setBlockReward(Integer blockReward) {
        this.blockReward = blockReward;
    }

    public Cryptocurrency getCryptocurrency() {
        return cryptocurrency;
    }

    public MiningInfo cryptocurrency(Cryptocurrency cryptocurrency) {
        this.cryptocurrency = cryptocurrency;
        return this;
    }

    public void setCryptocurrency(Cryptocurrency cryptocurrency) {
        this.cryptocurrency = cryptocurrency;
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
        MiningInfo miningInfo = (MiningInfo) o;
        if (miningInfo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), miningInfo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MiningInfo{" +
            "id=" + getId() +
            ", difficulty='" + getDifficulty() + "'" +
            ", blockReward='" + getBlockReward() + "'" +
            "}";
    }
}
