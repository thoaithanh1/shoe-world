package root.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import root.app.dto.*;
import root.app.entity.*;
import root.app.mapper.CustomMapper;
import root.app.repository.*;
import root.app.service.PaymentService;
import root.app.util.GenerateStringUtil;
import root.app.util.VnPayUtil;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {


    private final CartRepository cartRepository;

    private final CartDetailRepository cartDetailRepository;

    private final AddressRepository addressRepository;

    private final PaymentMethodRepository paymentMethodRepository;

    private final UserRepository userRepository;

    private final ProductDetailRepository detailRepository;

    private final OrderRepository orderRepository;

    private final OrderDetailRepository orderDetailRepository;

    private final PromotionRepository promotionRepository;

    private final CustomMapper customMapper;

    @Override
    public PaymentDto showInfoCartInPayment(Long userId) {
        Cart cart = cartRepository.findCartByUser(userId);
        List<AddressDto> addresses = getListAddressDto(userId);
        List<CartDetailDto> cartDetails = getListCartDetailDto(userId);
        return new PaymentDto(cart, addresses, cartDetails);
    }

    @Override
    public Boolean paymentOrderByPayAfterReceive(OrderPaymentDto orderPaymentDto) {
        String codeOrder = GenerateStringUtil.generateString(10);
        PaymentMethod payAfterReceive = paymentMethodRepository.findById(orderPaymentDto.getPaymentMethodId()).get();
        User userDB = userRepository.findById(orderPaymentDto.getUserId()).get();
        Address addressDB = addressRepository.findAddressByIsDefaultAndUser(true, userDB);
        Promotion promotionDB = promotionRepository.findById(orderPaymentDto.getPromotionId()).get();
        Order saveOrder = null;
        Boolean isCreatedOrderDetail = false;
        if(!orderPaymentDto.getCartDetailDtos().isEmpty()) {
            // Save Order
            Order order = new Order(codeOrder, orderPaymentDto.getTotalPrice(),
                    orderPaymentDto.getShippingFee(), orderPaymentDto.getNote(),
                    payAfterReceive, userDB, addressDB,
                    promotionDB, promotionDB.getDiscountValue());
            saveOrder = orderRepository.save(order);

            // Save OrderDetail
            isCreatedOrderDetail = saveOrderDetailAndUpdateQty(orderPaymentDto.getCartDetailDtos(), saveOrder);

            // Delete total price in cart by user
            deleteTotalPriceAfterOrdered(userDB.getId());
        }

        return saveOrder.getId() > 0 && isCreatedOrderDetail;
    }

    @Override
    public Boolean paymentOrderByVnPay(BigDecimal totalPrice, String paymentType, String codeOrder, String note, Long userId) {
        User user = new User();
        user.setId(userId);
        Address addressDB = addressRepository.findAddressByIsDefaultAndUser(true, user);
        List<CartDetailDto> listCartDetail = getListCartDetailDto(userId);
        PaymentMethod payAfterReceive = null;
        if(paymentType.equals("ATM") || paymentType.equals("QRCODE")) {
            payAfterReceive = paymentMethodRepository.findById(2).get();
        }

        Order saveOrder = null;
        Boolean isCreatedOrderDetail = false;
        if(!listCartDetail.isEmpty()) {
            // Save Order
            Order order = new Order(codeOrder, totalPrice, BigDecimal.ZERO, note,
                    payAfterReceive, user, addressDB, null, null);
            saveOrder = orderRepository.save(order);
            // Save OrderDetail
            isCreatedOrderDetail = saveOrderDetailAndUpdateQty(listCartDetail, saveOrder);

            // Delete total price in cart by user
            deleteTotalPriceAfterOrdered(userId);
        }

        return saveOrder != null && isCreatedOrderDetail;
    }

    @Override
    public String redirectPagePaymentVnPay(BigDecimal totalPrice, String note) throws UnsupportedEncodingException {

        BigDecimal total = totalPrice.multiply(BigDecimal.valueOf(100));
        String codeOrder = GenerateStringUtil.generateString(10);

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VnPayUtil.vnp_Version);
        vnp_Params.put("vnp_Command", VnPayUtil.vnp_Command);
        vnp_Params.put("vnp_TmnCode", VnPayUtil.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(total));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_BankCode", VnPayUtil.bankCode);
        vnp_Params.put("vnp_TxnRef", codeOrder);
        vnp_Params.put("vnp_OrderInfo", note);
        vnp_Params.put("vnp_OrderType", VnPayUtil.orderType);

        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VnPayUtil.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", VnPayUtil.vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String queryUrl = query.toString();
        String vnp_SecureHash = VnPayUtil.hmacSHA512(VnPayUtil.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        return VnPayUtil.vnp_PayUrl + "?" + queryUrl;
    }

    private List<AddressDto> getListAddressDto(Long userId) {
        return addressRepository.findAllAddressByUser(userId)
                .stream()
                .map(customMapper::convertAddressToDto)
                .collect(Collectors.toList());
    }

    private List<CartDetailDto> getListCartDetailDto(Long userId) {
        return cartDetailRepository.findCartDetailByCart(userId)
                .stream()
                .map(customMapper::convertCartDetailToDto)
                .collect(Collectors.toList());
    }

    private Boolean saveOrderDetailAndUpdateQty(List<CartDetailDto> cartDetails, Order saveOrder) {
        boolean isCreatedOrderDetail = false;
        for (CartDetailDto cd : cartDetails) {
            OrderDetail orderDetail = new OrderDetail(
                    cd.getProductDetail().getProduct().getName(),
                    cd.getQuantity(),
                    cd.getPrice(),
                    cd.getProductDetail().getSize().getName(),
                    cd.getProductDetail().getColor().getName(),
                    cd.getProductDetail(),
                    saveOrder
            );
            OrderDetail saveOrderDetail = orderDetailRepository.save(orderDetail);
            isCreatedOrderDetail = saveOrderDetail.getId() > 0;

            // Subtract the qty after ordering
            ProductDetail productDetailDB = detailRepository.findById(cd.getProductDetail().getId()).get();
            Integer quantityProduct = productDetailDB.getQuantity();
            productDetailDB.setQuantity(quantityProduct - cd.getQuantity());
            detailRepository.save(productDetailDB);

        }
        return isCreatedOrderDetail;
    }

    private void deleteTotalPriceAfterOrdered(Long userId) {
        Cart cart = cartRepository.findCartByUser(userId);
        cart.setTotal(BigDecimal.ZERO);
        Cart saveCart = cartRepository.save(cart);
        cartDetailRepository.deleteAllProductByCart(saveCart.getId());
    }
}
