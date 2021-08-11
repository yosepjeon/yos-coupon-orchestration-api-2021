package com.yosep.coupon.mapper

import com.yosep.coupon.coupon.data.jpa.dto.CouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.entity.ProductDiscountCoupon
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
    fun couponDtoForCreationToProductDiscountCoupon(couponDtoForCreation: CouponDtoForCreation): ProductDiscountCoupon
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