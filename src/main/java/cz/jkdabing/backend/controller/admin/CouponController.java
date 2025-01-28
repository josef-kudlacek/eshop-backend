package cz.jkdabing.backend.controller.admin;

import cz.jkdabing.backend.controller.AbstractBaseController;
import cz.jkdabing.backend.dto.CouponActivationDTO;
import cz.jkdabing.backend.dto.CouponDTO;
import cz.jkdabing.backend.service.CouponService;
import cz.jkdabing.backend.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/coupons")
public class CouponController extends AbstractBaseController {

    private final CouponService couponService;

    public CouponController(
            MessageService messageService,
            CouponService couponService
    ) {
        super(messageService);
        this.couponService = couponService;
    }

    @PostMapping
    public CouponDTO createCoupon(@Valid @RequestBody CouponDTO couponDTO) {
        return couponService.createCoupon(couponDTO);
    }

    @PutMapping("/{couponId}")
    public CouponDTO updateCoupon(@PathVariable UUID couponId, @Valid @RequestBody CouponDTO couponDTO) {
        return couponService.updateCoupon(couponId, couponDTO);
    }

    @GetMapping("/{couponId}")
    public CouponDTO getCoupon(@PathVariable UUID couponId) {
        return couponService.getCoupon(couponId);
    }

    //TODO: add filtering
    @GetMapping
    public List<CouponDTO> getAllCoupons() {
        return couponService.getAllCoupons();
    }

    @PutMapping("/{couponId}/change-active-state")
    public CouponDTO changeCouponActiveState(
            @PathVariable UUID couponId,
            @Valid @RequestBody CouponActivationDTO couponActivationDTO
    ) {
        return couponService.changeCouponActiveState(couponId, couponActivationDTO);
    }
}
