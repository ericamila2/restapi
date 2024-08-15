package dio.restapi2.service;

import dio.restapi2.model.Holder;

public interface HolderService extends CrudService<Long, Holder> {

    Holder findById(Long id);

    Holder create(Holder holderToCreate);
}
