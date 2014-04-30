package cuki.test;

import java.util.ArrayList;

public class TesteUtils {

	public static Short[] toShort(String str) {

		ArrayList<Short> list = new ArrayList<Short>();

		for (String s : str.split("(?<=\\G.{2})"))
			list.add((short) (((s.getBytes()[1] & 0xFF) << 8) | (s.getBytes()[0] & 0xFF)));

		Short[] ret = list.toArray(new Short[list.size()]);

		return ret;
	}
}
