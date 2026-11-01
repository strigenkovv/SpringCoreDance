package be.vdab.dance.festivals;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.math.BigDecimal;

@Service
@Transactional
public class FestivalService {
    private final FestivalRepository festivalRepository;

    public FestivalService(FestivalRepository festivalRepository) {
        this.festivalRepository = festivalRepository;
    }

    public List<Festival> findAll() {
        return festivalRepository.findAll();
    }

    public List<Festival> findUitverkocht() {
        return festivalRepository.findUitverkocht();
    }

    public List<FestivalMetReclameBudget> findMetReclameBudgetVanaf(BigDecimal vanaf) {
        return festivalRepository.findMetReclameBudgetVanaf(vanaf);
    }

    public void delete(long id) {
        festivalRepository.delete(id);
    }

    public long create(Festival festival) {
        return festivalRepository.create(festival);
    }

    public void annuleer(long id) {
        var festival = festivalRepository.findAndLockById(id)
                .orElseThrow();
        var aantalAndereFestivals = festivalRepository.findAantal() - 1;
        if (aantalAndereFestivals > 0) {
            var bedragPerFestival = festival.getReclameBudget()
                    .divide(BigDecimal.valueOf(aantalAndereFestivals));
            festivalRepository.verhoogReclameBudget(bedragPerFestival, id);
        }
        festivalRepository.delete(id);
    }
}
