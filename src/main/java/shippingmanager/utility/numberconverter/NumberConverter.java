package shippingmanager.utility.numberconverter;

public class NumberConverter {

    private static final String[] teens = {"", "jeden", "dwa", "trzy", "cztery",
            "pięć", "sześć", "siedem", "osiem",
            "dziewięć", "dziesięć", "jedenaście", "dwanaście",
            "trzynaście", "czternaście", "piętnaście", "szesnaście",
            "siedemnaście", "osiemnaście", "dziewiętnaście"};

    private static final String _20 = "dwadzieścia";
    private static final String _30 = "trzydzieści";
    private static final String _40 = "czterdzieści";
    private static final String _1e1 = "dziesiąt";
    private static final String _100 = "sto";
    private static final String _200 = "dwieście";
    private static final String _300 = "trzysta";
    private static final String _400 = "czterysta";
    private static final String _1e2 = "set";
    private static final String _1000 = "tysiąc";

    private static final String[] _1e3suff = {"ące", "ęcy"};
    private static final String[] _1e6suff = {"y", "ów"};

    private static final String _1e3 = "tysi";

    private static final String _1e6 = "milion";
    private static final String _1e9 = "miliard";
    private static final String _1e12 = "bilion";
    private static final String _1e15 = "biliard";
    private static final String _1e18 = "trylion";

    private static final String _1e21 = "tryliard";
    private static final String _1e24 = "kwadrylion";

    private static final int secSize = 3;

    public static String speakNumber(long value) {
        StringBuffer buffer = new StringBuffer();
        String stringValue = String.valueOf(value);
        int length = stringValue.length();
        int steps = length / secSize;
        int remain = length % secSize;
        if (length < secSize) {
            steps = 0;
        }
        if (remain != 0) {
            steps++;
        }

        for (int i = 1; i <= steps; i++) {
            int begin = length - (i * secSize);
            if (begin < 0) {
                begin = 0;
            }
            int end = length - ((i - 1) * secSize);
            String subValue = stringValue.substring(begin, end);
            buffer.insert(0, decode(subValue) + " " + determineSuffix(subValue, i) + " ");
        }
        return new String(buffer);
    }

    public static String speakNumber(int value) {
        return speakNumber((long) value);
    }

    public static String speakNumber(byte value) {
        return speakNumber(new Byte(value).longValue());
    }

    public static String speakNumber(String value) {
        return speakNumber(Long.parseLong(value));
    }

    private static String decode(String number) {
        String retval;
        int num = Integer.parseInt(number);

        if (num < 20) {
            retval = (teens[num]);
        } else if ((num >= 20) && (num < 30)) {
            retval = (_20 + " " + teens[num - 20]);
        } else if ((num >= 30) && (num < 40)) {
            retval = (_30 + " " + teens[num - 30]);
        } else if ((num >= 40) && (num < 50)) {
            retval = (_40 + " " + teens[num - 40]);
        } else if ((num >= 50) && (num < 100)) {
            int i = num / 10;
            int j = num % 10;
            retval = (teens[i] + _1e1 + " " + teens[j]);
        } else if ((num >= 100) && (num < 200)) {
            retval = (_100 + " " + decode(num % 100));
        } else if ((num >= 200) && (num < 300)) {
            retval = (_200 + " " + decode(num % 200));
        } else if ((num >= 300) && (num < 400)) {
            retval = (_300 + " " + decode(num % 300));
        } else if ((num >= 400) && (num < 500)) {
            retval = (_400 + " " + decode(num % 400));
        } else if ((num >= 500) && (num < 1000)) {
            int i = num / 100;
            int j = num % 100;
            retval = (teens[i] + _1e2 + " " + decode(j));
        } else retval = null;
        return retval;
    }

    private static String decode(int number) {
        return decode(String.valueOf(number));
    }

    private static String determineSuffix(String number, int position) {
        String suffix = "";
        if (number.equals("000")) {
            return suffix;
        }

        switch (position) {
            case 1:
                return suffix;
            case 2:
                if (number.endsWith("1") && (number.length() == 1))
                    suffix = _1000;
                if (!(number.endsWith("1") && number.length() == 1)) {
                    suffix = _1e3;
                    suffix += suffixHelper(number, _1e3suff);
                }
                return suffix;
            case 3:
                suffix = _1e6;
                break;
            case 4:
                suffix = _1e9;
                break;
            case 5:
                suffix = _1e12;
                break;
            case 6:
                suffix = _1e15;
                break;
            case 7:
                suffix = _1e18;
                break;
        }

        if (!(number.endsWith("1") && number.length() == 1))
            suffix += suffixHelper(number, _1e6suff);
        return suffix;
    }

    private static String suffixHelper(String number, String[] suffixes) {

        if (number.equals("")) {
            return "";
        }
        if (
                (number.endsWith("11")) ||
                        (number.endsWith("12")) ||
                        (number.endsWith("13")) ||
                        (number.endsWith("14"))) {
            return suffixes[1];
        } else {
            if (
                    (number.endsWith("2")) ||
                            (number.endsWith("3")) ||
                            (number.endsWith("4"))) {
                return suffixes[0];
            } else {
                return suffixes[1];
            }
        }

    }
}