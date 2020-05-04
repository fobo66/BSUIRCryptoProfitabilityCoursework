package by.bsuir.web.rest;

import by.bsuir.domain.MiningInfo;
import by.bsuir.repository.MiningInfoRepository;
import by.bsuir.repository.search.MiningInfoSearchRepository;
import by.bsuir.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
 * REST controller for managing {@link by.bsuir.domain.MiningInfo}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MiningInfoResource {

    private final Logger log = LoggerFactory.getLogger(MiningInfoResource.class);

    private static final String ENTITY_NAME = "miningInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MiningInfoRepository miningInfoRepository;

    private final MiningInfoSearchRepository miningInfoSearchRepository;

    public MiningInfoResource(MiningInfoRepository miningInfoRepository, MiningInfoSearchRepository miningInfoSearchRepository) {
        this.miningInfoRepository = miningInfoRepository;
        this.miningInfoSearchRepository = miningInfoSearchRepository;
    }

    /**
     * {@code POST  /mining-infos} : Create a new miningInfo.
     *
     * @param miningInfo the miningInfo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new miningInfo, or with status {@code 400 (Bad Request)} if the miningInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mining-infos")
    public ResponseEntity<MiningInfo> createMiningInfo(@Valid @RequestBody MiningInfo miningInfo) throws URISyntaxException {
        log.debug("REST request to save MiningInfo : {}", miningInfo);
        if (miningInfo.getId() != null) {
            throw new BadRequestAlertException("A new miningInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MiningInfo result = miningInfoRepository.save(miningInfo);
        miningInfoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/mining-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mining-infos} : Updates an existing miningInfo.
     *
     * @param miningInfo the miningInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated miningInfo,
     * or with status {@code 400 (Bad Request)} if the miningInfo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the miningInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mining-infos")
    public ResponseEntity<MiningInfo> updateMiningInfo(@Valid @RequestBody MiningInfo miningInfo) throws URISyntaxException {
        log.debug("REST request to update MiningInfo : {}", miningInfo);
        if (miningInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MiningInfo result = miningInfoRepository.save(miningInfo);
        miningInfoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, miningInfo.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /mining-infos} : get all the miningInfos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of miningInfos in body.
     */
    @GetMapping("/mining-infos")
    public List<MiningInfo> getAllMiningInfos() {
        log.debug("REST request to get all MiningInfos");
        return miningInfoRepository.findAll();
    }

    /**
     * {@code GET  /mining-infos/:id} : get the "id" miningInfo.
     *
     * @param id the id of the miningInfo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the miningInfo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mining-infos/{id}")
    public ResponseEntity<MiningInfo> getMiningInfo(@PathVariable Long id) {
        log.debug("REST request to get MiningInfo : {}", id);
        Optional<MiningInfo> miningInfo = miningInfoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(miningInfo);
    }

    /**
     * {@code DELETE  /mining-infos/:id} : delete the "id" miningInfo.
     *
     * @param id the id of the miningInfo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mining-infos/{id}")
    public ResponseEntity<Void> deleteMiningInfo(@PathVariable Long id) {
        log.debug("REST request to delete MiningInfo : {}", id);
        miningInfoRepository.deleteById(id);
        miningInfoSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/mining-infos?query=:query} : search for the miningInfo corresponding
     * to the query.
     *
     * @param query the query of the miningInfo search.
     * @return the result of the search.
     */
    @GetMapping("/_search/mining-infos")
    public List<MiningInfo> searchMiningInfos(@RequestParam String query) {
        log.debug("REST request to search MiningInfos for query {}", query);
        return StreamSupport
            .stream(miningInfoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
