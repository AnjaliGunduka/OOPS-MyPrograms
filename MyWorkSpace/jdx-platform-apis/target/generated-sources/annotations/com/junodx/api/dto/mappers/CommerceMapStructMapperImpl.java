package com.junodx.api.dto.mappers;

import com.junodx.api.dto.models.auth.UserOrderDto;
import com.junodx.api.dto.models.commerce.DiscountDto;
import com.junodx.api.dto.models.commerce.OrderBatchDto;
import com.junodx.api.dto.models.commerce.OrderDto;
import com.junodx.api.dto.models.commerce.OrderLineItemBatchDto;
import com.junodx.api.dto.models.commerce.OrderLineItemDto;
import com.junodx.api.dto.models.commerce.OrderStatusDto;
import com.junodx.api.dto.models.commerce.ProductAvailabilityDto;
import com.junodx.api.dto.models.commerce.ProductTruncatedDto;
import com.junodx.api.dto.models.commerce.ProviderOrderApprovalDto;
import com.junodx.api.dto.models.fulfillment.FulfillmentBatchDto;
import com.junodx.api.dto.models.fulfillment.ShippingDetailsBatchDto;
import com.junodx.api.dto.models.fulfillment.ShippingMethodBatchDto;
import com.junodx.api.dto.models.laboratory.LaboratoryOrderDetailsBatchDto;
import com.junodx.api.dto.models.providers.MedicalLicenseDto;
import com.junodx.api.dto.models.providers.PracticeDto;
import com.junodx.api.dto.models.providers.ProviderDto;
import com.junodx.api.dto.models.providers.SpecialtyDto;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.commerce.Discount;
import com.junodx.api.models.commerce.Order;
import com.junodx.api.models.commerce.OrderLineItem;
import com.junodx.api.models.commerce.OrderStatus;
import com.junodx.api.models.commerce.Product;
import com.junodx.api.models.commerce.ProductAvailablity;
import com.junodx.api.models.commerce.ProviderApproval;
import com.junodx.api.models.core.DMA;
import com.junodx.api.models.core.State;
import com.junodx.api.models.core.ZipCode;
import com.junodx.api.models.fulfillment.Fulfillment;
import com.junodx.api.models.fulfillment.ShippingDetails;
import com.junodx.api.models.fulfillment.ShippingMethod;
import com.junodx.api.models.laboratory.LaboratoryOrder;
import com.junodx.api.models.providers.Location;
import com.junodx.api.models.providers.MedicalLicense;
import com.junodx.api.models.providers.Practice;
import com.junodx.api.models.providers.Provider;
import com.junodx.api.models.providers.Specialty;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-02-06T11:57:39+0530",
    comments = "version: 1.4.1.Final, compiler: Eclipse JDT (IDE) 1.4.50.v20210914-1429, environment: Java 15.0.2 (Oracle Corporation)"
)
@Component
public class CommerceMapStructMapperImpl implements CommerceMapStructMapper {

    @Override
    public OrderDto orderToOrderDto(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderDto orderDto = new OrderDto();

        orderDto.setId( order.getId() );
        orderDto.setAmount( order.getAmount() );
        orderDto.setSubTotal( order.getSubTotal() );
        orderDto.setTotalShipping( order.getTotalShipping() );
        orderDto.setTotalTax( order.getTotalTax() );
        orderDto.setCurrency( order.getCurrency() );
        orderDto.setCustomer( userToUserOrderDto( order.getCustomer() ) );
        orderDto.setCurrentStatus( order.getCurrentStatus() );
        orderDto.setCheckoutId( order.getCheckoutId() );
        orderDto.setCartId( order.getCartId() );
        orderDto.setNotes( order.getNotes() );
        orderDto.setCrmOrderId( order.getCrmOrderId() );
        orderDto.setCrmContactId( order.getCrmContactId() );
        orderDto.setShippingOrderId( order.getShippingOrderId() );
        orderDto.setInsuranceBillingOrderId( order.getInsuranceBillingOrderId() );
        orderDto.setDiscount( discountToDiscountDto( order.getDiscount() ) );
        orderDto.setTax( order.getTax() );
        orderDto.setOrderStatusHistory( orderStatusListToOrderStatusDtoList( order.getOrderStatusHistory() ) );
        orderDto.setLineItems( orderLineItemsToOrderLineItemDtos( order.getLineItems() ) );
        orderDto.setMeta( order.getMeta() );

        return orderDto;
    }

