package com.jegensomme.codewars.metalchemist;

import static org.junit.Assert.*;
import org.junit.runners.JUnit4;
import java.util.*;
import java.util.stream.*;
import org.junit.Test;


public class Molecule_SampleTest {

    @Test
    public void test_Constuctors() {

        Molecule m = new Molecule();
        assertEquals("Should define the empty string as default name", "", m.getName());

        m = new Molecule("banana");
        assertEquals("Even if...", "banana", m.getName());
    }


    @Test
    public void test_SimpleCarbohydrates() {
/*
methane:

      H
      |
    H-C-H   <=>  CH4
      |
      H


octane:

    CH3-CH2-CH2-CH2-CH2-CH2-CH2-CH3

*/
        Molecule m = new Molecule("methane").brancher(1).closer();
        assertEquals("CH4", m.getFormula());
        assertEquals(16, m.getMolecularWeight(), PRECISION);

        m = new Molecule("octane").brancher(8).closer();
        assertEquals("C8H18", m.getFormula());
        assertEquals(114, m.getMolecularWeight(), PRECISION);
    }


    @Test
    public void test_AtomClassSpecs_usingMethane() {

        display("'element' and 'id' public properties");

        Molecule m = new Molecule("methane").brancher(1);
        List<Atom> atoms = null;
        try {                   atoms = m.getAtoms(); }
        catch (Exception e) { fail("Couldn't access to the list of atoms. Exception: " + e); }

        display("toString() implementation (basics 1): atom without bonds");
        assertEquals("Atom(C.1)", atoms.get(0).toString());


        m.closer();
        atoms=m.getAtoms();

        assertEquals("Wrong number of atoms", 5, atoms.size());
        for (int i = 0 ; i < 5 ; i++ ) {
            assertEquals(String.format("Wrong atom at the index %d in the atoms structure", i), i==0 ? "C" : "H", atoms.get(i).element);
            assertEquals(String.format("Wrong id value at the index %d in the atoms structure", i), i+1, atoms.get(i).id);
        }


        display("toString() implementation (basics 2)");

        assertEquals("Atom(C.1: H,H,H,H)", atoms.get(0).toString());
        assertEquals("Atom(H.5: C1)",      atoms.get(atoms.size()-1).toString());

    }


