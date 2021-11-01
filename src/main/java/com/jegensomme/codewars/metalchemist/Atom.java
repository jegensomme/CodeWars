package com.jegensomme.codewars.metalchemist;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.function.Predicate;

class Atom implements Comparable<Atom> {
    public int id;
    public String element;

    private int valence;
    private double weight;

    private final List<Atom> chain;

    private final AtomFactory factory;

    private final List<Atom> bonds = new ArrayList<>();
    private final List<Atom> bounds = new ArrayList<>();

    private static final Comparator<Atom> COMPARATOR = ((Comparator<Atom>)(a1, a2) ->
            Element.ELEMENT_COMPARATOR.compare(a1.element, a2.element)
    ).thenComparingInt(a -> a.id);

    public Atom(int id, String element, List<Atom> chain, AtomFactory factory) {
        this.id = id;
        this.element = element;
        this.chain = chain;
        this.factory = factory;
        Element eltInfo = Element.valueOf(element);
        this.valence = eltInfo.getValence();
        this.weight = eltInfo.getWeight();
    }

    public void addBound(Atom atom) {
        if (isFull() || atom.isFull() || equals(atom)) {
            throw new InvalidBond();
        }
        bounds.add(atom);
        atom.bounds.add(this);
    }

    public void add(String elt) {
        if (isFull()) {
            throw new InvalidBond();
        }
        Atom atom = factory.createAtom(elt);
        bonds.add(atom);
        atom.bonds.add(this);
    }

    public void addChaining(String... elements) {
        for (int i = 0; i < elements.length - 1; i++) {
            if (Element.valueOf(elements[i]).getValence() == 1) {
                throw new InvalidBond();
            }
        }
        if (isFull()) {
            throw new InvalidBond();
        }
        List<Atom> chain = factory.createChain(elements);
        addBound(chain.get(0));
    }

    public void mutate(String element) {
        Element elt = Element.valueOf(element);
        if (elt.getValence() < getBondsNumber()) {
            throw new InvalidBond();
        }
        this.element = element;
        valence = elt.getValence();
        weight = elt.getWeight();
    }

    private List<Atom> getChainNeighbours() {
        if (chain == null || chain.size() == 1) return List.of();
        int order = chain.indexOf(this);
        return order == 0 ? List.of(chain.get(1))
                : order == chain.size() - 1 ? List.of(chain.get(order - 1))
                : List.of(chain.get(order - 1), chain.get(order + 1));
    }

    private List<Atom> getAllBonds() {
        List<Atom> bonds = new ArrayList<>(getBondsNumber());
        bonds.addAll(getChainNeighbours());
        bonds.addAll(this.bonds);
        bonds.addAll(bounds);
        return bonds;
    }

    private int getBondsNumber() {
        return getChainNeighbours().size() + bonds.size() + bounds.size();
    }

    public boolean isFull() {
        return valence == getBondsNumber();
    }

    public double getWeight() {
        return weight;
    }

    public void retire() {
        if (chain != null) {
            chain.remove(this);
        }
        bonds.forEach(a -> a.bonds.remove(this));
        bounds.forEach(a -> a.bounds.remove(this));
        factory.remove(this);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Atom) {
            Atom that = (Atom) other;
            return id == that.id;
        }
        return false;
    }

    @Override
    public int compareTo(Atom o) {
        return COMPARATOR.compare(this, o);
    }

    @Override
    public String toString() {
        String presentation = "Atom(" + element + "." + id;
        if (getBondsNumber() == 0) {
            return presentation + ")";
        }
        List<Atom> allBonds = getAllBonds();
        Predicate<Atom> filter = (Atom a) -> "H".equals(a.element);
        List<Atom> hydrogens = allBonds.stream().filter(filter).collect(Collectors.toList());
        List<Atom> nonHydrogens = allBonds.stream().filter(filter.negate()).sorted().collect(Collectors.toList());
        List<String> atoms = nonHydrogens.stream().map(a -> a.element + a.id).collect(Collectors.toList());
        atoms.addAll(hydrogens.stream().map(a -> a.element).collect(Collectors.toList()));
        return presentation + ": " + String.join(",", atoms) + ")";
    }
}
