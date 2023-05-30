public class Es1_7 {
	public static boolean scan(String s) {
		int state = 0;
		int i = 0;

		while (state >= 0 && i < s.length()) {
			final char ch = s.charAt(i++);
			switch (state) {
				case 0:
					if (ch == 'f')
						state = 1;
					else if (ch != 'f' && Character.isLetter(ch))
						state = 4;
					else
						state = -1;
					break;
				case 1:
					if (ch == 'r')
						state = 2;
					else if (ch != 'r' && Character.isLetter(ch))
						state = 5;
					else
						state = -1;
					break;
				case 2:
					if (Character.isLetter(ch))
						state = 3;
					else
						state = -1;
				case 3:
					break;
				case 4:
					if (ch == 'r')
						state = 5;
					else
						state = -1;
					break;
				case 5:
					if (ch == 'a')
						state = 3;
					else
						state = -1;
					break;
			}
		}
		return state == 3;
	}

	public static void main(String[] args) {
		System.out.println(scan(args[0]) ? "OK" : "NOPE");
	}
}