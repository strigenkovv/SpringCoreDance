package be.vdab.dance.festivals;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public long create(Boeking boeking) {
        var festival = festivalRepository.findAndLockById(boeking.getFestivalId())
                .orElseThrow();
        if (boeking.getAantalTickets() > festival.getTicketsBeschikbaar()) {
            throw new IllegalArgumentException("Onvoldoende tickets beschikbaar");
        }
        festivalRepository.verlaagTicketsBeschikbaar(
                festival.getId(), boeking.getAantalTickets());
        return boekingRepository.create(boeking);
    }

    public List<BoekingOverzicht> findBoekingOverzicht() {
        return boekingRepository.findBoekingOverzicht();
    }

    public void annuleerBoeking(long id) {
        var boeking = boekingRepository.findAndLockById(id)
                .orElseThrow();
        festivalRepository.verhoogTicketsBeschikbaar(
                boeking.getFestivalId(), boeking.getAantalTickets());
        boekingRepository.delete(id);
    }
}
