package com.jegensomme.codewars.metalchemist;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class AtomFactory {
    private int count = 0;

    private final List<Atom> atoms = new ArrayList<>();

    public Atom createAtom(String elt) {
        return createAtom(elt, null);
    }

    public Atom createAtom(String elt, List<Atom> chain) {
        Atom atom = new Atom(++count, elt, chain, this);
        atoms.add(atom);
        return atom;
    }

    public List<Atom> createChain(String... elements) {
        List<Atom> chain = new ArrayList<>(elements.length);
        for (String element : elements) {
            chain.add(createAtom(element, chain));
        }
        return chain;
    }

    public List<Atom> createCarbonChain(int n) {
        return createChain(Stream.generate(() -> "C").limit(n).toArray(String[]::new));
    }

    public List<Atom> getAtoms() {
        return new ArrayList<>(atoms);
    }

    public void remove(Atom atom) {
        int idx = atoms.indexOf(atom);
        atoms.remove(idx);
        count--;
        for (int i = idx; i < count; i++) {
            atoms.get(i).id--;
        }
    }
}
