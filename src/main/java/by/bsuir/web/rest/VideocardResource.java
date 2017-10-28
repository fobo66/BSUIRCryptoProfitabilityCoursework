package by.bsuir.web.rest;

import com.codahale.metrics.annotation.Timed;
import by.bsuir.domain.Videocard;

import by.bsuir.repository.VideocardRepository;
import by.bsuir.repository.search.VideocardSearchRepository;
import by.bsuir.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Videocard.
 */
@RestController
@RequestMapping("/api")
public class VideocardResource {

    private final Logger log = LoggerFactory.getLogger(VideocardResource.class);

    private static final String ENTITY_NAME = "videocard";

    private final VideocardRepository videocardRepository;

    private final VideocardSearchRepository videocardSearchRepository;

    public VideocardResource(VideocardRepository videocardRepository, VideocardSearchRepository videocardSearchRepository) {
        this.videocardRepository = videocardRepository;
        this.videocardSearchRepository = videocardSearchRepository;
    }

    /**
     * POST  /videocards : Create a new videocard.
     *
     * @param videocard the videocard to create
     * @return the ResponseEntity with status 201 (Created) and with body the new videocard, or with status 400 (Bad Request) if the videocard has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/videocards")
    @Timed
    public ResponseEntity<Videocard> createVideocard(@RequestBody Videocard videocard) throws URISyntaxException {
        log.debug("REST request to save Videocard : {}", videocard);
        if (videocard.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new videocard cannot already have an ID")).body(null);
        }
        Videocard result = videocardRepository.save(videocard);
        videocardSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/videocards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /videocards : Updates an existing videocard.
     *
     * @param videocard the videocard to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated videocard,
     * or with status 400 (Bad Request) if the videocard is not valid,
     * or with status 500 (Internal Server Error) if the videocard couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/videocards")
    @Timed
    public ResponseEntity<Videocard> updateVideocard(@RequestBody Videocard videocard) throws URISyntaxException {
        log.debug("REST request to update Videocard : {}", videocard);
        if (videocard.getId() == null) {
            return createVideocard(videocard);
        }
        Videocard result = videocardRepository.save(videocard);
        videocardSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, videocard.getId().toString()))
            .body(result);
    }

    /**
     * GET  /videocards : get all the videocards.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of videocards in body
     */
    @GetMapping("/videocards")
    @Timed
    public List<Videocard> getAllVideocards() {
        log.debug("REST request to get all Videocards");
        return videocardRepository.findAll();
    }

    /**
     * GET  /videocards/:id : get the "id" videocard.
     *
     * @param id the id of the videocard to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the videocard, or with status 404 (Not Found)
     */
    @GetMapping("/videocards/{id}")
    @Timed
    public ResponseEntity<Videocard> getVideocard(@PathVariable Long id) {
        log.debug("REST request to get Videocard : {}", id);
        Videocard videocard = videocardRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(videocard));
    }

    /**
     * DELETE  /videocards/:id : delete the "id" videocard.
     *
     * @param id the id of the videocard to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/videocards/{id}")
    @Timed
    public ResponseEntity<Void> deleteVideocard(@PathVariable Long id) {
        log.debug("REST request to delete Videocard : {}", id);
        videocardRepository.delete(id);
        videocardSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/videocards?query=:query : search for the videocard corresponding
     * to the query.
     *
     * @param query the query of the videocard search
     * @return the result of the search
     */
    @GetMapping("/_search/videocards")
    @Timed
    public List<Videocard> searchVideocards(@RequestParam String query) {
        log.debug("REST request to search Videocards for query {}", query);
        return StreamSupport
            .stream(videocardSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
