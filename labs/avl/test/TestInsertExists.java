package avl.test;

import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import avl.AVLTree;
import avl.util.TreeToStrings;
import avl.validate.BSTValidationError;
import avl.validate.BSTValidator;

public class TestInsertExists {
	@Rule
	public FailReporter tvs = new FailReporter();

	private static BSTValidator<Integer> genTree() {
		AVLTree<Integer> tree = new AVLTree<Integer>();
		return new BSTValidator<Integer>(tree);
	}


	@Test
	public void testInsertSmallExists() {
		int[] values = {15, 10, 5, 7, 8, 3, 4, 20, 17};
		BSTValidator<Integer> bstv = genTree();
		AVLTree<Integer> tree = bstv.tree;
		for(int i=0; i<values.length; i++) {
			String before = TreeToStrings.toTree(tree);
			verifySize("before Insert", tree, i);
			bstv.check();
			tree.insert(values[i]);
			List<Integer> missing = new ArrayList<>();
			for(int j=0; j<=i; j++) {
				if(!tree.exists(values[j])) {
					missing.add(values[j]);
				}
			}
			try {
				if(missing.size()>0) {
					throw new BSTValidationError("After inserting "+ values[i] + " your tree is missing " + Arrays.toString(missing.toArray()));
				}
			} catch (Throwable t) {
				String oops = "\nTree before the problem occurred:\n";
				oops +=before;
				oops += before + "\n";
				oops += "What went wrong: " + t.getMessage() + "\n";
				oops += "Tree that triggered this problem:" + "\n";
				oops += TreeToStrings.toTree(tree);
				t.printStackTrace();
				throw new BSTValidationError(t + "" + oops);
			}
			verifySize("after Insert", tree, i+1);
			bstv.check();

		}
	
	} 


	private void verifySize(String event, AVLTree<?> tree, int expectedSize) {
		assertEquals("Expect tree " + event + " to have size " + 
				expectedSize + " but it did not", expectedSize, tree.size);
		System.out.println(TreeToStrings.toTree(tree));
	}

}
