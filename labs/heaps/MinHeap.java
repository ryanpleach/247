package heaps;

import java.util.Random;
import java.util.UUID;

import javax.swing.JOptionPane;

import heaps.util.HeapToStrings;
import heaps.validate.MinHeapValidator;
import timing.Ticker;

public class MinHeap<T extends Comparable<T>> implements PriorityQueue<T> {

	private Decreaser<T>[] array;
	private int size;
	private final Ticker ticker;

	/**
	 * I've implemented this for you.  We create an array
	 *   with sufficient space to accommodate maxSize elements.
	 *   Remember that we are not using element 0, so the array has
	 *   to be one larger than usual.
	 * @param maxSize
	 */
	@SuppressWarnings("unchecked")
	public MinHeap(int maxSize, Ticker ticker) {
		this.array = new Decreaser[maxSize+1];
		this.size = 0;
		this.ticker = ticker;
		ticker.tick(3);
	}

	//
	// Here begin the methods described in lecture
	//
	
	/**
	 * Insert a new thing into the heap.  As discussed in lecture, it
	 *   belongs at the end of objects already in the array.  You can avoid
	 *   doing work in this method by observing, as in lecture, that
	 *   inserting into the heap is reducible to calling decrease on the
	 *   newly inserted element.
	 *   
	 *   This method returns a Decreaser instance, which for the inserted
	 *   thing, tracks the thing itself, the location where the thing lives
	 *   in the heap array, and a reference back to MinHeap so it can call
	 *   decrease(int loc) when necessary.
	 */
	public Decreaser<T> insert(T thing) {
		ticker.tick(3); //instantiating a decreaser
		Decreaser<T> ans = new Decreaser<T>(thing, this, ++size);

		ticker.tick(); //inserting ans into the heap array
		array[size] = ans;
		decrease(size);
		
		ticker.tick();//return statement
		return ans;
	}

	/**
	 * This method responds to an element in the heap decreasing in
	 * value.   As described in lecture, that element might have to swap
	 * its way up the tree so that the heap property is maintained.
	 * 
	 * This method can be called from within this class, in response
	 *   to an insert.  Or it can be called from a Decreaser.
	 *   The information needed to call this method is the current location
	 *   of the heap element (index into the array) whose value has decreased.
	 *   
	 * Really important!   If this method changes the location of elements in
	 *   the array, then the loc field within those elements must be modified 
	 *   too.  For example, if a Decreaser d is currently at location 100,
	 *   then d.loc == 100.  If this method moves that element d to
	 *   location 50, then this method must set d.loc = 50.
	 *   
	 * In my solution, I made sure the above happens by writing a method
	 *    moveItem(int from, int to)
	 * which moves the Decreaser from index "from" to index "to" and, when
	 * done, sets array[to].loc = to
	 *   
	 * This method is missing the "public" keyword so that it
	 *   is only callable within this package.
	 * @param loc position in the array where the element has been
	 *     decreased in value
	 */
	void decrease(int loc) {
		ticker.tick(); //if test
		if (array[loc/2] == null) {
			ticker.tick();
			return;
		}
		ticker.tick(4); //get values (2) plus comparison and test
		if (array[loc].getValue().compareTo(array[loc/2].getValue()) < 0) {
			swap(loc/2,loc);
			decrease(loc/2);
		}
		
	}
	
	/** Swaps two elements in the heap. 
	 * 
	 * @param parent the loc of the parent element being swapped
	 * @param child the loc of the child element being swapped
	 */
	private void swap(int parent, int child) {
		ticker.tick(2); //tests
		if (array[parent] == null && array[child] == null) {
			ticker.tick();
			return;
		}
		ticker.tick(); //test
		if (array[parent] == null) {
			array[parent] = array[child];
			array[child] = null;
			ticker.tick(4);
			array[parent].loc = parent;
			return;
		}
		ticker.tick(); //test
		if (array[child] == null) {
			array[parent] = null;
			array[child] = null;
			ticker.tick(2);
		}
		//handle null cases
		
		
		
		//normal cases
		else {
			ticker.tick(6);
			Decreaser<T> d = array[parent];
			array[parent] = array[child];
			array[child] = d;

			array[parent].loc = parent;
			array[child].loc = child;
			return;
		}
	}
	
