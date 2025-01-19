package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.constants.AuditLogConstants;
import cz.jkdabing.backend.dto.CouponActivationDTO;
import cz.jkdabing.backend.dto.CouponDTO;
import cz.jkdabing.backend.dto.ProductBasicDTO;
import cz.jkdabing.backend.entity.CouponEntity;
import cz.jkdabing.backend.entity.ProductEntity;
import cz.jkdabing.backend.exception.custom.BadRequestException;
import cz.jkdabing.backend.exception.custom.NotFoundException;
import cz.jkdabing.backend.mapper.CouponMapper;
import cz.jkdabing.backend.repository.CouponRepository;
import cz.jkdabing.backend.repository.ProductRepository;
import cz.jkdabing.backend.service.AbstractService;
import cz.jkdabing.backend.service.AuditService;
import cz.jkdabing.backend.service.CouponService;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.util.TableNameUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class CouponServiceImpl extends AbstractService implements CouponService {

    private final CouponRepository couponRepository;

    private final CouponMapper couponMapper;

    private final ProductRepository productRepository;

    public CouponServiceImpl(
            MessageService messageService,
            AuditService auditService,
            CouponRepository couponRepository,
            CouponMapper couponMapper,
            ProductRepository productRepository
    ) {
        super(messageService, auditService);
        this.couponRepository = couponRepository;
        this.couponMapper = couponMapper;
        this.productRepository = productRepository;
    }

    @Override
    public CouponDTO createCoupon(CouponDTO couponDTO) {
        if (couponRepository.existsByCode(couponDTO.getCode())) {
            throw new BadRequestException(
                    getLocalizedMessage("error.coupon.already.exists", couponDTO.getCode())
            );
        }

        CouponEntity couponEntity = couponMapper.toEntity(couponDTO);
        couponEntity.setUsageCount(0);

        if (couponDTO.getApplicableProducts() != null) {
            List<UUID> productIds = couponDTO.getApplicableProducts().stream()
                    .map(ProductBasicDTO::getProductId)
                    .toList();
            List<ProductEntity> applicableProducts = productRepository.findAllById(productIds);
            couponEntity.setApplicableProducts(applicableProducts);
        }

        couponRepository.save(couponEntity);

        prepareAuditLog(
                TableNameUtil.getTableName(couponEntity.getClass()),
                couponEntity.getCouponId(),
                AuditLogConstants.ACTION_CREATE
        );

        return couponMapper.toDTO(couponEntity);
    }

    @Override
    @Transactional
    public CouponDTO updateCoupon(UUID couponId, CouponDTO couponDTO) {
        CouponEntity currentCouponEntity = findCouponByIdOrThrow(couponId);
        currentCouponEntity.setActive(false);
        currentCouponEntity.setExpirationDate(LocalDate.now());
        couponRepository.save(currentCouponEntity);

        prepareAuditLog(
                TableNameUtil.getTableName(currentCouponEntity.getClass()),
                currentCouponEntity.getCouponId(),
                AuditLogConstants.ACTION_UPDATE
        );

        return createCoupon(couponDTO);
    }

    @Override
    public CouponDTO getCoupon(UUID couponId) {
        CouponEntity couponEntity = findCouponByIdOrThrow(couponId);

        return couponMapper.toDTO(couponEntity);
    }

    @Override
    public List<CouponDTO> getAllCoupons() {
        List<CouponEntity> couponEntities = couponRepository.findAll();

        return couponMapper.toDTOs(couponEntities);
    }

    @Override
    public CouponDTO changeCouponActiveState(UUID couponId, CouponActivationDTO couponActivationDTO) {
        CouponEntity couponEntity = findCouponByIdOrThrow(couponId);
        couponMapper.updateEntity(couponActivationDTO, couponEntity);
        couponRepository.save(couponEntity);

        boolean isActive = couponActivationDTO.isActive();
        prepareAuditLog(
                TableNameUtil.getTableName(couponEntity.getClass()),
                couponEntity.getCouponId(),
                isActive ? AuditLogConstants.ACTION_ACTIVATE : AuditLogConstants.ACTION_DEACTIVATE
        );

        return couponMapper.toDTO(couponEntity);
    }

    @Override
    public CouponEntity validateAndRetrieveCoupon(String couponCode) {
        CouponEntity couponEntity = couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new NotFoundException(
                        getLocalizedMessage("error.coupon.not.found", couponCode)
                ));

        if (!couponEntity.isActive()) {
            throw new BadRequestException(getLocalizedMessage("error.coupon.not.active", couponCode));
        }

        if (couponEntity.getExpirationDate() != null && couponEntity.getExpirationDate().isBefore(LocalDate.now())) {
            throw new BadRequestException(getLocalizedMessage("error.coupon.expired", couponCode));
        }

        if (couponEntity.getMaxUsageCount() != null && couponEntity.getUsageCount() >= couponEntity.getMaxUsageCount()) {
            throw new BadRequestException(getLocalizedMessage("error.coupon.max.usage.reached", couponCode));
        }

        return couponEntity;
    }

    private CouponEntity findCouponByIdOrThrow(UUID couponId) {
        return couponRepository.findById(couponId)
                .orElseThrow(() -> new NotFoundException(
                        getLocalizedMessage("error.coupon.not.found", couponId)
                ));
    }
}
