package test.javassist;

public class TestSubject {

	public void add() {
		System.out.println("add");
	}

	public void add1() {
		System.out.println("add1");
	}

	public void add2() {
		System.out.println("add2");
	}

	public void action() {

	}

	public void action(String method) {
		System.out.println("yrdy");
		// if(method.equals("add")){
		// add();
		// }
		// else if(method.equals("add2")){
		// add2();
		// }
		// else if(method.equals("add1")){
		// add1();
		// }
	}


}
