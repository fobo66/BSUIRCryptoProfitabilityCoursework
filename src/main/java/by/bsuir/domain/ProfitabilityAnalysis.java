package by.bsuir.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A ProfitabilityAnalysis.
 */
@Entity
@Table(name = "profitability_analysis")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "profitabilityanalysis")
public class ProfitabilityAnalysis implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "result", nullable = false)
    private Boolean result;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ProfitabilityAnalysis date(LocalDate date) {
        this.date = date;
        return this;
    }

    public Boolean isResult() {
        return result;
    }

    public ProfitabilityAnalysis result(Boolean result) {
        this.result = result;
        return this;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ProfitabilityAnalysis user(User user) {
        this.user = user;
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
        ProfitabilityAnalysis profitabilityAnalysis = (ProfitabilityAnalysis) o;
        if (profitabilityAnalysis.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), profitabilityAnalysis.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProfitabilityAnalysis{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", result='" + isResult() + "'" +
            "}";
    }
}
