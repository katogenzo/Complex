package test;

public class ForListTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int total=100;
		int index=1;
		while(index<total)
			index<<=1;
		
		System.out.println(index);
		
		index=2;
		index<<=1;
		System.out.println(index);

	}

}