	/**
	 * Described in lecture, this method will return a minimum element from
	 *    the heap.  The hole that is created is handled as described in
	 *    lecture.
	 *    This method should call heapify to make sure the heap property is
	 *    maintained at the root node (index 1 into the array).
	 */
	public T extractMin() {
		T ans = array[1].getValue();
		//
		// There is effectively a hole at the root, at location 1 now.
		//    Fix up the heap as described in lecture.
		//    Be sure to store null in an array slot if it is no longer
		//      part of the active heap
		//
		array[1] = null;
		swap(1,size);
		heapify(1);
		size--;
		ticker.tick(4); 
		//
		return ans;
	}

	/**
	 * As described in lecture, this method looks at a parent and its two 
	 *   children, imposing the heap property on them by perhaps swapping
	 *   the parent with the lesser of the two children.  The child thus
	 *   affected must be heapified itself by a recursive call.
	 * @param where the index into the array where the parent lives
	 */
	private void heapify(int where) {
		ticker.tick();
		if (2*where > size) {
			ticker.tick(); 
			return;
		}
		ticker.tick();
		if (2*where + 1 > size) {
			ticker.tick();
			return;
		}
		ticker.tick();
		if (array[2*where] == null) {
			ticker.tick();
			return;
		}
		ticker.tick();
		if (array[2*where + 1] == null) {
			ticker.tick(5); //tests and return
			if (array[where].getValue().compareTo(array[2*where].getValue()) > 0) {
				swap(where,2*where);
				heapify(2*where); 
			}		
			return;
		}
		ticker.tick(6);
		if (array[where].getValue().compareTo(array[2*where].getValue()) < 0 && array[where].getValue().compareTo(array[2*where + 1].getValue()) < 0) {
			ticker.tick();
			return;
			}
		else {
			ticker.tick(4);
			if (array[2*where].getValue().compareTo(array[2*where+1].getValue()) < 0) {
				swap(where,2*where);
				heapify(2*where);
			}
			else {
				swap(where,2*where + 1);
				heapify(2*where + 1);
			}
		}
	}
	
	/**
	 * Does the heap contain anything currently?
	 * I implemented this for you.  Really, no need to thank me!
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	//
	// End of methods described in lecture
	//
	
	//
	// The methods that follow are necessary for the debugging
	//   infrastructure.
	//
	/**
	 * This method would normally not be present, but it allows
	 *   our consistency checkers to see if your heap is in good shape.
	 * @param loc the location
	 * @return the value currently stored at the location
	 */
	public T peek(int loc) {
		if (array[loc] == null)
			return null;
		else return array[loc].getValue();
	}

	/**
	 * Return the loc information from the Decreaser stored at loc.  They
	 *   should agree.  This method is used by the heap validator.
	 * @param loc
	 * @return the Decreaser's view of where it is stored
	 */
	public int getLoc(int loc) {
		return array[loc].loc;
	}

	public int size() {
		return this.size;
	}
	
	public int capacity() {
		return this.array.length-1;
	}
	

	/**
	 * The commented out code shows you the contents of the array,
	 *   but the call to HeapToStrings.toTree(this) makes a much nicer
	 *   output.
	 */
	public String toString() {
//		String ans = "";
//		for (int i=1; i <= size; ++i) {
//			ans = ans + i + " " + array[i] + "\n";
//		}
//		return ans;
		return HeapToStrings.toTree(this);
	}

	/**
	 * This is not the unit test, but you can run this as a Java Application
	 * and it will insert and extract 100 elements into the heap, printing
	 * the heap each time it inserts.
	 * @param args
	 */
	public static void main(String[] args) {
		JOptionPane.showMessageDialog(null, "You are welcome to run this, but be sure also to run the TestMinHeap JUnit test");
		MinHeap<Integer> h = new MinHeap<Integer>(500, new Ticker());
		MinHeapValidator<Integer> v = new MinHeapValidator<Integer>(h);
		Random r = new Random();
		for (int i=0; i < 100; ++i) {
			v.check();
			h.insert(r.nextInt(1000));
			v.check();
			System.out.println(HeapToStrings.toTree(h));
			//System.out.println("heap is " + h);
		}
		while (!h.isEmpty()) {
			int next = h.extractMin();
			System.out.println("Got " + next);
		}
	}


}
