package InorderTransversals;

public class InOrderTransversal {

    static class ExprNode {
        String value;
        ExprNode left, right;

        public ExprNode(String value) { 
            this.value = value;
        }
    }

    public static void inorder(ExprNode node) {
        if (node == null)
            return;

        boolean isOperator = node.value.equals("+") || node.value.equals("-") ||
                node.value.equals("*") || node.value.equals("/");

        if (isOperator)
            System.out.print("(");

        inorder(node.left);
        System.out.print(node.value); 
        inorder(node.right);

        if (isOperator)
            System.out.print(")");
    }

    public static void main(String[] args) {
        // Building expression: 3 + 4 * 5;
        ExprNode root = new ExprNode("+");
        root.left = new ExprNode("3");
        root.right = new ExprNode("*");
        root.right.left = new ExprNode("4");
        root.right.right = new ExprNode("5");

        System.out.print("Inorder (Infix): ");
        inorder(root); // Output: (3+(4*5))
        System.out.println(); 
    }
}
