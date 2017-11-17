package by.bsuir.web.rest;

import com.codahale.metrics.annotation.Timed;
import by.bsuir.domain.HardwareInfo;

import by.bsuir.repository.HardwareInfoRepository;
import by.bsuir.repository.search.HardwareInfoSearchRepository;
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
 * REST controller for managing HardwareInfo.
 */
@RestController
@RequestMapping("/api")
public class HardwareInfoResource {

    private static final String ENTITY_NAME = "hardwareInfo";
    private final Logger log = LoggerFactory.getLogger(HardwareInfoResource.class);
    private final HardwareInfoRepository hardwareInfoRepository;

    private final HardwareInfoSearchRepository hardwareInfoSearchRepository;

    public HardwareInfoResource(HardwareInfoRepository hardwareInfoRepository, HardwareInfoSearchRepository hardwareInfoSearchRepository) {
        this.hardwareInfoRepository = hardwareInfoRepository;
        this.hardwareInfoSearchRepository = hardwareInfoSearchRepository;
    }

    /**
     * POST  /hardware-infos : Create a new hardwareInfo.
     *
     * @param hardwareInfo the hardwareInfo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hardwareInfo, or with status 400 (Bad Request) if the hardwareInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/hardware-infos")
    @Timed
    public ResponseEntity<HardwareInfo> createHardwareInfo(@Valid @RequestBody HardwareInfo hardwareInfo) throws URISyntaxException {
        log.debug("REST request to save HardwareInfo : {}", hardwareInfo);
        if (hardwareInfo.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new hardwareInfo cannot already have an ID")).body(null);
        }
        HardwareInfo result = hardwareInfoRepository.save(hardwareInfo);
        hardwareInfoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/hardware-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hardware-infos : Updates an existing hardwareInfo.
     *
     * @param hardwareInfo the hardwareInfo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hardwareInfo,
     * or with status 400 (Bad Request) if the hardwareInfo is not valid,
     * or with status 500 (Internal Server Error) if the hardwareInfo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/hardware-infos")
    @Timed
    public ResponseEntity<HardwareInfo> updateHardwareInfo(@Valid @RequestBody HardwareInfo hardwareInfo) throws URISyntaxException {
        log.debug("REST request to update HardwareInfo : {}", hardwareInfo);
        if (hardwareInfo.getId() == null) {
            return createHardwareInfo(hardwareInfo);
        }
        HardwareInfo result = hardwareInfoRepository.save(hardwareInfo);
        hardwareInfoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, hardwareInfo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hardware-infos : get all the hardwareInfos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of hardwareInfos in body
     */
    @GetMapping("/hardware-infos")
    @Timed
    public List<HardwareInfo> getAllHardwareInfos() {
        log.debug("REST request to get all HardwareInfos");
        return hardwareInfoRepository.findAll();
    }

    /**
     * GET  /hardware-infos/:id : get the "id" hardwareInfo.
     *
     * @param id the id of the hardwareInfo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hardwareInfo, or with status 404 (Not Found)
     */
    @GetMapping("/hardware-infos/{id}")
    @Timed
    public ResponseEntity<HardwareInfo> getHardwareInfo(@PathVariable Long id) {
        log.debug("REST request to get HardwareInfo : {}", id);
        HardwareInfo hardwareInfo = hardwareInfoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(hardwareInfo));
    }

    /**
     * DELETE  /hardware-infos/:id : delete the "id" hardwareInfo.
     *
     * @param id the id of the hardwareInfo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/hardware-infos/{id}")
    @Timed
    public ResponseEntity<Void> deleteHardwareInfo(@PathVariable Long id) {
        log.debug("REST request to delete HardwareInfo : {}", id);
        hardwareInfoRepository.delete(id);
        hardwareInfoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/hardware-infos?query=:query : search for the hardwareInfo corresponding
     * to the query.
     *
     * @param query the query of the hardwareInfo search
     * @return the result of the search
     */
    @GetMapping("/_search/hardware-infos")
    @Timed
    public List<HardwareInfo> searchHardwareInfos(@RequestParam String query) {
        log.debug("REST request to search HardwareInfos for query {}", query);
        return StreamSupport
            .stream(hardwareInfoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