    @Override
    public OrderLineItemDto orderLineItemToOrderLineItemDto(OrderLineItem orderLineItem) {
        if ( orderLineItem == null ) {
            return null;
        }

        OrderLineItemDto orderLineItemDto = new OrderLineItemDto();

        orderLineItemDto.setId( orderLineItem.getId() );
        orderLineItemDto.setType( orderLineItem.getType() );
        orderLineItemDto.setDescription( orderLineItem.getDescription() );
        orderLineItemDto.setAmount( orderLineItem.getAmount() );
        orderLineItemDto.setOriginalUnitPrice( orderLineItem.getOriginalUnitPrice() );
        orderLineItemDto.setInsuranceEstimatedCoveredAmount( orderLineItem.getInsuranceEstimatedCoveredAmount() );
        orderLineItemDto.setSku( orderLineItem.getSku() );
        orderLineItemDto.setProductId( orderLineItem.getProductId() );
        orderLineItemDto.setProductName( orderLineItem.getProductName() );
        orderLineItemDto.setTaxable( orderLineItem.isTaxable() );
        orderLineItemDto.setRequiresShipping( orderLineItem.isRequiresShipping() );
        orderLineItemDto.setDirectlyProvided( orderLineItem.isDirectlyProvided() );
        orderLineItemDto.setInOfficeCollected( orderLineItem.isInOfficeCollected() );
        orderLineItemDto.setMeta( orderLineItem.getMeta() );

        return orderLineItemDto;
    }

    @Override
    public OrderStatusDto orderStatusToOrderStatusDto(OrderStatus orderStatus) {
        if ( orderStatus == null ) {
            return null;
        }

        OrderStatusDto orderStatusDto = new OrderStatusDto();

        orderStatusDto.setStatusType( orderStatus.getStatusType() );
        orderStatusDto.setUpdatedAt( orderStatus.getUpdatedAt() );

        return orderStatusDto;
    }

    @Override
    public ProviderOrderApprovalDto providerApprovalToProviderOrderApprovalDto(ProviderApproval providerApproval) {
        if ( providerApproval == null ) {
            return null;
        }

        ProviderOrderApprovalDto providerOrderApprovalDto = new ProviderOrderApprovalDto();

        providerOrderApprovalDto.setRequiresApproval( providerApproval.isRequiresApproval() );
        providerOrderApprovalDto.setApproved( providerApproval.isApproved() );
        providerOrderApprovalDto.setApprovalDate( providerApproval.getApprovalDate() );
        providerOrderApprovalDto.setApprovingProvider( providerToProviderDto( providerApproval.getApprovingProvider() ) );

        return providerOrderApprovalDto;
    }

    @Override
    public List<OrderLineItemDto> orderLineItemsToOrderLineItemDtos(List<OrderLineItem> orders) {
        if ( orders == null ) {
            return null;
        }

        List<OrderLineItemDto> list = new ArrayList<OrderLineItemDto>( orders.size() );
        for ( OrderLineItem orderLineItem : orders ) {
            list.add( orderLineItemToOrderLineItemDto( orderLineItem ) );
        }

        return list;
    }

    @Override
    public OrderBatchDto orderToOrderBatchDto(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderBatchDto orderBatchDto = new OrderBatchDto();

        orderBatchDto.setId( order.getId() );
        orderBatchDto.setAmount( order.getAmount() );
        orderBatchDto.setSubTotal( order.getSubTotal() );
        orderBatchDto.setTotalShipping( order.getTotalShipping() );
        orderBatchDto.setTotalTax( order.getTotalTax() );
        orderBatchDto.setCurrency( order.getCurrency() );
        orderBatchDto.setRequiresRedraw( order.isRequiresRedraw() );
        orderBatchDto.setWithInsurance( order.isWithInsurance() );
        orderBatchDto.setResultsAvailable( order.isResultsAvailable() );
        orderBatchDto.setCustomer( userToUserOrderDto( order.getCustomer() ) );
        orderBatchDto.setCurrentStatus( order.getCurrentStatus() );
        orderBatchDto.setLineItems( orderLineItemToOrderLineItemBatchDtos( order.getLineItems() ) );

        return orderBatchDto;
    }

