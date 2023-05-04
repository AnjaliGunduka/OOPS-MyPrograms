package Enum;

public enum Mathematics {
	PLUS {
		double eval(double x, double y)

	{
			return x + y;
		}
	},
	MINUS {
		double eval(double x, double y) {
			return x - y;
		}
	},
	TIMES {
		double eval(double x, double y) {
			return x * y;
		}
	},
	DIVIDED_BY {
		double eval(double x, double y) {
			return x / y;
		}
	};
	abstract double eval(double x, double y);

	public static void main(String[] args) {
		for (Mathematics a : Mathematics.values())
			System.out.println(a.eval(8, 8));
	}
}
