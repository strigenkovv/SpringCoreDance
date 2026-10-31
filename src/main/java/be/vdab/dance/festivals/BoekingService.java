package be.vdab.dance.festivals;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BoekingService {
    private final FestivalRepository festivalRepository;
    private final BoekingRepository boekingRepository;

    public BoekingService(FestivalRepository festivalRepository,
                          BoekingRepository boekingRepository) {
        this.festivalRepository = festivalRepository;
        this.boekingRepository = boekingRepository;
    }

    public long boek(Boeking boeking) {
        var festival = festivalRepository.findAndLockById(boeking.getFestivalId())
                .orElseThrow();
        if (boeking.getAantalTickets() > festival.getTicketsBeschikbaar()) {
            throw new IllegalArgumentException("Onvoldoende tickets beschikbaar");
        }
        festivalRepository.verlaagTicketsBeschikbaar(
                festival.getId(), boeking.getAantalTickets());
        return boekingRepository.create(boeking);
    }
}
