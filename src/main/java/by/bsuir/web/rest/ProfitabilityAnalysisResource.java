package by.bsuir.web.rest;

import com.codahale.metrics.annotation.Timed;
import by.bsuir.domain.ProfitabilityAnalysis;

import by.bsuir.repository.ProfitabilityAnalysisRepository;
import by.bsuir.repository.search.ProfitabilityAnalysisSearchRepository;
import by.bsuir.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ProfitabilityAnalysis.
 */
@RestController
@RequestMapping("/api")
public class ProfitabilityAnalysisResource {

    private final Logger log = LoggerFactory.getLogger(ProfitabilityAnalysisResource.class);

    private static final String ENTITY_NAME = "profitabilityAnalysis";

    private final ProfitabilityAnalysisRepository profitabilityAnalysisRepository;

    private final ProfitabilityAnalysisSearchRepository profitabilityAnalysisSearchRepository;
    public ProfitabilityAnalysisResource(ProfitabilityAnalysisRepository profitabilityAnalysisRepository, ProfitabilityAnalysisSearchRepository profitabilityAnalysisSearchRepository) {
        this.profitabilityAnalysisRepository = profitabilityAnalysisRepository;
        this.profitabilityAnalysisSearchRepository = profitabilityAnalysisSearchRepository;
    }

    /**
     * POST  /profitability-analyses : Create a new profitabilityAnalysis.
     *
     * @param profitabilityAnalysis the profitabilityAnalysis to create
     * @return the ResponseEntity with status 201 (Created) and with body the new profitabilityAnalysis, or with status 400 (Bad Request) if the profitabilityAnalysis has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/profitability-analyses")
    @Timed
    public ResponseEntity<ProfitabilityAnalysis> createProfitabilityAnalysis(@Valid @RequestBody ProfitabilityAnalysis profitabilityAnalysis) throws URISyntaxException {
        log.debug("REST request to save ProfitabilityAnalysis : {}", profitabilityAnalysis);
        if (profitabilityAnalysis.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new profitabilityAnalysis cannot already have an ID")).body(null);
        }
        ProfitabilityAnalysis result = profitabilityAnalysisRepository.save(profitabilityAnalysis);
        profitabilityAnalysisSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/profitability-analyses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /profitability-analyses : Updates an existing profitabilityAnalysis.
     *
     * @param profitabilityAnalysis the profitabilityAnalysis to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated profitabilityAnalysis,
     * or with status 400 (Bad Request) if the profitabilityAnalysis is not valid,
     * or with status 500 (Internal Server Error) if the profitabilityAnalysis couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/profitability-analyses")
    @Timed
    public ResponseEntity<ProfitabilityAnalysis> updateProfitabilityAnalysis(@Valid @RequestBody ProfitabilityAnalysis profitabilityAnalysis) throws URISyntaxException {
        log.debug("REST request to update ProfitabilityAnalysis : {}", profitabilityAnalysis);
        if (profitabilityAnalysis.getId() == null) {
            return createProfitabilityAnalysis(profitabilityAnalysis);
        }
        ProfitabilityAnalysis result = profitabilityAnalysisRepository.save(profitabilityAnalysis);
        profitabilityAnalysisSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, profitabilityAnalysis.getId().toString()))
            .body(result);
    }

    /**
     * GET  /profitability-analyses : get all the profitabilityAnalyses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of profitabilityAnalyses in body
     */
    @GetMapping("/profitability-analyses")
    @Timed
    public List<ProfitabilityAnalysis> getAllProfitabilityAnalyses() {
        log.debug("REST request to get all ProfitabilityAnalyses");
        return profitabilityAnalysisRepository.findAll();
        }

    /**
     * GET  /profitability-analyses/:id : get the "id" profitabilityAnalysis.
     *
     * @param id the id of the profitabilityAnalysis to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the profitabilityAnalysis, or with status 404 (Not Found)
     */
    @GetMapping("/profitability-analyses/{id}")
    @Timed
    public ResponseEntity<ProfitabilityAnalysis> getProfitabilityAnalysis(@PathVariable Long id) {
        log.debug("REST request to get ProfitabilityAnalysis : {}", id);
        ProfitabilityAnalysis profitabilityAnalysis = profitabilityAnalysisRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(profitabilityAnalysis));
    }

    /**
     * DELETE  /profitability-analyses/:id : delete the "id" profitabilityAnalysis.
     *
     * @param id the id of the profitabilityAnalysis to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/profitability-analyses/{id}")
    @Timed
    public ResponseEntity<Void> deleteProfitabilityAnalysis(@PathVariable Long id) {
        log.debug("REST request to delete ProfitabilityAnalysis : {}", id);
        profitabilityAnalysisRepository.delete(id);
        profitabilityAnalysisSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/profitability-analyses?query=:query : search for the profitabilityAnalysis corresponding
     * to the query.
     *
     * @param query the query of the profitabilityAnalysis search
     * @return the result of the search
     */
    @GetMapping("/_search/profitability-analyses")
    @Timed
    public List<ProfitabilityAnalysis> searchProfitabilityAnalyses(@RequestParam String query) {
        log.debug("REST request to search ProfitabilityAnalyses for query {}", query);
        return StreamSupport
            .stream(profitabilityAnalysisSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
