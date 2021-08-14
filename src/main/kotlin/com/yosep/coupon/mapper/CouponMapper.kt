package com.yosep.coupon.mapper

import com.yosep.coupon.coupon.data.jpa.dto.ProductDiscountCouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.dto.CreatedProductDiscountCouponDto
import com.yosep.coupon.coupon.data.jpa.dto.CreatedTotalDiscountCouponDto
import com.yosep.coupon.coupon.data.jpa.dto.TotalDiscountCouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.entity.Coupon
import com.yosep.coupon.coupon.data.jpa.entity.ProductDiscountCoupon
import com.yosep.coupon.coupon.data.jpa.entity.TotalPriceDiscountCoupon
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.factory.Mappers

@Mapper
interface CouponMapper {
    companion object {
        val INSTANCE = Mappers.getMapper(CouponMapper::class.java)!!
    }

    @Mappings(
        Mapping(target = "couponDiscount", source = "couponDiscountVo"),
        Mapping(target = "couponStock", source = "couponStockVo")
    )
    fun dtoToEntity(dto: ProductDiscountCouponDtoForCreation): ProductDiscountCoupon

    @Mappings(
        Mapping(target = "couponDiscount", source = "couponDiscountVo"),
        Mapping(target = "couponStock", source = "couponStockVo")
    )
    fun dtoToEntity(dto: TotalDiscountCouponDtoForCreation): TotalPriceDiscountCoupon

    @Mappings(
        Mapping(target = "couponDiscountVo", source = "couponDiscount"),
        Mapping(target = "couponStockVo", source = "couponStock")
    )
    fun entityToDto(coupon: ProductDiscountCoupon): CreatedProductDiscountCouponDto

    @Mappings(
        Mapping(target = "couponDiscount", source = "couponDiscountVo"),
        Mapping(target = "couponStock", source = "couponStockVo")
    )
    fun entityToDto(coupon: TotalPriceDiscountCoupon): CreatedTotalDiscountCouponDto
}


//@Mapper
//interface ProductMapper {
//    @Mapping(target = "category", ignore = true)
//    fun productDtoForCreationToProduct(productDtoForCreation: ProductDtoForCreation?): Product?
//
//    @Mapping(target = "productId", ignore = false)
//    fun productToCreatedProductDto(product: Product?): CreatedProductDto?
//
//    @Mapping(target = "productId", ignore = false)
//    fun productToSelectedProductDto(product: Product?): SelectedProductDto?
//
//    companion object {
//        val INSTANCE = Mappers.getMapper(ProductMapper::class.java)
//    }
//}