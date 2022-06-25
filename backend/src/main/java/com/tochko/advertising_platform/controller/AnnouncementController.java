package com.tochko.advertising_platform.controller;

import com.tochko.advertising_platform.controller.details.AnnouncementChartDetails;
import com.tochko.advertising_platform.controller.details.AnnouncementRatingDetails;
import com.tochko.advertising_platform.model.Announcement;
import com.tochko.advertising_platform.model.enums.AnnouncementStatus;
import com.tochko.advertising_platform.model.enums.AnnouncementType;
import com.tochko.advertising_platform.service.AnnouncementService;
import com.tochko.advertising_platform.service.FavoriteService;
import com.tochko.advertising_platform.service.impl.FavoriteServiceImpl;
import com.tochko.advertising_platform.service.search_criteries.AnnouncementSearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/announcement")
public class AnnouncementController {

    private final AnnouncementService announcementService;
    private final FavoriteService favoriteService;

    @Autowired
    public AnnouncementController(AnnouncementService announcementService,
                                  FavoriteServiceImpl favoriteService) {
        this.announcementService = announcementService;
        this.favoriteService = favoriteService;
    }

    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> pageAnnouncementsByUsername(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "4") int size,
            @RequestParam(required = false) AnnouncementType type,
            @RequestParam(required = false) String username,
            @RequestParam(required = false, defaultValue = "id", name = "sortField") String sortField,
            @RequestParam(required = false, defaultValue = "ASC", name = "sortDir") String sortDir) {
        try {
            Pageable paging = PageRequest.of(page - 1, size);

            List<Announcement> listAnn;
            Page<Announcement> pageAnn;
            pageAnn = announcementService.getAnnouncementsByUsername(username, paging);

            listAnn = pageAnn.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("announcements", listAnn);
            response.put("currentPage", pageAnn.getNumber());
            response.put("totalItems", pageAnn.getTotalElements());
            response.put("totalPages", pageAnn.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    //for graphics on adminPage
    @GetMapping("")
    public ResponseEntity<List<AnnouncementChartDetails>> list() {
        try {
            List<AnnouncementChartDetails> listAnn = announcementService.getAll();
            return new ResponseEntity<>(listAnn, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/adv")
    public ResponseEntity<Map<String, Object>> advertisingPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "4") int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) AnnouncementStatus status,
            @RequestParam(required = false, defaultValue = "id", name = "sortField") String sortField,
            @RequestParam(required = false, defaultValue = "ASC", name = "sortDir") String sortDir) {
        try {
            Pageable paging = PageRequest.of(page - 1, size);
            List<Announcement> listAnn;
            Page<Announcement> pageAnn;

            if (username == null) {
                pageAnn = announcementService.getByType(AnnouncementType.ADVERTISING, status, paging);
            } else {
                pageAnn = announcementService.getByTypeAndUsername(AnnouncementType.ADVERTISING, status, username, paging);
            }

            listAnn = pageAnn.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("announcements", listAnn);
            response.put("currentPage", pageAnn.getNumber());
            response.put("totalItems", pageAnn.getTotalElements());
            response.put("totalPages", pageAnn.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/plt")
    public ResponseEntity<Map<String, Object>> platformPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "4") int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) AnnouncementStatus status,
            @RequestParam(required = false, defaultValue = "id", name = "sortField") String sortField,
            @RequestParam(required = false, defaultValue = "ASC", name = "sortDir") String sortDir) {
        try {
            Pageable paging = PageRequest.of(page - 1, size);
            List<Announcement> listAnn;
            Page<Announcement> pageAnn;

            if (username == null) {
                pageAnn = announcementService.getByType(AnnouncementType.PLATFORM, status, paging);

            } else {
                pageAnn = announcementService.getByTypeAndUsername(AnnouncementType.PLATFORM, status, username, paging);
            }

            listAnn = pageAnn.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("announcements", listAnn);
            response.put("currentPage", pageAnn.getNumber());
            response.put("totalItems", pageAnn.getTotalElements());
            response.put("totalPages", pageAnn.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Announcement> get(@PathVariable("id") Long id,
                                            @RequestParam(required = false) AnnouncementType type) {
        try {
            Announcement announcement = announcementService.get(id);
            return new ResponseEntity<>(announcement, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Announcement> delete(@PathVariable("id") Long id) {
        try {
            announcementService.delete(id);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Announcement> update(
            @PathVariable("id") Long id,
            @RequestBody Announcement ann) {
        try {
            announcementService.update(id, ann);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> add(
            @RequestBody Announcement announcement) {
        try {
            Announcement savedAnn = announcementService.add(announcement);
            return new ResponseEntity<>(savedAnn, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> search(
            @RequestParam(required = false) Optional<AnnouncementType> type,
            @RequestParam(required = false) Optional<String> title,
            @RequestParam(required = false) Optional<String> brand,
            @RequestParam(required = false) Optional<String> country,
            @RequestParam(required = false) Optional<String> periodValue1,
            @RequestParam(required = false) Optional<String> periodValue2,
            @RequestParam(required = false) Optional<String> priceValue1,
            @RequestParam(required = false) Optional<String> priceValue2,
            @RequestParam(required = false) Optional<String> followersValue1,
            @RequestParam(required = false) Optional<String> followersValue2,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "3", required = false) int size) {
        try {
            List<Announcement> listAnn;
            Page<Announcement> pageAnn;
            AnnouncementSearchCriteria searchCriteria = AnnouncementSearchCriteria.builder()
                    .title(title)
                    .brand(brand)
                    .country(country)
                    .periodValue1(periodValue1)
                    .periodValue2(periodValue2)
                    .priceValue1(priceValue1)
                    .priceValue2(priceValue2)
                    .followersValue1(followersValue1)
                    .followersValue2(followersValue2)
                    .build();

            List<Announcement> listAnnouncements = announcementService.getAnnouncementWithCriteria(searchCriteria);
            if (type.get() == AnnouncementType.ADVERTISING) {
                listAnn = listAnnouncements
                        .stream()
                        .filter(r -> r.getType().equals(AnnouncementType.ADVERTISING))
                        .collect(Collectors.toList());
            } else {
                listAnn = listAnnouncements
                        .stream()
                        .filter(r -> r.getType().equals(AnnouncementType.PLATFORM))
                        .collect(Collectors.toList());
            }
            pageAnn = new PageImpl<>(listAnn);

            Map<String, Object> response = new HashMap<>();
            response.put("announcements", pageAnn.getContent());
            response.put("currentPage", pageAnn.getNumber());
            response.put("totalItems", pageAnn.getTotalElements());
            response.put("totalPages", pageAnn.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/favorite/add")
    public ResponseEntity<?> addAnnouncementToFavorite(@RequestParam("userId") Long userId,
                                                       @RequestParam("announcementId") Long announcementId) {
        try {
            favoriteService.addAnnouncementToFavorite(userId, announcementId);
            return new ResponseEntity<>(null, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/favorite/all")
    public ResponseEntity<Map<String, Object>> getFavoritesByUser(@RequestParam("userId") Long userId,
                                                                  @RequestParam(defaultValue = "1", required = false) int page,
                                                                  @RequestParam(defaultValue = "3", required = false) int size) {
        try {
            Pageable paging = PageRequest.of(page - 1, size);
            Page<Announcement> pageAnn = favoriteService.findFavoriteAnnouncementsByUserId(userId, paging);

            Map<String, Object> response = new HashMap<>();
            response.put("announcements", pageAnn.getContent());
            response.put("currentPage", pageAnn.getNumber());
            response.put("totalItems", pageAnn.getTotalElements());
            response.put("totalPages", pageAnn.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/favorite/delete")
    public ResponseEntity<Announcement> deleteAnnouncementFromFavorite(@RequestParam("userId") Long userId,
                                                                       @RequestParam("annId") Long annId) {
        try {
            favoriteService.delete(userId, annId);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/status")
    public ResponseEntity<?> changeAnnouncementStatus(@RequestBody AnnouncementStatus status,
                                                      @RequestParam("announcementId") Long announcementId) {
        try {
            announcementService.changeAnnouncementStatus(announcementId, status);
            return new ResponseEntity<>(null, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/rating/getleaders")
    public ResponseEntity<List<AnnouncementRatingDetails>> getFirstTenAnnouncementsInRating() {
        try {
            List<AnnouncementRatingDetails> announcementsWithRating =
                    announcementService.getFirstTenAnnouncementsInRating();

            return new ResponseEntity<>(announcementsWithRating, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/rating")
    public ResponseEntity<?> addRatingToAnnouncement(@RequestParam("announcementId") Long announcementId,
                                                     @RequestParam("userId") Long userId,
                                                     @RequestParam("rating") int rating) {
        try {
            announcementService.addRatingToAnnouncement(announcementId, userId, rating);
            log.info("Rating " + rating + " added to " + announcementId + " announcement");
            return new ResponseEntity<>(null, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/upload/{id}")
    public ResponseEntity<?> uploadFile(@PathVariable("id") Long announcementId,
                                        @RequestParam("file") MultipartFile file) {
        try {
            Announcement ann = announcementService.get(announcementId);
            if (ann != null) {
                announcementService.uploadFile(announcementId, file);
                log.info("Image uploaded");
            }
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            log.info("EXCEPTION: Image doesn't uploaded");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}