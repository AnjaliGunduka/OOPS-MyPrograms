package com.junodx.api.models.commerce;

import com.fasterxml.jackson.annotation.*;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.commerce.types.OrderStatusType;
import com.junodx.api.models.fulfillment.Fulfillment;
import com.junodx.api.models.laboratory.CustomerActionRequest;
import com.junodx.api.models.laboratory.LaboratoryStatus;
import com.junodx.api.models.laboratory.types.LaboratoryStatusType;
import com.junodx.api.models.payment.Transaction;
import com.junodx.api.models.payment.types.TransactionType;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name="order")
public class Order {
    @Id
    @Column(name = "id", length = 36, nullable = false, unique = true)
    private String id;

    @Column(name = "order_number", unique = true)
    private String orderNumber;

    @Column(name="amount", nullable = false)
    private float amount;

    @Column(name="sub_total")
    private float subTotal;

    @Column(name="total_shipping")
    private float totalShipping;

    @Column(name="total_tax")
    private float totalTax;

    @Column(name="amount_paid")
    private float amountPaid;

    @Column(name="amount_due")
    private float amountDue;

    @Column(name = "with_insurance", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean withInsurance;

    @Column(name="currency", nullable = false)
    private Currency currency;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User customer;

    //Make this unique and required to avoid a user double posting an order accidentally
    @Column(name = "checkout_id", length = 36, unique = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String checkoutId;

    //Make this unique and requires to avoid a user submitting the same cart from different devices accidentally
    @Column(name = "cart_id", length = 36) //, nullable = true, unique = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cartId;

    @Column(name="notes", length = 500)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String notes;


    @Column(name="crm_order_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String crmOrderId;

    @Column(name="crm_contact_id", unique = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String crmContactId;

    @Column(name="shipping_order_id", unique = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String shippingOrderId;

    @Column(name="insurance_billing_order_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String insuranceBillingOrderId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Discount discount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Tax tax;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<OrderStatus> orderStatusHistory;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonProperty("lineItems")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<OrderLineItem> lineItems;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "ordered_at")
    private Calendar orderedAt;

    @Column(name = "salesforce_pricebook_id")
    private String priceBookId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected List<CustomerActionRequest> customerActionRequests;

    @Column(name = "required_shipment", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean requiresShipment;

    @Column(name = "results_available", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean resultsAvailable;

    @Column(name = "requires_redraw", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean requiresRedraw;

    @Column(name = "is_open", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean open;

    @Column(name = "requires_provider_approval", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean requiresProviderApproval;

    private String approvingProviderName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ServiceOptions serviceOptions;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Meta meta;

    public Order(){
        this.meta = new Meta();
        this.id = UUID.randomUUID().toString();
        this.lineItems = new ArrayList<>();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(float subTotal) {
        this.subTotal = subTotal;
    }

    public float getTotalShipping() {
        return totalShipping;
    }

    public void setTotalShipping(float totalShipping) {
        this.totalShipping = totalShipping;
    }

    public float getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(float totalTax) {
        this.totalTax = totalTax;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public boolean isWithInsurance() {
        return withInsurance;
    }

    public void setWithInsurance(boolean withInsurance) {
        this.withInsurance = withInsurance;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public String getCheckoutId() {
        return checkoutId;
    }

    public void setCheckoutId(String checkoutId) {
        this.checkoutId = checkoutId;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCrmOrderId() {
        return crmOrderId;
    }

    public void setCrmOrderId(String crmOrderId) {
        this.crmOrderId = crmOrderId;
    }

    public String getCrmContactId() {
        return crmContactId;
    }

    public void setCrmContactId(String crmContactId) {
        this.crmContactId = crmContactId;
    }

    public String getShippingOrderId() {
        return shippingOrderId;
    }

    public void setShippingOrderId(String shippingOrderId) {
        this.shippingOrderId = shippingOrderId;
    }

    public String getInsuranceBillingOrderId() {
        return insuranceBillingOrderId;
    }

    public void setInsuranceBillingOrderId(String insuranceBillingOrderId) {
        this.insuranceBillingOrderId = insuranceBillingOrderId;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }


    public Tax getTax() {
        return tax;
    }

    public void setTax(Tax tax) {
        this.tax = tax;
    }



    public List<OrderLineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<OrderLineItem> lineItems) {

        if(this.lineItems == null)
            this.lineItems = new ArrayList<>();

        this.lineItems = lineItems;
        for(OrderLineItem item : this.lineItems)
            item.setOrder(this);
    }

    public void addLineItem(OrderLineItem lineItem) {
        if(this.lineItems == null)
            this.lineItems = new ArrayList<>();

        this.lineItems.add(lineItem);
        for(OrderLineItem item : this.lineItems)
            item.setOrder(this);
    }

    public void removeLineItem(OrderLineItem lineItem) {
        this.lineItems.remove(lineItem);
    }

    public void addOrderStatus(OrderStatus status){
        if(this.orderStatusHistory == null)
            this.orderStatusHistory = new ArrayList<>();

        //Don't add if there is another status that matches which is also the current status.
        //TODO check that we can add the same status > 1 time in cases where a line item has been fulfilled > 1 time
        if(!this.orderStatusHistory.stream().anyMatch(x -> x.getStatusType().equals(status.getStatusType()) && !this.getCurrentStatus().equals(status.getStatusType()))) {
            setAllStatusesToNotCurrent();
            this.orderStatusHistory.add(status);
        }
    }

    @JsonProperty("currentStatus")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public OrderStatusType getCurrentStatus(){
        if(this.orderStatusHistory != null) {
            List<OrderStatus> statusType = this.orderStatusHistory.stream().filter(x -> x.isCurrent()).collect(Collectors.toList());
            if (this.orderStatusHistory.size() == 0)
                return null;

            if (statusType != null && statusType.size() == 0) {
                setLatestStatusToCurrent();
                statusType = this.orderStatusHistory.stream().filter(x -> x.isCurrent()).collect(Collectors.toList());
            } else if (statusType != null && statusType.size() > 1)
                resetStatusAndFindCurrent();

            return statusType.get(0).getStatusType();
        }
        return null;
    }

    private void resetStatusAndFindCurrent(){
        List<OrderStatus> statusType = this.orderStatusHistory.stream().filter(x -> x.isCurrent()).collect(Collectors.toList());
        if(statusType != null && statusType.size() > 1) {
            Collections.sort(statusType);
            int index = 0;
            for(OrderStatus s : statusType){
                if(index > 0)
                    s.setCurrent(false);
            }
        }
    }

    private void setLatestStatusToCurrent(){
        Collections.sort(this.orderStatusHistory);
        if(this.orderStatusHistory.size() > 0)
            this.orderStatusHistory.get(0).setCurrent(true);
    }

    private void setAllStatusesToNotCurrent(){
        if(this.orderStatusHistory != null){
            for(OrderStatus l : this.orderStatusHistory)
                l.setCurrent(false);
        }
    }

    public List<OrderStatus> getOrderStatusHistory() {
        return orderStatusHistory;
    }

    public void setOrderStatusHistory(List<OrderStatus> orderStatusHistory) {
        if(this.orderStatusHistory == null)
            this.orderStatusHistory = new ArrayList<>();

        this.orderStatusHistory = orderStatusHistory;
        for(OrderStatus st : this.orderStatusHistory)
            st.setOrder(this);
    }

    public Calendar getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(Calendar orderedAt) {
        this.orderedAt = orderedAt;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction transaction){
        if(this.transactions == null)
            this.transactions = new ArrayList<>();
        this.transactions.add(transaction);
    }

    @JsonIgnore
    public Transaction getChargeTransaction(){
        if(this.transactions != null) {
            for (Transaction t : this.transactions) {
                if (t.getType() != null && t.getType().equals(TransactionType.CARD_CHARGED))
                    return t;
            }
        }

        return null;
    }

    public float getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(float amountPaid) {
        this.amountPaid = amountPaid;
    }

    public float getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(float amountDue) {
        this.amountDue = amountDue;
    }

    public String getPriceBookId() {
        return priceBookId;
    }

    public void setPriceBookId(String priceBookId) {
        this.priceBookId = priceBookId;
    }

    public boolean isRequiresShipment() {
        return requiresShipment;
    }

    public void setRequiresShipment(boolean requiresShipment) {
        this.requiresShipment = requiresShipment;
    }

    public boolean isResultsAvailable() {
        return resultsAvailable;
    }

    public void setResultsAvailable(boolean resultsAvailable) {
        this.resultsAvailable = resultsAvailable;
    }

    public boolean isRequiresRedraw() {
        return requiresRedraw;
    }

    public void setRequiresRedraw(boolean requiresRedraw) {
        this.requiresRedraw = requiresRedraw;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isRequiresProviderApproval() {
        return requiresProviderApproval;
    }

    public void setRequiresProviderApproval(boolean requiresProviderApproval) {
        this.requiresProviderApproval = requiresProviderApproval;
    }

    public String getApprovingProviderName() {
        return approvingProviderName;
    }

    public void setApprovingProviderName(String approvingProviderName) {
        this.approvingProviderName = approvingProviderName;
    }

    public ServiceOptions getServiceOptions() {
        return serviceOptions;
    }

    public void setServiceOptions(ServiceOptions serviceOptions) {
        this.serviceOptions = serviceOptions;
    }

    public List<CustomerActionRequest> getCustomerActionRequests() {
        return customerActionRequests;
    }

    public void setCustomerActionRequests(List<CustomerActionRequest> customerActionRequest) {
        this.customerActionRequests = customerActionRequest;
    }

    public void addCustomerActionRequest(CustomerActionRequest request) {
        if(this.customerActionRequests == null)
            this.customerActionRequests = new ArrayList<>();

        this.customerActionRequests.add(request);
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        if(this.meta == null)
            this.meta = new Meta();

        this.meta = meta;
    }

}
