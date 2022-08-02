package com.annawithtwon.ticketchen.artist;

import com.annawithtwon.ticketchen.exception.ErrorMessage;
import com.annawithtwon.ticketchen.exception.ResourceExistsException;
import com.annawithtwon.ticketchen.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;

    @Autowired
    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    public Artist getOneArtist(UUID id) {
        Optional<Artist> artist = artistRepository.findById(id) ;
        if (!artist.isPresent()) {
            throw new ResourceNotFoundException(ErrorMessage.ARTIST_NOT_FOUND);
        }
        return artist.get();
    }

    public Artist createArtist(Artist artist) throws ResourceExistsException {
        if (artistRepository.findByName(artist.getName()).isPresent()) {
            throw new ResourceExistsException(ErrorMessage.ARTIST_EXISTS);
        }
        return artistRepository.save(artist);
    }
}