    @Override
    public List<OrderBatchDto> orderToOrderBatchDtos(List<Order> orders) {
        if ( orders == null ) {
            return null;
        }

        List<OrderBatchDto> list = new ArrayList<OrderBatchDto>( orders.size() );
        for ( Order order : orders ) {
            list.add( orderToOrderBatchDto( order ) );
        }

        return list;
    }

    @Override
    public ProductTruncatedDto productToProductTruncatedDto(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductTruncatedDto productTruncatedDto = new ProductTruncatedDto();

        productTruncatedDto.setId( product.getId() );
        productTruncatedDto.setName( product.getName() );
        productTruncatedDto.setType( product.getType() );
        productTruncatedDto.setActive( product.isActive() );
        productTruncatedDto.setShippable( product.isShippable() );
        productTruncatedDto.setTaxable( product.isTaxable() );
        productTruncatedDto.setDimensions( product.getDimensions() );
        productTruncatedDto.setPrice( product.getPrice() );
        productTruncatedDto.setCurrency( product.getCurrency() );
        productTruncatedDto.setSku( product.getSku() );

        return productTruncatedDto;
    }

    @Override
    public List<ProductTruncatedDto> productToProductTruncatedDtos(List<Product> product) {
        if ( product == null ) {
            return null;
        }

        List<ProductTruncatedDto> list = new ArrayList<ProductTruncatedDto>( product.size() );
        for ( Product product1 : product ) {
            list.add( productToProductTruncatedDto( product1 ) );
        }

        return list;
    }

    @Override
    public ProductAvailabilityDto productAvailabilityToProductAvailabilityDto(ProductAvailablity availablity) {
        if ( availablity == null ) {
            return null;
        }

        ProductAvailabilityDto productAvailabilityDto = new ProductAvailabilityDto();

        productAvailabilityDto.setId( availablity.getId() );
        productAvailabilityDto.setProduct( productToProductTruncatedDto( availablity.getProduct() ) );
        List<ZipCode> list = availablity.getAllowedZipCodes();
        if ( list != null ) {
            productAvailabilityDto.setAllowedZipCodes( new ArrayList<ZipCode>( list ) );
        }
        List<State> list1 = availablity.getAllowedStates();
        if ( list1 != null ) {
            productAvailabilityDto.setAllowedStates( new ArrayList<State>( list1 ) );
        }
        List<DMA> list2 = availablity.getAllowedDMAs();
        if ( list2 != null ) {
            productAvailabilityDto.setAllowedDMAs( new ArrayList<DMA>( list2 ) );
        }
        productAvailabilityDto.setMeta( availablity.getMeta() );
        productAvailabilityDto.setOutOfRegion( availablity.getOutOfRegion() );
        productAvailabilityDto.setSoldOut( availablity.getSoldOut() );
        productAvailabilityDto.setNewStockAvailableAt( availablity.getNewStockAvailableAt() );
        productAvailabilityDto.setAvailableInYourRegionAt( availablity.getAvailableInYourRegionAt() );

        return productAvailabilityDto;
    }

    @Override
    public List<ProductAvailabilityDto> productAvailabilityToProductAvailabilityDtos(List<ProductAvailablity> availablity) {
        if ( availablity == null ) {
            return null;
        }

        List<ProductAvailabilityDto> list = new ArrayList<ProductAvailabilityDto>( availablity.size() );
        for ( ProductAvailablity productAvailablity : availablity ) {
            list.add( productAvailabilityToProductAvailabilityDto( productAvailablity ) );
        }

        return list;
    }

