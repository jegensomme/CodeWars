package com.jegensomme.codewars.metalchemist;

class T {
    public int c1, c2, b1, b2, nc, nb;
    public String elt = "";

    public T(int a, int b, int c, int d) { c1 = a; b1 = b; c2 = c; b2 = d; }
    public T(int a, int b, String c)     { nc = a; nb = b; elt = c; }

    @Override public String toString() {
        return elt.isEmpty() ? String.format("T(%d, %d, %d, %d)", c1, b1, c2, b2)
                : String.format("T(%d, %d, %s)", nc, nb, elt);
    }
}
