package com.estsoft.springproject.coupon;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class UserCouponTest {

	@Test
	public void testAddCoupon() {
		User user = new User("area00");
		assertEquals(0, user.getTotalCouponCount()); // 쿠폰 수령 전

		ICoupon coupon = new DummyCoupon(); // dummy 쿠폰을 만들어서 사용

		user.addCoupon(coupon);
		assertEquals(1, user.getTotalCouponCount()); // 쿠폰 수령 후 쿠폰수 검증
	}

	@DisplayName("쿠폰이 유효할 경우에만 유저에게 발급한다")
	@Test
	public void testAddCouponWithMock() {
		User user = new User("area00");
		assertEquals(0, user.getTotalCouponCount()); // 쿠폰 수령 전

		ICoupon coupon = Mockito.mock(ICoupon.class);    // mock 객체
		Mockito.when(coupon.isValid()).thenReturn(true);

		user.addCoupon(coupon);
		assertEquals(1, user.getTotalCouponCount()); // 쿠폰 수령 후 쿠폰수 검증
	}

	@DisplayName("쿠폰이 유효하지 않을 경우 발급 안됨")
	@Test
	public void testNoAddCouponWithMock() {
		User user = new User("area00");
		assertEquals(0, user.getTotalCouponCount()); // 쿠폰 수령 전

		ICoupon coupon = Mockito.mock(ICoupon.class);    // mock 객체
		Mockito.when(!coupon.isValid()).thenReturn(false);

		user.addCoupon(coupon);
		assertEquals(0, user.getTotalCouponCount()); // 쿠폰 수령 후 쿠폰수 검증
	}
}
