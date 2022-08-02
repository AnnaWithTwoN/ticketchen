package com.annawithtwon.ticketchen.artist;

import com.annawithtwon.ticketchen.exception.ResourceExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;

    @Autowired
    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public List<Artist> getArtists() {
        return artistRepository.findAll();
    }

    public Artist createArtist(Artist artist) throws ResourceExistsException {
        if (artistRepository.findByName(artist.getName()).isPresent()) {
            throw new ResourceExistsException("Artist");
        }
        return artistRepository.save(artist);
    }
}
