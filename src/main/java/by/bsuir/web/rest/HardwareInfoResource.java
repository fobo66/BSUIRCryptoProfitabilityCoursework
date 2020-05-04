package by.bsuir.web.rest;

import by.bsuir.domain.HardwareInfo;
import by.bsuir.repository.HardwareInfoRepository;
import by.bsuir.repository.search.HardwareInfoSearchRepository;
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
 * REST controller for managing {@link by.bsuir.domain.HardwareInfo}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class HardwareInfoResource {

    private final Logger log = LoggerFactory.getLogger(HardwareInfoResource.class);

    private static final String ENTITY_NAME = "hardwareInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HardwareInfoRepository hardwareInfoRepository;

    private final HardwareInfoSearchRepository hardwareInfoSearchRepository;

    public HardwareInfoResource(HardwareInfoRepository hardwareInfoRepository, HardwareInfoSearchRepository hardwareInfoSearchRepository) {
        this.hardwareInfoRepository = hardwareInfoRepository;
        this.hardwareInfoSearchRepository = hardwareInfoSearchRepository;
    }

    /**
     * {@code POST  /hardware-infos} : Create a new hardwareInfo.
     *
     * @param hardwareInfo the hardwareInfo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hardwareInfo, or with status {@code 400 (Bad Request)} if the hardwareInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/hardware-infos")
    public ResponseEntity<HardwareInfo> createHardwareInfo(@Valid @RequestBody HardwareInfo hardwareInfo) throws URISyntaxException {
        log.debug("REST request to save HardwareInfo : {}", hardwareInfo);
        if (hardwareInfo.getId() != null) {
            throw new BadRequestAlertException("A new hardwareInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HardwareInfo result = hardwareInfoRepository.save(hardwareInfo);
        hardwareInfoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/hardware-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /hardware-infos} : Updates an existing hardwareInfo.
     *
     * @param hardwareInfo the hardwareInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hardwareInfo,
     * or with status {@code 400 (Bad Request)} if the hardwareInfo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hardwareInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hardware-infos")
    public ResponseEntity<HardwareInfo> updateHardwareInfo(@Valid @RequestBody HardwareInfo hardwareInfo) throws URISyntaxException {
        log.debug("REST request to update HardwareInfo : {}", hardwareInfo);
        if (hardwareInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HardwareInfo result = hardwareInfoRepository.save(hardwareInfo);
        hardwareInfoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hardwareInfo.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /hardware-infos} : get all the hardwareInfos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hardwareInfos in body.
     */
    @GetMapping("/hardware-infos")
    public List<HardwareInfo> getAllHardwareInfos() {
        log.debug("REST request to get all HardwareInfos");
        return hardwareInfoRepository.findAll();
    }

    /**
     * {@code GET  /hardware-infos/:id} : get the "id" hardwareInfo.
     *
     * @param id the id of the hardwareInfo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hardwareInfo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hardware-infos/{id}")
    public ResponseEntity<HardwareInfo> getHardwareInfo(@PathVariable Long id) {
        log.debug("REST request to get HardwareInfo : {}", id);
        Optional<HardwareInfo> hardwareInfo = hardwareInfoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(hardwareInfo);
    }

    /**
     * {@code DELETE  /hardware-infos/:id} : delete the "id" hardwareInfo.
     *
     * @param id the id of the hardwareInfo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hardware-infos/{id}")
    public ResponseEntity<Void> deleteHardwareInfo(@PathVariable Long id) {
        log.debug("REST request to delete HardwareInfo : {}", id);
        hardwareInfoRepository.deleteById(id);
        hardwareInfoSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/hardware-infos?query=:query} : search for the hardwareInfo corresponding
     * to the query.
     *
     * @param query the query of the hardwareInfo search.
     * @return the result of the search.
     */
    @GetMapping("/_search/hardware-infos")
    public List<HardwareInfo> searchHardwareInfos(@RequestParam String query) {
        log.debug("REST request to search HardwareInfos for query {}", query);
        return StreamSupport
            .stream(hardwareInfoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