    @Override
    public ProductAvailablity productAvailabilityDtoToProductAvailability(ProductAvailabilityDto availabilityDto) {
        if ( availabilityDto == null ) {
            return null;
        }

        ProductAvailablity productAvailablity = new ProductAvailablity();

        productAvailablity.setId( availabilityDto.getId() );
        productAvailablity.setProduct( productTruncatedDtoToProduct( availabilityDto.getProduct() ) );
        List<ZipCode> list = availabilityDto.getAllowedZipCodes();
        if ( list != null ) {
            productAvailablity.setAllowedZipCodes( new ArrayList<ZipCode>( list ) );
        }
        List<State> list1 = availabilityDto.getAllowedStates();
        if ( list1 != null ) {
            productAvailablity.setAllowedStates( new ArrayList<State>( list1 ) );
        }
        List<DMA> list2 = availabilityDto.getAllowedDMAs();
        if ( list2 != null ) {
            productAvailablity.setAllowedDMAs( new ArrayList<DMA>( list2 ) );
        }
        productAvailablity.setMeta( availabilityDto.getMeta() );
        productAvailablity.setNewStockAvailableAt( availabilityDto.getNewStockAvailableAt() );
        productAvailablity.setAvailableInYourRegionAt( availabilityDto.getAvailableInYourRegionAt() );
        productAvailablity.setOutOfRegion( availabilityDto.getOutOfRegion() );
        productAvailablity.setSoldOut( availabilityDto.getSoldOut() );

        return productAvailablity;
    }

    @Override
    public List<ProductAvailablity> productAvailabilityDtoToProductAvailabilitys(List<ProductAvailabilityDto> availabilityDto) {
        if ( availabilityDto == null ) {
            return null;
        }

        List<ProductAvailablity> list = new ArrayList<ProductAvailablity>( availabilityDto.size() );
        for ( ProductAvailabilityDto productAvailabilityDto : availabilityDto ) {
            list.add( productAvailabilityDtoToProductAvailability( productAvailabilityDto ) );
        }

        return list;
    }

    @Override
    public OrderLineItemBatchDto orderLineItemToOrderLineItemBatchDto(OrderLineItem item) {
        if ( item == null ) {
            return null;
        }

        OrderLineItemBatchDto orderLineItemBatchDto = new OrderLineItemBatchDto();

        orderLineItemBatchDto.setId( item.getId() );
        orderLineItemBatchDto.setFulfillments( fulfillmentListToFulfillmentBatchDtoList( item.getFulfillments() ) );
        orderLineItemBatchDto.setLaboratoryOrderDetails( laboratoryOrderToLaboratoryOrderDetailsBatchDto( item.getLaboratoryOrderDetails() ) );
        orderLineItemBatchDto.setType( item.getType() );
        orderLineItemBatchDto.setProductId( item.getProductId() );
        orderLineItemBatchDto.setProductName( item.getProductName() );
        orderLineItemBatchDto.setCurrentFulfillmentId( item.getCurrentFulfillmentId() );

        return orderLineItemBatchDto;
    }

    @Override
    public List<OrderLineItemBatchDto> orderLineItemToOrderLineItemBatchDtos(List<OrderLineItem> items) {
        if ( items == null ) {
            return null;
        }

        List<OrderLineItemBatchDto> list = new ArrayList<OrderLineItemBatchDto>( items.size() );
        for ( OrderLineItem orderLineItem : items ) {
            list.add( orderLineItemToOrderLineItemBatchDto( orderLineItem ) );
        }

        return list;
    }

    @Override
    public FulfillmentBatchDto fulfillmentToFulfillmentBatchDto(Fulfillment fulfillment) {
        if ( fulfillment == null ) {
            return null;
        }

        FulfillmentBatchDto fulfillmentBatchDto = new FulfillmentBatchDto();

        fulfillmentBatchDto.setId( fulfillment.getId() );
        fulfillmentBatchDto.setShippingDetails( shippingDetailsToShippingDetailsBatchDto( fulfillment.getShippingDetails() ) );
        fulfillmentBatchDto.setCompleted( fulfillment.isCompleted() );
        fulfillmentBatchDto.setShipmentCreated( fulfillment.isShipmentCreated() );
        fulfillmentBatchDto.setRedraw( fulfillment.isRedraw() );

        return fulfillmentBatchDto;
    }

    @Override
    public ShippingDetailsBatchDto shippingDetailsToShippingDetailsBatchDto(ShippingDetails shippingDetails) {
        if ( shippingDetails == null ) {
            return null;
        }

        ShippingDetailsBatchDto shippingDetailsBatchDto = new ShippingDetailsBatchDto();

        shippingDetailsBatchDto.setId( shippingDetails.getId() );
        shippingDetailsBatchDto.setCurrentStatus( shippingDetails.getCurrentStatus() );
        shippingDetailsBatchDto.setToMethod( shippingMethodToShippingMethodBatchDto( shippingDetails.getToMethod() ) );
        shippingDetailsBatchDto.setReturnMethod( shippingMethodToShippingMethodBatchDto( shippingDetails.getReturnMethod() ) );

        return shippingDetailsBatchDto;
    }

