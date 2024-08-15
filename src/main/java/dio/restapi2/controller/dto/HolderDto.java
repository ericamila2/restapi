package dio.restapi2.controller.dto;


import dio.restapi2.model.Holder;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

public record HolderDto(
        Long id,
        String name,
        AccountDto account,
        CardDto card,
        List<FeatureDto> features,
        List<NewsDto> news) {

    public HolderDto(Holder model) {
        this(
                model.getId(),
                model.getName(),
                ofNullable(model.getAccount()).map(AccountDto::new).orElse(null),
                ofNullable(model.getCard()).map(CardDto::new).orElse(null),
                ofNullable(model.getFeatureList()).orElse(emptyList()).stream().map(FeatureDto::new).collect(toList()),
                ofNullable(model.getNewsList()).orElse(emptyList()).stream().map(NewsDto::new).collect(toList())
        );
    }

    public Holder toModel() {
        Holder model = new Holder();
        model.setId(this.id);
        model.setName(this.name);
        model.setAccount(ofNullable(this.account).map(AccountDto::toModel).orElse(null));
        model.setCard(ofNullable(this.card).map(CardDto::toModel).orElse(null));
        model.setFeatureList(ofNullable(this.features).orElse(emptyList()).stream().map(FeatureDto::toModel).collect(toList()));
        model.setNewsList(ofNullable(this.news).orElse(emptyList()).stream().map(NewsDto::toModel).collect(toList()));
        return model;
    }

}

