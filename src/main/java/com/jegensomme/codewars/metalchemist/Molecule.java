package com.jegensomme.codewars.metalchemist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByKey;

class Molecule {
    private final String name;

    private final List<List<Atom>> chains = new ArrayList<>();

    private final AtomFactory atomFactory = new AtomFactory();

    private boolean mutable = true;

    public Molecule() {
        this("");
    }

    public Molecule(String name) {
        this.name = name;
    }

    public Molecule brancher(int... branches) {
        if (!mutable) {
            throw new LockedMolecule();
        }
        chains.addAll(Arrays.stream(branches)
                .mapToObj(atomFactory::createCarbonChain)
                .collect(Collectors.toList()));
        return this;
    }

    public Molecule bounder(T... ts) {
        if (!mutable) {
            throw new LockedMolecule();
        }
        for (T t : ts) {
            Atom first = getAtom(t.b1, t.c1);
            Atom second = getAtom(t.b2, t.c2);
            first.addBound(second);
        }
        return this;
    }

    public Molecule mutate(T... ts) {
        if (!mutable) {
            throw new LockedMolecule();
        }
        for (T t : ts) {
            getAtom(t.nb, t.nc).mutate(t.elt);
        }
        return this;
    }

    public Molecule add(T... ts) {
        if (!mutable) {
            throw new LockedMolecule();
        }
        for (T t : ts) {
            getAtom(t.nb, t.nc).add(t.elt);
        }
        return this;
    }

    public Molecule addChaining(int nc, int nb, String... elt) {
        if (!mutable) {
            throw new LockedMolecule();
        }
        getAtom(nb, nc).addChaining(elt);
        return this;
    }

    public Molecule closer() {
        if (!mutable) {
            throw new LockedMolecule();
        }
        for (Atom atom : getAtoms()) {
            while (!atom.isFull()) atom.add("H");
        }
        mutable = false;
        return this;
    }

    public Molecule unlock() {
        getAtoms().stream().filter(a -> "H".equals(a.element)).forEach(Atom::retire);
        chains.removeIf(List::isEmpty);
        if (chains.isEmpty()) {
            throw new EmptyMolecule();
        }
        mutable = true;
        return this;
    }

    public String getFormula() {
        if (mutable) {
            throw new UnlockedMolecule();
        }
        return getAtoms().stream()
                .collect(Collectors.groupingBy(a -> a.element, Collectors.counting()))
                .entrySet().stream()
                .sorted(comparingByKey(Element.ELEMENT_COMPARATOR))
                .map(e -> e.getKey() + (e.getValue() > 1 ? e.getValue() : ""))
                .collect(Collectors.joining());
    }

    public double getMolecularWeight() {
        if (mutable) {
            throw new UnlockedMolecule();
        }
        return getAtoms().stream().mapToDouble(Atom::getWeight).sum();
    }

    public List<Atom> getAtoms() {
        return atomFactory.getAtoms();
    }

    public String getName() {
        return name;
    }

    private Atom getAtom(int nb, int nc) {
        return chains.get(nb - 1).get(nc - 1);
    }
}