    @Override
    public ShippingMethodBatchDto shippingMethodToShippingMethodBatchDto(ShippingMethod shippingMethod) {
        if ( shippingMethod == null ) {
            return null;
        }

        ShippingMethodBatchDto shippingMethodBatchDto = new ShippingMethodBatchDto();

        shippingMethodBatchDto.setId( shippingMethod.getId() );
        shippingMethodBatchDto.setTrackingUrl( shippingMethod.getTrackingUrl() );

        return shippingMethodBatchDto;
    }

    protected UserOrderDto userToUserOrderDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserOrderDto userOrderDto = new UserOrderDto();

        userOrderDto.setId( user.getId() );
        userOrderDto.setFirstName( user.getFirstName() );
        userOrderDto.setLastName( user.getLastName() );
        userOrderDto.setEmail( user.getEmail() );
        userOrderDto.setDateOfBirth( user.getDateOfBirth() );
        userOrderDto.setUserType( user.getUserType() );
        userOrderDto.setStripeCustomerId( user.getStripeCustomerId() );

        return userOrderDto;
    }

    protected DiscountDto discountToDiscountDto(Discount discount) {
        if ( discount == null ) {
            return null;
        }

        DiscountDto discountDto = new DiscountDto();

        discountDto.setType( discount.getType() );
        discountDto.setMode( discount.getMode() );
        discountDto.setDiscountApplied( discount.isDiscountApplied() );
        discountDto.setAmountDiscounted( discount.getAmountDiscounted() );

        return discountDto;
    }

    protected List<OrderStatusDto> orderStatusListToOrderStatusDtoList(List<OrderStatus> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderStatusDto> list1 = new ArrayList<OrderStatusDto>( list.size() );
        for ( OrderStatus orderStatus : list ) {
            list1.add( orderStatusToOrderStatusDto( orderStatus ) );
        }

        return list1;
    }

    protected SpecialtyDto specialtyToSpecialtyDto(Specialty specialty) {
        if ( specialty == null ) {
            return null;
        }

        SpecialtyDto specialtyDto = new SpecialtyDto();

        specialtyDto.setType( specialty.getType() );
        specialtyDto.setProvider( providerToProviderDto( specialty.getProvider() ) );

        return specialtyDto;
    }

    protected List<SpecialtyDto> specialtyListToSpecialtyDtoList(List<Specialty> list) {
        if ( list == null ) {
            return null;
        }

        List<SpecialtyDto> list1 = new ArrayList<SpecialtyDto>( list.size() );
        for ( Specialty specialty : list ) {
            list1.add( specialtyToSpecialtyDto( specialty ) );
        }

        return list1;
    }

    protected MedicalLicenseDto medicalLicenseToMedicalLicenseDto(MedicalLicense medicalLicense) {
        if ( medicalLicense == null ) {
            return null;
        }

        MedicalLicenseDto medicalLicenseDto = new MedicalLicenseDto();

        medicalLicenseDto.setState( medicalLicense.getState() );
        medicalLicenseDto.setLicenseNumber( medicalLicense.getLicenseNumber() );
        medicalLicenseDto.setProvider( providerToProviderDto( medicalLicense.getProvider() ) );

        return medicalLicenseDto;
    }

    protected List<MedicalLicenseDto> medicalLicenseListToMedicalLicenseDtoList(List<MedicalLicense> list) {
        if ( list == null ) {
            return null;
        }

        List<MedicalLicenseDto> list1 = new ArrayList<MedicalLicenseDto>( list.size() );
        for ( MedicalLicense medicalLicense : list ) {
            list1.add( medicalLicenseToMedicalLicenseDto( medicalLicense ) );
        }

        return list1;
    }

    protected List<ProviderDto> providerListToProviderDtoList(List<Provider> list) {
        if ( list == null ) {
            return null;
        }

        List<ProviderDto> list1 = new ArrayList<ProviderDto>( list.size() );
        for ( Provider provider : list ) {
            list1.add( providerToProviderDto( provider ) );
        }

        return list1;
    }

    protected PracticeDto practiceToPracticeDto(Practice practice) {
        if ( practice == null ) {
            return null;
        }

        PracticeDto practiceDto = new PracticeDto();

        practiceDto.setId( practice.getId() );
        practiceDto.setName( practice.getName() );
        practiceDto.setActive( practice.isActive() );
        practiceDto.setContactAddress( practice.getContactAddress() );
        practiceDto.setPrimaryPhone( practice.getPrimaryPhone() );
        practiceDto.setPatientEmail( practice.getPatientEmail() );
        practiceDto.setBillingEmail( practice.getBillingEmail() );
        practiceDto.setPrimaryEmail( practice.getPrimaryEmail() );
        practiceDto.setProviders( providerListToProviderDtoList( practice.getProviders() ) );
        List<Location> list1 = practice.getLocations();
        if ( list1 != null ) {
            practiceDto.setLocations( new ArrayList<Location>( list1 ) );
        }
        practiceDto.setMeta( practice.getMeta() );

        return practiceDto;
    }

    protected ProviderDto providerToProviderDto(Provider provider) {
        if ( provider == null ) {
            return null;
        }

        ProviderDto providerDto = new ProviderDto();

        providerDto.setId( provider.getId() );
        providerDto.setFirstName( provider.getFirstName() );
        providerDto.setLastName( provider.getLastName() );
        providerDto.setEmail( provider.getEmail() );
        providerDto.setContactPhone( provider.getContactPhone() );
        providerDto.setContactAddress( provider.getContactAddress() );
        providerDto.setNpi( provider.getNpi() );
        providerDto.setUpin( provider.getUpin() );
        providerDto.setPracticing( provider.isPracticing() );
        providerDto.setSpecialties( specialtyListToSpecialtyDtoList( provider.getSpecialties() ) );
        providerDto.setLicenses( medicalLicenseListToMedicalLicenseDtoList( provider.getLicenses() ) );
        providerDto.setPractice( practiceToPracticeDto( provider.getPractice() ) );
        providerDto.setMeta( provider.getMeta() );

        return providerDto;
    }

    protected Product productTruncatedDtoToProduct(ProductTruncatedDto productTruncatedDto) {
        if ( productTruncatedDto == null ) {
            return null;
        }

        Product product = new Product();

        product.setId( productTruncatedDto.getId() );
        product.setName( productTruncatedDto.getName() );
        product.setType( productTruncatedDto.getType() );
        product.setActive( productTruncatedDto.isActive() );
        product.setShippable( productTruncatedDto.isShippable() );
        product.setTaxable( productTruncatedDto.isTaxable() );
        product.setDimensions( productTruncatedDto.getDimensions() );
        product.setPrice( productTruncatedDto.getPrice() );
        product.setCurrency( productTruncatedDto.getCurrency() );
        product.setSku( productTruncatedDto.getSku() );

        return product;
    }

    protected List<FulfillmentBatchDto> fulfillmentListToFulfillmentBatchDtoList(List<Fulfillment> list) {
        if ( list == null ) {
            return null;
        }

        List<FulfillmentBatchDto> list1 = new ArrayList<FulfillmentBatchDto>( list.size() );
        for ( Fulfillment fulfillment : list ) {
            list1.add( fulfillmentToFulfillmentBatchDto( fulfillment ) );
        }

        return list1;
    }

    protected LaboratoryOrderDetailsBatchDto laboratoryOrderToLaboratoryOrderDetailsBatchDto(LaboratoryOrder laboratoryOrder) {
        if ( laboratoryOrder == null ) {
            return null;
        }

        LaboratoryOrderDetailsBatchDto laboratoryOrderDetailsBatchDto = new LaboratoryOrderDetailsBatchDto();

        laboratoryOrderDetailsBatchDto.setId( laboratoryOrder.getId() );
        laboratoryOrderDetailsBatchDto.setReportableTestRunId( laboratoryOrder.getReportableTestRunId() );
        laboratoryOrderDetailsBatchDto.setReportableTestReportId( laboratoryOrder.getReportableTestReportId() );
        laboratoryOrderDetailsBatchDto.setReportConfiguration( laboratoryOrder.getReportConfiguration() );

        return laboratoryOrderDetailsBatchDto;
    }
}
