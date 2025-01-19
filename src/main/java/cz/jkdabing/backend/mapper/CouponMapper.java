package cz.jkdabing.backend.mapper;

import cz.jkdabing.backend.dto.CouponActivationDTO;
import cz.jkdabing.backend.dto.CouponDTO;
import cz.jkdabing.backend.entity.CouponEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CouponMapper {

    CouponEntity toEntity(CouponDTO couponDTO);

    @Mapping(source = "active", target = "isActive")
    CouponDTO toDTO(CouponEntity couponEntity);

    List<CouponDTO> toDTOs(List<CouponEntity> couponEntities);

    void updateEntity(CouponActivationDTO couponActivationDTO, @MappingTarget CouponEntity couponEntity);
}
