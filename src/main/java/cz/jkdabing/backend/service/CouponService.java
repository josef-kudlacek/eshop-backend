package cz.jkdabing.backend.service;

import cz.jkdabing.backend.dto.CouponActivationDTO;
import cz.jkdabing.backend.dto.CouponDTO;

import java.util.List;
import java.util.UUID;

public interface CouponService {

    CouponDTO createCoupon(CouponDTO couponDTO);

    CouponDTO updateCoupon(UUID couponId, CouponDTO couponDTO);

    CouponDTO getCoupon(UUID couponId);

    List<CouponDTO> getAllCoupons();

    CouponDTO changeCouponActiveState(UUID couponId, CouponActivationDTO couponActivationDTO);
}
