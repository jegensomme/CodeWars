package com.jegensomme.codewars.metalchemist;

import java.util.Comparator;

enum Element {
    H(1, 1.0), B(3, 10.8),
    C(4, 12.0), N(3, 14.0),
    O(2, 16.0), F(1, 19.0),
    Mg(2, 24.3), P(3, 31.0),
    S(2, 32.1), Cl(1, 35.5),
    Br(1, 80.0);

    Element(int valence, double weight) {
        this.valence = valence;
        this.weight = weight;
    }

    public static final Comparator<String> ELEMENT_COMPARATOR = (e1, e2) -> e1.equals(e2) ? 0
            : "C".equals(e1) ? -1 : "C".equals(e2) ? 1
            : "H".equals(e1) ? -1 : "H".equals(e2) ? 1
            : "O".equals(e1) ? -1 : "O".equals(e2) ? 1
            : e1.compareTo(e2);

    private final int valence;
    private final double weight;

    public int getValence() {
        return valence;
    }

    public double getWeight() {
        return weight;
    }
}
