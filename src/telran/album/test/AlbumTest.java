package telran.album.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import telran.album.dao.Album;
import telran.album.dao.AlbumImpl;
import telran.album.model.Photo;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class AlbumTest {

    private int size = 6;
    private Photo[] photos;
    private Album album;
    private final LocalDateTime baseDateTime = LocalDateTime.of(2014, 11, 10, 13, 43, 10);
    private final Comparator<Photo> comparator = (p1, p2) -> {
        int res = Integer.compare(p1.getAlbumId(), p2.getAlbumId());
        return res == 0 ? Integer.compare(p1.getPhotoId(), p2.getPhotoId()) : res;

    };

    @BeforeEach
    void setUp(){

        album = new AlbumImpl(6);

        photos = new Photo[size];
        photos[0] = new Photo(1,
                        1111,
                        "Holidays",
                        "http://fotobank.com/1111.jpg",
                        baseDateTime);
        photos[1] = new Photo(1,
                        2222,
                        "Working",
                        "http://fotobank.com/2222.jpg",
                        baseDateTime.plus(2, ChronoUnit.DAYS));
        photos[2] = new Photo(2,
                        1111,
                        "Friends",
                        "http://fotobank.com/02_1111.jpg",
                        baseDateTime.minus(13, ChronoUnit.MINUTES));
        photos[3] = new Photo(3,
                        3333,
                        "Cars",
                        "http://fotobank.com/3333.jpg",
                        baseDateTime.plus(6, ChronoUnit.HOURS));
        photos[4] = new Photo(1,
                        4444,
                        "Trains",
                        "http://fotobank.com/4444.jpg",
                        baseDateTime.minusDays(3));
        for (int i = 0; i < photos.length; i++) {
            album.addPhoto(photos[i]);
        }

    }
    @Test
    void testAddPhoto() {

        Photo newPhoto = new Photo(3,
                5555,
                "Planes",
                "http://fotobank.com/5555.jpg",
                baseDateTime.minusHours(4));

        Photo newPhoto1 = new Photo(3,
                        6666,
                        "Cars",
                        "http://fotobank.com/6666.jpg",
                        baseDateTime.minusDays(15));

        assertFalse(album.addPhoto(null));
        assertFalse(album.addPhoto(photos[4]));
        assertTrue(album.addPhoto(newPhoto));
        assertEquals(size, album.size());
        assertFalse(album.addPhoto(newPhoto1));

    }

    @Test
    void testRemovePhoto() {
        assertFalse(album.removePhoto(1234, 5));
        assertTrue(album.removePhoto(photos[3].getPhotoId(), photos[3].getAlbumId()));
        assertEquals(size - 2, album.size());
    }

    @Test
    void testUpdatePhoto() {

        Photo photo = photos[3];

        assertFalse(album.updatePhoto(1234, 5, "http://test.jpg"));
        assertTrue(album.updatePhoto(photo.getPhotoId(), photo.getAlbumId(), "http://drive.google.com/photo3.jpg"));
        assertEquals("http://drive.google.com/photo3.jpg", album.getPhotoFromAlbum(3333, 3).getUrl());
    }

    @Test
    void testGetPhotoFromAlbum() {

        assertEquals(photos[3], album.getPhotoFromAlbum(3333, 3));
        assertNull(album.getPhotoFromAlbum(1234, 3));
    }

    @Test
    void testGetAllPhotoFromAlbum() {
        Photo[] expected = {photos[0], photos[1], photos[4]};
        Photo[] actual = album.getAllPhotoFromAlbum(1);
        Arrays.sort(actual, comparator);
        assertArrayEquals(expected, actual);
        assertNull(album.getAllPhotoFromAlbum(5));
    }

    @Test
    void testGetPhotoBetweenDate() {

        LocalDate baseDate = LocalDate.of(2014, 11, 10);
        LocalDate dateFrom = baseDate.minusDays(1);
        LocalDate dateTo = baseDate.plusDays(1);

        Photo[] expected = {photos[0], photos[2], photos[3]};
        Photo[] res = album.getPhotoBetweenDate(dateFrom, dateTo);
        Arrays.sort(res, comparator);

        assertArrayEquals(expected, res);

    }

    @Test
    void testSize() {
        assertEquals(size - 1, album.size());
    }
}