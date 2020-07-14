package test;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.NoSuchElementException;

import test.Test_Class.Test_Class_Inner;

class Test_Anon extends Test_ {
 
	private static boolean b = !(true);
	private static int x = -(3);
	public static final Enumeration<?> EMPTY_ENUMERATION = new Enumeration<Object>() {
        @Override
		public boolean hasMoreElements() {
            return isAnonymous(); 
        }
        
        @Override
		public Object nextElement() {
            throw new NoSuchElementException();
        }
        
        public boolean isAnonymous() {
        	return this.getClass().isAnonymousClass();
        }
        
    };


  public Test_Anon() {}
  
  int myInt = 33;
  
  private void privTest(int testInt) {
	  System.out.println(this.myInt);
  }
  
  @Override
public int showt() {
	  System.out.println("OHOH");
	  assert(false);
	  return 0;
  }
  private void testThis() {
	  int testInt = 44;
	  Runnable r = new Runnable() {

		@Override
		public void run() {
			privTest(testInt);
			Test_Anon.super.showt();
		}
	  };

	  r.run();  
	  
  }
  public static void main(String[] args) { 
	  int z = x;
	  new Test_Anon().testThis();
	  
	  boolean a = !(Test_Class.Test_Class_Inner.i == 3);
	  new Test_Class.Test_Class_Inner();
	  new Test_Class.Test_Class_Inner().newInstance();
	  Test_Class_Inner.newInstance();
	  System.out.println(EMPTY_ENUMERATION.hasMoreElements());
	  assert(EMPTY_ENUMERATION.hasMoreElements());
	  Object xx = new MouseListener() {

		int y = 0;
		@Override
		public void mouseClicked(MouseEvent e) {
			System.out.println(x + " " + y + " " + z);
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		  
	  };
	  System.out.println("Test_Anon OK");
	  
  }
	
}