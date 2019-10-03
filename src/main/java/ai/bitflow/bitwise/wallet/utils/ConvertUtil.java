package ai.bitflow.bitwise.wallet.utils;

import ai.bitflow.bitwise.wallet.constants.abstracts.BlockchainConstant;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ConvertUtil {

    private static final String ISO_FORMAT    = "yyyy-MM-dd'T'HH:mm:ss"; // .SSS zzz
    private static final TimeZone UTC = TimeZone.getTimeZone("UTC");
    private static final SimpleDateFormat isoFormatter =
            new SimpleDateFormat(ISO_FORMAT);
    static { isoFormatter.setTimeZone(UTC); }

    public static Date getLimitDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, BlockchainConstant.SEND_UNSENT_TX_LIMIT_DATE);
        return cal.getTime();
    }

    public static String getTestRandomOrderId() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[16];
        random.nextBytes(bytes);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        String token = encoder.encodeToString(bytes);
        return "TEST" + token;
    }

    public static String getTestRandomTxId() {
        return "TEST" + getTestRandomOrderId();
    }

    public static long getTestRandomTxTime() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static int getTestRandomInt() {
        Random rand = new SecureRandom();
        return rand.nextInt(999999999);
    }

    /**
     *
     * @param rpcUrl
     * @return
     */
    public static String[] parseIpPort(String rpcUrl) {
        String[] ret = new String[2];
        int idx = rpcUrl.lastIndexOf(":");
        ret[0] = rpcUrl.substring(0, idx).replaceFirst("http://", "");
        ret[1] = rpcUrl.substring(idx+1).replace("/", "");
        return ret;
    }

    public static long nowToUTCDisplayLong() {
        return Long.parseLong(Instant.now().toString().substring(0,19).replaceAll("[-]", "").replaceAll("[:]","").replaceAll("T", ""));
    }

    public static long utcTimestampToEpoch(String timestamp) {
        // String timestamp = "2017-18-08T12:59:30";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-dd-MM HH:mm:ss");
        // parse to LocalDateTime
        LocalDateTime dt = LocalDateTime.parse(timestamp.replace("T", " "), dtf);
        // assume the LocalDateTime is in UTC
        Instant instant = dt.toInstant(ZoneOffset.UTC);
        return instant.getEpochSecond();
    }

    public static String toISOFormatTimestamp(long yyyymmddhhmmss) {
        try {
            Date thatday = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse("" + yyyymmddhhmmss);
            return isoFormatter.format(thatday).toString();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String longToHex(long longval) {
        return "0x"
                + Long.toHexString(longval);
    }
    public static Long hexToLong(String hexval) {
        return new BigInteger
                (hexval.substring(2), 16).longValue();
    }

    public static String ethToWeiHex(double ethval) {
        return Numeric.toHexStringWithPrefix(
                Convert.toWei(BigDecimal.valueOf(ethval), Convert.Unit.ETHER)
                        .toBigInteger());
    }
    public static String tokenAmountToHex(double tokenval, int decimals) {
        return Numeric.toHexStringNoPrefix(BigDecimal.valueOf(tokenval)
                .multiply(BigDecimal.TEN.pow(decimals)).toBigInteger());
    }
    public static double hexToEth(String hexval) {
        return Convert.fromWei(new BigDecimal(Numeric.toBigInt(hexval))
                , Convert.Unit.ETHER).doubleValue();
    }
    public static Long hexToWei(String hex) {
        return hexToLong(hex);
    }
    public static String weiToHex(Long wei) {
        return "0x" + Long.toHexString(wei);
    }
    public static BigInteger ethToWei(double ethval) {
        return Convert.toWei(BigDecimal.valueOf(ethval), Convert.Unit.ETHER).toBigInteger();
    }
    public static double ethToGWei(double ethval) {
        return Convert.fromWei(Convert.toWei(BigDecimal.valueOf(ethval), Convert.Unit.ETHER)
                , Convert.Unit.GWEI).doubleValue();
    }

    public static double hexTokenAmountToDouble(String hex, int decimals) {
        return new BigDecimal(Numeric.toBigInt(hex)).divide(BigDecimal.TEN.pow(decimals)).doubleValue();
    }

    // String to 64 length HexString (equivalent to 32 Hex lenght)
    public static String asciiToHex(String asciiValue) {
        char[] chars = asciiValue.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++)
        {
            hex.append(Integer.toHexString((int) chars[i]));
        }

        return hex.toString() + "".join("", Collections.nCopies(32 - (hex.length()/2), "00"));
    }

    public static String hexToASCII(String hexValue) {
        StringBuilder output = new StringBuilder("");
        for (int i = 0; i < hexValue.length(); i += 2)
        {
            String str = hexValue.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    public static int integerDigits(BigDecimal n) {
        return n.signum() == 0 ? 0 : n.precision() - n.scale() -1;
    }

    public static BigInteger bigIntegerCeil(BigInteger src) {
        int digits = integerDigits(new BigDecimal(src));
        BigInteger biggestFirst = BigInteger.valueOf(Math.round(Math.pow(10, digits)));
        return src.subtract(src.mod(biggestFirst)).add(biggestFirst);
    }

    public static BigInteger bigIntegerAddFirst(BigInteger src) {
        int digits = integerDigits(new BigDecimal(src));
        BigInteger biggestFirst = BigInteger.valueOf(Math.round(Math.pow(10, digits)));
        return src.add(biggestFirst);
    }

    /**
     * 8 GWEI
     * @param src
     * @return
     */
    public static BigInteger getMinGasPrice(BigInteger src) {
        if (Convert.fromWei(src.toString(), Convert.Unit.GWEI).doubleValue() < 8) {
            return new BigInteger("8000000000");
        }
        int digits = integerDigits(new BigDecimal(src));
        BigInteger biggestFirst = BigInteger.valueOf(Math.round(Math.pow(10, digits)));
        return src.add(biggestFirst);
    }

    public static int getBigintZeroCount(BigInteger i) {
        return ("" + i).length()-1;
    }

    public static Integer timestampToEpoch(String timestamp){
        if(timestamp == null) return null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); // .SSSZ
            Date dt = sdf.parse(timestamp);
            long epoch = dt.getTime();
            return (int)(epoch/1000);
        } catch(ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toCurrencyFormat(Double number) {
        return String.format("%,.4f", number);
    }
    public static String toCurrencyFormat8(Double number) {
        NumberFormat nf = NumberFormat.getInstance();
        return nf.format(number);
    }
    public static String toCurrencyFormat8Int(Double number) {
        NumberFormat nf = NumberFormat.getInstance();
        return nf.format(number.intValue());
    }
}
