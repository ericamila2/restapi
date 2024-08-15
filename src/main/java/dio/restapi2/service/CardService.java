package dio.restapi2.service;

import dio.restapi2.model.Card;

public interface CardService extends CrudService<Long, Card> {

    Card findById(Long id);

}
