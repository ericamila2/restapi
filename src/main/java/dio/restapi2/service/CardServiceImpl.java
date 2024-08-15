package dio.restapi2.service;

import dio.restapi2.model.Card;
import dio.restapi2.repository.CardRepository;
import dio.restapi2.service.exceptions.BusinessException;
import dio.restapi2.service.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    private static final Long UNCHANGEABLE_CARD_ID = 1L;

    private final CardRepository cardRepository;

    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Transactional(readOnly = true)
    public List<Card> findAll() {
        return this.cardRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Card findById(Long id) {
        return this.cardRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Card create(Card cardToCreate) {
        return this.cardRepository.save(cardToCreate);
    }

    @Transactional
    public Card update(Long id, Card cardToUpdate) {
        this.validateChangeableId(id, "updated");
        Card dbCard = this.findById(id);
        if (!dbCard.getId().equals(cardToUpdate.getId())) {
            throw new BusinessException("Update IDs must be the same.");
        }

        dbCard.setNumber(cardToUpdate.getNumber());
        dbCard.setLimit(cardToUpdate.getLimit());

        return this.cardRepository.save(dbCard);
    }

    @Transactional
    public void delete(Long id) {
        this.validateChangeableId(id, "deleted");
        Card dbCard = this.findById(id);
        this.cardRepository.delete(dbCard);
    }

    private void validateChangeableId(Long id, String operation) {
        if (UNCHANGEABLE_CARD_ID.equals(id)) {
            throw new BusinessException("Card with ID %d can not be %s.".formatted(UNCHANGEABLE_CARD_ID, operation));
        }
    }
}