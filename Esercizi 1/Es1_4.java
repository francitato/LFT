public class Es1_4 {
	public static boolean scan(String s) {
		int state = 0;
		int i = 0;

		while (state >= 0 && i < s.length()) {
			final char ch = s.charAt(i++);

			switch (state) {

				case 0:
					if (ch == ' ')
						state = 0;
					else if (Character.isDigit(ch)) {
						if (ch % 2 == 0)
							state = 1;
						else
							state = 2;
					} else
						state = -1;
					break;

				case 1:
					if (Character.isDigit(ch)) {
						if (ch % 2 == 0)
							state = 1;
						else
							state = 2;
					} else if (Character.isUpperCase(ch) && ch <= 'K')
						state = 5;
					else if (ch == ' ')
						state = 3;
					else
						state = -1;
					break;

				case 2:
					if (Character.isDigit(ch)) {
						if (ch % 2 == 0)
							state = 1;
						else
							state = 2;
					} else if (Character.isUpperCase(ch) && ch > 'K')
						state = 5;
					else if (ch == ' ')
						state = 4;
					else
						state = -1;
					break;

				case 3:
					if (Character.isUpperCase(ch) && ch <= 'K')
						state = 5;
					else if (ch == ' ')
						state = 3;
					else
						state = -1;
					break;

				case 4:
					if (Character.isUpperCase(ch) && ch > 'K')
						state = 5;
					else if (ch == ' ')
						state = 4;
					else
						state = -1;
					break;

				case 5:
					if (Character.isLowerCase(ch))
						state = 5;
					else if (ch == ' ')
						state = 6;
					else
						state = -1;
					break;

				case 6:
					if (Character.isUpperCase(ch))
						state = 5;
					else if (ch == ' ')
						state = 6;
					else
						state = -1;
					break;
			}
		}
		return state == 5 || state == 6;
	}

	public static void main(String[] args) {
		System.out.println(scan(args[0]) ? "OK" : "NOPE");
	}
}