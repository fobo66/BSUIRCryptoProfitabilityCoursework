package by.bsuir.service.mapper;


import by.bsuir.domain.*;
import by.bsuir.service.dto.CryptocurrencyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cryptocurrency} and its DTO {@link CryptocurrencyDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CryptocurrencyMapper extends EntityMapper<CryptocurrencyDTO, Cryptocurrency> {



    default Cryptocurrency fromId(Long id) {
        if (id == null) {
            return null;
        }
        Cryptocurrency cryptocurrency = new Cryptocurrency();
        cryptocurrency.setId(id);
        return cryptocurrency;
    }
}
