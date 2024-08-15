package dio.restapi2.service;

import dio.restapi2.model.Holder;
import dio.restapi2.repository.HolderRepository;
import dio.restapi2.service.exceptions.BusinessException;
import dio.restapi2.service.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static java.util.Optional.ofNullable;

@Service
public class HolderServiceImpl implements HolderService {

    private static final Long UNCHANGEABLE_USER_ID = 1L;

    private final HolderRepository holderRepository;

    public HolderServiceImpl(HolderRepository holderRepository) {
        this.holderRepository = holderRepository;
    }

    @Transactional(readOnly = true)
    public List<Holder> findAll() {
        return this.holderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Holder findById(Long id) {
        return this.holderRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Holder create(Holder holderToCreate) {
        ofNullable(holderToCreate).orElseThrow(() -> new BusinessException("Holder to create must not be null."));
        ofNullable(holderToCreate.getAccount()).orElseThrow(() -> new BusinessException("Holder account must not be null."));
        ofNullable(holderToCreate.getCard()).orElseThrow(() -> new BusinessException("Holder card must not be null."));

        this.validateChangeableId(holderToCreate.getId(), "created");
        if (holderRepository.existsByAccountNumber(holderToCreate.getAccount().getNumber())) {
            throw new BusinessException("This account number already exists.");
        }
        if (holderRepository.existsByCardNumber(holderToCreate.getCard().getNumber())) {
            throw new BusinessException("This card number already exists.");
        }
        return this.holderRepository.save(holderToCreate);
    }

    @Transactional
    public Holder update(Long id, Holder holderToUpdate) {
        this.validateChangeableId(id, "updated");
        Holder dbHolder = this.findById(id);
        if (!dbHolder.getId().equals(holderToUpdate.getId())) {
            throw new BusinessException("Update IDs must be the same.");
        }

        dbHolder.setName(holderToUpdate.getName());
        dbHolder.setAccount(holderToUpdate.getAccount());
        dbHolder.setCard(holderToUpdate.getCard());
        dbHolder.setFeatureList(holderToUpdate.getFeatureList());
        dbHolder.setNewsList(holderToUpdate.getNewsList());

        return this.holderRepository.save(dbHolder);
    }

    @Transactional
    public void delete(Long id) {
        this.validateChangeableId(id, "deleted");
        Holder dbHolder = this.findById(id);
        this.holderRepository.delete(dbHolder);
    }

    private void validateChangeableId(Long id, String operation) {
        if (UNCHANGEABLE_USER_ID.equals(id)) {
            throw new BusinessException("Holder with ID %d can not be %s.".formatted(UNCHANGEABLE_USER_ID, operation));
        }
    }
}