package by.bsuir.web.rest;

import by.bsuir.domain.Videocard;
import by.bsuir.repository.VideocardRepository;
import by.bsuir.repository.search.VideocardSearchRepository;
import by.bsuir.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link by.bsuir.domain.Videocard}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VideocardResource {

    private final Logger log = LoggerFactory.getLogger(VideocardResource.class);

    private static final String ENTITY_NAME = "videocard";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VideocardRepository videocardRepository;

    private final VideocardSearchRepository videocardSearchRepository;

    public VideocardResource(VideocardRepository videocardRepository, VideocardSearchRepository videocardSearchRepository) {
        this.videocardRepository = videocardRepository;
        this.videocardSearchRepository = videocardSearchRepository;
    }

    /**
     * {@code POST  /videocards} : Create a new videocard.
     *
     * @param videocard the videocard to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new videocard, or with status {@code 400 (Bad Request)} if the videocard has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/videocards")
    public ResponseEntity<Videocard> createVideocard(@RequestBody Videocard videocard) throws URISyntaxException {
        log.debug("REST request to save Videocard : {}", videocard);
        if (videocard.getId() != null) {
            throw new BadRequestAlertException("A new videocard cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Videocard result = videocardRepository.save(videocard);
        videocardSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/videocards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /videocards} : Updates an existing videocard.
     *
     * @param videocard the videocard to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated videocard,
     * or with status {@code 400 (Bad Request)} if the videocard is not valid,
     * or with status {@code 500 (Internal Server Error)} if the videocard couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/videocards")
    public ResponseEntity<Videocard> updateVideocard(@RequestBody Videocard videocard) throws URISyntaxException {
        log.debug("REST request to update Videocard : {}", videocard);
        if (videocard.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Videocard result = videocardRepository.save(videocard);
        videocardSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, videocard.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /videocards} : get all the videocards.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of videocards in body.
     */
    @GetMapping("/videocards")
    public List<Videocard> getAllVideocards() {
        log.debug("REST request to get all Videocards");
        return videocardRepository.findAll();
    }

    /**
     * {@code GET  /videocards/:id} : get the "id" videocard.
     *
     * @param id the id of the videocard to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the videocard, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/videocards/{id}")
    public ResponseEntity<Videocard> getVideocard(@PathVariable Long id) {
        log.debug("REST request to get Videocard : {}", id);
        Optional<Videocard> videocard = videocardRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(videocard);
    }

    /**
     * {@code DELETE  /videocards/:id} : delete the "id" videocard.
     *
     * @param id the id of the videocard to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/videocards/{id}")
    public ResponseEntity<Void> deleteVideocard(@PathVariable Long id) {
        log.debug("REST request to delete Videocard : {}", id);
        videocardRepository.deleteById(id);
        videocardSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/videocards?query=:query} : search for the videocard corresponding
     * to the query.
     *
     * @param query the query of the videocard search.
     * @return the result of the search.
     */
    @GetMapping("/_search/videocards")
    public List<Videocard> searchVideocards(@RequestParam String query) {
        log.debug("REST request to search Videocards for query {}", query);
        return StreamSupport
            .stream(videocardSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
