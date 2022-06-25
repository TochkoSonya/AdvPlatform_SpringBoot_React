package com.tochko.advertising_platform.service.search_criteries;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@Builder
public class AnnouncementSearchCriteria {

    private Optional<String> brand;
    private Optional<String> title;
    private Optional<String> country;
    private Optional<String> periodValue1;
    private Optional<String> periodValue2;
    private Optional<String> priceValue1;
    private Optional<String> priceValue2;
    private Optional<String> followersValue1;
    private Optional<String> followersValue2;
}
