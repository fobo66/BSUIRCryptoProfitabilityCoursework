package by.bsuir.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A MiningInfo.
 */
@Entity
@Table(name = "mining_info")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "mininginfo")
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

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
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
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MiningInfo)) {
            return false;
        }
        return id != null && id.equals(((MiningInfo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MiningInfo{" +
            "id=" + getId() +
            ", difficulty=" + getDifficulty() +
            ", blockReward=" + getBlockReward() +
            "}";
    }
}
