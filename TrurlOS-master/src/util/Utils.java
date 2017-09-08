package util;

public class Utils {
	public static String hexDump(String input) {
		String ans = "";
		for(int i = 0; i < input.length() - 1; i++) {
			ans += Integer.toHexString(input.charAt(i));
			ans += " ";
		} if(input.length() > 0) {
			ans += Integer.toHexString(input.charAt(input.length() - 1));
		}
		return ans;
	}

	public static boolean inHex(String string) {
		for(char c : string.toCharArray()) {
			if(! "1234567890abcdef".contains(("" + c).toLowerCase()))
				return false;
		}
		return true;
	}
	
	public static String toHexString(int b) {
		String ans = Integer.toHexString(b);
		if(ans.length() < 1) {
			return "00";
		} else if(ans.length() < 2) {
			return "0" + ans;
		}
		return "" + ans.charAt(ans.length() - 2) + ans.charAt(ans.length() - 1);
	}
}
