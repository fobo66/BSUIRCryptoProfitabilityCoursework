package by.bsuir.web.rest;

import com.codahale.metrics.annotation.Timed;
import by.bsuir.domain.MiningInfo;

import by.bsuir.repository.MiningInfoRepository;
import by.bsuir.repository.search.MiningInfoSearchRepository;
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
 * REST controller for managing MiningInfo.
 */
@RestController
@RequestMapping("/api")
public class MiningInfoResource {

    private final Logger log = LoggerFactory.getLogger(MiningInfoResource.class);

    private static final String ENTITY_NAME = "miningInfo";

    private final MiningInfoRepository miningInfoRepository;

    private final MiningInfoSearchRepository miningInfoSearchRepository;
    public MiningInfoResource(MiningInfoRepository miningInfoRepository, MiningInfoSearchRepository miningInfoSearchRepository) {
        this.miningInfoRepository = miningInfoRepository;
        this.miningInfoSearchRepository = miningInfoSearchRepository;
    }

    /**
     * POST  /mining-infos : Create a new miningInfo.
     *
     * @param miningInfo the miningInfo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new miningInfo, or with status 400 (Bad Request) if the miningInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mining-infos")
    @Timed
    public ResponseEntity<MiningInfo> createMiningInfo(@Valid @RequestBody MiningInfo miningInfo) throws URISyntaxException {
        log.debug("REST request to save MiningInfo : {}", miningInfo);
        if (miningInfo.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new miningInfo cannot already have an ID")).body(null);
        }
        MiningInfo result = miningInfoRepository.save(miningInfo);
        miningInfoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/mining-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mining-infos : Updates an existing miningInfo.
     *
     * @param miningInfo the miningInfo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated miningInfo,
     * or with status 400 (Bad Request) if the miningInfo is not valid,
     * or with status 500 (Internal Server Error) if the miningInfo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mining-infos")
    @Timed
    public ResponseEntity<MiningInfo> updateMiningInfo(@Valid @RequestBody MiningInfo miningInfo) throws URISyntaxException {
        log.debug("REST request to update MiningInfo : {}", miningInfo);
        if (miningInfo.getId() == null) {
            return createMiningInfo(miningInfo);
        }
        MiningInfo result = miningInfoRepository.save(miningInfo);
        miningInfoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, miningInfo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mining-infos : get all the miningInfos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of miningInfos in body
     */
    @GetMapping("/mining-infos")
    @Timed
    public List<MiningInfo> getAllMiningInfos() {
        log.debug("REST request to get all MiningInfos");
        return miningInfoRepository.findAll();
    }

    /**
     * GET  /mining-infos/:id : get the "id" miningInfo.
     *
     * @param id the id of the miningInfo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the miningInfo, or with status 404 (Not Found)
     */
    @GetMapping("/mining-infos/{id}")
    @Timed
    public ResponseEntity<MiningInfo> getMiningInfo(@PathVariable Long id) {
        log.debug("REST request to get MiningInfo : {}", id);
        MiningInfo miningInfo = miningInfoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(miningInfo));
    }

    /**
     * DELETE  /mining-infos/:id : delete the "id" miningInfo.
     *
     * @param id the id of the miningInfo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mining-infos/{id}")
    @Timed
    public ResponseEntity<Void> deleteMiningInfo(@PathVariable Long id) {
        log.debug("REST request to delete MiningInfo : {}", id);
        miningInfoRepository.delete(id);
        miningInfoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/mining-infos?query=:query : search for the miningInfo corresponding
     * to the query.
     *
     * @param query the query of the miningInfo search
     * @return the result of the search
     */
    @GetMapping("/_search/mining-infos")
    @Timed
    public List<MiningInfo> searchMiningInfos(@RequestParam String query) {
        log.debug("REST request to search MiningInfos for query {}", query);
        return StreamSupport
            .stream(miningInfoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