    @Test
    public void test_CreateCarbohydratesAndBoundThemCorrectly_idTracking_rawFormula_molecularWeight() {
/*
cyclohexane:

    CH2-CH2-CH2
    |       |
    CH2-CH2-CH2


1,1-dimethyl-2-propylcyclohexane:

                CH3   CH3
                   \ /
    CH3-CH2-CH2-CH2-CH2-CH2
                |       |
                CH2-CH2-CH2


cubane (something like that, with one more "CH" group at the back of the cube...):

       CH-----CH
      /      /|
     /      / |
    CH-----CH CH
    |      | /
    |      |/
    CH-----CH


benzene:

     CH-CH
    //   \\
    CH    CH
    \    /
     CH=CH

*/
        String[] names    = {"cyclohexane",  "1,1-dimethyl-2-propylcyclohexane",  "cubane - one branch", "cubane - two branches", "benzene - double bonds"};
        String[] forms    = {"C6H12",        "C11H22",                             "C8H8",                "C8H8",                   "C6H6"};
        double[] mWeights = {84,             154,                                   104,                   104,                     78};
        int[][]  branches = {{6},            {9,1,1},                              {8},                   {4,4},                    {2,2,2}};
        T[][]    bounds   = {{new T(1,1,6,1)},
                {new T(4,1,9,1), new T(5,1,1,2), new T(5,1,1,3)},
                {new T(3,1,6,1), new T(2,1,7,1), new T(1,1,8,1), new T(4,1,1,1), new T(5,1,8,1)},
                {new T(1,1,4,1), new T(1,2,4,2), new T(1,1,1,2), new T(2,1,2,2), new T(3,1,3,2), new T(4,1,4,2)},
                {new T(1,1,2,1), new T(1,2,2,2), new T(1,3,2,3), new T(2,1,1,2), new T(2,2,1,3), new T(2,3,1,1)}};
        List<List<String>> expecteds = Arrays.asList( Arrays.asList("Atom(C.1: C2,C6,H,H)", "Atom(C.2: C1,C3,H,H)", "Atom(C.3: C2,C4,H,H)", "Atom(C.4: C3,C5,H,H)", "Atom(C.5: C4,C6,H,H)", "Atom(C.6: C1,C5,H,H)"),
                Arrays.asList("Atom(C.1: C2,H,H,H)", "Atom(C.2: C1,C3,H,H)", "Atom(C.3: C2,C4,H,H)", "Atom(C.4: C3,C5,C9,H)", "Atom(C.5: C4,C6,C10,C11)", "Atom(C.6: C5,C7,H,H)", "Atom(C.7: C6,C8,H,H)", "Atom(C.8: C7,C9,H,H)", "Atom(C.9: C4,C8,H,H)", "Atom(C.10: C5,H,H,H)", "Atom(C.11: C5,H,H,H)"),
                Arrays.asList("Atom(C.1: C2,C4,C8,H)", "Atom(C.2: C1,C3,C7,H)", "Atom(C.3: C2,C4,C6,H)", "Atom(C.4: C1,C3,C5,H)", "Atom(C.5: C4,C6,C8,H)", "Atom(C.6: C3,C5,C7,H)", "Atom(C.7: C2,C6,C8,H)", "Atom(C.8: C1,C5,C7,H)"),
                Arrays.asList("Atom(C.1: C2,C4,C5,H)", "Atom(C.2: C1,C3,C6,H)", "Atom(C.3: C2,C4,C7,H)", "Atom(C.4: C1,C3,C8,H)", "Atom(C.5: C1,C6,C8,H)", "Atom(C.6: C2,C5,C7,H)", "Atom(C.7: C3,C6,C8,H)", "Atom(C.8: C4,C5,C7,H)"),
                Arrays.asList("Atom(C.1: C2,C2,C6,H)", "Atom(C.2: C1,C1,C3,H)", "Atom(C.3: C2,C4,C4,H)", "Atom(C.4: C3,C3,C5,H)", "Atom(C.5: C4,C6,C6,H)", "Atom(C.6: C1,C5,C5,H)") );

        for (int i = 0 ; i < names.length ; i++) {
            display(names[i]);
            Molecule m = new Molecule(names[i]).brancher(branches[i]).bounder(bounds[i]).closer();
            testThisMolecule(m, forms[i], mWeights[i], expecteds.get(i));
        }
    }


    @Test
    public void test_MutateAddAddChaining_ValenceNumbersConsistencies() {

/*
Furane:
    O
  /   \
CH     CH
 \\   //
  CH-CH

isopropylmagnesium bromide:
      CH3
        \
         CH-Mg-Br
        /
      CH3
*/

        String[] names    = {"Furane: no additional hydrogens while closing the molecule after mutation",  "isopropylmagnesium bromide"       };
        String[] forms    = {"C4H4O",                                                                      "C3H7BrMg"                         };
        double[] mWeights = { 68,                                                                           147.3                             };
        int[][]  branches = {{5},                                                                          {4,1}                              };
        T[][]    bounds   = {{new T(5,1,1,1), new T(5,1,4,1), new T(2,1,3,1)},                             {new T(2,1,1,2)}                   };
        T[][]    adds     = {{new T(1,1,"O")},                                                             {new T(3,1,"Mg"), new T(4,1,"Br")} };
        List<List<String>> expecteds = Arrays.asList(
                Arrays.asList("Atom(O.1: C2,C5)", "Atom(C.2: C3,C3,O1,H)", "Atom(C.3: C2,C2,C4,H)", "Atom(C.4: C3,C5,C5,H)", "Atom(C.5: C4,C4,O1,H)"),
                Arrays.asList("Atom(C.1: C2,H,H,H)", "Atom(C.2: C1,C5,Mg3,H)", "Atom(Mg.3: C2,Br4)", "Atom(Br.4: Mg3)", "Atom(C.5: C2,H,H,H)") );

        for (int i = 0 ; i < names.length ; i++) {
            display(names[i]);
            Molecule m = new Molecule(names[i]).brancher(branches[i]).bounder(bounds[i]).mutate(adds[i]).closer();
            testThisMolecule(m, forms[i], mWeights[i], expecteds.get(i));
        }
    }


