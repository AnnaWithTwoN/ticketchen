package com.annawithtwon.ticketchen.artist;

import com.annawithtwon.ticketchen.common.PaginatedResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("artists")
public class ArtistController {

    private final ArtistService artistService;

    @Autowired
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping
    public PaginatedResponseDTO<Artist> getAllArtists(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) boolean desc
    ) {
        return artistService.getAllArtists(page, size, sort, desc);
    }

    @GetMapping(path = "{artistId}")
    public Artist getOneArtist(@PathVariable("artistId") UUID id) {
        return artistService.getOneArtist(id);
    }

    @PostMapping
    public Artist createArtist(@RequestBody Artist artist) {
        return artistService.createArtist(artist);
    }
}
