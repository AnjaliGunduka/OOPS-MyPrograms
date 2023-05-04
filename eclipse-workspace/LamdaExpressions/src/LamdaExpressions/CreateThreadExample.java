package LamdaExpressions;

public class CreateThreadExample {
	public static void main(String[] args) {

		Thread t2 = new Thread(new Runnable() {//Anonymous class

			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("Create the thread");
			}

		});
		t2.start();
		System.out.println("main thread created");
	}
}