    /* ************************
     *     FAILURE TESTS
     * ************************/

    @Test
    public void test_InvalidBond_invalidBasicBuilds() {

        String[] names    = {"No self-bounding",
                "Should fail when exceeding the valence number adding new alkyls to the same atom",
                "Should fail when exceeding the valence number with multiple bonds"};
        int[][]  branches = {{6}, {3,1,1,1}, {4}};
        T[][]    bounds   = {{new T(1,1,1,1)},
                {new T(2,1,1,2), new T(2,1,1,3), new T(2,1,1,4)},
                {new T(2,1,3,1), new T(2,1,3,1), new T(2,1,3,1)}};

        for (int i = 0 ; i < names.length ; i++) {
            display(names[i]);

            boolean checkRaised = true;
            try {
                new Molecule(names[i]).brancher(branches[i]).bounder(bounds[i]).closer();
                checkRaised = false;
            } catch (InvalidBond iEB) {
            } catch (Exception e) {
                fail("Should throw an InvalidBond exception but was " + e);
            }
            assertTrue("Should have thrown an InvalidBond exception", checkRaised);
        }
    }


    @Test
    public void test_InvalidBond_InvalidMutationsOrAdditions_CheckMoleculeIntegrityAfterFailure() {

        String[] names    = {"Should fail when mutating a carbon with three atoms already linked to an oxygen",
                "Should fail when mutating a carbon with two double bonds to nitrogen",
                "Should fail when adding a new hydrogen to a carbon with already 4 bonds",
                "Should fail when mutating an atom and then adding too much atoms on it"};
        int[][]  branches = {{3,1},             {3},                                {3},                              {3}};
        T[][]    bounds   = {{new T(2,1,1,2)},  {new T(1,1,2,1), new T(3,1,2,1)},  {new T(1,1,2,1), new T(3,1,2,1)},  {new T(1,1,2,1)}};
        String[] execs    = {"mutate",          "mutate",                           "add",                            "mutadd"};
        T[][]    tups     = {{new T(2,1,"O")},  {new T(2,1,"N")},                   {new T(2,1,"H")},                 {new T(2,1,"N"), new T(2,1,"O")}};

        for (int i = 0 ; i < names.length ; i++) {
            display(names[i]);

            Molecule m = new Molecule(names[i]).brancher(branches[i]);

            m.bounder(bounds[i]);
            if (execs[i].equals("mutadd"))
                m.mutate(tups[i][0]);

            List<String> integrity = extractNoneHToStr(m);

            boolean checkRaised = true;
            try {
                switch (execs[i]) {
                    case "mutate":  m.mutate(tups[i]).closer();             break;
                    case "add":     m.add(tups[i]).closer();                break;
                    case "mutadd":  m.add(tups[i][1]).closer();             break;
                    default:        throw new RuntimeException("Wrong configuration of the tests");
                }
                checkRaised = false;
            } catch (InvalidBond iEB) {
                assertEquals("The molecule shouldn't be modified when one of these errors occur", integrity, extractNoneHToStr(m));
            }
            assertTrue("Should have thrown an InvalidBond exception", checkRaised);
        }
    }



    final static double PRECISION = 1e-8;

    private <U> void display(U msg) { System.out.println(msg); }

    private List<String> extractNoneHToStr(Molecule m) {
        return m.getAtoms().stream()
                .filter( at -> !at.element.equals("H"))
                .map(    at -> at.toString() )
                .collect(Collectors.toList());
    }

    private void testThisMolecule(Molecule m, String formula, double mm, List<String> strNoH) {
        assertEquals("Checking bonds (for non-hydrogens)", strNoH, extractNoneHToStr(m));
        assertEquals("Testing raw formula", formula, m.getFormula());
        assertEquals("Testing molecular weight", mm, m.getMolecularWeight(), PRECISION);
    }

}
