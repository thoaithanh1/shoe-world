package root.app.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class VnPayUtil {

    public static String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static String vnp_ReturnUrl = "http://localhost:4200/api/v1/payment/vnpay/response";
    public static String vnp_TmnCode = "BR77VWRJ";
    public static String secretKey = "RUQEYLKOXAPVURPJVIXFKMWYUCNCGKPP";

    public static String vnp_Version = "2.1.0";
    public static String vnp_Command = "pay";
    public static String orderType = "other";
    public static String bankCode = "NCB";
    public static String vnp_IpAddr = "127.0.0.1";

    public static String hmacSHA512(final String key, final String data) {
        try {

            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
    }
}
