package be.vdab.dance.festivals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoekingServiceUnitTest {
    @Mock
    private FestivalRepository festivalRepository;
    @Mock
    private BoekingRepository boekingRepository;
    @InjectMocks
    private BoekingService boekingService;

    @Test
    void createMaaktEenBoekingAan() {
        var festival = new Festival(1, "Rock Werchter", 100, BigDecimal.ZERO);
        var boeking = new Boeking(0, "Test", 3, festival.getId());
        when(festivalRepository.findAndLockById(festival.getId()))
                .thenReturn(Optional.of(festival));
        when(boekingRepository.create(boeking)).thenReturn(42L);

        assertThat(boekingService.create(boeking)).isEqualTo(42L);

        // verify controleert of de juiste methods van de mock repositories
        // werden opgeroepen. Dit is een extra controle naast assertThat.
        verify(festivalRepository).verlaagTicketsBeschikbaar(festival.getId(), 3);
        verify(boekingRepository).create(boeking);
    }

    @Test
    void createMisluktAlsErTeWeinigTicketsZijn() {
        var festival = new Festival(1, "Rock Werchter", 2, BigDecimal.ZERO);
        var boeking = new Boeking(0, "Test", 3, festival.getId());
        when(festivalRepository.findAndLockById(festival.getId()))
                .thenReturn(Optional.of(festival));

        assertThatThrownBy(() -> boekingService.create(boeking))
                .isInstanceOf(IllegalArgumentException.class);

        // never controleert dat deze methods niet werden opgeroepen
        // wanneer het aantal tickets onvoldoende is.
        verify(festivalRepository, never())
                .verlaagTicketsBeschikbaar(festival.getId(), 3);
        verify(boekingRepository, never()).create(boeking);
    }
}
