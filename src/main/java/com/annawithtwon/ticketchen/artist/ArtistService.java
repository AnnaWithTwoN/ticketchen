package com.annawithtwon.ticketchen.artist;

import com.annawithtwon.ticketchen.common.PaginatedResponseDTO;
import com.annawithtwon.ticketchen.exception.ErrorMessage;
import com.annawithtwon.ticketchen.exception.ResourceExistsException;
import com.annawithtwon.ticketchen.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;

    @Autowired
    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public PaginatedResponseDTO<Artist> getAllArtists(int page, int size, String sort, boolean desc) {
        PageRequest request = PageRequest.of(page, size);
        if (sort != null) {
            request = request.withSort(
                    desc ? Sort.Direction.DESC : Sort.Direction.ASC,
                    sort
            );
        }
        Page<Artist> response = artistRepository.findAll(request);
        return new PaginatedResponseDTO(
                response.toList(),
                response.getNumber(),
                response.getSize(),
                response.getTotalPages()
        );
    }

    public Artist getOneArtist(UUID id) throws ResourceNotFoundException {
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
