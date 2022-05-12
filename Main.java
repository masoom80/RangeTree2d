package com.company;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;


class QuickSort {
    /* This function takes last element as pivot,
       places the pivot element at its correct
       position in sorted array, and places all
       smaller (smaller than pivot) to left of
       pivot and all greater elements to right
       of pivot */
    int partition_x(leaf[] arr, int low, int high) {
        leaf pivot = arr[high];
        int i = (low - 1); // index of smaller element
        for (int j = low; j < high; j++) {
            // If current element is smaller than the pivot
            if (arr[j].x < pivot.x) {
                i++;

                // swap arr[i] and arr[j]
                leaf temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // swap arr[i+1] and arr[high] (or pivot)
        leaf temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

    int partition_y(leaf[] arr, int low, int high) {
        leaf pivot = arr[high];
        int i = (low - 1); // index of smaller element
        for (int j = low; j < high; j++) {
            // If current element is smaller than the pivot
            if (arr[j].y < pivot.y) {
                i++;

                // swap arr[i] and arr[j]
                leaf temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // swap arr[i+1] and arr[high] (or pivot)
        leaf temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }


    /* The main function that implements QuickSort()
      arr[] --> Array to be sorted,
      low  --> Starting index,
      high  --> Ending index */
    void sort_x(leaf[] arr, int low, int high) {
        if (low < high) {
            /* pi is partitioning index, arr[pi] is
              now at right place */
            int pi = partition_x(arr, low, high);

            // Recursively sort elements before
            // partition and after partition
            sort_x(arr, low, pi - 1);
            sort_x(arr, pi + 1, high);
        }
    }

    void sort_y(leaf[] arr, int low, int high) {
        if (low < high) {
            /* pi is partitioning index, arr[pi] is
              now at right place */
            int pi = partition_y(arr, low, high);

            // Recursively sort elements before
            // partition and after partition
            sort_y(arr, low, pi - 1);
            sort_y(arr, pi + 1, high);
        }
    }

    /* A utility function to print array of size n */
}

class node_not_leaf {
    double data;
    node_not_leaf parent;
    range_tree_1d_y y_dimension;
    node_not_leaf right, left;
    double max;

    node_not_leaf() {
        parent = null;
        right = null;
        left = null;
        y_dimension = null;
    }

    node_not_leaf(double data, double max, node_not_leaf left, node_not_leaf right) {
        parent = null;
        this.max = max;
        y_dimension = null;
        this.left = left;
        this.right = right;
        if (left != null)
            left.parent = this;
        if (right != null)
            right.parent = this;
        this.data = data;
    }
}

class leaf extends node_not_leaf {
    double x, y;
    leaf next;


    leaf(double x, double y) {
        super();
        parent = null;

        this.x = x;
        this.y = y;
        next = null;

    }

    leaf duplicate() {
        return new leaf(x, y);
    }
}

class range_tree_1d_x {
    node_not_leaf root;
    int size;

    range_tree_1d_x(leaf[] sorted_list, int size) {
        this.size = size;
        node_not_leaf[] parents = new node_not_leaf[(size / 2) + 1];
        int parents_size = 0;
        for (int i = 0; i < size - 1; i++) {
            sorted_list[i].next = sorted_list[i + 1];
        }
        if (size % 2 == 0) {
            for (int j = 0; j < size && j + 1 < size; j += 2) {
                parents[parents_size] = make_parent_x(sorted_list[j], sorted_list[j + 1]);
                parents_size++;
            }
        } else {
            for (int j = 0; j < size - 1 && j + 1 < size - 1; j += 2) {
                parents[parents_size] = make_parent_x(sorted_list[j], sorted_list[j + 1]);
                parents_size++;
            }
            parents[parents_size] = sorted_list[size - 1];
            parents_size++;
        }
        connecting_parents(parents, parents_size);
    }

    private void connecting_parents(node_not_leaf[] fathers, int fathers_size) {
        if (fathers_size < 2) {
            root = fathers[0];
            return;
        }
        node_not_leaf[] parents = new node_not_leaf[(fathers_size / 2) + 1];
        int parents_size = 0;
        if (fathers_size % 2 == 0) {
            for (int j = 0; j < fathers_size && j + 1 < fathers_size; j += 2) {
                parents[parents_size] = make_parent_x(fathers[j], fathers[j + 1]);
                parents_size++;
            }
        } else {
            for (int j = 0; j < fathers_size - 1 && j + 1 < fathers_size - 1; j += 2) {
                parents[parents_size] = make_parent_x(fathers[j], fathers[j + 1]);
                parents_size++;
            }
            parents[parents_size] = fathers[fathers_size - 1];
            parents_size++;
        }
        connecting_parents(parents, parents_size);
    }

    node_not_leaf find_split(double start, double end) {
        node_not_leaf split = this.root;
        if (split != null) {
            while (!(split instanceof leaf) && (end <= split.data || split.data < start)) {
                if (end <= split.data) {
                    split = split.left;
                } else {
                    split = split.right;
                }
            }
            return split;
        }
        return null;
    }

    ArrayList<node_not_leaf> query(double start, double end) {
        ArrayList<node_not_leaf> q = new ArrayList<>();
        node_not_leaf split = find_split(start, end), iterator;
        if (split == null) {
            return null;
        }
        if (split instanceof leaf) {
            if (((leaf) split).x <= end && ((leaf) split).x >= start) {
                q.add(split);
            }
        } else {
            iterator = split.left;
            while (iterator.left != null) {
                if (start <= iterator.data) {
                    if (iterator.right.left != null) {
                        q.add(iterator.right.y_dimension.root);
                    } else {
                        q.add(iterator.right);
                    }
                    iterator = iterator.left;
                } else {
                    iterator = iterator.right;
                }
            }
            if (((leaf) iterator).x <= end && ((leaf) iterator).x >= start) {
                q.add(iterator);
            }
            iterator = split.right;
            while (iterator.left != null) {
                if (end >= iterator.data) {
                    if (iterator.left.left != null) {
                        q.add(iterator.left.y_dimension.root);
                    } else {
                        q.add(iterator.left);
                    }
                    iterator = iterator.right;
                } else {
                    iterator = iterator.left;
                }
            }
            if (((leaf) iterator).x <= end && ((leaf) iterator).x >= start) {
                q.add(iterator);
            }
        }
        return q;
    }

    node_not_leaf make_parent_x(node_not_leaf left, node_not_leaf right) {
        if (left == null || right == null) {
            return null;
        }
        if (left instanceof leaf && right instanceof leaf) {
            return new node_not_leaf(((leaf) left).x, ((leaf) right).x, left, right);
        } else if (left instanceof leaf) {
            return new node_not_leaf(((leaf) left).x, right.max, left, right);
        } else if (right instanceof leaf) {
            return new node_not_leaf(left.max, ((leaf) right).x, left, right);
        } else {
            return new node_not_leaf(left.max, right.max, left, right);
        }
    }


}

class range_tree_1d_y {
    node_not_leaf root;
    int size;

    range_tree_1d_y(leaf[] sorted_list, int size) {
        if (size < 2) {
            return;
        }
        this.size = size;
        node_not_leaf[] parents = new node_not_leaf[(size / 2) + 1];
        int parents_size = 0;
        for (int i = 0; i < size - 1; i++) {
            sorted_list[i].next = sorted_list[i + 1];
        }
        if (size % 2 == 0) {
            for (int j = 0; j < size && j + 1 < size; j += 2) {
                parents[parents_size] = make_parent_y(sorted_list[j], sorted_list[j + 1]);
                parents_size++;
            }
        } else {
            for (int j = 0; j < size - 1 && j + 1 < size - 1; j += 2) {
                parents[parents_size] = make_parent_y(sorted_list[j], sorted_list[j + 1]);
                parents_size++;
            }
            parents[parents_size] = sorted_list[size - 1];
            parents_size++;
        }
        connecting_parents(parents, parents_size);
    }

    private void connecting_parents(node_not_leaf[] fathers, int fathers_size) {
        if (fathers_size < 2) {
            root = fathers[0];
            return;
        }
        node_not_leaf[] parents = new node_not_leaf[fathers_size / 2 + 1];
        int parents_size = 0;
        if (fathers_size % 2 == 0) {
            for (int j = 0; j < fathers_size && j + 1 < fathers_size; j += 2) {
                parents[parents_size] = make_parent_y(fathers[j], fathers[j + 1]);
                parents_size++;
            }
        } else {
            for (int j = 0; j < fathers_size - 1 && j + 1 < fathers_size - 1; j += 2) {
                parents[parents_size] = make_parent_y(fathers[j], fathers[j + 1]);
                parents_size++;
            }
            parents[parents_size] = fathers[fathers_size - 1];
            parents_size++;
        }
        connecting_parents(parents, parents_size);
    }

    public static ArrayList<leaf> query(double start, double end, ArrayList<node_not_leaf> a) {
        ArrayList<leaf> q = new ArrayList<>();
        for (node_not_leaf node_not_leaf : a) {
            node_not_leaf split = node_not_leaf, iterator;
            if (split == null) {
                break;
            }
            while (!(split instanceof leaf) && (end <= split.data || split.data < start)) {
                if (end <= split.data) {
                    split = split.left;
                } else {
                    split = (split).right;
                }
            }

            if (split instanceof leaf) {
                if (((leaf) split).y <= end && ((leaf) split).y >= start) {
                    q.add((leaf) split);
                }
            } else {
                iterator = split.left;
                while (iterator.left != null) {
                    if (start <= iterator.data) {
                        if (iterator.right.left != null) {
                            node_not_leaf min = iterator.right;
                            node_not_leaf max = min;
                            while (min.left != null && max.right != null) {
                                min = (min).left;
                                max = (max).right;
                            }
                            if (min.left == null) {
                                while (max.right != null) {
                                    max = (max).right;
                                }
                            } else {
                                while ((min).left != null) {
                                    min = (min).left;
                                }
                            }
                            while (min != max) {
                                q.add((leaf) min);
                                min = ((leaf) min).next;
                            }
                            q.add((leaf) max);
                        } else {
                            q.add((leaf) iterator.right);
                        }
                        iterator = iterator.left;
                    } else {
                        iterator = iterator.right;
                    }
                }
                if (((leaf) iterator).y <= end && ((leaf) iterator).y >= start) {
                    q.add((leaf) iterator);
                }
                iterator = split.right;
                while (iterator.left != null) {
                    if (end >= iterator.data) {
                        if ((iterator).left.left != null) {
                            node_not_leaf min = (iterator).left;
                            node_not_leaf max = min;
                            while (min.left != null && max.right != null) {
                                min = min.left;
                                max = max.right;
                            }
                            if (min.left == null) {
                                while (max.right != null) {
                                    max = max.right;
                                }
                            } else {
                                while (min.left != null) {
                                    min = min.left;
                                }
                            }
                            while (min != max) {
                                q.add((leaf) min);
                                min = ((leaf) min).next;
                            }
                            q.add((leaf) max);
                        } else {
                            q.add((leaf) iterator.left);
                        }
                        iterator = iterator.right;
                    } else {
                        iterator = iterator.left;
                    }
                }
                if (((leaf) iterator).y <= end && ((leaf) iterator).y >= start) {
                    q.add((leaf) iterator);
                }
            }
        }
        return q;
    }

    node_not_leaf make_parent_y(node_not_leaf left, node_not_leaf right) {
        if (left == null || right == null) {
            return null;
        }
        if (left instanceof leaf && right instanceof leaf) {
            return new node_not_leaf(((leaf) left).y, ((leaf) right).y, left, right);
        } else if (left instanceof leaf) {
            return new node_not_leaf(((leaf) left).y, right.max, left, right);
        } else if (right instanceof leaf) {
            return new node_not_leaf(left.max, ((leaf) right).y, left, right);
        } else {
            return new node_not_leaf(left.max, right.max, left, right);

        }
    }
}

class range_tree_2d {
    range_tree_1d_x x_tree;
    int size;

    void make_y_dimensions() {
        QuickSort qs = new QuickSort();
        Queue<node_not_leaf> queue = new LinkedList<>();
        queue.add(this.x_tree.root);
        ArrayList<leaf> l;
        while (!queue.isEmpty()) {
            /* poll() removes the present head.
            For more information on poll() visit
            http://www.tutorialspoint.com/java/
            util/linkedlist_poll.htm */
            node_not_leaf tempNode = queue.poll();
            l = find_children(tempNode);
            leaf[] list = l.toArray(new leaf[0]);
            qs.sort_y(list, 0, l.size() - 1);
            tempNode.y_dimension = new range_tree_1d_y(list, l.size());

            /*Enqueue left child */
            if (tempNode.left != null) {
                queue.add(tempNode.left);
            }

            /*Enqueue right child */
            if (tempNode.right != null) {
                queue.add(tempNode.right);
            }
        }
    }

    range_tree_2d(leaf[] sorted_list, int size) {
        x_tree = new range_tree_1d_x(sorted_list, size);
        this.size = size;
        make_y_dimensions();
    }

    private ArrayList<leaf> find_children(node_not_leaf iterator1) {
        node_not_leaf iterator0 = iterator1;
        while (iterator0.left != null && iterator1.right != null) {
            iterator0 = iterator0.left;
            iterator1 = iterator1.right;
        }
        if (iterator0.left == null) {
            while (iterator1.right != null) {
                iterator1 = iterator1.right;
            }
        } else {
            while (iterator0.left != null) {
                iterator0 = iterator0.left;
            }
        }
        return get_children((leaf) iterator0, (leaf) iterator1);
    }

    ArrayList<leaf> get_children(leaf iterator0, leaf iterator1) {
        ArrayList<leaf> children = new ArrayList<>();
        while (iterator0 != iterator1) {
            children.add(iterator0.duplicate());
            iterator0 = iterator0.next;
        }
        children.add(iterator1.duplicate());
        return children;
    }

    static void getLeafNodes(node_not_leaf root, ArrayList<leaf> a) {

        // If node is null, return
        if (root == null)
            return;

        // If node is leaf node, print its data
        if (root.left == null &&
                root.right == null) {
            a.add(((leaf) root).duplicate());
            return;
        }

        // If left child exists, check for leaf
        // recursively
        if (root.left != null)
            getLeafNodes(root.left, a);

        // If right child exists, check for leaf
        // recursively
        if (root.right != null)
            getLeafNodes(root.right, a);
    }

    void find_query(double x1, double x2, double y1, double y2, StringBuilder s) {
        ArrayList<node_not_leaf> q = x_tree.query(x1, x2);
        ArrayList<leaf> answers;
        answers = range_tree_1d_y.query(y1, y2, q);
        if (answers.isEmpty()) {
            s.append("None\n");
        } else {
            leaf[] list = answers.toArray(new leaf[0]);
            QuickSort qs = new QuickSort();
            qs.sort_y(list, 0, answers.size() - 1);

            for (int j = 0; j < answers.size(); j++) {
                Double d = list[j].x;
                DecimalFormat format = new DecimalFormat("0.#");
                s.append(format.format(d)).append(" ");
            }
            s.append("\n");
            for (int j = 0; j < answers.size(); j++) {
                Double d = list[j].y;
                DecimalFormat format = new DecimalFormat("0.#");
                s.append(format.format(d)).append(" ");

            }
            s.append("\n");
        }
    }
}

public class Main {
    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        public FastReader() {
            br = new BufferedReader(new
                    InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }

        long nextLong() {
            return Long.parseLong(next());
        }

        double nextDouble() {
            return Double.parseDouble(next());
        }

        String nextLine() {
            String str = "";
            try {
                str = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return str;
        }
    }

    public static void main(String[] args) {
        leaf[] list;
        FastReader fs = new FastReader();
        int n = fs.nextInt();
        int i = 0, size = n;
        double[] x = new double[n];
        double[] y = new double[n];
        while (n > 0) {
            x[i] = fs.nextDouble();
            n--;
            i++;
        }
        n = size;
        i = 0;
        while (size > 0) {
            y[i] = fs.nextDouble();
            size--;
            i++;
        }
        size = n;
        list = new leaf[n];
        int operations = fs.nextInt();
        i = 0;
        while (n > 0) {
            list[i] = new leaf(x[i], y[i]);
            n--;
            i++;
        }
        QuickSort qs = new QuickSort();
        qs.sort_x(list, 0, size - 1);
        range_tree_2d rangetree = new range_tree_2d(list, size);
        double x1, x2, y1, y2;
        StringBuilder s = new StringBuilder();
        while (operations > 0) {
            x1 = fs.nextDouble();
            y1 = fs.nextDouble();
            x2 = fs.nextDouble();
            y2 = fs.nextDouble();
            rangetree.find_query(x1, x2, y1, y2, s);
            operations--;
        }
        System.out.print(s.toString());
    }
}
